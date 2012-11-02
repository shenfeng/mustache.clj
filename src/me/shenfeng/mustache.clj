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

(defn- gen-var [[tmpl-name template] tmpls tran]
  (let [name (str/replace (str/replace (str (.sym tmpl-name)) #"_" "-")
                          "/" "-")]
    (eval `(deftemplate ~(symbol name) ~template ~tmpls ~tran))))

(defn deftemplates [tmpls & [tran]]
  (dorun (map #(gen-var %1 tmpls tran) tmpls)))

;;; list all resources given by the patten on classpath
(defn resources [pattern] (ResourceList/getResources pattern))
