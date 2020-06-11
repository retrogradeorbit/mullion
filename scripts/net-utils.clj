(ns net-utils
  (:import [java.net Socket ConnectException]))

(defn wait-for-port
  "Waits for TCP connection to be available on host and port. Options map
  supports `:timeout` and `:pause`. If `:timeout` is provided and reached,
  `:default`'s value (if any) is returned. The `:pause` option determines
  the time waited between retries."
  ([host port]
   (wait-for-port host port nil))
  ([^String host ^long port {:keys [:default :timeout :pause] :as opts}]
   (let [opts (merge {:host host
                      :port port}
                     opts)
         t0 (System/currentTimeMillis)]
     (loop []
       (let [v (try (.close (Socket. host port))
                    (- (System/currentTimeMillis) t0)
                    (catch ConnectException _e
                      (let [took (- (System/currentTimeMillis) t0)]
                        (if (and timeout (>= took timeout))
                          :wait-for-port.impl/timed-out
                          :wait-for-port.impl/try-again))))]
         (cond (identical? :wait-for-port.impl/try-again v)
               (do (Thread/sleep (or pause 100))
                   (recur))
               (identical? :wait-for-port.impl/timed-out v)
               default
               :else
               (assoc opts :took v)))))))
