(ns me.shenfeng.mustache
  (:require [clojure.string :as str]
            [clojure.java.io :as io])
  (:import [me.shenfeng.mustache ResourceList Mustache Context]
           java.io.File))

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
     (defn ~name
       ([]
          (.render tmpl# (Context. (f# {})) ~partials))
       ([data#]
          (.render tmpl# (Context. (f# data#)) ~partials))
       ([data# partial#]
          (.render tmpl# (Context. (f# data#)) (or partial# ~partials))))))

(defn- get-content [file]
  (slurp (or (io/resource file)
             (try (io/reader file) (catch Exception e)))))

(defn- get-name [^String file ^String folder]
  (let [idx (.indexOf file folder)
        remain (.substring file (+ idx (count folder) 1))]
    ;; drop extention
    (keyword (.substring remain 0
                         (.lastIndexOf remain (int \.))))))

(defn deftemplates [tmpls & [tran]]
  (doseq [[^clojure.lang.Keyword name template] tmpls]
    (let [name (str/replace (str (.sym name)) #"_|/" "-")]
      (eval `(deftemplate ~(symbol name) ~template ~tmpls ~tran)))))

(defn- gen-vars [folder files tran]
  (let [tmpls (reduce (fn [m f]
                        (assoc m
                          (get-name f folder) (get-content f)))
                      {} files)]
    (deftemplates tmpls tran)))

(defn mktmpls-from-folder [folder extentions & [tran clear?]]
  (when clear? (.clear Mustache/CACHE))
  (let [^File dir (if (instance? File folder) folder (File. ^String folder))]
    (gen-vars (.getName dir)
              (map str (filter
                        (fn [^File f]
                          (and (.isFile f)
                               (some (fn [e]
                                       (.endsWith (.getName f) e)) extentions)))
                        (file-seq dir)))
              tran)))

(defn mktmpls-from-resouces [folder extentions & [tran clear?]]
  (when clear? (.clear Mustache/CACHE))
  (gen-vars folder (ResourceList/getResources folder extentions) tran))