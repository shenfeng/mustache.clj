(ns me.shenfeng.mustache
  (:import me.shenfeng.mustache.Mustache
           me.shenfeng.mustache.Context))

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
(defmacro deftemplate [name template]
  `(let [tmpl# (Mustache/preprocess ~template)]
     (defn ~name
       ([data#]
          (.render tmpl# (Context. data# nil)))
       ([data# partial#]
          (.render tmpl# (Context. data# nil) partial#)))))
