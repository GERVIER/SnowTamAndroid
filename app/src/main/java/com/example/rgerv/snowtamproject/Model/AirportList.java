package com.example.rgerv.snowtamproject.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rgerv on 20/11/2017.
 * Singleton used to stocked the airport list. Since it will be used by all the view,
 * this will be more practical than passed it through the view using intent
 *
 */

public class AirportList {
    private static AirportList instance = new AirportList();
    public static AirportList getInstance()
    {
        return instance;
    }

    private AirportList(){}

    private List<Airport> airportList = new ArrayList<Airport>();

    //GETTER AND SETTER
    public List<Airport> getAirportList() {
        return airportList;
    }

    public void setAirportList(List<Airport> airportList) {
        this.airportList = airportList;
    }

}
