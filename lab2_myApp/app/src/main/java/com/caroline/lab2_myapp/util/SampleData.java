package com.caroline.lab2_myapp.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.caroline.lab2_myapp.database.*;

public class SampleData {
    private static final String DOG_1="Alpha";
    private static final String DOG_2="Simba";
    private static final String DOG_3="Lucy";

    public static Date getDate(int diff){
        GregorianCalendar cal = new GregorianCalendar();
        cal.add(Calendar.MILLISECOND,diff);
        return cal.getTime();
    }

    public static List<petEntity> getPets(){
        List<petEntity> pets = new ArrayList<petEntity>();
        pets.add(new petEntity("mytime",DOG_1));
        pets.add(new petEntity("mytime2",DOG_2));
        pets.add(new petEntity("mytime3",DOG_3));

        return pets;
    }
}
