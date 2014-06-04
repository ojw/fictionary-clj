(ns fictionary-clj.render
  (:require [seesaw.core :as s])
  (:gen-class))

(defn generate-random-words [alphabet-size word-size word-count]
  (zipmap
  (map
    (fn [x] (map (fn [x] (inc (rand-int alphabet-size)))
                 (range 0 (inc (rand-int word-size)))))
    (range 0 word-count)) (repeat "")))

(defn render-letter [letter]
  (let [padded-letter (if (< letter 10) (str "0" letter) letter)]
    (s/label :icon (str "alphabet/letters-" padded-letter ".png"))))

(defn render-word [letters]
  (map render-letter letters))
  
(defn render-dictionary-entry [[letters definition]]
  (let [image (render-word letters)]
    (s/horizontal-panel
     :size [600 :by 30]
     :items (flatten [image (s/text definition)]))))

(defn render-random-word [dictionary random-words [letters description]]
  (let [text-box (s/text :text description :id letters)]
    (s/horizontal-panel :items (flatten [(render-word letters)
                                         text-box
                                         (s/button :text "+"
                                                   :listen [:action (fn [e] (swap! dictionary #(assoc % letters (s/config text-box :text))))])]))))

(defn dictionary-content [dictionary]
  (map render-dictionary-entry dictionary))
