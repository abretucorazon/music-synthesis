(ns klangmeister.ui.jam
  (:require
    [goog.dom :as gdom]
    [om.next :as om :refer-macros [defui]]
    [om.dom :as dom]
    [klangmeister.ui.graph :as graph]
    [klangmeister.actions :as action]
    [klangmeister.ui.editor :as ed]))


(defn control-ui [looping? OnClickLoop OnClickStop]
  (let [play-btn (if-not looping?
                   (dom/button #js {:onClick OnClickLoop} "Loop")
                   (dom/button #js {:onClick OnClickStop} "Stop"))]
       (dom/div #js {:className "controls"} play-btn)))


(defui Jam-ui
  Object
  (render [this]
    (let [{:keys [handle! state]} (om/props this)
          {:keys [looping?]}      (:main state)]
      (dom/div nil
         (graph/graph-ui {:state state})
         (ed/editor {:target :main :text (-> state :main :text) :handle! handle! :state state})
         (control-ui looping? #(handle! (action/->Play :main)) #(handle! (action/->Stop :main)))
         (dom/div nil
          (dom/p nil "Now we know how to make both instruments and melodies, we can create whole songs. To make the experience more interactive, lets graph the notes we're playing, and loop them over and over again as we edit the synthesiser and the composition.")
          (dom/p nil "If you find anything confusing, refer back to the "
                 (dom/a #js {:href "/klangmeister/synthesis"} "synthesis tutorial ") "or the "
                 (dom/a #js {:href "/klangmeister/composition"} " composition tutorial") ". There is a list of available functions in the "
                 (dom/a #js {:href "/klangmeister/reference"} "reference") "."))))))


(def jam-ui (om/factory  Jam-ui))
