package be.helha.hakem_android_project.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Treatment implements Serializable {

    private Pill pill;
    private List<PartOfDay> partOfDays;
    private Date beginning;
    private Date end;

    public Treatment(Pill pill, List<PartOfDay> partOfDays, Date beginning, Date end) {
        this.pill = pill;
        this.partOfDays = partOfDays;
        this.beginning = beginning;
        this.end = end;
    }

    public void setPill(Pill pill) {
        this.pill = pill;
    }

    public void setPartOfDays(List<PartOfDay> partOfDays) {
        this.partOfDays = partOfDays;
    }

    public void setBeginning(Date beginning) {
        this.beginning = beginning;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Pill getPill() {
        return pill;
    }

    public List<PartOfDay> getPartOfDays() {
        return partOfDays;
    }

    public Date getBeginning() {
        return beginning;
    }

    public Date getEnd() {
        return end;
    }




}
