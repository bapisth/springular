package com.hemendra.springular.model;

/**
 * @author Hemendra
 */
public class UserDto {

    private Integer id;
    private String userName;
    private String password;

    private String name;
    private String address;
    private String mobile;
    private String branch;

    public UserDto() {
    }

    public UserDto(String name, String address, String mobile, String branch) {
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", mobile='" + mobile + '\'' +
                ", branch='" + branch + '\'' +
                '}';
    }
}
