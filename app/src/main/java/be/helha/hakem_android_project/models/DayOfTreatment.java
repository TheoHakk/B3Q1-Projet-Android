package be.helha.hakem_android_project.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * The DayOfTreatment class represents a day with associated treatments for morning, noon, and evening.
 * This class is used to facilitate the display of treatments in the calendar
 */
public class DayOfTreatment {
    private Calendar mDate;
    private List<Treatment> mTreatsForMorning;
    private List<Treatment> mTreatsForNoon;
    private List<Treatment> mTreatsForEvening;

    /**
     * Constructs a new instance of the DayOfTreatment class.
     *
     * @param mDate The date for the day of treatment.
     */
    public DayOfTreatment(Calendar mDate) {
        this.mDate = mDate;
        mTreatsForMorning = new ArrayList<>();
        mTreatsForNoon = new ArrayList<>();
        mTreatsForEvening = new ArrayList<>();
    }

    /**
     * Gets the date for the day of treatment.
     *
     * @return The Calendar object representing the date.
     */
    public Calendar getDate() {
        return mDate;
    }

    /**
     * Adds a treatment for the morning to the list of treatments of morning.
     *
     * @param treat The Treatment object to be added.
     */
    public void addTreatForMorning(Treatment treat) {
        mTreatsForMorning.add(treat);
    }

    /**
     * Adds a treatment for the noon to the list of treatments of noon.
     *
     * @param treat The Treatment object to be added.
     */
    public void addTreatForNoon(Treatment treat) {
        mTreatsForNoon.add(treat);
    }

    /**
     * Adds a treatment for the evening to the list of treatments of evening.
     *
     * @param treat The Treatment object to be added.
     */
    public void addTreatForEvening(Treatment treat) {
        mTreatsForEvening.add(treat);
    }

    /**
     * Gets the list of treatments for the morning.
     *
     * @return The list of Treatment objects for the morning.
     */
    public List<Treatment> getTreatsForMorning() {
        return mTreatsForMorning;
    }

    /**
     * Gets the list of treatments for the noon.
     *
     * @return The list of Treatment objects for the noon.
     */
    public List<Treatment> getTreatsForNoon() {
        return mTreatsForNoon;
    }

    /**
     * Gets the list of treatments for the evening.
     *
     * @return The list of Treatment objects for the evening.
     */
    public List<Treatment> getTreatsForEvening() {
        return mTreatsForEvening;
    }

    /**
     * Checks if there is at least one treatment for the day.
     * Used for knowing if we have to display the day in the calendar.
     *
     * @return true if there is at least one treatment, false otherwise.
     */
    public boolean hasTreatment() {
        return !mTreatsForEvening.isEmpty() || !mTreatsForMorning.isEmpty() || !mTreatsForNoon.isEmpty();
    }

    /**
     * Gets the date string in the format "dd/MM/yyyy".
     *
     * @return The formatted date string.
     */
    public String getDateString() {
        int day = mDate.get(Calendar.DAY_OF_MONTH);
        int month = mDate.get(Calendar.MONTH) + 1; //We add 1 for getting the real month
        int year = mDate.get(Calendar.YEAR);
        return day + "/" + month + "/" + year;
    }
}
