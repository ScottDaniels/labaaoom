/*
	Mnemonic:	Main_menu
	Abstract:	Class which supports the login interface; the initial activity.
	Author:		E. Scott Daniels   edaniels7@gatech.edu for CS6460
	Date:		17 September 2017
*/

package com.rocklizard.labaaoom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class LaBaaooM extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labaoom);

        //TODO:  grab an instance of the database, if no user exists (first invocation) get a user and password

    }

    /*
        Invoked when user enters login button on the login page
    */
    public void Attempt_login( View v ) {
        Intent target;          // target activity
        Class targetc;

        //TODO:   grab an instance of the datacache and validate the user/password

        targetc = Main_menu.class;
        target = new Intent( this, targetc );   // can't pass new... on startActivity() call
        startActivity(  target  );
    }
}
