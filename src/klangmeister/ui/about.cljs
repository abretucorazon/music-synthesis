(ns klangmeister.ui.about
  (:require
    [goog.dom :as gdom]
    [om.next :as om :refer-macros [defui]]
    [om.dom :as dom]
    [klangmeister.ui.editor :as ed]
    [klangmeister.actions :as action]
    [klangmeister.framework :as framework]
    ))

(declare code)

; Data to initialize About page in global app state
(def init-data  {:about {:text code :value nil :doc nil :error nil}})


(def code "(->> (phrase (repeat 1/16) [0 2 4 7 9 11 14 16 18 21])
                    (where :pitch (comp C major)))")

(def intro "Klangmeister is a live coding environment for the browser. It lets you design synthesisers and compose music using
             computer code - without having to install anything on your own computer.")

(defn footnote-ui[]
  (dom/div nil
    (dom/p nil "Klangmeister works best in Chrome, because the other browsers have less stable implementations of the synthesis features
              that Klangmeister relies on. They're working on it though.")
    (dom/p nil (dom/a #js {:href "https://twitter.com/ctford"} "I") " recommend starting with the "
                        (dom/a #js {:href "/klangmeister/synthesis"} "synthesis tutorial") ".")))

(defn control-ui [onClick]
  (dom/div #js {:className "controls"}
          (dom/button #js {:onClick onClick} "play")))

(defn on-click [component]
  (let [param {:target :about}]
    (om/transact! component `[(music/PlayOnce ~param)])))

(defui About-ui
  static om/IQuery
  (query [this]
         [:about])

  Object
  (render [this]
    (let [{:keys [text doc error]} (om/props this)]
      (dom/div nil
          (dom/p nil intro)
          (ed/editor {:target :about :text code :doc doc :error error})
          (control-ui (fn [e] (on-click this)))
          (footnote-ui)
          ))))

(def about-ui (om/factory  About-ui))
