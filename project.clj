(defproject fictionary-clj "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [seesaw "1.4.4"]]
  ; :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :aot [fictionary-clj.core]
  :main fictionary-clj.core)
