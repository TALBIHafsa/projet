package com.example.halalscan.App;

import java.util.List;
public class product {
    private String id;
    private String name;
    private String image;
    private List<String> ingredients;
    private List<String> haramIngredients;
    private String statut;
    private String country; // Make sure this matches the Firebase field name exactly

    // Getters and setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getImage() { return image; }
    public List<String> getIngredients() { return ingredients; }
    public List<String> getHaramIngredients() { return haramIngredients; }
    public String getStatut() { return statut; }
    public String getCountry() { return country; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setImage(String image) { this.image = image; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }
    public void setHaramIngredients(List<String> haramIngredients) { this.haramIngredients = haramIngredients; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setCountry(String country) { this.country = country; }
}