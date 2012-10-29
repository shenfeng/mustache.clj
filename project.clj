(defproject me.shenfeng/mustache "0.0.7"
  :description "Mustache write in java, for clojure"
  :dependencies [[clojure "1.4.0"]]
  :warn-on-reflection true
  :java-source-path "src/java"
  :javac-options {:source "1.6" :target "1.6" :debug "true" :fork "true"}
  :dev-dependencies [[swank-clojure "1.4.0"]
                     [junit/junit "4.8.2"]
                     [stencil "0.3.0"]
                     [de.ubercode.clostache/clostache "1.3.0"]])

