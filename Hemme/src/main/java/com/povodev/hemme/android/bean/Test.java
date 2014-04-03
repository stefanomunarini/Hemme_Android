package com.povodev.hemme.android.bean;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Stefano on 03/04/14.
 */
public class Test extends ArrayList<Result> {

    public static ArrayList<Result> test;

    public Test(){}

    public Test(Collection<? extends Result> c){
        super(c);
    }
}
