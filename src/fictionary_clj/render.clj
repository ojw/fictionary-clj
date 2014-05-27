(ns fictionary-clj.render
  (:require [seesaw.core :as s])
  (:gen-class))

(defn generate-random-words [alphabet-size word-size word-count]
  (map
    (fn [x] (map (fn [x] (rand-int alphabet-size)) 
                 (range 0 (inc (rand-int word-size)))))
    (range 0 word-count)))

(defn render-letter [letter]
  (s/label :icon (str "madakna_middle/letters-" letter ".png")))

(defn render-word [[letters definition]]
  (let [image (map render-letter letters)
        def (s/text definition)]
    (s/horizontal-panel
     :size [600 :by 30]
     :items (flatten [image def]))))

(defn render-random-word [letters]
    (s/horizontal-panel :items [(render-word [letters ""]) 
                                (s/button :text "+")]))

(defn dictionary-content [dictionary]
   (map render-word dictionary))
