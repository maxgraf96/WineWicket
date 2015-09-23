package com.graf.wicket.wine;

import org.apache.wicket.util.io.IClusterable;

import java.io.Serializable;

/**
 * Created by Max on 02.09.2015.
 */
public class Wine implements Serializable {
    private String name,ort,type,agingPrivate;
    private Integer id,year;
    private Float abHofPrice;
    private boolean bestellbar;

    public Wine(){}
    public Wine(final Wine wine){
        this.id = wine.id;
        this.name = wine.name;
        this.ort = wine.ort;
        this.type = wine.type;
        this.agingPrivate = wine.agingPrivate;
        this.year = wine.year;
        this.abHofPrice = wine.abHofPrice;
        this.bestellbar = wine.bestellbar;
    }
    public Wine(int clean){
        setName(null);
        setId(null);
        setOrt(null);
        setType(null);
        setAgingPrivate(null);
        setYear(null);
        setAbHofPrice(null);
        setBestellbar(false);
    }

    //getset
    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }

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

    public String getType(){
        return type;
    }
    public void setType(String winetype){
        this.type = winetype;
    }

    public String getAgingPrivate(){
        return agingPrivate;
    }
    public void setAgingPrivate(String agingPrivate){
        this.agingPrivate = agingPrivate;
    }

    public Integer getYear(){
        return year;
    }
    public void setYear(Integer year){
        this.year = year;
    }

    public Float getAbHofPrice(){
        return abHofPrice;
    }
    public void setAbHofPrice(Float abHofPrice){
        this.abHofPrice = abHofPrice;
    }

    public boolean getBestellbar(){
        return bestellbar;
    }
    public void setBestellbar(boolean bestellbar){
        this.bestellbar = bestellbar;
    }

}
