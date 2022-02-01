package ru.itmo.wp.lesson8.form;

import javax.validation.constraints.NotNull;

public class UserStatusForm {
    @NotNull
    private Long userId;

    @NotNull
    private boolean userStatus;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isUserStatus() {
        return userStatus;
    }

    public void setUserStatus(boolean userStatus) {
        this.userStatus = userStatus;
    }
}
