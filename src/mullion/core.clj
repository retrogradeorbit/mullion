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

(def lock (Object.))

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
   :push-button (fn [{:keys [on-click receiver]} ^String text]
                  (let [w (QPushButton. ^QString (QString/fromUtf8 text))]
                    (when on-click
                      (Qt5Widgets/QAbstractButton_clicked
                       w
                       receiver
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
  (QApplication/quit)
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (libs/init!)

  (when (= args '("--test-load"))
    (libs/debug-load)
    (System/exit 0))

  (libs/load-libs)
  (let [app (make-app)
        qobj (QObject.)
        ]
    (prn "App" app)
    (prn 'obj qobj)
    ;;(Thread/sleep 3000)
    (let [window (QWidget.)
          tree (make-widgets
                [:v-box-layout
                 [:label#time "0 seconds"]
                 ;;[:text-edit {:on-change #(println "change")}]
                 [:push-button
                  {:on-click quit-app
                   :receiver window} "&Quit"]
                 #_[:push-button {:on-click quit-app} "&Quit 2"]]

                #_[:widget {}
                   [:v-box-layout {}]])]

      ;;(puget/cprint window)


      (locking lock
        (.setLayout window (:widget tree))
        (.show ^QWidget window))

      ;; keep getting one of the following

      ;; - QWidget: Must construct a QApplication before a QWidget

      ;; - A fatal error has been detected by the Java Runtime Environment:SIGSEGV

      ;; # Problematic frame:
      ;; # C  [libQt5Widgets.so.5+0x19c075]  QWidget::show()+0x15

      ;; # Problematic frame:
      ;; # C  0x0000000000000000

      ;; - QPixmap: Must construct a QGuiApplication before a QPixmap

      ;; - QApplication::exec: Please instantiate the QApplication object first

      ;;      (process:18699): GLib-CRITICAL **: 01:11:53.628: g_main_context_pop_thread_default: assertion 'stack != NULL' failed
      ;; QGuiApplication::font(): no QGuiApplication instance and no application font set.
      ;; QGuiApplication::font(): no QGuiApplication instance and no application font set.
      ;; QGuiApplication::font(): no QGuiApplication instance and no application font set.
      ;; QGuiApplication::font(): no QGuiApplication instance and no application font set.

      ;; ... and sometimes it runs fine :shrug:
      (future
           (loop [c 1]
             (Thread/sleep 1000)
             (locking lock
               (->> c
                    (format "%d seconds")
                    QString/fromUtf8
                    (.setText (get-widget :time))))
             (recur (inc c))))


      #_(puget/cprint window)

      #_(.show (:widget window))

      #_ (future
           (Thread/sleep 1000)
           (quit-app true)
           )


      (System/exit (QApplication/exec)
                     )

      ))

  #_ (QApplication/exec)
  )
