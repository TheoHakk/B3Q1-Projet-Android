package be.helha.hakem_android_project.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DayOfTreatment {

    private Calendar mDate;
    private List<Treatment> mTreatsForMorning;
    private List<Treatment> mTreatsForNoon;
    private List<Treatment> mTreatsForEvening;

    public DayOfTreatment(Calendar mDate) {
        this.mDate = mDate;
        mTreatsForMorning = new ArrayList<>();
        mTreatsForNoon = new ArrayList<>();
        mTreatsForEvening = new ArrayList<>();
    }

    public Calendar getDate() {
        return mDate;
    }

    public void addTreatForMorning(Treatment treat) {
        mTreatsForMorning.add(treat);
    }

    public void addTreatForNoon(Treatment treat) {
        mTreatsForNoon.add(treat);
    }

    public void addTreatForEvening(Treatment treat) {
        mTreatsForEvening.add(treat);
    }

    public List<Treatment> getTreatsForMorning() {
        return mTreatsForMorning;
    }

    public List<Treatment> getTreatsForNoon() {
        return mTreatsForNoon;
    }

    public List<Treatment> getTreatsForEvening() {
        return mTreatsForEvening;
    }

    public boolean hasTreatment() {
        return !mTreatsForEvening.isEmpty() || !mTreatsForMorning.isEmpty() || !mTreatsForNoon.isEmpty();
    }

    public String getDateString() {
        int day = mDate.get(Calendar.DAY_OF_MONTH);
        int month = mDate.get(Calendar.MONTH) + 1; //We add 1 for getting the real month
        int year = mDate.get(Calendar.YEAR);
        return day + "/" + month + "/" + year;
    }

}
