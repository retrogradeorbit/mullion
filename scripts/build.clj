#!/usr/bin/env spire

(ns build-javacpp-qt
  (:require [digitalocean :as do]
            [net-utils :as net-utils]
            [spire.modules :as spire]))

(def ssh-key (-> (do/account-keys)
                 (do/get-key-by-name "desktop key")))

(defn provision [ip-address]
  (spire/ssh {:username "root"
              :hostname ip-address
              :accept-host-key true
              :agent-forwarding true}
             (spire/apt :update)
             (spire/apt :install ["docker.io" "docker-compose" "openjdk-11-jdk" "maven"])
             (spire/service :restarted {:name "docker"})
             (spire/failed?
              (spire/shell {:cmd "ssh -oStrictHostKeyChecking=no -T git@github.com"}))
             (spire/mkdir {:path "build"})
             (spire/mkdir {:path ".ccache"})
             (spire/mkdir {:path ".m2"})
             (spire/group :present {:name "travis"})
             (spire/mkdir {:path "/home/travis"})
             (spire/user :present {:name "travis"
                                   :group "travis"
                                   :groups ["docker"] ;; doesnt change
                                   :shell "/bin/bash"
                                   :home "/home/travis"})
             (spire/line-in-file :present {:path "/etc/sudoers"
                                           :line "travis ALL=(ALL) NOPASSWD: ALL"
                                           :regexp #"^travis ALL"})
             #_ (spire/attrs {:path "/home/travis"
                           :owner "travis"
                              :group "travis"})
             (spire/shell {:cmd "chown travis:travis /home/travis"})
             (spire/authorized-keys :present
                                    {:user "travis"
                                     :key (:public_key ssh-key)})

             (spire/ssh
              {:username "travis"
               :hostname ip-address
               :agent-forwarding true
               }
              (spire/debug (spire/shell {:cmd "ls -alF"}))
              (spire/mkdir {:path "build"})
              (spire/mkdir {:path "build/bytedeco"})
              (spire/mkdir {:path "build/javacpp-presets"})
              (spire/mkdir {:path ".ccache"})
              (spire/mkdir {:path ".m2"})

              (spire/failed?
               (spire/shell {:cmd "ssh -oStrictHostKeyChecking=no -T git@github.com"}))
              #_ (spire/shell {:cmd "git clone git@github.com:retrogradeorbit/javacpp-presets.git"
                            :dir "build/bytedeco"})
              (spire/line-in-file :absent {:path "build/bytedeco/javacpp-presets/ci/install-travis.sh"
                                           :regexp #"while true; do uptime; sleep 60; done"})
              (spire/line-in-file :absent {:path "build/bytedeco/javacpp-presets/ci/install-travis.sh"
                                           :regexp #"sleep 6600; sudo killall -s SIGINT java"}))

             #_(spire/debug
                (spire/shell {:cmd "ci/install-travis.sh nodeploy"
                              :dir "javacpp-presets"
                              :env {:OS "linux-x86_64"
                                    :PROJ "qt"
                                    :TRAVIS_BUILD_DIR "/home/travis/build/bytedeco/javacpp-presets"}}))
             (spire/download {:src ".m2/repository/org/bytedeco"
                              :dest "remote-builds"
                              })
             ))

#_(provision "167.71.96.144")

(spire/ssh {:username "travis"
            :hostname "167.71.96.144"
            :agent-forwarding true
            }
           (spire/download {:src ".m2/repository/org/bytedeco"
                            :dest "remote-builds"
                            :recurse true})
           )

#_ (let [{:keys [networks] :as droplet}
      (->
       {:name "qt-build"
        :region "nyc3"
        :size "s-1vcpu-2gb"
        :image "ubuntu-18-04-x64"
        :backups false
        :ipv6 true
        :user_data nil
        :private_networking nil
        :volumes nil
        :tags ["build-qt"]
        :ssh_keys [(:id ssh-key)]}
       do/create-droplet
       do/wait-for-droplet)
      ip-address (get-in networks [:v4 0 :ip_address])]

  (net-utils/wait-for-port ip-address 22)
  (try
    (provision ip-address)
    #_(finally
      (do/delete-droplet droplet))))

(comment
  export TRAVIS_BUILD_DIR="/home/travis/build/bytedeco/javacpp-presets"
  export OS="linux-x86_64"
  export PROJ="qt"
;;  cd /home/travis/build/bytedeco/javacpp-presets
;;  ci/install-travis.sh

  nodeploy
  )
