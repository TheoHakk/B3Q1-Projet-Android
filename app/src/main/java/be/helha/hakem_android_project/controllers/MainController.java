package be.helha.hakem_android_project.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.helha.hakem_android_project.models.PartOfDay;
import be.helha.hakem_android_project.models.Pill;
import be.helha.hakem_android_project.models.Treatment;

public class MainController {
    List<Treatment> treatments;

    List<Pill> pills;


    public MainController() {
        initializeTreatments();
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }

    public List<Pill> getPills() {
        return pills;
    }

    private void initializeTreatments() {
        //TODO SQL-Request for getting all treatments

        //Temporary
        List<PartOfDay> partOfDays = new ArrayList<>();
        partOfDays.add(PartOfDay.MORNING);
        partOfDays.add(PartOfDay.EVENING);

        List<PartOfDay> partOfDays2 = new ArrayList<>();
        partOfDays2.add(PartOfDay.NOON);
        partOfDays2.add(PartOfDay.EVENING);

        Pill pill1 = new Pill("Doliprane", 7, partOfDays);
        Pill pill2 = new Pill("Aspirine", 5, partOfDays2);

        Date beginDate1 = new Date(2023, 11, 1);
        Date endDate1 = new Date(2023, 11, 8);

        Date beginDate2 = new Date(2023, 11, 1);
        Date endDate2 = new Date(2023, 11, 6);

        Treatment treatment1 = new Treatment(pill1, partOfDays, beginDate1, endDate1);
        Treatment treatment2 = new Treatment(pill2, partOfDays2, beginDate2, endDate2);

        treatments = new ArrayList<>();
        treatments.add(treatment1);
        treatments.add(treatment2);

    }
}
