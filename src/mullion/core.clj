(ns mullion.core
  (:require [mullion.libs :as libs]
            [mullion.reflection]
            [clojure.string :as string]
            [puget.printer :as puget]
            )
  (:import [org.bytedeco.qt.Qt5Widgets
            QApplication QTextEdit QPushButton QVBoxLayout QHBoxLayout QWidget QLabel]
           [org.bytedeco.qt.helper
            Qt5Widgets
            Qt5Widgets$ClickedCallback
            Qt5Widgets$TextChangedCallback
            Qt5Widgets$TriggeredCallback]
           [org.bytedeco.qt.Qt5Core QString QObject]
           [org.bytedeco.javacpp PointerPointer IntPointer])
  (:gen-class))

(set! *warn-on-reflection* true)

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
                (let [w (QTextEdit.)]
                    (when on-change
                      (Qt5Widgets/QTextEdit_textChanged
                       w
                       (QObject.)
                       (proxy [Qt5Widgets$TextChangedCallback] []
                         (textChanged []
                           (on-change)))
                       0))
                    w)
                #_(let [w (proxy [QTextEdit] []
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
                 (doall (rest r))
                 r)
        children? (and remain (vector? (first remain)))
        children (when children? (mapv make-widgets remain))
        [kw ident] (parse-keyword t)
        con (get-constructor kw)
        ]

    (let [w (apply con opts (if children? (mapv :widget children) remain))]
      (when ident
        (swap! id-registry assoc ident w))
      {:widget w
       ;;:opts opts
       ;;:tag t
       ;;:remain remain
       :children children})))

;; (defn alter-widgets [widgets markup]
;;   (let [[t & r] markup
;;         opts (if (map? (first r))
;;                (first r)
;;                {})
;;         remain (if (map? (first r))
;;                  (rest r)
;;                  r)
;;         children? (and remain (vector? (first remain)))
;;         children (when children? (mapv make-widgets remain))
;;         [kw ident] (parse-keyword t)
;;         ]

;;     )
;;   )

0

(defn make-app []
  (QApplication.
             (IntPointer. (int-array [3]))
             (PointerPointer.
              ^"[Ljava.lang.String;"
              (into-array
               String
               ["gettingstarted"
                "-platformpluginpath"
                (libs/get-lib-dir)])
              )))

(defn quit-app [_]
  (QApplication/quit))

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

      (puget/cprint window)

      (System/exit (QApplication/exec)))

    )
  )
