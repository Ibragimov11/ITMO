<#import "commons.ftlh" as c>

<@c.page>
    <div class="notice form-box">
        <div class="header">Notice</div>
        <div class="body">
            <form class="add-notice" method="post" action="">
                <label>
                       <textarea class="text-area" name="content" cols="44" rows="16"></textarea>
                </label>
                <@c.error "noticeForm.content"/>
                <div class="button-field" style="margin-left: 11.5rem">
                    <input type="submit" value="Notice">
                </div>
            </form>
        </div>
    </div>
</@c.page>
