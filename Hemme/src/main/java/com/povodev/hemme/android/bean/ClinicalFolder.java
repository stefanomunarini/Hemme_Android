package com.povodev.hemme.android.bean;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Stefano on 02/04/14.
 */
public class ClinicalFolder extends ArrayList<ClinicalEvent> {

    public ClinicalFolder(){}

    public ClinicalFolder(Collection<? extends ClinicalEvent> c){
        super(c);
    }

}
