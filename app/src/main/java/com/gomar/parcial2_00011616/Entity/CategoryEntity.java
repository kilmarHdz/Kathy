package com.gomar.parcial2_00011616.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "table_category")
public class CategoryEntity {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "category_name")
    private String categoryName;

    public CategoryEntity(@NonNull String categoryName) {
        this.categoryName = categoryName;
    }

    @NonNull
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(@NonNull String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "CategoryGame{" +
                "categoryName='" + categoryName + '\'' +
                '}';
    }
}