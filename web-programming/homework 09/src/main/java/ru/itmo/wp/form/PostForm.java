package ru.itmo.wp.form;

import javax.persistence.*;
import javax.validation.constraints.*;

public class PostForm {
    @NotBlank
    @Size(min = 1, max = 60)
    private String title;

    @NotBlank
    @Size(min = 1, max = 65000)
    @Lob
    private String text;

    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Not all tags are correct")
    private String tags;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
