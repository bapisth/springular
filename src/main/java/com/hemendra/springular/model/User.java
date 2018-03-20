package com.hemendra.springular.model;

/**
 * @author Hemendra
 */
public class User {
    private String name;
    private String address;
    private String mobile;
    private String branch;

    public User(String name, String address, String mobile, String branch) {
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.branch = branch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", mobile='" + mobile + '\'' +
                ", branch='" + branch + '\'' +
                '}';
    }
}
