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
	Abstract:	Class which supports the main menu allowing the user to
				select one of several activities. This class extends the force_login_activity
				in order to ensure that the login is verified after navigation away from the
				application happens. There is one twist though, on creation this activity
				forces the 'external' state to be set in the FLA wrapper which causes the
				login screen to be displayed when this activity is created (at the time the
				app is started).
	Author:		E. Scott Daniels   edaniels7@gatech.edu for CS6460
	Date:		17 September 2017
*/

package com.rocklizard.labaaoom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/*
	Support the main menu activity.  This must extend the force login activity, and use fork_internal()
	in order to ensure that return from an 'outside' activity drives the login verification.
*/
public class Main_menu extends Force_login_activity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main_menu );

		Force_external();			// we always assume create comes from outside
	}

	/*
		Does all of the dirty work of creating a bundle and passing the action string to
		the underlying select student activity.
	*/
	private void drive_slist( String action ) {
		Class targetc;
		Intent target;
		Bundle bun;

		bun = new Bundle();
		bun.putString( "target_name", action );			// action select will take

		targetc = Student_list.class;
		target = new Intent( this, targetc );
		target.putExtras( bun );
		Fork_internal( target );
	}



    /*
        Functions driven when a menu item is selected from the list
    */

    // ---------- student related -----------------------------------
    public void Go_add_student( View v ) {
		Class targetc;
		Intent target;

		targetc = New_student.class;
		target = new Intent( this, targetc );
		Fork_internal( target );
    }

	public void Go_mod_student( View v ) {
		drive_slist( "modify_student" );
	}

    public void Go_del_student( View v ) {
		Class targetc;
		Intent target;
		Bundle bun;

		bun = new Bundle();
		bun.putString( "target_action", "delete_student" );			// what to do when Go is pressed

		targetc = Multi_select_list.class;
		target = new Intent( this, targetc );
		target.putExtras( bun );
		Fork_internal( target );
    }

    // --------- administration callbacks --------------------------
    public void Go_add_instructor( View v ) {
		Class targetc;
		Intent target;

		targetc = Add_creds.class;
		target = new Intent( this, targetc );
		Fork_internal( target );
    }

    // ----------- evaluation related -------------------------------
    public void Go_evaluate( View v ) {
		drive_slist( "eval" );
    }

	public void Go_view_student( View v ) {
		drive_slist( "show" );
    }
}
