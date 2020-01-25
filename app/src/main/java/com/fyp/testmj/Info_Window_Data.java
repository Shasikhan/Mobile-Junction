package com.fyp.testmj;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

public class Info_Window_Data {
    private String LabEngineerImage, LabEngineerName, LabEngineerSkills, LabEngineerEmail, LabEngineerPhone;
    private Button bRequest;
    private float ratingBar;



    public String getLabEngineerName() {
        return LabEngineerName;
    }

    public void setLabEngineerName(String labEngineerName) {
        LabEngineerName = labEngineerName;
    }

    public String getLabEngineerSkills() {
        return LabEngineerSkills;
    }

    public void setLabEngineerSkills(String labEngineerSkills) {
        LabEngineerSkills = labEngineerSkills;
    }

    public String getLabEngineerEmail() {
        return LabEngineerEmail;
    }

    public void setLabEngineerEmail(String labEngineerEmail) {
        LabEngineerEmail = labEngineerEmail;
    }

    public String getLabEngineerPhone() {
        return LabEngineerPhone;
    }

    public void setLabEngineerPhone(String labEngineerPhone) {
        LabEngineerPhone = labEngineerPhone;
    }

    public float getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(Float ratingBar) {
        this.ratingBar = ratingBar;
    }

    public String getLabEngineerImage() {
        return LabEngineerImage;
    }

    public void setLabEngineerImage(String labEngineerImage) {
        LabEngineerImage = labEngineerImage;
    }
}
