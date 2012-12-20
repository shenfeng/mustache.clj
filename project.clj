(defproject me.shenfeng/mustache "1.1-SNAPSHOT"
  :description "Mustache write in java, for clojure"
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :warn-on-reflection true
  :java-source-path "src/java"
  :jar-exclusions [#".*java$"]
  :javac-options ["-source" "1.6" "-target" "1.6" "-g" "-encoding" "utf8"]
  :plugins [[lein-swank "1.4.4"]]
  :profiles {:dev {:dependencies [[junit/junit "4.8.2"]
                                  [stencil "0.3.0"]
                                  [de.ubercode.clostache/clostache "1.3.0"]]}})
