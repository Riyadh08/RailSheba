package com.example.railsheba;

public class UserTicketDetails {
    String togo,date,time,coach,seats,cost,cardnumber,mobile,cardname,ticketnum,forcheck;

    public UserTicketDetails(String togo, String date, String time, String coach, String seats, String cost,String cardnumber,String mobile,String cardname,String ticketnum,String forcheck) {
        this.togo = togo;
        this.date = date;
        this.time = time;
        this.coach = coach;
        this.seats = seats;
        this.cost = cost;
        this.cardnumber=cardnumber;
        this.mobile=mobile;
        this.cardname=cardname;
        this.ticketnum=ticketnum;
        this.forcheck=forcheck;
    }

    public UserTicketDetails() {
    }

    public String getForcheck() {
        return forcheck;
    }

    public void setForcheck(String forcheck) {
        this.forcheck = forcheck;
    }

    public String getTicketnum() {
        return ticketnum;
    }

    public void setTicketnum(String ticketnum) {
        this.ticketnum = ticketnum;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTogo() {
        return togo;
    }

    public void setTogo(String togo) {
        this.togo = togo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
