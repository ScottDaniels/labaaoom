/*
	Mnemonic:	Main_menu
	Abstract:	Class which supports the login interface; the initial activity.
	Author:		E. Scott Daniels   edaniels7@gatech.edu for CS6460
	Date:		17 September 2017
*/

package com.rocklizard.labaaoom;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.rocklizard.labaaoom.M5hash.Mk_md5;

public class LaBaaooM extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_labaoom );

        //ActionBar actionBar = getActionBar();
        //actionBar.setBackgroundDrawable(new ColorDrawable( Color.GREEN));

        Datacache.Mk_datacache( getApplicationContext() );          // must initialise the datacach with context
    }

    /*
        This is a dummy class that always pushes to the login activity so that we can make things
        like the end of an evaluation revert to login.
    */
    protected void onResume( ) {
        //Class targetc;          // class which is the target destination as this is a hopping point only
        //Intent target;         // intent to push the focus to

        super.onResume();

		/*
        targetc = Login.class;
        target = new Intent( this, targetc );
        startActivity( target );
        */
    }

    /*
        Invoked when user enters login button on the login page
    */
    public void Attempt_login( View v ) {
        String  master_hash = "6901d625c28ea5fa1a88ad8e8aafe289";    // master, undeletable hash from default uid/pw combination
        Intent  target;          // target activity
        Class   targetc;
        EditText pw_thing;        // password field thingy to pull entered text from
        EditText un_thing;        // user name  field thingy to pull entered text from
        String  result;             // hash result from password

        //future:   grab an instance of the datacache and validate the user/password

        pw_thing = (EditText) findViewById(R.id.password);
        un_thing = (EditText) findViewById(R.id.user_name);
        result = Mk_md5(  un_thing.getText().toString(), pw_thing.getText().toString() );

        pw_thing.setText( "" );                         // safe to clear the pw field now

        if( result.equals( master_hash ) ) {            // future:  check what they might have changed it to as well
            targetc = Main_menu.class;
            target = new Intent( this, targetc );
            startActivity( target );
        } else {
            Toast t;

            t = Toast.makeText(this, "Invalid credentials entered", Toast.LENGTH_LONG);
            t.setGravity( Gravity.TOP | Gravity.START, 200, 200);
            t.show();
        }
    }
}
