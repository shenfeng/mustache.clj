# mustache.clj - Logic-less {{mustache}} templates for Clojure

[mustache.clj](https://github.com/shenfeng/mustache.clj)
is a (yet another) implementation of the [Mustache](http://mustache.github.com/)
template system for Clojure.

By preprocessing template into a tree like data structure, It's quite fast.

## Usage

```clj
[me.shenfeng/mustache "0.0.8"]
```
mustache.clj export a functions `deftemplate`:

```clj

(deftemplate template "{{template}}")

(template {:you-data "data"})

```

There is also a `deftemplates`, combined with `resources`, is handy.
More info, please refer the code, and [usage of Rssminer](https://github.com/shenfeng/rssminer/blob/master/src/rssminer/tmpls.clj)


### Motivation

I write it for the need of [Rssminer](http://rssminer.net)

* Rssminer need to fast with limited MEM and CPU
* It make the [i18n quite easy](https://github.com/shenfeng/rssminer/blob/master/src/rssminer/i18n.clj)
* Mustache is used both server side and client side

### Template

```html
<!-- test/sample.tpl -->
<h1>{{ title }}</h1>
<p class="desc">{{ desc }}</p>
<ul>
  {{#tags}}
    <li class="tag">{{ tag }}</li>{{/tags}}
  {{# hidden }}
    this will not show, if hidden is false or empty list
  {{/ hidden }}
</ul>
```
### Code

```clj
(deftemplate template (slurp "test/sample.tpl"))

(def data {:title "mustache.clj"
           :desc "Logic-less {{mustache}} templates for Clojure"
           :tags [{:tag "Clojure"}
                  {:tag "Mustache"}
                  {:tag "Performance"}]})

(println (template data))
```

### Output

```html
<h1>mustache.clj</h1>
<p class="desc">Logic-less {{mustache}} templates for Clojure</p>
<ul>
    <li class="tag">Clojure</li>
    <li class="tag">Mustache</li>
    <li class="tag">Performance</li>
</ul>
```

## Limitation

 * Set Delimiter is not implemented now. Anyway, why change {{}} to <% %>

## License

Distributed under the Eclipse Public License, the same as Clojure.
