package com.rocklizard.labaaoom;

/**
 * Created by scooter on 9/17/17.
 */


//package com.rocklizard.labaaoom;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.junit.Assert.*;
import com.rocklizard.labaaoom.M5hash;

/*
	Tests which validate the md5 hash functions.
*/
	public class Md5hash_test {

    @Test
    public void Test_m5hash() throws Exception {
        String plain = "now is the time";
        String salt = "a bit of salt is nice";
        String result;

		result = M5hash.Mk_md5( "", "" );
		assertTrue( "generated md5 was not expected when src/salt are empty", result.equals( "d41d8cd98f00b204e9800998ecf8427e" ) );

		result = M5hash.Mk_md5( plain, salt );
		System.out.printf( ">>> result=%s \n", result );
        assertTrue( "generated md5 was not expected", result.equals( "18638c83f5032432c7af3f75d732b6bd" ) );
    }
}
