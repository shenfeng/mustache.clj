# mustache.clj - Logic-less {{mustache}} templates with Clojure

[mustache.clj](https://github.com/shenfeng/mustache.clj)
is an implementation of the [Mustache](http://mustache.github.com/)
template system in Java for clojure.

By preprocessing template into a tree like data structure, It's super
fast. My dev machine can Render
[this template](https://github.com/shenfeng/mustache.clj/blob/master/test/test.tpl)
about 30000 times per seconds.

## Usage

### Template

```html
<!-- test/sample.tpl -->
<h2>this is a test</h2>
<ul>
  {{#arr}}
    <li>
      <p>{{ name }}</p>
    </li>
  {{/arr}}
</ul>
```
### Code

```clj
(deftemplate template (slurp "test/sample.tpl"))

(println (to-html template {:arr [{:name "name1"}
                                  {:name "name2"}]}))

```

### output

```html
<h2>this is a test</h2><ul>

    <li><p>name1</p></li>

    <li><p>name2</p></li>

</ul>
```

## Limitation

 * Partials is not implemented now.
 * Set Delimiter is not implemented now. Anyway, why change {{}} to <% %>

## License

Distributed under the Eclipse Public License, the same as Clojure.
