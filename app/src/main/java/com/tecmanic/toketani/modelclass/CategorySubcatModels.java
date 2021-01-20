package com.tecmanic.toketani.modelclass;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class CategorySubcatModels {

    String id;
    String title;
    String slug;
    String parent;
    String leval;
    String description;
    String image;
    String status;
    String count;
    String pCount;
    String productDescriptionArb;


    public String getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getSlug(){
        return slug;
    }

    public String getProductDescriptionArb() {
        return productDescriptionArb;
    }


    public String getParent(){
        return parent;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setLeval(String leval) {
        this.leval = leval;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setpCount(String pCount) {
        this.pCount = pCount;
    }

    public void setProductDescriptionArb(String productDescriptionArb) {
        this.productDescriptionArb = productDescriptionArb;
    }


    public String getLeval(){
        return leval;
    }

    public String getDescription(){
        return description;
    }

    public String getImage(){
        return image;
    }

    public String getStatus(){
        return status;
    }




    public String getCount(){
        return count;
    }

    public String getpCount(){
        return pCount;
    }

}
