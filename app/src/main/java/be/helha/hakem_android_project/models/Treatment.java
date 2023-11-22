package be.helha.hakem_android_project.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * The Treatment class represents a medication treatment with a pill, parts of the day,
 * and the beginning and end dates of the treatment. Also we have the id for the database.
 */
public class Treatment implements Serializable {

    private int mId;
    private Pill mPill;
    private List<PartOfDay> mPartsOfDay;
    private Calendar mBeginning;
    private Calendar mEnd;

    /**
     * Constructs a new instance of the Treatment class.
     *
     * @param pill       The Pill object representing the medication.
     * @param partsOfDay The list of parts of the day for which the medication is prescribed.
     * @param beginning  The beginning date of the treatment.
     * @param end        The end date of the treatment.
     */
    public Treatment(Pill pill, List<PartOfDay> partsOfDay, Calendar beginning, Calendar end) {
        this.mPill = pill;
        this.mPartsOfDay = partsOfDay;
        this.mBeginning = beginning;
        this.mEnd = end;
    }

    /**
     * Sets the identifier of the treatment.
     *
     * @param id The new identifier of the treatment.
     */
    public void setId(int id) {
        this.mId = id;
    }

    /**
     * Gets the identifier of the treatment.
     *
     * @return The identifier of the treatment.
     */
    public int getId() {
        return this.mId;
    }

    /**
     * Sets the pill of the treatment.
     *
     * @param pill The new Pill object representing the medication.
     */
    public void setPill(Pill pill) {
        this.mPill = pill;
    }

    /**
     * Sets the list of parts of the day for which the medication is prescribed.
     *
     * @param partsOfDay The new list of parts of the day.
     */
    public void setPartsOfDay(List<PartOfDay> partsOfDay) {
        this.mPartsOfDay = partsOfDay;
    }

    /**
     * Sets the beginning date of the treatment.
     *
     * @param beginning The new beginning date of the treatment.
     */
    public void setBeginning(Calendar beginning) {
        this.mBeginning = beginning;
    }

    /**
     * Sets the end date of the treatment.
     *
     * @param end The new end date of the treatment.
     */
    public void setEnd(Calendar end) {
        this.mEnd = end;
    }

    /**
     * Gets the Pill object representing the medication.
     *
     * @return The Pill object.
     */
    public Pill getPill() {
        return mPill;
    }

    /**
     * Gets the list of parts of the day for which the medication is prescribed.
     *
     * @return The list of parts of the day.
     */
    public List<PartOfDay> getPartsOfDay() {
        return mPartsOfDay;
    }

    /**
     * Gets the beginning date of the treatment.
     *
     * @return The beginning date.
     */
    public Calendar getBeginning() {
        return mBeginning;
    }

    /**
     * Gets the end date of the treatment.
     *
     * @return The end date.
     */
    public Calendar getEnd() {
        return mEnd;
    }

    /**
     * Gets the formatted string of the beginning date in the format "dd/MM/yyyy".
     *
     * @return The formatted beginning date string.
     */
    public String getBeginningString() {
        int day = mBeginning.get(Calendar.DAY_OF_MONTH);
        int month = mBeginning.get(Calendar.MONTH) + 1; // We add 1 for getting the real month
        int year = mBeginning.get(Calendar.YEAR);
        return day + "/" + month + "/" + year;
    }

    /**
     * Gets the formatted string of the end date in the format "dd/MM/yyyy".
     *
     * @return The formatted end date string.
     */
    public String getEndString() {
        int day = mEnd.get(Calendar.DAY_OF_MONTH);
        int month = mEnd.get(Calendar.MONTH) + 1;
        int year = mEnd.get(Calendar.YEAR);
        return day + "/" + month + "/" + year;
    }

    /**
     * Checks if a given date is within the range of the treatment.
     *
     * @param date The date to be checked.
     * @return true if the date is within the range, false otherwise.
     */
    public boolean containsTheDate(Calendar date) {
        return date.getTime().compareTo(mBeginning.getTime()) > 0 && date.getTime().compareTo(mEnd.getTime()) <= 0;
    }
}
