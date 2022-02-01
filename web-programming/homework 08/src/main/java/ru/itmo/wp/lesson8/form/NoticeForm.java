package ru.itmo.wp.lesson8.form;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;

@SuppressWarnings("unused")
public class NoticeForm {
    @NotBlank
    @Lob
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
