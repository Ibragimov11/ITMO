<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#-- @ftlvariable name="targetUserId" type="long" -->
<#-- @ftlvariable name="text" type="java.lang.String" -->
<#-- @ftlvariable name="error" type="java.lang.String" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="enter-box form-box">
        <div class="header">Create Article</div>
        <div class="body">
            <form class="create-article" method="post" action="">
                <input type="hidden" name="action" value="create"/>
                <div class="field">
                    <div class="name" style="width: 20%;">
                        <label for="title">Title</label>
                    </div>
                    <div class="value">
                        <input id="title" name="title"/>
                    </div>
                </div>
                <p>
                    <label>
                        <textarea class="article-area" name="text" cols="44" rows="16"></textarea>
                    </label>
                </p>
                <div class="error"></div>
                <div class="button-field">
                    <input type="submit" value="Create">
                </div>
            </form>
        </div>
    </div>

    <script>
        $(function () {
            $(".enter-box").submit(function () {
                const $error = $(this).find(".error")

                ajax({
                    action: "create",
                    title: $(this).find("input[name='title']").val(),
                    text: $(this).find("textarea[name='text']").val()
                }, function (response) {
                    if (response["error"]) {
                        $error.text(response["error"]);
                    } else if(response["redirect"]) {
                        location.href = response["redirect"];
                    }
                });

                return false;
            });
        })
    </script>
</@c.page>
