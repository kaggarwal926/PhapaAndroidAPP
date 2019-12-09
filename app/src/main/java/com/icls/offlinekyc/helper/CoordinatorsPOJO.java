
package com.icls.offlinekyc.helper;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class CoordinatorsPOJO {

    @PrimaryKey
    @NotNull
    private String login_id;

    private String user_name;

    @NotNull
    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(@NotNull String login_id) {
        this.login_id = login_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
