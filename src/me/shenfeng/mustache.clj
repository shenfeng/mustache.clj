(ns me.shenfeng.mustache
  (:require [clojure.string :as str]
            [clojure.java.io :as io])
  (:import [me.shenfeng.mustache ResourceList Mustache Context]
           java.io.File
           clojure.lang.Keyword))

(defn- get-content [file]
  (slurp (or (io/resource file)
             (try (io/reader file) (catch Exception e)))))

(defn- get-name [^String file ^String folder]
  (let [idx (.indexOf file folder)
        remain (.substring file (+ idx (count folder) 1))]
    ;; drop extention
    (keyword (.substring remain 0
                         (.lastIndexOf remain (int \.))))))

(defn- get-tmpls [files folder]
  (reduce (fn [m f]
            (assoc m
              (get-name f folder) (get-content f)))
          {} files))

(defn- tmpls-from-rerouces [folder extentions]
  (let [^File dir (if (instance? File folder) folder (File. ^String folder))]
    (get-tmpls (map str
                    (filter
                     (fn [^File f]
                       (and (.isFile f)
                            (some (fn [e]
                                    (.endsWith (.getName f) e)) extentions)))
                     (file-seq dir)))
               (.getName dir))))

(defn- tmpls-from-folder [folder extentions]
  (get-tmpls (ResourceList/getResources folder extentions) folder))

(defn- fn-name [n]         ; app/search_result => app-search-result
  (symbol (str/replace (str (.sym ^Keyword n)) #"_|/" "-")))

;; ---------------------- public functions ------------------------------

;;; from string to Mustache
(defn mk-template [template]
  (Mustache/preprocess template))

(defn to-html
  ([^Mustache template data]
     (let [^Context c (Context. data nil)]
       (.render template c nil)))
  ([^Mustache template data partial]
     (let [^Context c (Context. data nil)]
       (.render template c partial))))

;;; template is the string of template
;;; tran is a fn, take context, return context
(defmacro deftemplate [name template & [partials tran]]
  `(let [tmpl# (Mustache/preprocess ~template)
         f# (or ~tran identity)]
     (defn ~name [& [~'data ~'partials]] ; better names for tools
       (let [cxt# (Context. (f# (or ~'data {})))]
         (.render tmpl# cxt# (or ~'partials ~partials))))))

(defmacro gen-tmpls-from-resources [folder extentions & [tran]]
  (.clear Mustache/CACHE)               ; clear paritials cache
  (let [tmpls (tmpls-from-rerouces folder extentions)
        defs (map (fn [[name template]]
                    `(deftemplate ~(fn-name name) ~template ~tmpls ~tran))
                  tmpls)]
    `(do ~@defs)))

(defmacro gen-tmpls-from-folder [folder extentions & [tran]]
  (.clear Mustache/CACHE)               ; clear paritials cache
  (let [tmpls (tmpls-from-folder folder extentions)
        defs (map (fn [[name template]]
                    `(deftemplate ~(fn-name name) ~template ~tmpls ~tran))
                  tmpls)]
    `(do ~@defs)))