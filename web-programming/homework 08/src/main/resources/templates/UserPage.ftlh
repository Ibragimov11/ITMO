<#import "commons.ftlh" as c>

<@c.page>
    <#if user??>
        <div>
            Id: ${user.id}
        </div>
        <div>
            Login: ${user.login}
        </div>
        <div>
            Creation time: ${user.creationTime}
        </div>
    <#else>
        No such user
    </#if>
</@c.page>
