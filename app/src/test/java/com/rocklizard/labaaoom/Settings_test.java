package com.rocklizard.labaaoom;

/*
	Mnemonic:	setttings_test
	Abstract:	Class that provides sanity check of the settings class.
	Author:		E. Scott Daniels
	Date:		17 September 2017
*/

import org.junit.Test;
import static junit.framework.Assert.assertTrue;
//import static org.junit.Assert.assertEquals;
//import org.junit.Test;
//import static org.junit.Assert.*;
import com.rocklizard.labaaoom.Settings;


public class Settings_test {

	/*
		Test the ability to generate a struct from a string.
	*/
	@Test
    public void Test_string_create() throws Exception {
		Settings settings;
		String ss;

		settings = new Settings( "background:normal,size:medium,style:sans" ); 		// order should not matter
		ss = settings.ToString( );

		//System.out.printf( ">>>> got: (%s) \n", ss );
		assertTrue( "settings create from string failed tostring check",  ss.equals( "background:normal,style:sans,size:medium,sgroup:grade1,rgroup:grade1" ) );
    }

    /*
    	Test the get/is functions
    */
    @Test
	public void Test_is_get( ) throws Exception {
		Settings settings;

		settings = new Settings( );		// just the defaults
		assertTrue( "settings get/is test: size not med", settings.IsSize( settings.MED ) );
		assertTrue( "settings get/is test: style reported large", !settings.IsSize( settings.LARGE ) );
		assertTrue( "settings get/is test: style reported small", !settings.IsSize( settings.SMALL ) );

		assertTrue( "settings get/is test: style not sans", settings.IsStyle( settings.SANS ) );
		assertTrue( "settings get/is test: reported style serif", !settings.IsStyle( settings.SERIF ) );

		assertTrue( "settings get/is test: bg not normal", settings.IsBackground( settings.NORMAL ) );
		assertTrue( "settings get/is test: reported bg inerted", !settings.IsBackground( settings.INVERTED ) );
	}

    /*
    	Test the set functions
    */
    @Test
	public void Test_sets( ) throws Exception {
		Settings settings;

		settings = new Settings( );		// just the defaults
		assertTrue( "settings get/is test: size not med", settings.IsSize( settings.MED ) );

		settings.SetSize( settings.LARGE );
		assertTrue( "settings get/is test: style reported large", settings.IsSize( settings.LARGE ) );

		settings.SetSize( settings.SMALL );
		assertTrue( "settings get/is test: style reported small", settings.IsSize( settings.SMALL ) );

		assertTrue( "settings get/is test: style not sans", settings.IsStyle( settings.SANS ) );
		settings.SetStyle( settings.SERIF );
		assertTrue( "settings get/is test: reported style serif", settings.IsStyle( settings.SERIF ) );

		assertTrue( "settings get/is test: bg not normal", settings.IsBackground( settings.NORMAL ) );
		settings.SetBackground( settings.INVERTED );
		assertTrue( "settings get/is test: reported bg inerted", settings.IsBackground( settings.INVERTED ) );
	}

}
