(ns me.shenfeng.mustache-test
  (:use me.shenfeng.mustache
        clostache.parser
        clojure.test))

(deftest test-variable
  (let [t (mk-template "{{name}}")
        name "abcdefg"]
    (is (= name (to-html t {:name name}))))
  (let [e (mk-template "{{name}}")
        name "<b>"]
    (is (= "&lt;b&gt;" (to-html e {:name name}))))
  (let [e (mk-template "{{&name}}")
        name "<b>"]
    (is (= "<b>" (to-html e {:name name}))))
  (let [e (mk-template "{{{name}}}")
        name "<b>"]
    (is (= "<b>" (to-html e {:name name})))))

(deftest test-section
  (let [t (mk-template "{{#t}}true{{/t}}")]
    (is (= "true" (to-html t {:t true}))))
  (let [t (mk-template "{{#f}}false{{/f}}")]
    (is (= "" (to-html t {:t false})))))

(deftest test-array
  (let [t (mk-template "{{#arr}}{{.}}{{/arr}}")]
    (is (= "1234" (to-html t {:arr (range 1 5)})))))

(deftest test-comments
  (let [t (mk-template "{{!comment}}")]
    (is (= "" (to-html t {:data true})))))

(deftest test-partial
  (let [t (mk-template "Hello {{>partial}}!")]
    (is (= "Hello World"
           (to-html t {:name "World"} {:partial "{{name}}"})))))

(deftemplate template (slurp "test/tpl.tpl"))

(def data {:title "mustache.clj - Logic-less {{mustache}} templates for Clojure"
           :list (map (fn [id]
                        {:id id
                         :name (str "name" id)}) (range 1 4))})

(dotimes [i 100000]                     ; warm up
  (template data))

(deftemplates {:name_one "{{name}}"
               :name_two "Hello {{name}}, {{>name_one}}"})

(deftest test-deftemplates
  (is (= (name-one {:name "abc"}) "abc"))
  (is (= (name-two {:name "abc"}) "Hello abc, abc")))

(deftest test-resouces
  (is (seq (resources #".*shenfeng.*"))))

(println "Perf test: Render 10k Times\n"
         (slurp "test/tpl.tpl")
         "With data\n " data
         "Take: \n")
(time
 (dotimes [i 100000]
   template data))

;; (println "\nResult: \n"
;;          (to-html template data))


;; (deftemplate hello-world "{{name}}")
