package com.evangunawan.donation.Model;

public class Donation {
    private String username;
    private long startDate;
    private long endDate;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getUsername() {
        return username;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }


}
