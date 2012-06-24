{{#pager}}
  <div class="links-pager">
    {{#prev_page}}
      <span class="prev page" data-page="{{ prev_page }}">
        &larr;
      </span>
    {{/prev_page}}
    <span class="total">{{page}} / {{ total_page }}</span>
    {{#next_page}}
      <span class="next page" data-page="{{ next_page }}">
        &rarr;
      </span>
    {{/next_page}}

  </div>
{{/pager}}
<ul data-links="{{ link_type }}">
  {{#links}}
    <li class="source">
      <span title="{{ title }}" class="title">
        {{ title }}
      </span>
      <div class="url-holder">
        <img class="favicon" src="{{favicon}}" data-id={{id}}
        onerror="MW.utils.favicon_error(this)" width="16"
        height="16"/>
        <span class="url">{{ url }}</span>
      </div>
    </li>
  {{/links}}
</ul>
{{#pager}}
  <div class="links-pager">
    {{#prev_page}}
      <span class="prev page" data-page="{{ prev_page }}">
        &larr;
      </span>
    {{/prev_page}}
    <span class="total">{{page}} / {{ total_page }}</span>
    {{#next_page}}
      <span class="next page" data-page="{{ next_page }}">
        &rarr;
      </span>
    {{/next_page}}
  </div>
{{/pager}}
