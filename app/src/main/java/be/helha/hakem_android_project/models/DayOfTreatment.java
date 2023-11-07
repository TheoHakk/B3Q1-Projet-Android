package be.helha.hakem_android_project.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DayOfTreatment {

    Calendar date;
    List<Treatment> treatsForMorning;
    List<Treatment> treatsForNoon;
    List<Treatment> treatsForEvening;

    public DayOfTreatment(Calendar date) {
        this.date = date;
        treatsForEvening = new ArrayList<>();
        treatsForMorning = new ArrayList<>();
        treatsForNoon = new ArrayList<>();
    }

    public Calendar getDate() {
        return date;
    }

    public void addTreatForMorning(Treatment treat) {
        treatsForMorning.add(treat);
    }

    public void addTreatForNoon(Treatment treat) {
        treatsForNoon.add(treat);
    }

    public void addTreatForEvening(Treatment treat) {
        treatsForEvening.add(treat);
    }

    public List<Treatment> getTreatsForMorning() {
        return treatsForMorning;
    }

    public List<Treatment> getTreatsForNoon() {
        return treatsForNoon;
    }

    public List<Treatment> getTreatsForEvening() {
        return treatsForEvening;
    }

    public boolean hasTreatment() {
        return !treatsForEvening.isEmpty() || !treatsForMorning.isEmpty() || !treatsForNoon.isEmpty();
    }

    public String getDateString() {
        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH) + 1; //We add 1 for getting the real month
        int year = date.get(Calendar.YEAR);
        return day + "/" + month + "/" + year;
    }

}
