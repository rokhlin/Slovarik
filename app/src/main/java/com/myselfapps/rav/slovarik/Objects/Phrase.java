package com.myselfapps.rav.slovarik.Objects;

import java.util.HashMap;
import java.util.Map;

public class Phrase {
    private String Id;
    private String Primary;
    private String Transcription="-";
    private String Secondary;
    private String Category="";
    private String Label="";
    private String Notes ="";
    private String Dictionary="";
    private String Field1="";
    private String Field2="";
    private String Field3="";

    public Phrase() {
    }

    public Phrase(String primary, String transcription, String secondary, String category, String label, String notes, String dictionary) {
        Primary = primary;
        Transcription = transcription;
        Secondary = secondary;
        Category = category;
        Label = label;
        Notes = notes;
        Dictionary = dictionary;
    }

    public Phrase(HashMap<String,String> fields) {
        for (Map.Entry<String,String> w : fields.entrySet()){
            switch (w.getKey()){
                case "rowid":
                    Id = w.getValue();
                    break;
                case "Primary":
                    Primary = w.getValue();
                    break;
                case "Transcription":
                    Transcription = w.getValue();
                    break;
                case "Secondary":
                    Secondary = w.getValue();
                    break;
                case "Category":
                    Category = w.getValue();
                    break;
                case "Label":
                    Label = w.getValue();
                    break;
                case "Notes":
                    Notes = w.getValue();
                    break;
                case "Dictionary":
                    Dictionary = w.getValue();
                    break;
                case "Field1":
                    Field1 = w.getValue();
                    break;
                case "Field2":
                    Field2 = w.getValue();
                    break;
                case "Field3":
                    Field3 = w.getValue();
                    break;
                default:
                    break;
            }
        }
    }

    public String getTranscription() {
        return Transcription;
    }

    public void setTranscription(String transcription) {
        Transcription = transcription;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getPrimary() {
        return Primary;
    }

    public void setPrimary(String primary) {
        Primary = primary;
    }

    public String getSecondary() {
        return Secondary;
    }

    public void setSecondary(String secondary) {
        Secondary = secondary;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
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

    public String getField1() {
        return Field1;
    }

    public void setField1(String field1) {
        Field1 = field1;
    }

    public String getField2() {
        return Field2;
    }

    public void setField2(String field2) {
        Field2 = field2;
    }

    public String getField3() {
        return Field3;
    }

    public void setField3(String field3) {
        Field3 = field3;
    }
}
