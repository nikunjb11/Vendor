package com.softhub.vendor;

public class FetchPackageItems {

    private String packItem_id;
    private String packItem_name;
    private String packItem_quntity;

    public FetchPackageItems(String packItem_id, String packItem_name, String packItem_quntity) {
        this.packItem_id = packItem_id;
        this.packItem_name = packItem_name;
        this.packItem_quntity = packItem_quntity;
    }

    public String getPackItem_id() {
        return packItem_id;
    }

    public void setPackItem_id(String packItem_id) {
        this.packItem_id = packItem_id;
    }

    public String getPackItem_name() {
        return packItem_name;
    }

    public void setPackItem_name(String packItem_name) {
        this.packItem_name = packItem_name;
    }

    public String getPackItem_quntity() {
        return packItem_quntity;
    }

    public void setPackItem_quntity(String packItem_quntity) {
        this.packItem_quntity = packItem_quntity;
    }
}
