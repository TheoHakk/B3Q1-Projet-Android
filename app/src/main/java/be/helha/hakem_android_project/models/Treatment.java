package be.helha.hakem_android_project.models;

import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class Treatment implements Serializable {

    private int id;
    private Pill pill;
    private List<PartOfDay> partsOfDay;
    private Calendar beginning;
    private Calendar end;

    public Treatment(Pill pill, List<PartOfDay> partsOfDay, Calendar beginning, Calendar end) {
        this.pill = pill;
        this.partsOfDay = partsOfDay;
        this.beginning = beginning;
        this.end = end;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() { return this.id; }

    public void setPill(Pill pill) {
        this.pill = pill;
    }

    public void setPartsOfDay(List<PartOfDay> partsOfDay) {
        this.partsOfDay = partsOfDay;
    }

    public void setBeginning(Calendar beginning) {
        this.beginning = beginning;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public Pill getPill() {
        return pill;
    }

    public List<PartOfDay> getPartsOfDay() {
        return partsOfDay;
    }

    public Calendar getBeginning() {
        return beginning;
    }

    public Calendar getEnd() {
        return end;
    }


    //Because of this shity way of collecting Date, we have an issue with the date format
    //We need to specify an index for the month, because it starts at 0
    public String getBeginningString() {
        int day = beginning.get(Calendar.DAY_OF_MONTH);
        int month = beginning.get(Calendar.MONTH) + 1; //We add 1 for getting the real month
        int year = beginning.get(Calendar.YEAR);
        return day + "/" + month + "/" + year;
    }

    public String getEndString() {
        int day = end.get(Calendar.DAY_OF_MONTH);
        int month = end.get(Calendar.MONTH) + 1;
        int year = end.get(Calendar.YEAR);
        return day + "/" + month + "/" + year;
    }


    public boolean containsTheDate(Calendar date){

        if(date.getTime().after(beginning.getTime()) && date.getTime().before(end.getTime()))
            return true;
        return false;
    }

}
