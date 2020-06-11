(ns digitalocean
  (:require [spire.modules :refer :all]
            [clojure.data.json :as json]))

(defn- make-url [suffix & args]
  (apply format (str "https://api.digitalocean.com/v2/" suffix) args))

(defn request [args]
  (curl (merge {:url (make-url "droplets")
                :headers {:authorization (format "Bearer %s" (System/getenv "DO_TOKEN"))
                          :content-type "application/json"}
                :decode-opts {:key-fn keyword}}
               args)))

(defn get-droplets []
  (-> (request {})
      (get-in [:decoded :droplets])))

(defn get-droplet [{:keys [id]}]
  (-> (request {:url (make-url "droplets/%d" id)})
      (get-in [:decoded :droplet])))

(defn create-droplet [drop]
  (-> (request {:method :POST
             :data-binary (json/write-str drop)})
      (get-in [:decoded :droplet]))
  )

(defn wait-for-droplet [drop]
  (loop [next-drop (get-droplet drop)]
    (if (empty? (get-in next-drop [:networks :v4]))
      (do
        (Thread/sleep 1000)
        (recur (get-droplet drop)))
      next-drop
      )))

(defn delete-droplet [{:keys [id]}]
  (-> (request {:method :DELETE
             :url (make-url "droplets/%d" id)})))

(defn account-keys []
  (-> (request {:url (make-url "account/keys")})
      (get-in [:decoded :ssh_keys])))

(defn get-key-by-name [ssh-keys name]
  (->> ssh-keys
       (filter #(= name (:name %)))
       first))
