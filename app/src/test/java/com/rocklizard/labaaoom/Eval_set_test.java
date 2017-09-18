package com.rocklizard.labaaoom;
/*
	Mnemonic:	eval_set_test
	Abstract:	Unit testing for the evaluation set class.
	Author:		E. Scott Daniels   edaniels7@gatech.edu for CS6460
	Date:		17 September 2017
*/


import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.junit.Assert.*;
import com.rocklizard.labaaoom.Student;

public class Eval_set_test {

    @Test
    public void Test_eval_set() throws Exception {
        Eval_set eset;
        double mmai[];          // min, max, ave, i  return values
        double mean;            // compute because who knows with java rounding if we can set a constant that will match

        eset = new Eval_set();
        eset.Add_entry( "2017.09.17-00:00:00,K-1,norm,12.6" );
        eset.Add_entry( "2017.09.17-00:00:00,K-1,norm,11.8" );
        eset.Add_entry( "2017.09.17-00:00:00,K-1,norm,12.0" );
        eset.Add_entry( "2017.09.17-00:00:00,K-1,norm,10.1" );
        eset.Add_entry( "2017.09.17-00:00:00,K-1,norm,9.3" );

        mean = (12.6 + 11.8 + 12.0 + 10.1 + 9.3)/5.0;
		mmai = eset.Get_mmai();
        System.out.printf( "mmai:  %.2f %.2f %.2f %.0f mean=%.2f\n", mmai[0], mmai[1], mmai[2], mmai[3], mean );
        assertTrue( "mmai test: max was not right",  mmai[1] == 12.6 );
        assertTrue( "mmai test: min was not right",  mmai[0] == 9.3 );
        assertTrue( "mmai test: mean was not right",  mmai[2] == mean );
        assertTrue( "mmai test: i was not right",  mmai[3] == 5.0 );
    }
}

