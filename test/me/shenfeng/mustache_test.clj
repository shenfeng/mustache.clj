(ns me.shenfeng.mustache-test
  (:use me.shenfeng.mustache
        clostache.parser
        clojure.test))

(deftemplate template (slurp "test/sample.tpl"))
(println (to-html template {:arr [{:name "name1"}
                                  {:name "name2"}]}))


(defn- mk-link [i] {:favicon "http://dev-img1.mei.fm/crop?d=moc.bhhdb.www"
                    :id (+ 51685 i)
                    :title "http://www.bdhhb.com/Order.asp?types=2&Code=19"
                    :url "http://www.bdhhb.com/Order.asp?types=2&Code=19"})

(def data {:pager {:next_page 2
                   :page 1
                   :prev_page false
                   :total_page 7}
           :links (map mk-link (range 1 10))})

(deftemplate test-tpl (slurp "test/test.tpl"))
(time (dotimes [i 100000] (to-html test-tpl data)))

(def tmplate (slurp "test/test.tpl"))
(time (dotimes [i 1000] (render tmplate data)))
