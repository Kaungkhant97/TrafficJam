package com.unihackchallenge.mmtrafficreport;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kaungkhantthu on 2/10/17.
 * "address": "haha",
 * "latitude": "12.223",
 * "longitude": "18.232e",
 * "byReporter": "u aye",
 * "__v": 0,
 * "publishedDate": "2017-02-10T09:47:42.884Z",
 * "status": "sadnasj",
 * "comment": "bu bu"
 */

public class location {

    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("latitude")
    @Expose
    public String latitude;
    @SerializedName("longitude")
    @Expose
    public String longitude;
    @SerializedName("byReporter")
    @Expose
    public String byReporter;
    @SerializedName("__v")
    @Expose
    public Integer v;
    @SerializedName("publishedDate")
    @Expose
    public String publishedDate;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("comment")
    @Expose
    public String comment;

    @SerializedName("role")
    @Expose
    public String role;

    public location(String address, String latitude, String longitude, String byReporter,  String status, String comment,String role) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.byReporter = byReporter;
        this.status = status;
        this.comment = comment;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getByReporter() {
        return byReporter;
    }

    public void setByReporter(String byReporter) {
        this.byReporter = byReporter;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}