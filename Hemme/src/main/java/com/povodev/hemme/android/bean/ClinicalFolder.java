package com.povodev.hemme.android.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Stefano on 02/04/14.
 */
public class ClinicalFolder extends ArrayList<ClinicalEvent> implements Serializable{

    public static ArrayList<ClinicalEvent> clinicalFolder;

    public ClinicalFolder(){}

    public ClinicalFolder(Collection<? extends ClinicalEvent> c){
        super(c);
    }

}
