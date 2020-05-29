(ns mullion.core
  (:require [mullion.libs :as libs]
            [mullion.reflection])
  (:import [org.bytedeco.qt.Qt5Widgets QApplication QTextEdit]
           [org.bytedeco.javacpp PointerPointer IntPointer])
  (:gen-class))

(set! *warn-on-reflection* true)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (libs/init!)

  (when (= args '("--test-load"))
    (libs/debug-load)
    (System/exit 0))

  (libs/load-libs)
  (let [lib-dir (libs/get-lib-dir)
        app (QApplication.
             (IntPointer. (int-array [3]))
             (PointerPointer.
              (into-array
               String
               ["gettingstarted"
                "-platformpluginpath"
                lib-dir])
              ))
        text-edit (proxy [QTextEdit] []
                    (closeEvent [ev] (println "close!" ev)))]
    (.show text-edit)
    (System/exit (QApplication/exec))

    )
  )
