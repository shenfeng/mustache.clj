# mustache.clj - Logic-less {{mustache}} templates for Clojure

[mustache.clj](https://github.com/shenfeng/mustache.clj)
is an implementation of the [Mustache](http://mustache.github.com/)
template system in Java for clojure.

By preprocessing template into a tree like data structure, It's super
fast. My dev machine can Render
[this template](https://github.com/shenfeng/mustache.clj/blob/master/test/test.tpl)
about 33000 times per seconds.

## Usage

```clj
[me.shenfeng/mustache "0.0.1"]
```
It export 2 functions `deftemplate` and `to-html`

### Template

```html
<!-- test/sample.tpl -->
<h2>this is a test</h2>
<ul>
  {{#arr}}
    <li><p>{{ name }}</p></li>
  {{/arr}}
</ul>
```
### Code

```clj
(deftemplate template (slurp "test/sample.tpl"))

(println (to-html template {:arr [{:name "name1"}
                                  {:name "name2"}]}))

```

### Output

```html
<h2>this is a test</h2><ul>

    <li><p>name1</p></li>

    <li><p>name2</p></li>

</ul>
```

## Limitation

 * Set Delimiter is not implemented now. Anyway, why change {{}} to <% %>

## License

Distributed under the Eclipse Public License, the same as Clojure.
