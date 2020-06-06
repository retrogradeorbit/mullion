(ns mullion.core
  (:require [mullion.libs :as libs]
            [mullion.reflection]
            [clojure.string :as string])
  (:import [org.bytedeco.qt.Qt5Widgets
            QApplication QTextEdit QPushButton QVBoxLayout QHBoxLayout QWidget QLabel]
           [org.bytedeco.qt.helper
            Qt5Widgets
            Qt5Widgets$ClickedCallback
            Qt5Widgets$TriggeredCallback]
           [org.bytedeco.qt.Qt5Core QString QObject]
           [org.bytedeco.javacpp PointerPointer IntPointer])
  (:gen-class))

(set! *warn-on-reflection* true)

(def layout [:window
             [:v-box-layout
              [:text-edit {:on-close (fn [ev]
                                       (println "close!" ev))}]
              [:push-button {:on-click (fn [ev]
                                         (println "quit clicked" ev)
                                         (QApplication/quit))}
               "&Quit"]]])

(def types
  { ;;:window QWindow
   :widget (fn [opts layout]
             (let [w (QWidget.)]
               (.setLayout w layout)
               w))
   :label (fn [opts ^String text]
            (let [w (QLabel. ^QString (QString/fromUtf8 text))]
              w))
   :v-box-layout (fn [opts & children]
                   (let [w (QVBoxLayout.)]
                     (doseq [child children]
                       (.addWidget w child))
                     w))
   :h-box-layout (fn [opts & children]
                   (let [w (QHBoxLayout.)]
                     (doseq [child children]
                       (.addWidget w child))
                     w))
   :text-edit (fn [{:keys [on-change]} & remain]
                (let [w (proxy [QTextEdit] []
                          (event [ev]
                            (println "Event:" ev))
                          (changeEvent [ev]
                            (println "changeEvent:" ev)
                            )
                          )]
                  w))
   :push-button (fn [{:keys [on-click]} ^String text]
                  (let [w (QPushButton. ^QString (QString/fromUtf8 text))]
                    (when on-click
                      (Qt5Widgets/QAbstractButton_clicked
                       w
                       (QObject.)
                       (proxy [Qt5Widgets$ClickedCallback] []
                         (clicked [ev]
                           (on-click ev)))
                       0))
                    w))})

(defn get-constructor [t]
  (types t))

(defn parse-keyword
  "parse a keyword and split off an id string if one is present"
  [kw]
  (let [s (name kw)
        [base ident] (string/split s #"#" 2)]
    [(keyword base) (when ident (keyword ident))]))

(defonce id-registry (atom {}))

(defn get-widget [ident]
  (get @id-registry ident))

(defn make-widgets [markup]
  (let [[t & r] markup
        opts (if (map? (first r))
               (first r)
               {})
        remain (if (map? (first r))
                 (rest r)
                 r)
        children? (and remain (vector? (first remain)))
        children (when children? (mapv make-widgets remain))
        [kw ident] (parse-keyword t)
        con (get-constructor kw)
        ]
    (comment)
    (prn 'kw kw 'ident ident)
    (prn t '=> con opts children)
    (println)

    (let [w (apply con opts (if children? (map :widget children) remain))]
      (when ident
        (swap! id-registry assoc ident w))
      {:widget w
       :children children})))



#_ (defn -main
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
        ;;label (QLabel. (QString/fromUtf8 "0 seconds"))
        ;;quit-button (QPushButton. (QString/fromUtf8 "&Quit") )
        ;;layout (QVBoxLayout.)
        ;;window (QWidget.)
        ]
    #_(Qt5Widgets/QAbstractButton_clicked
       quit-button (QObject.)
       (proxy [Qt5Widgets$ClickedCallback] []
         (clicked [b]
           (println "quit clicked" b)
           (QApplication/quit))
         )
       0)

    ;;(.addWidget layout label)
    ;;(.addWidget layout quit-button)
    ;;(.setLayout window layout)
    ;;(.show window)

    ;;(prn (QWidget.))
    ;;(prn (new QWidget))
    (let [window (make-widgets
                  [:widget
                   [:v-box-layout
                    [:label#time "0 seconds"]
                    [:push-button
                     {:on-click (fn [ev] (QApplication/quit))}
                     "&Quit"]]]

                  #_[:widget {}
                     [:v-box-layout {}]])]
      (.show (:widget window))
      (future
        (loop [c 1]
          (Thread/sleep 1000)
          (->> c
               (format "%d seconds")
               QString/fromUtf8
               (.setText (get-widget :time)))
          (recur (inc c)))
        )
      (System/exit (QApplication/exec)))

    )
  )
