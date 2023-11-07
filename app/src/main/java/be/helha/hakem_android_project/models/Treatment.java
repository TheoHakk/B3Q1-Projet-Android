package be.helha.hakem_android_project.models;

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
    public int getId(){
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
        return beginning.get(Calendar.DAY_OF_MONTH) + "/" + beginning.get(Calendar.MONTH) + "/" + beginning.get(Calendar.YEAR);
    }
    public String getEndString() {
        return end.get(Calendar.DAY_OF_MONTH) + "/" + end.get(Calendar.MONTH) + "/" + end.get(Calendar.YEAR);
    }

}
