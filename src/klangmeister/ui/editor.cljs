(ns klangmeister.ui.editor
  (:require
    [goog.dom :as gdom]
    [om.next :as om :refer-macros [defui]]
    [om.dom :as dom]
    [klangmeister.actions :as action]
    [klangmeister.compile.eval :as eval]
    [klangmeister.framework :as framework]
))


;;(def init-data {:editor {:target nil}})



(defn current-token [editor]
  (-> editor
      (.getTokenAt (.getCursor editor))
      .-string))


; Update app-state with edited content
(defn refresh [component text target]
  (let [param {:text text :target target}] ;(action/Refresh text target)]
    (om/transact! component `[(code/Refresh ~param)])))


; Handler for editing activities
(defn on-change [component editor target]
  (refresh component (.getValue editor) target))


; Handler function for cursor change events
(defn on-cursor-activity [component editor target]
  (let [param (action/Doc (current-token editor) target)]
       (om/transact! component `[(code/Doc ~param)])))


(defui Editor-ui
;;  static om/IQuery
;;     (query [this]
;;         [:target])

  Object

  (componentDidMount [this]
      (let [{:keys [target text]} (om/props this)
            pane (.fromTextArea js/CodeMirror
                     (-> (dom/node this) (.getElementsByClassName "text") (aget 0)) ;; DOM node of textarea element
                     #js {:mode "clojure"
                          :theme "solarized"
                          :lineNumbers true
                          :matchBrackets true
                          :autoCloseBrackets true
                          :lineWrapping true
                          :viewportMargin js/Infinity})]
          (.on pane "change"         #(on-change this % target))
          (.on pane "cursorActivity" #(on-cursor-activity this % target))
          (refresh this text target)
    ))


  (render [this]
    (let [{:keys [target error doc text]} (om/props this)
          [function docstring example]     doc]
      (dom/div #js {:className(str "editor" (if error " error" ""))}
          (dom/textarea #js  {:defaultValue text :autoComplete "off" :className "text"})
          (dom/div #js {:className "doc"}
                  (when doc (dom/div nil (concat function ": " docstring)))
                  (when doc (dom/div #js {:className "example"} example))
              )))
  ))




(def editor (om/factory  Editor-ui))
