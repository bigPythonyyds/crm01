package com.yjxxt.crm.query;

import com.yjxxt.crm.base.BaseQuery;

public class UserQuery extends BaseQuery {
    private String userName;
    private String phone;
    private String email;

    public UserQuery() {
    }


    public String getUseName() {
        return userName;
    }

    public void setUseName(String useName) {
        this.userName = useName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserQuery{" +
                "useName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
