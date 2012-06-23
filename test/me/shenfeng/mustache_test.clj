(ns me.shenfeng.mustache-test
  (:use me.shenfeng.mustache
        clostache.parser
        clojure.test))

(def data {:pager {:next_page 2
                   :page 1
                   :prev_page false
                   :total_page 7}
           :links (repeat 10
                          {:favicon "http://dev-img1.mei.fm/crop?d=moc.bhhdb.www"
                           :id 51685
                           :title "http://www.bdhhb.com/Order.asp?types=2&Code=19"
                           :url "http://www.bdhhb.com/Order.asp?types=2&Code=19"})})

(def tmplate (slurp "test/test.tpl"))

(deftemplate test-tpl (slurp "test/test.tpl"))

(time (dotimes [i 2000] (render tmplate data)))
(time (dotimes [i 2000] (to-html test-tpl data)))

;; (println (to-html test-tpl data))

;; (render (slurp "tmpls/test.tpl") data)

(deftemplate template
  "<p>this is a test</p> <ul> {{#arr}} <li> <p>{{{ name }}}</p> <div> {{#arr}} <p>{{ name }}</p> {{/arr}} </div> {{/arr}} </li> </ul>")

(def data2 {:arr [{:name "outer",
                   :arr [{:name "nested"}]
                   }, {
                       :name "outer",
                       :arr [{:name "nested"}]
                       }]})

(deftest test-mustche
  (println (to-html template data2)))
