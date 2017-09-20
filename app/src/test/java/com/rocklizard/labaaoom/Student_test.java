package com.rocklizard.labaaoom; /**
 * Created by scooter on 9/17/17.
 */

//package com.rocklizard.labaaoom;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.junit.Assert.*;
import com.rocklizard.labaaoom.Student;

public class Student_test {

	/*
	    Test ability to generate student from a datacach like list and to generate
	    a datacache list from the student
	*/
    @Test
    public void Test_student() throws Exception {
        Student st;
        String[] dc_list;       // dummy list as it would come from the datacache
        String[] slist;         // list of strings back form some function
        int i;

        dc_list = new String[7];
        dc_list[0] = "name:Fred Flintstone";
        dc_list[1] = "ne:2017.09.17-00:00:00,K-1,norm,12.6";
        dc_list[2] = "ne:2017.09.17-00:00:00,K-1,norm,13.2";
        dc_list[3] = "ne:2017.09.17-00:00:00,K-1,norm,14.5";
        dc_list[4] = "le:2017.09.19-00:00:00,K-1,norm,10.8";
        dc_list[5] = "le:2017.09.19-00:00:00,K-1,norm,10.9";
        dc_list[6] = "ne:2017.09.17-00:00:00,K-1,norm,14.1";

        st = new Student( dc_list );
        assertTrue( "could not create student struct", st != null );

        slist = st.GenDcEntry();
        System.out.printf( "Got student list with %d entries\n", slist.length );
        for( i = 0; i < slist.length; i++ ) {
            System.out.printf( "\t[%d] %s\n", i, slist[i] );
        }
        assertTrue( "dc entry list didn't have 7 things", slist.length == 7 );

    }
}

