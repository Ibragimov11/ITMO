<#-- @ftlvariable name="user_id" type="java.lang.Long" -->
<#-- @ftlvariable name="handle" type="java.lang.String" -->
<#import "./common.ftlh" as c>

<@c.page>
    <#if handle?has_content || user_id?has_content>
        <#if handle?has_content>
            <#assign user = c.findBy(users, "handle", handle)!/>
        <#else>
            <#assign user = c.findBy(users, "id", user_id)!/>
        </#if>

        <#if user?has_content>
            <div class="user">
                <div class="personal-information">
                    <div class="info">
                        <div class="username">
                            <@c.userlink user false/>
                        </div>
                        <ul>
                            <li>id: ${user.id}
                            <li>handle: ${user.handle}
                            <li>name: ${user.name}
                            <li>posts:
                                <#assign count = posts?filter(post -> post.userId == user.id)?size>
                                <a href="/posts?user_id=${user.id}">${count}</a>
                        </ul>
                    </div>
                    <div class="user-avatar">
                        <img src="https://userpic.codeforces.com/no-title.jpg" alt="avatar">
                    </div>
                </div>
                <div class="arrows">
                    <div class="left_arrow">
                        <#if c.hasPrev(users, "id", user.id)??>
                            <#assign nextUser = c.getPrev(users, "id", user.id)!/>
                            <a href="/user?handle=${nextUser.handle}">⟵</a>
                        <#else>
                            ⟵
                        </#if>
                    </div>
                    <div class="right_arrow">
                        <#if c.hasNext(users, "id", user.id)??>
                            <#assign nextUser = c.getNext(users, "id", user.id)!/>
                            <a href="/user?handle=${nextUser.handle}">⟶</a>
                        <#else>
                            ⟶
                        </#if>
                    </div>
                </div>
            </div>
        <#else>
            No such user
        </#if>
    <#else>
        No such user
    </#if>
</@c.page>
