package com.softhub.vendor;

public class FetchGroupList {

    private String group_sr;
    private String group_name;
    private String group_address;
    private String group_preference;


    public FetchGroupList(String group_sr, String group_name, String group_address, String group_preference) {
        this.group_sr = group_sr;
        this.group_name = group_name;
        this.group_address = group_address;
        this.group_preference = group_preference;
    }

    public FetchGroupList(String group_address, String group_preference) {
        this.group_address = group_address;
        this.group_preference = group_preference;
    }

    public FetchGroupList(String group_preference) {
        this.group_preference = group_preference;
    }



    public String getGroup_sr() {
        return group_sr;
    }

    public void setGroup_sr(String group_sr) {
        this.group_sr = group_sr;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_address() {
        return group_address;
    }

    public void setGroup_address(String group_address) {
        this.group_address = group_address;
    }

    public String getGroup_preference() {
        return group_preference;
    }

    public void setGroup_preference(String group_preference) {
        this.group_preference = group_preference;
    }
}
