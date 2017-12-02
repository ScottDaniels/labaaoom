/*

=================================================================================================
    (c) Copyright 2017 By E. Scott Daniels. All rights reserved.

    Redistribution and use in source and binary forms, with or without modification, are
    permitted provided that the following conditions are met:

        1. Redistributions of source code must retain the above copyright notice, this list of
            conditions and the following disclaimer.

        2. Redistributions in binary form must reproduce the above copyright notice, this list
            of conditions and the following disclaimer in the documentation and/or other materials
            provided with the distribution.

    THIS SOFTWARE IS PROVIDED BY E. Scott Daniels ``AS IS'' AND ANY EXPRESS OR IMPLIED
    WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
    FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL E. Scott Daniels OR
    CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
    SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
    ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
    NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
    ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

    The views and conclusions contained in the software and documentation are those of the
    authors and should not be interpreted as representing official policies, either expressed
    or implied, of E. Scott Daniels.
=================================================================================================
*/
/*
	Mnemonic:	Main_menu
	Abstract:	Class which supports the login interface. It is invoked by the force login activity
				when we believe that the user started something else and has returned so that
				we force a login.

				CAUTION:  this is NOT the app entry activity!!!

				Future:  need a single set of validataion functions (maybe a part of the datacache)

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

public class Login extends AppCompatActivity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_login );
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

    /*
        Invoked when user enters login go_button on the login page
    */
    public void Reattempt_login( View v ) {
        String  master_hash = "6901d625c28ea5fa1a88ad8e8aafe289";    // master, undeletable hash from default uid/pw combination
        EditText pw_thing;        			// password field thingy to pull entered text from
        EditText un_thing;        			// user name  field thingy to pull entered text from
        String  result;            			// hash result from password
		Datacache dc;
		//Toast t;

        pw_thing = (EditText) findViewById(R.id.password);
        un_thing = (EditText) findViewById(R.id.user_name);
        result = Mk_md5(  un_thing.getText().toString(), pw_thing.getText().toString() );
        pw_thing.setText( "" );                         // safe to clear the pw field now

		dc = Datacache.Mk_datacache( getApplicationContext() );
		if( dc == null ) {                        // shouldn't happen but parinoia prevents a crash without messages
			System.out.printf( ">>> ###ERROR## no datacache pointer\n" );
			//Toast.makeText( this, "###ERR### internal mishap: no datacache!", Toast.LENGTH_LONG ).show( );
			Tools.PopError( this, "###ERR### internal mishap: no datacache!" );
			return;
		}

		if( dc.HasElement( "passwd" ) ) {													// user has defined at least one instructor id/passwd
			if( dc.ValidateInstructor( un_thing.getText().toString(), result ) ) {			// ok to finish if what was entered is defined and matches
				finish();
				return;
			} else {
				//t = Toast.makeText( this, "Please use valid credentials.", Toast.LENGTH_LONG );
				//t.setGravity( Gravity.TOP | Gravity.START, 250, 100 );
				//t.show( );
				Tools.PopError( this, "Please use valid credentials." );
				return;			// must return here to prevent the system default creds being accepted when a password element exists
			}
		}

        if( result.equals( master_hash ) ) {            // future:  check what they might have changed it to as well
			finish();
        } else {
           //t = Toast.makeText(this, "Invalid credentials entered", Toast.LENGTH_LONG);
           //t.setGravity( Gravity.TOP | Gravity.START, 250, 100);
           //t.show();
			Tools.PopError( this, "Invalid credentials entered." );
        }
    }

    /*
    	It is VERY important that this activity override the function of the back button.
    	If is isn't overridden, and ignored, pressing back will defet the revalidation
    	process.
    */
	@Override
	public void onBackPressed() {
		//Toast.makeText( this, "Enter valid user/password or press home button.", Toast.LENGTH_SHORT ).show( );
		Tools.PopError( this, "Enter valid user/password or press home button." );
	}
}
