<#-- @ftlvariable name="users" type="ru.itmo.wp.model.domain.User[]" -->
<#-- @ftlvariable name="articles" type="ru.itmo.wp.model.domain.Article[]" -->
<#import "commons.ftlh" as c>

<@c.page>
    <div class="articles">
        <div class="noData">No data</div>
        <template>
            <article>
                <div class="title"></div>
                <div class="information"></div>
                <div class="body"></div>
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
        </template>
    </div>

    <script>
        $(function () {
            const $noData = $(".articles .noData");
            $noData.text("Loading...");

            ajax({
                action: "findAll"
            }, function (response) {
                response["articlesViews"].forEach(function ({article, user}) {
                    $noData.hide();
                    const $tr = $($(".articles template").prop("content")).clone();
                    $tr.find(".title").text(article.title);
                    $tr.find(".information").text("By " + user.login);
                    $tr.find(".body").text(article.text);
                    $(".articles").append($tr);
                });
            })
        })
    </script>
</@c.page>

<#function findBy items key value>
    <#list items as item>
        <#if item[key] == value>
            <#return item/>
        </#if>
    </#list>
</#function>