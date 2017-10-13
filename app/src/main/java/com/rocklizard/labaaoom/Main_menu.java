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
//import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
//import android.widget.CheckedTextView;
//mport android.widget.LinearLayout;
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
        Functions driven when a menu item is selected from the list
    */
    public void Go_add_student( View v ) {
		Class targetc;
		Intent target;

		targetc = New_student.class;
		target = new Intent( this, targetc );
		startActivity( target );
//		Toast.makeText(this, "Add stundent", Toast.LENGTH_LONG).show();
    }

    public void Go_evaluate( View v ) {
		Class targetc;
		Intent target;
		Bundle bun;

		bun = new Bundle();
		bun.putString( "target_name", "eval" );			// pass what the selection is to drive

		targetc = Student_list.class;
		target = new Intent( this, targetc );
		target.putExtras( bun );
		startActivity( target );
		//Toast.makeText(this, "Starting Evaluation", Toast.LENGTH_LONG).show();
    }

	public void Go_view_student( View v ) {
		Class targetc;
		Intent target;
		Bundle bun;

		bun = new Bundle();
		bun.putString( "target_name", "show" );			// pass what the selection is to drive


		targetc = Student_list.class;
		target = new Intent( this, targetc );
		target.putExtras( bun );
		startActivity( target );

		//Toast.makeText(this, "View Student", Toast.LENGTH_LONG).show();
    }
}
