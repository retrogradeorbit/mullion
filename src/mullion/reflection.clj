(ns mullion.reflection
  (:require [cheshire.core :as cheshire]))

;; Write out graal reflection configs

(def jni-reflection
  {'org.bytedeco.javacpp.Loader {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.Pointer {:conf #{:all-public-*}
                                  :fields #{'address 'position 'limit
                                            'capacity 'deallocator}}
   'org.bytedeco.javacpp.BytePointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.ShortPointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.IntPointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.LongPointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.FloatPointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.DoublePointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.CharPointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.BooleanPointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.PointerPointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.BoolPointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.CLongPointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.SizeTPointer {:conf #{:all-public-*}}
   'java.lang.UnsatisfiedLinkError {:conf #{:all-public-*}}
   'java.lang.String {:conf #{:all-public-*}}
   'java.lang.NullPointerException {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.Pointer$NativeDeallocator {:conf #{:all-public-*}
                                                    :fields #{'ownerAddress}}
   'java.nio.Buffer {:conf #{:all-public-*}
                     :fields #{'ownerAddress 'position 'limit 'capacity}}
   'org.bytedeco.javacpp.tools.Generator$BooleanEnum {:conf #{:all-public-*}
                                                      :fields #{'value}}
   'org.bytedeco.javacpp.tools.Generator$ByteEnum {:conf #{:all-public-*}
                                                   :fields #{'value}}
   'org.bytedeco.javacpp.tools.Generator$ShortEnum {:conf #{:all-public-*}
                                                    :fields #{'value}}
   'org.bytedeco.javacpp.tools.Generator$IntEnum {:conf #{:all-public-*}
                                                  :fields #{'value}}
   'org.bytedeco.javacpp.tools.Generator$LongEnum {:conf #{:all-public-*}
                                                   :fields #{'value}}})

(def svm-reflection
  {'org.bytedeco.javacpp.presets.javacpp {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.Loader {:conf #{:all-public-*}}
   'org.bytedeco.qt.global.Qt5Core {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.Pointer {:conf #{:all-public-*}
                                  :fields #{'address 'position 'limit
                                            'capacity 'deallocator}}
   'org.bytedeco.javacpp.BytePointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.ShortPointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.IntPointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.LongPointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.FloatPointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.DoublePointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.CharPointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.BooleanPointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.PointerPointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.BoolPointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.CLongPointer {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.SizeTPointer {:conf #{:all-public-*}}
   'java.lang.UnsatisfiedLinkError {:conf #{:all-public-*}}
   'java.lang.String {:conf #{:all-public-*}}
   'org.bytedeco.javacpp.Pointer$NativeDeallocator {:conf #{:all-public-*}
                                                    :fields #{'ownerAddress}}
   'java.nio.Buffer {:conf #{:all-public-*}
                     :fields #{'ownerAddress 'position 'limit 'capacity}}
   'java.lang.NullPointerException {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Core.QAbstractEventDispatcher$TimerInfo {:conf #{:all-public-*}}
   'java.lang.RuntimeException {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Core.QAbstractEventDispatcher {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Core.QByteArray {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Core.QObject {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Core.QChildEvent {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Core.QString {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Core.QCoreApplication {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Core.QDeferredDeleteEvent {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Core.QDynamicPropertyChangeEvent {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Core.QEvent {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Core.QEvent$Type {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Core.QEventLoop {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Core.QMessageLogContext {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Core.QMessageLogger {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Core.QSize {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Core.QTimerEvent {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Core.QVariant {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Core.QtMessageHandler {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Gui.QIcon {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Gui.QCloseEvent {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QAbstractButton {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QWidget {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QAbstractScrollArea {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QAbstractSpinBox {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Gui.QFont {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QMenu {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QAction {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QStyle {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QApplication {:conf #{:all-public-*}}
   'org.bytedeco.qt.helper.Qt5Widgets$QLayoutItem {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QBoxLayout {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QCheckBox {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QLineEdit {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QComboBox {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QDialog {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QFrame {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QGridLayout {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QGroupBox {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QHBoxLayout {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QLabel {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QLayout {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QPushButton {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QMessageBox {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QSizePolicy$ControlType {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QSizePolicy {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QAbstractSpinBox$StepType {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QSpinBox {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QSystemTrayIcon {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QTextEdit$ExtraSelection {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QTextEdit {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QToolButton {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QVBoxLayout {:conf #{:all-public-*}}
   'org.bytedeco.qt.helper.Qt5Gui$QPaintDevice {:conf #{:all-public-*}}
   'org.bytedeco.qt.helper.Qt5Widgets$MessageClickedCallback {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Widgets.QSystemTrayIcon$ActivationReason {:conf #{:all-public-*}}
   'org.bytedeco.qt.helper.Qt5Widgets$ActivatedCallback {:conf #{:all-public-*}}
   'org.bytedeco.qt.helper.Qt5Widgets$CurrentIndexChangedCallback1 {:conf #{:all-public-*}}
   'org.bytedeco.qt.helper.Qt5Widgets$TriggeredCallback {:conf #{:all-public-*}}
   'org.bytedeco.qt.helper.Qt5Widgets$ToggledCallback {:conf #{:all-public-*}}
   'org.bytedeco.qt.helper.Qt5Widgets$ClickedCallback {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Gui.QGuiApplication {:conf #{:all-public-*}}
   'org.bytedeco.qt.Qt5Gui.QTextCodec {:conf #{:all-public-*}}
   })

(defn make-reflection-json [reflection-config]
  (let [config
        (->> reflection-config
             (map (fn [[sym {:keys [conf fields]}]]
                    (let [struct {"name" (name sym)}
                          struct (if (conf :all-public-*)
                                   (assoc struct
                                          "allPublicMethods" true
                                          "allPublicConstructors" true
                                          "allPublicClasses" true)
                                   struct)
                          struct (if fields
                                   (assoc struct
                                          "fields"
                                          (into []
                                                (for [field fields]
                                                  {"name" (name field)
                                                   "allowWrite" true})))
                                   struct)]
                      struct))))]
    (cheshire/generate-string config {:pretty true})))

(defonce _write-svm-reflection
  (when-not (System/getProperty "org.graalvm.nativeimage.imagecode")
    (when (.isDirectory (io/file "graal-configs"))
      (spit "graal-configs/reflect-config.json"
            (make-reflection-json svm-reflection)))))

(defonce _write-jni-reflection
  (when-not (System/getProperty "org.graalvm.nativeimage.imagecode")
    (when (.isDirectory (io/file "graal-configs"))
      (spit "graal-configs/jni-config.json"
            (make-reflection-json jni-reflection)))))
