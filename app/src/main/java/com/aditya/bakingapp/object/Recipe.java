package com.aditya.bakingapp.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ASUS A456U on 07/08/2017.
 */

public class Recipe extends RealmObject implements Parcelable {

    @SerializedName("id")
    @Expose
    @PrimaryKey
    private long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("ingredients")
    @Expose
    private RealmList<Ingredient> ingredients;

    @SerializedName("steps")
    @Expose
    private RealmList<Step> steps;

    @SerializedName("servings")
    @Expose
    private int servings;

    @SerializedName("image")
    @Expose
    private String image;

    public Recipe() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = (RealmList<Ingredient>) ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = (RealmList<Step>) steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    protected Recipe(Parcel in) {
        id = in.readLong();
        name = in.readString();
        ArrayList<Ingredient> ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        this.ingredients = new RealmList<>();
        this.ingredients.addAll(ingredients);
        ArrayList<Step> steps = in.createTypedArrayList(Step.CREATOR);
        this.steps = new RealmList<>();
        this.steps.addAll(steps);
        servings = in.readInt();
        image = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        dest.writeInt(servings);
        dest.writeString(image);
    }
}
