(ns mullion.reflection
  (:require [cheshire.core :as cheshire]
            [clojure.java.io :as io]))

;; Write out graal reflection configs

(def jni-reflection
  {'org.bytedeco.javacpp.Loader {:conf #{:all}}
   'org.bytedeco.javacpp.Pointer {:conf #{:all}
                                  :fields #{'address 'position 'limit
                                            'capacity 'deallocator}}
   'org.bytedeco.javacpp.BytePointer {:conf #{:all}}
   'org.bytedeco.javacpp.ShortPointer {:conf #{:all}}
   'org.bytedeco.javacpp.IntPointer {:conf #{:all}}
   'org.bytedeco.javacpp.LongPointer {:conf #{:all}}
   'org.bytedeco.javacpp.FloatPointer {:conf #{:all}}
   'org.bytedeco.javacpp.DoublePointer {:conf #{:all}}
   'org.bytedeco.javacpp.CharPointer {:conf #{:all}}
   'org.bytedeco.javacpp.BooleanPointer {:conf #{:all}}
   'org.bytedeco.javacpp.PointerPointer {:conf #{:all}}
   'org.bytedeco.javacpp.BoolPointer {:conf #{:all}}
   'org.bytedeco.javacpp.CLongPointer {:conf #{:all}}
   'org.bytedeco.javacpp.SizeTPointer {:conf #{:all}}
   'java.lang.UnsatisfiedLinkError {:conf #{:all}}
   'java.lang.String {:conf #{:all}}
   'java.lang.Object {:conf #{:all}}
   'java.lang.NullPointerException {:conf #{:all}}
   'org.bytedeco.javacpp.Pointer$NativeDeallocator {:conf #{:all}
                                                    :fields #{'ownerAddress}}
   'java.nio.Buffer {:conf #{:all}
                     :fields #{'ownerAddress 'position 'limit 'capacity}}
   'org.bytedeco.javacpp.tools.Generator$BooleanEnum {:conf #{:all}
                                                      :fields #{'value}}
   'org.bytedeco.javacpp.tools.Generator$ByteEnum {:conf #{:all}
                                                   :fields #{'value}}
   'org.bytedeco.javacpp.tools.Generator$ShortEnum {:conf #{:all}
                                                    :fields #{'value}}
   'org.bytedeco.javacpp.tools.Generator$IntEnum {:conf #{:all}
                                                  :fields #{'value}}
   'org.bytedeco.javacpp.tools.Generator$LongEnum {:conf #{:all}
                                                   :fields #{'value}}
   'org.bytedeco.qt.Qt5Core.FromBase64Result {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QWidget {:methods
                                        {'closeEvent ['org.bytedeco.qt.Qt5Gui.QCloseEvent]}
                                        }
   'org.bytedeco.qt.helper.Qt5Widgets$ClickedCallback {:methods {'call ['int 'boolean]}}

   }

  )

(def svm-reflection
  {'org.bytedeco.javacpp.presets.javacpp {:conf #{:all}}
   'org.bytedeco.javacpp.Loader {:conf #{:all}}
   'org.bytedeco.qt.global.Qt5Core {:conf #{:all}}
   'org.bytedeco.javacpp.Pointer {:conf #{:all}
                                  :fields #{'address 'position 'limit
                                            'capacity 'deallocator}}
   'org.bytedeco.javacpp.BytePointer {:conf #{:all}}
   'org.bytedeco.javacpp.ShortPointer {:conf #{:all}}
   'org.bytedeco.javacpp.IntPointer {:conf #{:all}}
   'org.bytedeco.javacpp.LongPointer {:conf #{:all}}
   'org.bytedeco.javacpp.FloatPointer {:conf #{:all}}
   'org.bytedeco.javacpp.DoublePointer {:conf #{:all}}
   'org.bytedeco.javacpp.CharPointer {:conf #{:all}}
   'org.bytedeco.javacpp.BooleanPointer {:conf #{:all}}
   'org.bytedeco.javacpp.PointerPointer {:conf #{:all}}
   'org.bytedeco.javacpp.BoolPointer {:conf #{:all}}
   'org.bytedeco.javacpp.CLongPointer {:conf #{:all}}
   'org.bytedeco.javacpp.SizeTPointer {:conf #{:all}}
   'java.lang.UnsatisfiedLinkError {:conf #{:all}}
   'java.lang.String {:conf #{:all}}
   'java.lang.Object {:conf #{:all}}
   'org.bytedeco.javacpp.Pointer$NativeDeallocator {:conf #{:all}
                                                    :fields #{'ownerAddress}}
   'java.nio.Buffer {:conf #{:all}
                     :fields #{'ownerAddress 'position 'limit 'capacity}}
   'java.lang.NullPointerException {:conf #{:all}}
   'java.lang.ClassNotFoundException {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.QAbstractEventDispatcher$TimerInfo {:conf #{:all}}
   'java.lang.RuntimeException {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.FromBase64Result {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.QAbstractEventDispatcher {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.QByteArray {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.QObject {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.QChildEvent {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.QString {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.QCoreApplication {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.QDeferredDeleteEvent {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.QDynamicPropertyChangeEvent {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.QEvent {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.QEvent$Type {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.QEventLoop {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.QMessageLogContext {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.QMessageLogger {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.QSize {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.QTimerEvent {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.QVariant {:conf #{:all}}
   'org.bytedeco.qt.Qt5Core.QtMessageHandler {:conf #{:all}}
   'org.bytedeco.qt.Qt5Gui.QIcon {:conf #{:all}}
   'org.bytedeco.qt.Qt5Gui.QCloseEvent {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QAbstractButton {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QWidget {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QAbstractScrollArea {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QAbstractSpinBox {:conf #{:all}}
   'org.bytedeco.qt.Qt5Gui.QFont {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QMenu {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QAction {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QStyle {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QApplication {:conf #{:all}}
   'org.bytedeco.qt.helper.Qt5Widgets$QLayoutItem {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QBoxLayout {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QCheckBox {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QLineEdit {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QComboBox {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QDialog {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QFrame {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QGridLayout {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QGroupBox {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QHBoxLayout {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QLabel {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QLayout {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QPushButton {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QMessageBox {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QSizePolicy$ControlType {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QSizePolicy {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QAbstractSpinBox$StepType {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QSpinBox {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QSystemTrayIcon {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QTextEdit$ExtraSelection {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QTextEdit {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QToolButton {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QVBoxLayout {:conf #{:all}}
   'org.bytedeco.qt.helper.Qt5Gui$QPaintDevice {:conf #{:all}}
   'org.bytedeco.qt.helper.Qt5Widgets$MessageClickedCallback {:conf #{:all}}
   'org.bytedeco.qt.Qt5Widgets.QSystemTrayIcon$ActivationReason {:conf #{:all}}
   'org.bytedeco.qt.helper.Qt5Widgets$ActivatedCallback {:conf #{:all}}
   'org.bytedeco.qt.helper.Qt5Widgets$CurrentIndexChangedCallback1 {:conf #{:all}}
   'org.bytedeco.qt.helper.Qt5Widgets$TriggeredCallback {:conf #{:all}}
   'org.bytedeco.qt.helper.Qt5Widgets$ToggledCallback {:conf #{:all}}
   'org.bytedeco.qt.helper.Qt5Widgets$ClickedCallback {:conf #{:all}}
   'org.bytedeco.qt.Qt5Gui.QGuiApplication {:conf #{:all}}
   'org.bytedeco.qt.Qt5Gui.QTextCodec {:conf #{:all}}
   })

(defn make-reflection-json [reflection-config]
  (let [config
        (->> reflection-config
             (map (fn [[sym {:keys [conf fields methods]}]]
                    (let [struct {"name" (name sym)}
                          struct (if (:all conf)
                                   (assoc struct
                                          "allPublicMethods" true
                                          "allPublicConstructors" true
                                          "allPublicClasses" true
                                          "allDeclaredMethods" true
                                          "allDeclaredConstructors" true
                                          "allDeclaredClasses" true
                                          )
                                   struct)
                          struct (if fields
                                   (assoc struct
                                          "fields"
                                          (into []
                                                (for [field fields]
                                                  {"name" (name field)
                                                   "allowWrite" true})))
                                   struct)
                          struct (if methods
                                   (assoc struct
                                          "methods"
                                          (into []
                                                (for [[method params] methods]
                                                  {"name" (name method)
                                                   "parameterTypes" (mapv name params)})))
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
