(ns me.shenfeng.mustache
  (:require [clojure.string :as str])
  (:import [me.shenfeng.mustache ResourceList Mustache Context]))

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
(defmacro deftemplate [name template & [partials]]
  `(let [tmpl# (Mustache/preprocess ~template)]
     (defn ~name
       ([]
          (.render tmpl# (Context. {} nil) ~partials))
       ([data#]
          (.render tmpl# (Context. data# nil) ~partials))
       ([data# partial#]
          (.render tmpl# (Context. data# nil) (or partial# ~partials))))))

(defn- gen-var [[tmpl-name template] tmpls]
  (let [name (str/replace (str/replace (str (.sym tmpl-name)) #"_" "-")
                          "/" "-")]
    (eval `(deftemplate ~(symbol name) ~template ~tmpls))))

(defn deftemplates [tmpls]
  (dorun (map #(gen-var %1 tmpls) tmpls)))

(defn resources [pattern] (ResourceList/getResources pattern))
