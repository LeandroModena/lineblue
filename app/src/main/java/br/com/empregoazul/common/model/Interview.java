package br.com.empregoazul.common.model;

public class Interview {

    private Integer ID;
    private String company;
    private String local;
    private String opportunity;
    private String date;
    private String hour;
    private String speak;
    private String annotation;
    private int status;


    public Interview() {

    }

    public Interview(String company, String local, String opportunity, String date, String hour, String speak, String annotation) {
        this.company = company;
        this.local = local;
        this.opportunity = opportunity;
        this.date = date;
        this.hour = hour;
        this.speak = speak;
        this.annotation = annotation;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getID() {
        return ID;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(String opportunity) {
        this.opportunity = opportunity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getSpeak() {
        return speak;
    }

    public void setSpeak(String speak) {
        this.speak = speak;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
