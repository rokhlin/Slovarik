package com.myselfapps.rav.slovarik;

/**
 * Created by Антон on 10.06.2015.
 */
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

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Word() {}

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
