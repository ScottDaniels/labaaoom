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
