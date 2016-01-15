package com.example.cmuntean.bomtur;

import java.util.Date;

public class Person {
    public String name;
    public Tollbooth tollbooth;
    public String regnr;
    public Date timeStamp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tollbooth getTollbooth() {
        return tollbooth;
    }

    public void setTollbooth(Tollbooth tollbooth) {
        this.tollbooth = tollbooth;
    }

    public String getRegnr() {
        return regnr;
    }

    public void setRegnr(String regnr) {
        this.regnr = regnr;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
