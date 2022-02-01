<#-- @ftlvariable name="actual_page" type="String" -->

<#macro page>
    <!DOCTYPE html>
    <html lang="ru">
    <head>
        <meta charset="UTF-8">
        <title>Codeforces</title>
        <link rel="stylesheet" href="/css/normalize.css">
        <link rel="stylesheet" href="/css/style.css">
        <link rel="icon" href="/favicon.ico">
    </head>
    <body>
    <@header/>
    <div class="middle">
        <@sidebar/>
        <main>
            <#nested/>
        </main>
    </div>
    <@footer/>
    </body>
    </html>
</#macro>

<#macro header>
    <header>
        <a href="#" class="logo"><img alt="Codeforces" title="Codeforces"
                                      src="/img/codeforces-logo-with-telegram.png"></a>
        <div class="languages">
            <a href="#"><img alt="In English" title="In English"
                             src="/img/gb.png"></a>
            <a href="#"><img alt="In Russian" title="In Russian"
                             src="/img/ru.png"></a>
        </div>
        <div class="enter-or-register-box">
            <#if user??>
                <@userlink user true/>
                |
                <a href="/logout">Logout</a>
            <#else>
                <a href="/enter">Enter</a>
                |
                <a href="/register">Register</a>
            </#if>
        </div>
        <nav>
            <#assign items=[
            {"href": "/index", "name": "Home"},
            {"href": "/misc/help", "name": "Help"},
            {"href": "/users", "name": "USERS"},
            {"href": "/contests", "name": "Contests"}
            ]/>
            <ul>
                <#list items as item>
                    <li>
                        <#if actual_page == item.href>
                            <div class="page_with_underline">
                                <a href="${item.href}">${item.name}</a>
                            </div>
                        <#else>
                            <a href="${item.href}">${item.name}</a>
                        </#if>
                    </li>
                </#list>
            </ul>
        </nav>
    </header>
</#macro>

<#macro sidebar>
    <aside>
        <#list posts?reverse as post>
            <#assign user = findBy(users, "id", post.userId)!/>

            <section>
                <div class="header">Post ${post.id}</div>
                <div class="body">
                    <#if 250 < post.text?length>
                        ${post.text?substring(0, 250)}&hellip;
                    <#else>
                        ${post.text}
                    </#if>
                </div>
                <div class="footer">
                    <a href="/post?post_id=${post.id}">View all</a>
                </div>
            </section>
        </#list>
    </aside>
</#macro>

<#macro footer>
    <footer>
        2010-2021 &copy; by Mike Mirzayanov
    </footer>
</#macro>

<#macro userlink user nameOnly>
<#-- @ftlvariable name="user" type="ru.itmo.web.lesson4.model.User" -->
    <#if nameOnly>
        <a href="/user?handle=${user.handle}">${user.name}</a>
    <#else>
        <a class="user-color user-color-${user.color?lower_case}" href="/user?handle=${user.handle}">${user.name}</a>
    </#if>
</#macro>

<#macro pagePost post short>
<#-- @ftlvariable name="post" type="ru.itmo.web.lesson4.model.Post" -->
    <#assign user = findBy(users, "id", post.userId)!/>

    <article>
        <div class="title">
            ${post.title}
        </div>
        <div class="information">By <@userlink user false/></div>
        <div class="body">
            <#if 250 < post.text?length && short>
                ${post.text?substring(0, 250)}&hellip;
            <#else>
                ${post.text}
            </#if>
        </div>
        <div class="footer">
            <div class="left">
                <img src="img/voteup.png" title="Vote Up" alt="Vote Up"/>
                <span class="positive-score">+173</span>
                <img src="img/votedown.png" title="Vote Down" alt="Vote Down"/>
            </div>
            <div class="right">
                <img src="img/date_16x16.png" title="Publish Time" alt="Publish Time"/>
                2 days ago
                <img src="img/comments_16x16.png" title="Comments" alt="Comments"/>
                <a href="#">68</a>
            </div>
        </div>
    </article>
</#macro>

<#function findBy items key value>
    <#list items as item>
        <#if item[key] == value>
            <#return item/>
        </#if>
    </#list>
</#function>

<#function hasNext items key value>
    <#list items as item>
        <#if value < item[key]>
            <#return true/>
        </#if>
    </#list>
</#function>

<#function getNext items key value>
    <#list items as item>
        <#if value < item[key]>
            <#return item/>
        </#if>
    </#list>
</#function>

<#function hasPrev items key value>
    <#list items?reverse as item>
        <#if item[key] < value>
            <#return true/>
        </#if>
    </#list>
</#function>

<#function getPrev items key value>
    <#list items?reverse as item>
        <#if item[key] < value>
            <#return item/>
        </#if>
    </#list>
</#function>
