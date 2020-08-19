package com.softhub.vendor;

public class FetchUserDetails {

    private String cust_id;
    private String cust_name;
    private String cust_email;
    private String cust_mob;
    private String cust_addr;

    public FetchUserDetails(String cust_id, String cust_name, String cust_email, String cust_mob, String cust_addr) {
        this.cust_id = cust_id;
        this.cust_name = cust_name;
        this.cust_email = cust_email;
        this.cust_mob = cust_mob;
        this.cust_addr = cust_addr;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCust_email() {
        return cust_email;
    }

    public void setCust_email(String cust_email) {
        this.cust_email = cust_email;
    }

    public String getCust_mob() {
        return cust_mob;
    }

    public void setCust_mob(String cust_mob) {
        this.cust_mob = cust_mob;
    }

    public String getCust_addr() {
        return cust_addr;
    }

    public void setCust_addr(String cust_addr) {
        this.cust_addr = cust_addr;
    }
}
