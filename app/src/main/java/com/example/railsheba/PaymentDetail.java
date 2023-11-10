package com.example.railsheba;

public class PaymentDetail {
    public String total_cost;
    public String total_seats;
    public String togo;
    public String time;
    public String date;
    public String coach;
    public String total;

    public PaymentDetail(String total_cost, String total_seats, String togo, String time, String date, String coach, String total) {
        this.total_cost = total_cost;
        this.total_seats = total_seats;
        this.togo = togo;
        this.time = time;
        this.date = date;
        this.coach = coach;
        this.total = total;
    }

    public PaymentDetail() {
    }
}
