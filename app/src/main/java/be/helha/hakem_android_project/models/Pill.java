package be.helha.hakem_android_project.models;

import java.io.Serializable;
import java.util.List;

/**
 * The Pill class represents a medication pill with a name, duration, parts of the day,
 * and an optional identifier for the database.
 */
public class Pill implements Serializable {

    private String mName;
    private int mDuration;
    private List<PartOfDay> mPartsOfDay;
    private int mId;

    /**
     * Constructs a new instance of the Pill class.
     *
     * @param mName      The name of the pill.
     * @param mDuration  The recommended duration of the pill.
     * @param partOfDays The list of parts of the day for which the pill is prescribed.
     */
    public Pill(String mName, int mDuration, List<PartOfDay> partOfDays) {
        this.mName = mName;
        this.mDuration = mDuration;
        this.mPartsOfDay = partOfDays;
    }

    /**
     * Constructs a new instance of the Pill class with an identifier.
     *
     * @param name       The name of the pill.
     * @param duration   The duration of the pill.
     * @param partsOfDay The list of parts of the day for which the pill is prescribed.
     * @param id         The identifier of the pill.
     */
    public Pill(String name, int duration, List<PartOfDay> partsOfDay, int id) {
        this(name, duration, partsOfDay);
        this.mId = id;
    }

    /**
     * Gets the name of the pill.
     *
     * @return The name of the pill.
     */
    public String getName() {
        return mName;
    }

    /**
     * Gets the duration of the pill.
     *
     * @return The duration of the pill.
     */
    public int getDuration() {
        return mDuration;
    }

    /**
     * Gets the list of parts of the day for which the pill is prescribed.
     *
     * @return The list of parts of the day.
     */
    public List<PartOfDay> getPartsOfDay() {
        return mPartsOfDay;
    }

    /**
     * Sets the name of the pill.
     *
     * @param mName The new name of the pill.
     */
    public void setName(String mName) {
        this.mName = mName;
    }

    /**
     * Sets the duration of the pill.
     *
     * @param mDuration The new duration of the pill.
     */
    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    /**
     * Sets the list of parts of the day for which the pill is prescribed.
     *
     * @param mPartsOfDay The new list of parts of the day.
     */
    public void setPartsOfDay(List<PartOfDay> mPartsOfDay) {
        this.mPartsOfDay = mPartsOfDay;
    }

    /**
     * Gets the identifier of the pill.
     *
     * @return The identifier of the pill.
     */
    public int getId() {
        return mId;
    }

}
