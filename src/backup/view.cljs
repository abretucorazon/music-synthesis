(ns klangmeister.ui.view
  (:require
    [goog.dom :as gdom]
    [om.next :as om :refer-macros [defui]]
    [om.dom :as dom]
    [klangmeister.ui.jam :as jam]
    [klangmeister.ui.composition :as comp]
    [klangmeister.ui.reference :as ref]
    [klangmeister.ui.about :as about]
    [klangmeister.ui.synthesis-tutorial :as synth]))


(defn link [current this href title]
  (if (= current this)
    title
    (dom/a #js {:href href} title)))

(defn tabs [current]
  (dom/div #js {:id "menu"}
   (dom/ul nil
    (dom/li nil (link current :synthesis "/klangmeister/synthesis" "Synthesis"))
    (dom/li nil (link current :composition "/klangmeister/composition" "Composition"))
    (dom/li nil (link current :performance "/klangmeister/performance" "Performance"))
    (dom/li nil (link current :reference "/klangmeister/reference" "Reference"))
    (dom/li nil (link current :about "/klangmeister/about" "About")))))


(defn frame [current content-ui this]
   (let [{:keys [handle! state]} (om/props this)]
      (dom/div nil
         (tabs current)
         (content-ui {:handle! handle! :state state})
       )))


(defui Performance-ui
  Object
  (render [this]
    (frame :performance #(jam/jam-ui %) this)))

(defui Synthesis-ui
  Object
  (render [this]
    (frame :synthesis #(synth/synth-ui %) this)))

(defui About-ui
  Object
  (render [this]
    (frame :about #(about/about-ui %) this)))

(defui Composition-ui
  Object
  (render [this]
    (frame :composition #(comp/composition-ui %) this)))

(defui Reference-ui
  Object
  (render [this]
    (frame :reference #(ref/ref-ui %) this)))


;; Routes definitions for Compassus
(def routes {:synthesis    Synthesis-ui
             :composition  Composition-ui
             :performance  Performance-ui
             :reference    Reference-ui
             :about        About-ui})

(def index-route :about) ;; Default route

(def bidi-routes{"synthesis"      :synthesis
                 "composition"    :composition
                 "performance"    :performance
                 "reference"      :reference
                 "about"          :about})
