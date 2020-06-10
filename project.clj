(defproject mullion "0.1.0-SNAPSHOT"
  :description "Simple GUIs"
  :url "https://github.com/retrogradeorbit/mullion"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.2-alpha1"]
                 [org.bytedeco/qt-platform "5.14.2-1.5.3"]

                 ;; from sonatype
                 ;;[org.bytedeco/qt-platform "5.15.0-1.5.4-SNAPSHOT"]

                 ;; local install
                 ;;[org.bytedeco/javacpp "1.5.4-SNAPSHOT"]
                 ;;[org.bytedeco/javacpp-platform "1.5.4-SNAPSHOT"]
                 ;;[org.bytedeco/qt "5.15.0-1.5.4-SNAPSHOT"]
                 ;;[org.bytedeco/qt-platform "5.15.0-1.5.4-SNAPSHOT"]

                 [cheshire "5.10.0"]
                 [mvxcvi/puget "1.2.0"]

                 ]
  ;;:repositories [["sonatype" {:url "https://oss.sonatype.org/content/repositories/snapshots"}]]
  :main ^:skip-aot mullion.core
  :java-source-paths ["src/java"]
  :jvm-opts [#=(str "-Djava.library.path=" #= (java.lang.System/getenv "HOME") "/.mullion/libs")
             ;;"-Dorg.bytedeco.javacpp.logger.debug=true"
             ]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"
                                  ;;"-Dorg.bytedeco.javacpp.logger.debug=true"
                                  #=(str "-Djava.library.path=" #= (java.lang.System/getenv "HOME") "/.mullion/libs")]
                       }})
