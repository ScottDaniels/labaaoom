
/*
	Mnemonic:	eval_set_test
	Abstract:	Unit testing for the evaluation set class.
	Author:		E. Scott Daniels   edaniels7@gatech.edu for CS6460
	Date:		17 September 2017
*/
package com.rocklizard.labaaoom;


import org.junit.Test;
import com.rocklizard.labaaoom.Eval_set;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.junit.Assert.*;
import com.rocklizard.labaaoom.Student;


public class Ave_data_test {

    /*
        Basic test to verify that add value and average works
    */
    @Test
    public void Test_ave_data() throws Exception {
        Ave_data ad;
        int i;

		ad = new Ave_data( "K1.Meyers", "Grade1", "sent" );
        for( i = 1; i < 11; i++ ) {
            ad.AddValue( (double) i);
        }

        assertTrue( "average expected was not generated",  ad.GetAve() ==  5.5 );
    }

    /*
        Test conversion to datacache string list
    */
    @Test
    public void Test_dclist() throws Exception {
        Ave_data ad;
        String[] dclist;
        int i;
        String tokens[];

		ad = new Ave_data( "K1.Meyers", "Grade1", "sent" );
        ad = new Ave_data( "K1.Meyers", "Grade1", "sent" );
        for( i = 1; i < 11; i++ ) {
            ad.AddValue( (double) i);
        }

        dclist = ad.GenDcEntry( );
        assertTrue( "datacache list for average not == 3 elements",  dclist.length ==  3 );

        for( i = 0; i < dclist.length; i++ ) {
            tokens = dclist[i].split( ":" );
            switch( tokens[0] ) {
                case "sum":
                    assertEquals( "sum not texpected value",  "55.000000", tokens[1] );
                    break;

                case "elements":
                    assertEquals( "elements not texpected value",  "10", tokens[1] );
                    break;
            }
        }

        assertTrue( "average expected was not generated",  ad.GetAve() ==  5.5 );
    }

}

