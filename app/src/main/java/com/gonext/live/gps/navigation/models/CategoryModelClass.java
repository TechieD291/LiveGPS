package com.gonext.live.gps.navigation.models;

/**
 * Created by sell-news on 2/9/17.
 */

public class CategoryModelClass {
    String categoryId;
    String categoryName;
    String CategoryIcon;

    public String getCategoryId() {
        return categoryId;
    }

    public CategoryModelClass(String categoryId, String categoryName, String categoryIcon, int categorySelected) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        CategoryIcon = categoryIcon;
        this.categorySelected = categorySelected;
    }

    public CategoryModelClass() {
    }


    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryIcon() {
        return CategoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        CategoryIcon = categoryIcon;
    }

    public int getCategorySelected() {
        return categorySelected;
    }

    public void setCategorySelected(int categorySelected) {
        this.categorySelected = categorySelected;
    }

    int categorySelected;

}
