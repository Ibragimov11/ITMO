<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#-- @ftlvariable name="targetUserId" type="long" -->
<#-- @ftlvariable name="text" type="java.lang.String" -->
<#-- @ftlvariable name="talks" type="ru.itmo.wp.model.domain.Talk[]" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="form-box">
        <div class="header">Send Message</div>
        <div class="body">
            <form class="send-message" method="post" action="">
                <input type="hidden" name="action" value="submit"/>
                <p>
                    to:
                    <label>
                        <select class="user-select" name="targetUserId" required>
                            <#list users as user>
                                <option value="${user.id}">${user.login}</option>
                            </#list>
                        </select>
                    </label>
                </p>
                <p>
                    <label>
                        <textarea class="message-area" name="text" cols="44" rows="16" required></textarea>
                    </label>
                </p>
                <div class="button-field">
                    <input type="submit" value="Submit">
                </div>
            </form>
        </div>
    </div>
    <div class="datatable">
        <div class="caption">Talk</div>
        <table>
            <thead>
            <tr>
                <th>Id</th>
                <th>Sender</th>
                <th>Receiver</th>
                <th>Text</th>
                <th>Date</th>
            </tr>
            </thead>
            <tbody>
            <#list talks as talk>
                <tr>
                    <td>${talk.id}</td>
                    <td>${(c.findBy(users, "id", talk.sourceUserId)!).login}</td>
                    <td>${(c.findBy(users, "id", talk.targetUserId)!).login}</td>
                    <td>${talk.text}</td>
                    <td>${talk.creationTime}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</@c.page>
