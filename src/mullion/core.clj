(ns mullion.core
  (:require [mullion.libs]
            [mullion.reflection])
  (:gen-class))

(set! *warn-on-reflection* true)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
