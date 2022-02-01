<#import "commons.ftlh" as c>

<@c.page>
    <div class="enter form-box">
        <div class="header">Enter</div>
        <div class="body">
            <form method="post" action="">
                <div class="field">
                    <div class="name">
                        <label for="login">Login</label>
                    </div>
                    <div class="value">
                        <input id="login" name="login"/>
                    </div>
                </div>
                <div class="field">
                    <div class="name">
                        <label for="password">Password</label>
                    </div>
                    <div class="value">
                        <input id="password" name="password" type="password"/>
                    </div>
                </div>
                <div class="error"></div>
                <div class="button-field">
                    <input type="submit" value="Enter">
                </div>
            </form>
        </div>
    </div>

    <script>
        $(function () {
            $(".enter").submit(function () {
                const $error = $(this).find(".error")

                ajax({
                    action: "enter",
                    login: $(this).find("input[name='login']").val(),
                    password: $(this).find("input[name='password']").val()
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
