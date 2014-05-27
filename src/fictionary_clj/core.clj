(ns fictionary-clj.core
  (:require [seesaw.core :as s]
            [fictionary-clj.render :as r])
  (:gen-class))

(def alphabet (atom []))
(def dictionary (atom {}))
(def random-words (atom []))

(def main-window
  (s/frame
   :title "Fictionary"
   :size [800 :by 600]
   :content 
   (s/tabbed-panel
    :tabs [{:title "Alphabet"
            :content (s/grid-panel
                      :rows 8 :columns 6
                      :id :alphabet
                      :items [])}
           {:title "Dictionary"
            :content (s/vertical-panel
                      :id :dictionary
                      :items [])}
           {:title "New Words"
            :content (s/vertical-panel
                      :items [(s/vertical-panel
                               :id :random-words
                               :items [])
                              (s/horizontal-panel
                               :items [(s/button :text "New Random Words"
                                                 :listen [:action (fn [_] (swap! random-words (fn [_] (r/generate-random-words (count @alphabet) 10 15))))])
                                       
                                       (s/button :text "Keyboard"
                                                 :listen [:action (fn [e] (swap! dictionary #(assoc % [1 2 3 4 5] "Sample Word")))])])])}])))

(add-watch random-words 
           :render-random-words 
           (fn [key ref old-state new-state]
             (s/config! (s/select main-window [:#random-words]) 
                        :items (map r/render-random-word new-state))))
(add-watch dictionary 
           :render-dictionary 
           (fn [key ref old-state new-state]
             (s/config! (s/select main-window [:#dictionary]) 
                        :items (r/dictionary-content new-state))))
(add-watch alphabet 
           :render-alphabet 
           (fn [key ref old-state new-state]
             (s/config! (s/select main-window [:#alphabet]) 
                        :items (map r/render-letter new-state))))

(swap! alphabet (constantly (range 1 48)))
(swap! dictionary (constantly {[1 2 3] "Sample Word" [4 5 6 7] "Another Word"}))
(swap! random-words (constantly (r/generate-random-words (count @alphabet) 10 15)))

(defn -main [& args]
  (s/invoke-later
   (doto main-window
     s/show!)))
