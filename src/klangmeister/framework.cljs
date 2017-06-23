(ns klangmeister.framework
  (:require
    [goog.dom :as gdom]
    [om.next :as om :refer-macros [defui]]
    [om.dom :as dom]
    [klangmeister.processing :as proc]
  ))

;(defprotocol Action
;  "Calculate the new state and `handle!` any new Actions that arise."
;  (process [this handle! state]))

(declare handler-for)

(defn apply-action!
  "Update the atom using the action."
  [state-atom action]
  (swap!
    state-atom
    (fn [state] state)));(process action (handler-for state-atom) state))))

(defn handler-for
  "Build a `handle!` function for the atom."
  [state-atom]
  (partial apply-action! state-atom))


;(def handle!
;  "An handler that components can use to raise events."
;  (framework/handler-for state-atom))



;======= Read =========
;
(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state] :as env} key params]
  (let [st @state]
    (if-let [[_ value] (find st key)]
      {:value value}
      {:value :not-found})))



;======= Mutate =========
;
(defmulti mutate om/dispatch)

(defmethod mutate 'code/Refresh
  [{:keys [state] :as env} key params]
  {:action #(swap! state proc/refresh params)})


;(defmethod mutate 'code/Gist
;  [{:keys [state] :as env} key params]
;    {:action #(swap! state gist params)})

;(defmethod mutate 'music/Stop
;  [{:keys [state] :as env} key params]
;    {:action #(swap! state stop params)})

(defmethod mutate 'music/PlayOnce
  [{:keys [state] :as env} key params]
    {:action #(swap! state proc/play-once params)})


(defmethod mutate 'code/Doc
  [{:keys [state] :as env} key params]
    {:action #(swap! state proc/doc params)})


(defmethod mutate :default
  [{:keys [state] :as env} action params]
    {:action #()}) ; #(swap! state update-in [:count] inc)}

