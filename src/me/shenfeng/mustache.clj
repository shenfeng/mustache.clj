(ns me.shenfeng.mustache
  (:import me.shenfeng.mustache.Mustache
           me.shenfeng.mustache.Context))

(defn mk-template [template]
  (Mustache/preprocess template))

(defmacro deftemplate [name template]
  `(def ~name (Mustache/preprocess ~template)))

(defn to-html
  ([^Mustache template data]
     (let [^Context c (Context. data nil)]
       (.render template c nil)))
  ([^Mustache template data partial]
     (let [^Context c (Context. data nil)]
       (.render template c partial))))

