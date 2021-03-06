(ns klangmeister.ui.reference
    (:require
      [goog.dom :as gdom]
      [om.next :as om :refer-macros [defui]]
      [om.dom :as dom]
))


(def signals
  {"sawtooth" ["A waveform with prominent harmonics" "(sawtooth 440)"]
   "sine" ["A simple sine wave" "(sawtooth 440)"]
   "square" ["A wave containing only odd harmonics" "(square 440)"]
   "triangle" ["A wave with subtle harmonics" "(triangle 440)"]
   "white-noise" ["Random noise" "white-noise"]})

(def shapers
  {"gain" ["Multiply a signal by the given factor." "(gain 0.5)"]
   "high-pass" ["Cut out low frequencies." "(high-pass 500)"]
   "low-pass" ["Cut out high frequencies." "(low-pass 1000)"]})

(def envelopes
  {"adsr" ["An envelope with attack, decay, sustain and release." "(adsr 0.1 0.2 0.5 0.1)"]
   "percussive" ["A simple envelope with just attack and decay" "(percussive 0.1 0.3)"]})

(def combinators
  {"add" ["Add two or more signals together." "(add
  (sine 440)
  (sine 660))"]
   "connect->" ["Connect two or more signal processors together" "(connect->
  (sawtooth 440)
  (low-pass 800))"]})

(def melody-builders
  {"phrase" ["Turn a list of durations and a list of pitches into a melody."
             "(phrase
  [1 1 2/3 1/3 1]
  [0 0   0   2 3])"]})

(def melody-combinators
  {"then" ["Sequence two melodies together."
           "(->> intro
  (then verse))"]
   "times" ["Repeat a melody."
            "(->> chorus
  (times 4))"]
   "with" ["Overlay two melodies."
           "(->> bass
  (with drums))"]})

(def scalars
  {"A..G" ["The different musical keys."
"(->> piece
  (where :pitch
    (comp C major)))"]
           "all" ["Set a property of every note in a melody."
"(->> solo
  (all :instrument horn))"]
           "bpm" ["Specify a beats-per-minute tempo."
                  "(->> piece
  (tempo (bpm 120)))"]
           "flat" ["Lower a scale by a semitone"
"(->> piece
  (where :pitch
    (comp B flat major)))"]
           "sharp" ["Raise a scale by a semitone"
"(->> piece
  (where :pitch
    (comp B flat major)))"]
           "high" ["Raise a scale by an octave."
"(->> piece
  (where :pitch
    (comp low C major)))"]
           "low" ["Lower a scale by an octave."
"(->> piece
  (where :pitch
    (comp low C major)))"]
           "major" ["A happy-sounding scale."
"(->> piece
  (where :pitch
    (comp C major)))"]
           "minor" ["A sad-sounding scale."
"(->> piece
  (where :pitch
    (comp D minor)))"]
           "where" ["Update a particular attribute of each note in a melody."
"(->> piece
  (where :pitch
    (comp D minor)))"]
           "tempo" ["Put a melody in a tempo at a particular beats-per-minute."
"(->> piece
  (tempo (bpm 120)))"]})

(def various-keys
  (->> "ABCDEFG"
       (map (fn [k]
              [(str k) ["A musical key." (str "(->> piece (where :pitch (comp " k " major)))")]]))
       (reduce conj {})))

(def core-fns
  {"comp" ["Compose two or more functions together" "comp not even?"]
   "comment" ["Comment out code." "(comment This won't be evaluated."]
   "->>" ["Thread together seveal operations on a sequence." "(->> (range 0 10) (map inc))"]})

(def all (merge signals shapers envelopes combinators melody-builders melody-combinators scalars various-keys core-fns))

(defn table [defs]
  (dom/table #js {:className "reference"}
   (dom/thead nil
     (dom/tr nil
       (dom/th nil "")
       (dom/th nil "Description")
       (dom/th nil "Usage")))
   (dom/tbody nil
    (map
      (fn [[name [description usage]]]
        (dom/tr nil
         (dom/td #js {:className "name"} name)
         (dom/td nil description)
         (dom/td #js {:className "usage"} usage)))
      (sort-by first defs)))))


(defui Ref-ui
  Object
  (render [this]
    (dom/div nil
     (dom/h2 nil "Synthesis")
     (dom/p nil "These functions are used to generate signals that can then be shaped or combined with other signals.")
     (table signals)
     (dom/p nil "These functions are used to shape other signals.")
     (table shapers)
     (dom/p nil "These functions define the shape of a note.")
     (table envelopes)
     (dom/p nil "These functions combine signals together.")
     (table combinators)
     (dom/h2 nil "Composition")
     (dom/p nil "These functions are used to build melodies.")
     (table melody-builders)
     (dom/p nil "These functions are used to combine melodies.")
     (table melody-combinators)
     (dom/p nil "These functions are used to put melodies into specific keys and tempos.")
     (table scalars))))

(def ref-ui (om/factory  Ref-ui))
