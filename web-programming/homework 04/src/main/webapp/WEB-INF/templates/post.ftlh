<#-- @ftlvariable name="post_id" type="Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#if post_id?has_content>
        <#assign post = c.findBy(posts, "id", post_id)!/>

        <#if post?has_content>
            <#assign user = c.findBy(users, "id", post.userId)!/>
            <@c.pagePost post=post short=false/>
        <#else>
            No such post
        </#if>
    <#else>
        No such post
    </#if>
</@c.page>
