package com.myselfapps.rav.slovarik.Objects;

public class Label {
    private String Id;
    private String Name;
    private String Group="";
    private String Notes ="";
    private String Dictionary="";
    private String Visibility="";

    public Label() {
    }

    public Label(String name) {
        Name = name;
    }

    public Label( String name, String dictionary) {
        Dictionary = dictionary;
        Name = name;
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

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
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
