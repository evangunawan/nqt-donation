package com.evangunawan.donation.Model;

public class Donation {
    private String username;
    private int startDate;
    private int endDate;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public String getUsername() {
        return username;
    }

    public int getStartDate() {
        return startDate;
    }

    public int getEndDate() {
        return endDate;
    }


}
