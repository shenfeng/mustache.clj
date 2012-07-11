(ns me.shenfeng.perf-bench
  (:use me.shenfeng.mustache
        stencil.core)
  (:require [stencil.loader]))

(def template-str "
<!DOCTYPE html>
<html>
  <head>
    <title>{{ title }}</title>
  </head>
  <body>
    <ul>
      {{#list}}<li>id: {{id}}, name: {{name}}</li>
      {{/list}}
    </ul>
  </body>
</html>
")

(deftemplate template template-str)

(def data {:title "mustache clojure implementation"
           :list (concat [{:id "https://github.com/davidsantiago/stencil"
                           :name "stencil"}
                          {:id "https://github.com/shenfeng/mustache.clj"
                           :name "shenfeng"}]
                         (map (fn [id]
                                {:id id
                                 :name (str "test-test" id)}) (range 1 10)))})

(stencil.loader/register-template "hithere" template-str)

(defn bench [&{:keys [loops] :or {loops 100000}}]
  (println "warn up jvm")
  (dotimes [i loops]
    (to-html template data)
    (render-file "hithere" data))

  (println "bench stencil")
  ;; from a test run 6671.472264 msecs
  (time (dotimes [i loops]
          (render-file "hithere" data)))

  ;; from a tes run 625.272019 msecs
  (println "bench mustache.clj")
  (time (dotimes [i loops]
          ;; bench mustache.clj
          (to-html template data))))
