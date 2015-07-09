package com.myselfapps.rav.slovarik;

public class Category {
    private String Id;
    private String Name;
    private String Phrase_id="";
    private String Word_id="";
    private String Notes ="";
    private String Dictionary="";
    private String Visibility="";

    public Category() {
    }

    public Category(String name) {
        Name = name;
        Id = "";
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhrase_id() {
        return Phrase_id;
    }

    public void setPhrase_id(String phrase_id) {
        Phrase_id = phrase_id;
    }

    public String getWord_id() {
        return Word_id;
    }

    public void setWord_id(String word_id) {
        Word_id = word_id;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getDictionary() {
        return Dictionary;
    }

    public void setDictionary(String dictionary) {
        Dictionary = dictionary;
    }

    public String getVisibility() {
        return Visibility;
    }

    public void setVisibility(String visibility) {
        Visibility = visibility;
    }
}
