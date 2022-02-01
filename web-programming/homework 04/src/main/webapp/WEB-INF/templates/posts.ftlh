<#-- @ftlvariable name="user_id" type="Long" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#if user_id?has_content>
        <#assign user = c.findBy(users, "id", user_id)!/>
    </#if>

    <#if user?has_content>
        <#assign count = posts?filter(post -> post.userId == user.id)?size>
        <#if 0 < count>
            <#list posts?reverse as post>
                <#if post.userId == user.id>
                    <@c.pagePost post=post short=true/>
                </#if>
            </#list>
        <#else>
            This user didn't publish posts
        </#if>
    <#else>
        No such user
    </#if>

</@c.page>