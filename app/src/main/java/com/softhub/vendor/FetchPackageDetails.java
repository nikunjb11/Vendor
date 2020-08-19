package com.softhub.vendor;

public class FetchPackageDetails {

    private String pack_id;
    private String pack_name;
    private String pack_coupon;
    private String pack_price;
    private String pack_type;

    public FetchPackageDetails(String pack_id, String pack_name, String pack_coupon, String pack_price, String pack_type) {
        this.pack_id = pack_id;
        this.pack_name = pack_name;
        this.pack_coupon = pack_coupon;
        this.pack_price = pack_price;
        this.pack_type = pack_type;
    }

    public String getPack_id() {
        return pack_id;
    }

    public void setPack_id(String pack_id) {
        this.pack_id = pack_id;
    }

    public String getPack_name() {
        return pack_name;
    }

    public void setPack_name(String pack_name) {
        this.pack_name = pack_name;
    }

    public String getPack_coupon() {
        return pack_coupon;
    }

    public void setPack_coupon(String pack_coupon) {
        this.pack_coupon = pack_coupon;
    }

    public String getPack_type() {
        return pack_type;
    }

    public void setPack_type(String pack_type) {
        this.pack_type = pack_type;
    }

    public String getPack_price() {
        return pack_price;
    }

    public void setPack_price(String pack_price) {
        this.pack_price = pack_price;
    }
}
