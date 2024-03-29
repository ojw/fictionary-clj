(ns fictionary-clj.core
  (:require [seesaw.core :as s]
            [fictionary-clj.render :as r]
            [clojure.java.io :as io]
            [clojure.java.classpath :as cp])
  (:gen-class))

(defn- save-dictionary [dictionary]
  (if-let [dir (first (filter #(re-matches #".*resources" (str %)) (cp/classpath-directories)))]
    (do
      (spit (str dir "/dictionary.clj") dictionary)
      (s/alert "Save successful. :D"))
    (s/alert "ERROR!  Looks like saving doesn't work :/")))

(def alphabet (atom []))
(def dictionary (atom {}))
(def random-words (atom {}))

(def main-window
  (s/frame
   :title "Fictionary"
   :size [800 :by 600]
   :content 
   (s/tabbed-panel
    :tabs [{:title "Alphabet"
            :content (s/scrollable (s/grid-panel
                      :rows 8 :columns 6
                      :id :alphabet
                      :items []))}
           {:title "Dictionary"
            :content (s/scrollable (s/vertical-panel
                                    :items [(s/vertical-panel
                                             :id :dictionary
                                             :items [])
                                            (s/button :text "Save"
                                                      :listen [:action (fn [_] (save-dictionary @dictionary))])]))}
           {:title "New Words"
            :content (s/vertical-panel
                      :items [(s/vertical-panel
                               :id :random-words
                               :items [])
                              (s/horizontal-panel
                               :items [(s/button :text "New Random Words"
                                                 :listen [:action (fn [_] (swap! random-words (fn [_] (r/generate-random-words (count @alphabet) 10 15))))])
                                       
                                       (s/button :text "Keyboard"
                                                 :listen [:action (fn [_] (swap! dictionary #(assoc % [1 2 3 4 5] "Sample Word")))])])])}])))

(add-watch random-words 
           :render-random-words 
           (fn [key ref old-state new-state]
             (s/config! (s/select main-window [:#random-words]) 
                        :items (map (partial r/render-random-word dictionary random-words) new-state))))
(add-watch dictionary 
           :render-dictionary 
           (fn [key ref old-state new-state]
             (if (not= (count old-state) (count new-state))
               (s/config! (s/select main-window [:#dictionary]) 
                          :items (r/dictionary-content new-state)))))
(add-watch alphabet 
           :render-alphabet 
           (fn [key ref old-state new-state]
             (s/config! (s/select main-window [:#alphabet]) 
                        :items (map r/render-letter new-state))))

(swap! alphabet (constantly (range 1 48)))
(swap! dictionary (fn [_] (let [loaded (try (read-string (slurp (io/resource "dictionary.clj"))) (catch Exception e {}))] (if (map? loaded) loaded {}  ))))
(swap! random-words (constantly (r/generate-random-words (count @alphabet) 10 15)))

(defn -main [& args]
  (s/invoke-later
   (doto main-window
     s/show!)))
