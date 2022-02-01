<#import "./common.ftlh" as c>

<@c.page>
    <div class="datatable">
        <div class="caption">Users</div>
        <table>
            <thead>
                <tr>
                    <th>Id</th>
                    <th>Handle</th>
                    <th>Name</th>
                </tr>
            </thead>
            <tbody>
                <#list users as user>
                    <tr>
                        <td>${user.id}</td>
                        <td><@c.userlink user false/></td>
                        <td>${user.name}</td>
                    </tr>
                </#list>
            </tbody>
        </table>
    </div>
</@c.page>
