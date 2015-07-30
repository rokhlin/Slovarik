package com.myselfapps.rav.slovarik.Objects;

import java.util.HashMap;
import java.util.Map;

public class Word {
    private String Id;
    private String Primary;
    private String Secondary;
    private String Transcription = "";
    private String Gender;
    private String PartOfSpeech;
    private String Notes = "";
    private String Audio = "";
    private String Picture = "";
    private String Group1 = "";
    private String Group2 = "";
    private String Dictionary = "";
    private String PluralForm = "";
    private String Exception = "";
    private String Field1 = "";
    private String Field2 = "";
    private String Field3 = "";
    private String Field4 = "";
    private String Field5 = "";

    public String getDictionary() {
        return Dictionary;
    }

    public void setDictionary(String dictionary) {
        Dictionary = dictionary;
    }

    public String getPluralForm() {
        return PluralForm;
    }

    public void setPluralForm(String pluralForm) {
        PluralForm = pluralForm;
    }

    public String getException() {
        return Exception;
    }

    public void setException(String exception) {
        Exception = exception;
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

    public String getField4() {
        return Field4;
    }

    public void setField4(String field4) {
        Field4 = field4;
    }

    public String getField5() {
        return Field5;
    }

    public void setField5(String field5) {
        Field5 = field5;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Word() {}

    public Word(HashMap<String,String> fields) {
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
                case "Gender":
                    Gender = w.getValue();
                    break;
                case "PartOfSpeech":
                    PartOfSpeech = w.getValue();
                    break;
                case "Notes":
                    Notes = w.getValue();
                    break;
                case "Audio":
                    Audio = w.getValue();
                    break;
                case "Picture":
                    Picture = w.getValue();
                    break;
                case "Group1":
                    Group1 = w.getValue();
                    break;
                case "Group2":
                    Group2 = w.getValue();
                    break;
                case "Dictionary":
                    Dictionary = w.getValue();
                    break;
                case "PluralForm":
                    PluralForm = w.getValue();
                    break;
                case "Exception":
                    Exception = w.getValue();
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
                case "Field4":
                    Field4 = w.getValue();
                    break;
                case "Field5":
                    Field5 = w.getValue();
                    break;
                default:
                    break;
            }
        }
    }

    public Word(String primary,  String transcription, String secondary, String gender, String partOfSpeech, String notes, String audio, String picture, String group1, String group2) {
        Primary = primary;
        Secondary = secondary;
        Transcription = transcription;
        Gender = gender;
        PartOfSpeech = partOfSpeech;
        Notes = notes;
        Audio = audio;
        Picture = picture;
        Group1 = group1;
        Group2 = group2;
    }

    public Word(String primary, String transcription, String secondary, String gender, String partOfSpeech, String notes) {
        Primary = primary;
        Secondary = secondary;
        Transcription = transcription;
        Gender = gender;
        PartOfSpeech = partOfSpeech;
        Notes = notes;
    }

    public String getPrimary() {
        return Primary;
    }

    public void setPrimary(String primary) {
        Primary = primary;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getAudio() {
        return Audio;
    }

    public void setAudio(String audio) {
        Audio = audio;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public String getGroup1() {
        return Group1;
    }

    public void setGroup1(String group1) {
        Group1 = group1;
    }

    public String getGroup2() {
        return Group2;
    }

    public void setGroup2(String group2) {
        Group2 = group2;
    }

    public String getSecondary() {
        return Secondary;
    }

    public void setSecondary(String secondary) {
        Secondary = secondary;
    }

    public String getTranscription() {
        return Transcription;
    }

    public void setTranscription(String transcription) {
        Transcription = transcription;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPartOfSpeech() {
        return PartOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        PartOfSpeech = partOfSpeech;
    }



}
