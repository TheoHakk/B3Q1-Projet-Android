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

    public int getId() {
        return this.id;
    }

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


    public String getBeginningString() {
        int day = beginning.get(Calendar.DAY_OF_MONTH);
        int month = beginning.get(Calendar.MONTH) + 1; //We add 1 for getting the real month
        int year = beginning.get(Calendar.YEAR);
        //We need to format the date to a string with the format dd/mm/yyyy
        String sdate = day + "/" + month + "/" + year;
        Log.i("begin date : ", sdate);
        return sdate;
    }

    public String getEndString() {
        int day = end.get(Calendar.DAY_OF_MONTH);
        int month = end.get(Calendar.MONTH) + 1;
        int year = end.get(Calendar.YEAR);
        String sdate = day + "/" + month + "/" + year;
        Log.i("end date : ", sdate);
        return sdate;
    }


    public boolean containsTheDate(Calendar date) {

        Log.i("Wouah date :", date.getTime().toString());
        Log.i("Wouah beginning :", beginning.getTime().toString());
        Log.i("Wouah end :", end.getTime().toString());


        if (date.getTime().compareTo(beginning.getTime()) > 0 && date.getTime().compareTo(end.getTime()) <= 0) {
            Log.i("Wouah", "containsTheDate: true");
            return true;
        }
        return false;
    }

}
