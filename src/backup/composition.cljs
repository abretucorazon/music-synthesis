(ns klangmeister.ui.composition
  (:require
    [goog.dom :as gdom]
    [om.next :as om :refer-macros [defui]]
    [om.dom :as dom]
    [klangmeister.ui.editor :as ed]
    [klangmeister.actions :as action]))


(def steps
  {:phrase
   ["A melody is a sequence of notes. Each note occurs at a particular time and with a particular pitch.
    We can compose a melody out of a list of the durations of each note and a list of the pitches of each note."
    "(phrase [3/3 3/3 2/3 1/3 3/3] ; You can change both the durations and the pitches.
        [ 72  72  72  74  76])"]
   :then
   ["We can combine two melodies to make a longer one."
    "(->>
  (phrase [3/3 3/3 2/3 1/3 3/3] ; Can you recognise the tune yet?
          [ 72  72  72  74  76])
  (then
    (phrase [2/3 1/3 2/3 1/3 3/3]
            [ 76  74  76  77  79])))"]
   :with
   ["Alternatively, we can play two melodies at the same time. If they're well-chosen, we'll get harmony. If not, we might
    still get something interesting."
   "(->> ; The arrow combines melodies like connect-> combines synthesiser nodes.
  (phrase [3/3 3/3 2/3 1/3 3/3]
          [ 72  72  72  74  76])
  (with
    (phrase [2/3 1/3 2/3 1/3 3/3]
            [ 76  74  76  77  79])))"]
   :times
   ["We can repeat melodies several times."
    "(->>
  (phrase [3/3 3/3 2/3 1/3 3/3]
          [ 72  72  72  74  76])
  (with
    (phrase [2/3 1/3 2/3 1/3 3/3]
            [ 76  74  76  77  79]))
  (times 2)) ; What happens if the times is put before the with? Why?"]
   :bpm
   ["So far every unit of time has been a second, in other words 60 beats per minute.
    If we want to speed up or slow down the music, we can define how many beats-per-minute there are. We use 'tempo' to
    update the time and duration of each note according to the chosen 'bpm'."
    "(->>
  (phrase [3/3 3/3 2/3 1/3 3/3]
          [ 72  72  72  74  76])
  (with
    (phrase [2/3 1/3 2/3 1/3 3/3]
            [ 76  74  76  77  79]))
  (times 2)
  (tempo (bpm 90))) ; Try making the music faster or slower."]
   :key
   ["We have to be careful which pitches we choose if we want the result to sound musical. A given piece of music focuses on certain pitches and ignores others, determined by the key of the piece. For example, 72 and 74 are in C major, but 73 is not. A convenient way to write songs in-key is to number the notes within the key and then later use code to translate those numbers into specific pitches. Here we use 'where' to put each note in C major."
    "(->>
  (phrase [3/3 3/3 2/3 1/3 3/3]
          [  0   0   0   1   2])
  (with
    (phrase [2/3 1/3 2/3 1/3 3/3]
            [  2   1   2   3   4]))
  (times 2)
  (tempo (bpm 90))
  (where :pitch (comp high C major))) ; How does minor sound different to major?"]})


(defn play-button [onClick]
  (dom/div #js {:className "controls"}
          (dom/button #js {:onClick onClick} "play")))


(defn section-ui [k handle! state]
  (let [[text code] (steps k)]
    (dom/div nil
       (dom/p nil text)
       (ed/editor {:target k :text code :handle! handle! :state state})
       (play-button #(handle! (action/->PlayOnce k))))))

(defui Composition-ui
  Object
  (render [this]
    (let [{:keys [handle! state]} (om/props this)]
      (dom/div nil
       (section-ui :phrase handle! state)
       (section-ui :then handle! state)
       (section-ui :with handle! state)
       (section-ui :times handle! state)
       (section-ui :bpm handle! state)
       (section-ui :key handle! state)
       (dom/div nil
          (dom/p nil "Now that you know how to design synthesisers and compose melodies, try "
            (dom/a #js {:href "/klangmeister/performance"} "putting the two together.")))
      (dom/div nil) (dom/p nil)))))

(def composition-ui (om/factory  Composition-ui))
