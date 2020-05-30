(ns mullion.core
  (:require [mullion.libs :as libs]
            [mullion.reflection])
  (:import [org.bytedeco.qt.Qt5Widgets
            QApplication QTextEdit QPushButton QVBoxLayout QWidget]
           [org.bytedeco.qt.helper Qt5Widgets Qt5Widgets$ClickedCallback]
           [org.bytedeco.qt.Qt5Core QString QObject]
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
                    (closeEvent [ev] (println "close!" ev)))
        quit-button (QPushButton. (QString/fromUtf8 "&Quit") )
        layout (QVBoxLayout.)
        window (QWidget.)
        ]
    (Qt5Widgets/QAbstractButton_clicked
     quit-button (QObject.)
     (proxy [Qt5Widgets$ClickedCallback] []
       (clicked [b]
         (println "quit clicked" b)
         (QApplication/quit))
       )
     0)

    (.addWidget layout text-edit)
    (.addWidget layout quit-button)
    (.setLayout window layout)
    (.show window)
    (System/exit (QApplication/exec))

    )
  )
