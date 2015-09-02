package com.graf.wicket.wine;

import org.apache.wicket.util.io.IClusterable;

/**
 * Created by Max on 02.09.2015.
 */
public class Wine implements IClusterable {
    private String name,ort,winetype,agingPrivate;
    private int year;
    private float abHofPrice;
    private boolean bestellbar;

    public Wine(){}
    public Wine(final Wine wine){
        this.name = wine.name;
        this.year = wine.year;
    }

    //getset
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getOrt(){
        return ort;
    }
    public void setOrt(String ort){
        this.ort = ort;
    }

    public String getWinetype(){
        return winetype;
    }
    public void setWinetype(String winetype){
        this.winetype = winetype;
    }

    public String getAgingPrivate(){
        return agingPrivate;
    }
    public void setAgingPrivate(String agingPrivate){
        this.agingPrivate = agingPrivate;
    }

    public int getYear(){
        return year;
    }
    public void setYear(){
        this.year = year;
    }

    public float getAbHofPrice(){
        return abHofPrice;
    }
    public void setAbHofPrice(float abHofPrice){
        this.abHofPrice = abHofPrice;
    }

    public boolean getBestellbar(){
        return bestellbar;
    }
    public void setBestellbar(boolean bestellbar){
        this.bestellbar = bestellbar;
    }

}
