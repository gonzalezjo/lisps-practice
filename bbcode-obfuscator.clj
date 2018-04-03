(ns example
  (:gen-class
   :main false
   :state obstate))

; Port of a script I originally wrote in Lua (MyBBMagic) 
; It can be written in a bunch of ways, and involving state, string manipulation, etc., 
; So I like to use the concept to practice things.

; Formatting is horrible because I usd some weird Scheme formatter online. 
; It was worse before.
; Sorry anyway.

(defn to-mut [func] {:mut func, :open (atom false)})

(def muts [
           (to-mut (fn [char opening] (if opening (str "[b]" char) (str "[/b]" char))))
           (to-mut (fn [char opening] (if opening (str "[i]" char) (str "[/i]" char))))
           (to-mut (fn [char opening] (if opening (str "[u]" char) (str "[/u]" char))))])

(defn mut-char [char]
  (let [ mut (rand-nth muts) state (:open mut) ] ((:mut mut) char (swap! state not))))

(defn mut-char-duo [char
                    char-two]
  (if (clojure.string/blank? (str char-two)) (mut-char char) (str char (mut-char char-two))))

(defn mut-epilogue []
  (clojure.string/join (mapv #((:mut %) "" false) (filterv #(= true @ (:open %)) muts))))

(defn mut-str [string]
  (if-not (clojure.string/blank? string) (str (reduce mut-char-duo (seq string)) (mut-epilogue))
    "No string passed."))

(defn -main []
  (println (mut-str "Obfuscated")))

(compile 'example)
