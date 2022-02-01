<#import "./common.ftlh" as c>

<@c.page>
      <#list posts?reverse as post>
         <@c.pagePost post=post short=true/>
      </#list>
</@c.page>
