<#-- @ftlvariable name="post" type="ru.itmo.wp.domain.Post" -->
<#import "commons.ftlh" as c>

<@c.page>
    <#if post??>
        <@c.post post=post/>
        <#if user??>
            <div class="form">
                <div class="body">
                    <form method="post" action="">
                        <input type="hidden" name="action" value="comment"/>
                        <div class="field">
                            <div class="name">
                                <label for="text">Comment</label>
                            </div>
                            <div class="value">
                                <textarea id="text" name="text"></textarea>
                            </div>
                            <@c.error "comment.text"/>
                        </div>
                        <div class="button-field">
                            <input type="submit" value="Comment">
                        </div>
                    </form>
                </div>
            </div>
        </#if>

        <#list post.comments as comment>
            <div class="comment">${comment.user.login}: ${comment.text}</div>
        </#list>
    <#else>
        No such post
    </#if>
</@c.page>
