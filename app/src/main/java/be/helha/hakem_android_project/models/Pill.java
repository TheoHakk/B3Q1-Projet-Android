package be.helha.hakem_android_project.models;

import java.io.Serializable;
import java.util.List;

public class Pill implements Serializable {

    private String mName;
    private int mDuration;
    private List<PartOfDay> mPartsOfDay;
    private int mId;

    public Pill(String mName, int mDuration, List<PartOfDay> partOfDays) {
        this.mName = mName;
        this.mDuration = mDuration;
        this.mPartsOfDay = partOfDays;
    }


    public String getName() {
        return mName;
    }

    public int getDuration() {
        return mDuration;
    }

    public List<PartOfDay> getPartsOfDay() {
        return mPartsOfDay;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public void setPartsOfDay(List<PartOfDay> mPartsOfDay) {
        this.mPartsOfDay = mPartsOfDay;
    }

    public int getId() {
        return mId;
    }

    public int setId(int id) {
        return this.mId = id;
    }


    public String getStringPartsOfDay() {
        String partsOfDay = "";
        for (PartOfDay partOfDay : this.mPartsOfDay)
            partsOfDay += partOfDay.toString() + " ";
        return partsOfDay;
    }
}
