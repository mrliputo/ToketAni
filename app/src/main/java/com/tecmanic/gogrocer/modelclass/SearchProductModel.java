package com.tecmanic.gogrocer.modelclass;

public class SearchProductModel {
    String id;
    String pNAme;
    SubProductList subprodList;

    public SearchProductModel(String id, String pNAme, SubProductList subprodList) {
        this.id = id;
        this.pNAme = pNAme;
        this.subprodList = subprodList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpNAme() {
        return pNAme;
    }

    public void setpNAme(String pNAme) {
        this.pNAme = pNAme;
    }

    public SubProductList getSubprodList() {
        return subprodList;
    }

    public void setSubprodList(SubProductList subprodList) {
        this.subprodList = subprodList;
    }
}
