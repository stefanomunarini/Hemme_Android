package com.povodev.hemme.android.bean;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Stefano on 21/05/14.
 */
public class PatientDoctorList extends ArrayList<PatientDoctorItem> {

    public static ArrayList<PatientDoctorItem> items;

    public PatientDoctorList(){}

    public PatientDoctorList(Collection<? extends PatientDoctorItem> c){
        super(c);
    }
}
