package com.example.railsheba;

public class AddTrainclass {
String timeloc,trainlocation,total;

    public AddTrainclass(String timeloc, String trainlocation,String total) {
        this.timeloc = timeloc;
        this.trainlocation = trainlocation;
        this.total=total;
    }

    public AddTrainclass() {
    }

    public String getTimeloc() {
        return timeloc;
    }

    public void setTimeloc(String timeloc) {
        this.timeloc = timeloc;
    }

    public String getTrainlocation() {
        return trainlocation;
    }

    public void setTrainlocation(String trainlocation) {
        this.trainlocation = trainlocation;
    }
    public String getTotal(){return total;}
    public void setTotal(String total){this.total=total;}
}
