package com.unihackchallenge.mmtrafficreport;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationResponse {

@SerializedName("data")
@Expose
private List<location> data = null;

public List<location> getData() {
return data;
}

public void setData(List<location> data) {
this.data = data;
}

}