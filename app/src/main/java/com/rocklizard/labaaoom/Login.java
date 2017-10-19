/*
	Mnemonic:	Main_menu
	Abstract:	Class which supports the login interface; the initial activity.
	Author:		E. Scott Daniels   edaniels7@gatech.edu for CS6460
	Date:		17 September 2017
*/

package com.rocklizard.labaaoom;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;

import static com.rocklizard.labaaoom.M5hash.Mk_md5;
import static com.rocklizard.labaaoom.R.id.time;

public class Login extends Activity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_login );

	}


    private void nap( int ms ) {
        try {
            Thread.sleep( ms );
        } catch( java.lang.InterruptedException e ) {
			return;
        }
    }

    /*
        Invoked when user enters login go_button on the login page
    */
    public void Attempt_login( View v ) {
        String  master_hash = "6901d625c28ea5fa1a88ad8e8aafe289";    // master, undeletable hash from default uid/pw combination
        Intent  target;          // target activity
        Class   targetc;
        EditText pw_thing;        // password field thingy to pull entered text from
        EditText un_thing;        // user name  field thingy to pull entered text from
        String  result;             // hash result from password
        //Button  but_thing;

        //future:   grab an instance of the datacache and validate the user/password

        //but_thing = (Button) findViewById(R.id.login_button);
        //but_thing.setBackgroundColor( Color.parseColor( "#009040" ) );
		//nap( 500 );
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
            t.setGravity( Gravity.TOP | Gravity.START, 250, 100);
            t.show();
        }
        //but_thing.setBackgroundColor( Color.parseColor( "#003000" ) );
    }

}
