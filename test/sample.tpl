<h1>{{ title }}</h1>
<p class="desc">{{ desc }}</p>
<ul>
  {{#tags}}
    <li class="tag">{{ tag }}</li>{{/tags}}
  {{# hidden }}
    this will not show, if hidden is false or empty list
  {{/ hidden }}
</ul>
