# mustache.clj - Logic-less {{mustache}} templates for Clojure

[mustache.clj](https://github.com/shenfeng/mustache.clj)
is a (yet another) implementation of the [Mustache](http://mustache.github.com/)
template system for Clojure.

By preprocessing template into a tree like data structure, It's quite fast.

## Motivation

I initally write it for my part time project: [Rssminer](http://rssminer.net)

* Rssminer need to fast with limited MEM and CPU
* It makes the [i18n quite easy](https://github.com/shenfeng/rssminer/blob/master/src/rssminer/i18n.clj)
* Mustache is used both server side and client side

## Features
* Clean compact code: the jar size is about 11k
* Zero dependency
* Fast: renders the `test/sample.tpl` with the test data about **500k times per seconds** on 13 inch Macbook Air
* For Clojure

## Usage

### Quick start

```clj
[me.shenfeng/mustache "1.1-SNAPSHOT"]
```
#### Template

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
#### Code

```clj
(deftemplate tmpl-fn (slurp "test/sample.tpl"))

(def data {:title "mustache.clj"
           :desc "Logic-less {{mustache}} templates for Clojure"
           :tags [{:tag "Clojure"}
                  {:tag "Mustache"}
                  {:tag "Performance"}]})

(println (tmpl-fn data))
```

#### Output

```html
<h1>mustache.clj</h1>
<p class="desc">Logic-less {{mustache}} templates for Clojure</p>
<ul>
    <li class="tag">Clojure</li>
    <li class="tag">Mustache</li>
    <li class="tag">Performance</li>
</ul>
```

### Generate functions from templates folder

templates folder:

```sh
templates
├── admin.tpl                => admin
├── login.tpl                => login
├── m
│   ├── landing.tpl          => m-landing
│   ├── p_header.tpl         => m-p-header
│   └── subs.tpl             => m-subs
└── tmpls
    ├── app
    │   ├── feed_content.tpl => tmpls-app-feed-content
```

```clj
(gen-tmpls-from-folder "templates" [".tpl"]) ;  generates the clojure fn

; now you can write something like this
(admin {:key "str" :array [1 2 3 5]})
```

### Generate functions from classpath resources

`gen-tmpls-from-resources` just like gen-tmpls-from-folder, except find templates files from classpath

### Transform template data before apply it to the template

You can pass a function (optional) to `deftemplate`, `mktmpls-from-folder`, `mktmpls-from-resouces`, allows you to transform the template data

```clj
(defn add-gloal-data [data]
  (assoc data
    :dev? (config/dev?)                 ; distingish dev and prod
    :server-host (get-in *current-req* [:header "host"]) ; stg1, test, prod host are different
    ;; other data, like different data based on local => for i18n
    ))

(gen-tmpls-from-folder "templates" [".tpl"] add-gloal-data)
```

## Limitation

 * Set Delimiter is not implemented now. Anyway, why change {{}} to <% %>
 * Lambdas is not implemented

## Non Standard ?

  use `?` to test value true. example

```html
{{?links}}
links is value true, this string get outputed
{{/links}}

{{^links}}
links is value false, this string get outputed
{{/links}}

{{#links}}
this is repeated (count links) times
{{/links}}

When using ?, Mustache.clj will print a warnning to stderr

```

## License

Distributed under the Eclipse Public License, the same as Clojure.
