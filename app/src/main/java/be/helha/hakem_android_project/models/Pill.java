package be.helha.hakem_android_project.models;

import java.io.Serializable;
import java.util.List;

public class Pill implements Serializable {

    private String name;
    private int duration;
    private List<PartOfDay> partOfDays;
    private int id;

    public Pill(String name, int duration, List<PartOfDay> partOfDays) {
        this.name = name;
        this.duration = duration;
        this.partOfDays = partOfDays;
    }


    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public List<PartOfDay> getPartOfDays() {
        return partOfDays;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPartOfDays(List<PartOfDay> partOfDays) {
        this.partOfDays = partOfDays;
    }

    public int getId() {
        return id;
    }

    public int setId(int id) {
        return this.id = id;
    }


    public String getStringPartsOfDay() {
        String partsOfDay = "";
        for (PartOfDay partOfDay : this.partOfDays) {
            partsOfDay += partOfDay.toString() + " ";
        }
        return partsOfDay;
    }
}
