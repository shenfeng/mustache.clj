(ns me.shenfeng.mustache-test
  (:use me.shenfeng.mustache
        clostache.parser
        clojure.test))

(deftemplate template (slurp "test/sample.tpl"))

(def data {:variable "Value with <unsafe> data"
           :arr [{:name "name1"}, {:name "name2"}]
           :item_list (map (fn [id]
                             {:id id
                              :name (str "abc " id)}) (range 1 20))})

(def partials {:partial (slurp "test/tpl.tpl")})

(println (to-html template data partials))

(dotimes [i 5]
  (time (dotimes [i 10000]
          (to-html template data partials))))

(time (dotimes [i 100000]
        (to-html template data partials)))

(time
 (dotimes [i 100000]
   (to-html template data partials)))
