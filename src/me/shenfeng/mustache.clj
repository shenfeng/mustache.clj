(ns me.shenfeng.mustache
  (:import me.shenfeng.mustache.Mustache))

(defmacro deftemplate [name template]
  `(def ~name (Mustache. ~template)))

(defn to-html [^Mustache template data]
  (.render template data))
