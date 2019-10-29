package com.pc.searchapi.model;


import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by chitra_chitralekha on 10/28/19.
 */

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PlanData {

    private String ackID;
    private String planName;
    private String sponsorNAME;
    private String  sponsMailUS;
    private String sponsMailUSCity;


    //standard setters and getters for bo

    public String getAckID() {
        return ackID;
    }

    public void setAckID(String ackID) {
        this.ackID = ackID;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getSponsorNAME() {
        return sponsorNAME;
    }

    public void setSponsorNAME(String sponsorNAME) {
        this.sponsorNAME = sponsorNAME;
    }

    public String getSponsMailUS() {
        return sponsMailUS;
    }

    public void setSponsMailUS(String sponsMailUS) {
        this.sponsMailUS = sponsMailUS;
    }

    public String getSponsMailUSCity() {
        return sponsMailUSCity;
    }

    public void setSponsMailUSCity(String sponsMailUSCity) {
        this.sponsMailUSCity = sponsMailUSCity;
    }
}

