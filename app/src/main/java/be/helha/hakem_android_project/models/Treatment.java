package be.helha.hakem_android_project.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class Treatment implements Serializable {

    private int mId;
    private Pill mPill;
    private List<PartOfDay> mPartsOfDay;
    private Calendar mBeginning;
    private Calendar mEnd;

    public Treatment(Pill pill, List<PartOfDay> partsOfDay, Calendar mBeginning, Calendar end) {
        this.mPill = pill;
        this.mPartsOfDay = partsOfDay;
        this.mBeginning = mBeginning;
        this.mEnd = end;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getId() {
        return this.mId;
    }

    public void setPill(Pill mPill) {
        this.mPill = mPill;
    }

    public void setPartsOfDay(List<PartOfDay> mPartsOfDay) {
        this.mPartsOfDay = mPartsOfDay;
    }

    public void setBeginning(Calendar mBeginning) {
        this.mBeginning = mBeginning;
    }

    public void setEnd(Calendar mEnd) {
        this.mEnd = mEnd;
    }

    public Pill getPill() {
        return mPill;
    }

    public List<PartOfDay> getPartsOfDay() {
        return mPartsOfDay;
    }

    public Calendar getBeginning() {
        return mBeginning;
    }

    public Calendar getEnd() {
        return mEnd;
    }


    public String getBeginningString() {
        int day = mBeginning.get(Calendar.DAY_OF_MONTH);
        int month = mBeginning.get(Calendar.MONTH) + 1; //We add 1 for getting the real month
        int year = mBeginning.get(Calendar.YEAR);
        //We need to format the date to a string with the format dd/mm/yyyy
        return day + "/" + month + "/" + year;
    }

    public String getEndString() {
        int day = mEnd.get(Calendar.DAY_OF_MONTH);
        int month = mEnd.get(Calendar.MONTH) + 1;
        int year = mEnd.get(Calendar.YEAR);
        return day + "/" + month + "/" + year;
    }


    public boolean containsTheDate(Calendar date) {
        return date.getTime().compareTo(mBeginning.getTime()) > 0 && date.getTime().compareTo(mEnd.getTime()) <= 0;
    }

}
