package com.rocklizard.labaaoom; /**
 * Created by scooter on 9/17/17.
 */

//package com.rocklizard.labaaoom;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.junit.Assert.*;
import com.rocklizard.labaaoom.Student;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class Student_test {

    @Test
    public void Test_student() throws Exception {
        Student st;

        st = new Student( "Fred" );
        assertTrue( "could not create student struct", st != null );
    }
}

