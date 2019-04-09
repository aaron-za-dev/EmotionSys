package com.aaronzadev.model.pojo;

public class Month {

    private final int MonthID;
    private final String MonthName;

    public Month(int monthID, String monthName) {
        MonthID = monthID;
        MonthName = monthName;
    }

    public int getMonthID() {
        return MonthID;
    }

    public String getMonthName() {
        return MonthName;
    }

    @Override
    public String toString() {
        return MonthName;
    }
}
