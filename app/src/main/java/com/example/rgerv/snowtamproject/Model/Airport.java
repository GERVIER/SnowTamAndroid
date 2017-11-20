package com.example.rgerv.snowtamproject.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rgerv on 20/11/2017.
 * Created from the API : https://www.icao.int/safety/iStars/Pages/API-Data-Service.aspx
 * Describe an airport.
 */

public class Airport {
    private String icaoCode;
    private String stateName;
    private Double latitude;
    private Double longitude;

    public Airport(){}

    public Airport(JSONObject _json_code){
        try{
            this.setIcaoCode(_json_code.getString("ICAO_Code"));
            this.setStateName(_json_code.getString("State_Name"));
            this.setLatitude(_json_code.getDouble("Latitude"));
            this.setLongitude(_json_code.getDouble("Longitude"));
        }
        catch (JSONException e){
            this.setStateName("Error");
            this.setIcaoCode("Error");
            this.setLatitude(.0);
            this.setLongitude(.0);
        }

    }

    public Airport(String _icaoCode, String _stateName, Double _latitude, Double _longitude){
        this.setStateName(_stateName);
        this.setIcaoCode(_icaoCode);
        this.setLatitude(_latitude);
        this.setLongitude(_longitude);
    }


    
    public String getIcaoCode() {
        return icaoCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getStateName() {
        return stateName;
    }

    public void setIcaoCode(String icaoCode) {
        this.icaoCode = icaoCode;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String toString(){
        return  "AIRPORT CODE : " + getIcaoCode() +"\n"
                + "AIRPORT STATE : " + getStateName() +"\n"
                + "AIRPORT LAT : " + getLatitude() +"\n"
                + "AIRPORT LONG : " + getLongitude() +"\n";
    }


}
