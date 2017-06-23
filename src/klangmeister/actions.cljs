(ns klangmeister.actions)

(defrecord Play [target])
(defrecord PlayOnce [target])
(defrecord Doc [string target])
(defrecord Test [target])
(defrecord Gist [gist target])
(defrecord Import [uri target])
(defrecord Loop [target])
(defrecord Stop [target])
(defrecord Refresh [text target])



(defn Play [target] {:target target})

(defn PlayOnce [target] {:target target})

(defn Doc [string target] {:string string :target target})

(defn Test [target] {:target target})

(defn Gist [gist target] {:gist gist :target target})

(defn Import [uri target] {:uri uri :target target})

(defn Loop [target] {:target target})

(defn Stop [target] {:target target})

(defn refresh [text target] {:text text :target target})
