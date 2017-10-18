package com.rocklizard.labaaoom;

/*
	Mnemonic:	sentence_group_test
	Abstract:	Class that provides sanity check of the sentence groups; unit test.
	Author:		E. Scott Daniels
	Date:		17 September 2017
*/

import org.junit.Test;
import static junit.framework.Assert.assertTrue;
//import static org.junit.Assert.assertEquals;
//import org.junit.Test;
//import static org.junit.Assert.*;
import com.rocklizard.labaaoom.Sentence_group;


public class Sentence_group_test {

	/*
		Internal function to make a datacache like entry.
	*/
	private String[] mk_dc_entry( String name, int count ) {
		String[] dc_entry;          // entry like would come form the datacache
		int i;

		dc_entry = new String[ count + 1 ];
		dc_entry[ 0 ] = "name:kindergarten";

		for( i = 1; i < count+1; i++ ) {
			dc_entry[ i ] = "sentence:" + "this is sentence " + Integer.toString( i );
		}

		return dc_entry;
	}

	@Test
    public void Test_gropup_create() throws Exception {
		Sentence_group sg;
		String[] dc_entry;
		boolean state;

		dc_entry = mk_dc_entry( "kindergarten", 30 );
		sg = new Sentence_group( dc_entry );				// create a sentence group

		state = sg.Select( 20, 2 );			// should be false as there aren't two groups of 20
		assertTrue( "allowed seleciton of group of 20 starting at 2",  state == false );
    }

   	@Test
    public void Test_iteration() throws Exception {
		Sentence_group sg;
		String[] dc_entry;
		boolean state;
		int i;
		String s;

		dc_entry = mk_dc_entry( "kindergarten", 60 );
		sg = new Sentence_group( dc_entry );				// create a sentence group

		state = sg.Select( 20, 2 );			// should be false as there aren't two groups of 20
		assertTrue( "unable to select for an iteration", state );

		for( i = 0; i <  20; i++ ) {
			s = sg.GetNext();				// next string
			assertTrue( "iteration stopped early " + Integer.toString( i ), s != null );
			System.err.printf( "iteration test [%d] %s\n", i, s  );
		}

		s = sg.GetNext();
		assertTrue( "after 20 iterations, get-next returned non null", s == null );
    }

    /*
    	Test getting the last group.
    */
    @Test
    public void Test_lg_iteration() throws Exception {
		Sentence_group sg;
		String[] dc_entry;
		boolean state;
		int i;
		String s;

		dc_entry = mk_dc_entry( "kindergarten", 60 );
		sg = new Sentence_group( dc_entry );				// create a sentence group

		state = sg.Select( 20, 3 );			// last group of 20
		assertTrue( "unable to select last group for an iteration", state );

		for( i = 0; i <  20; i++ ) {
			s = sg.GetNext();				// next string
			assertTrue( "last group iteration stopped early " + Integer.toString( i ), s != null );
			System.err.printf( "last group: iteration test [%d] %s\n", i, s  );
		}

		s = sg.GetNext();
		assertTrue( "last group: after 20 iterations, get-next returned non null", s == null );
    }

    /*
    	Test getting the first group.
    */
    @Test
    public void Test_fg_iteration() throws Exception {
		Sentence_group sg;
		String[] dc_entry;
		boolean state;
		int i;
		String s;

		dc_entry = mk_dc_entry( "kindergarten", 60 );
		sg = new Sentence_group( dc_entry );				// create a sentence group

		state = sg.Select( 20, 0 );			// first group (0) of 20
		assertTrue( "unable to select first group for an iteration", state );

		for( i = 0; i <  20; i++ ) {
			s = sg.GetNext();				// next string
			assertTrue( "first group iteration stopped early " + Integer.toString( i ), s != null );
			System.err.printf( "first group: iteration test [%d] %s\n", i, s  );
		}

		s = sg.GetNext();
		assertTrue( "first group: after 20 iterations, get-next returned non null", s == null );
    }

    /*
    	Test getting an odd sized group from the middle.
    */
    @Test
    public void Test_odd_iteration() throws Exception {
		Sentence_group sg;
		String[] dc_entry;
		boolean state;
		int i;
		String s;

		dc_entry = mk_dc_entry( "kindergarten", 60 );
		sg = new Sentence_group( dc_entry );				// create a sentence group

		state = sg.Select( 7, 5 );			// get the fifth group of 7 sentences
		assertTrue( "unable to select odd group for an iteration", state );

		for( i = 0; i <  7; i++ ) {
			s = sg.GetNext();				// next string
			assertTrue( "odd group iteration stopped early " + Integer.toString( i ), s != null );
			System.err.printf( "odd group: iteration test [%d] %s\n", i, s  );
		}

		s = sg.GetNext();
		assertTrue( "odd group: after 20 iterations, get-next returned non null", s == null );
    }
}

