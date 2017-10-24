/*
	Mnemonic:	Main_menu
	Abstract:	The application entry point class.  It doesn't do much other than
				"create" the instance of the datacache, and then start the manin menu
				activity. The main menu has one unique property, it forces the login
				activity to drive on creation, even though it's being called by a LaBaaooM
				activity in order to ensure that we drive the login when the app is started.
	Author:		E. Scott Daniels   edaniels7@gatech.edu for CS6460
	Date:		17 September 2017
*/

package com.rocklizard.labaaoom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LaBaaooM extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_labaoom );

        Datacache.Mk_datacache( getApplicationContext() );          // must initialise the datacach with context
    }

    /*
    	All that this class does is to force the start of main menu which causes the login
    	screen (our splash) to be presented.
    */
    @Override
    protected void onResume( ) {
        Intent target;
        Class targetc;

		super.onResume();

        targetc = Main_menu.class;
        target = new Intent( this, targetc );
        startActivity( target );
		finish();
    }
}
