/*
	Mnemonic:	Main_menu
	Abstract:	Class which supports the main menu allowing the user to
				select one of several activities.
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

public class Main_menu extends Activity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main_menu );
	}

	/*
		All activities must finish on pause to force restart at the login screen.
	*/
	@Override
	protected void onPause( ) {
		super.onPause();					// must finish before driving super class
	}

	/*
		Does all of the dirty work of crating a bundle and passing the action string to
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
		startActivity( target );
	}

    /*
        Functions driven when a menu item is selected from the list
    */
    public void Go_add_student( View v ) {
		Class targetc;
		Intent target;

		targetc = New_student.class;
		target = new Intent( this, targetc );
		startActivity( target );
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
		startActivity( target );
    }

    public void Go_evaluate( View v ) {
		drive_slist( "eval" );
    }

	public void Go_view_student( View v ) {
		drive_slist( "show" );
    }
}
