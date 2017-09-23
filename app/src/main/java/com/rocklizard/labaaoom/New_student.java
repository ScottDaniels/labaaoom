package com.rocklizard.labaaoom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class New_student extends Activity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_new_student );
	}

	/*
		All activities must finish on pause to force restart at the login screen.
	*/
	@Override
	protected void onPause( ) {
		finish();							// ensure that we go back to the log in screen
		super.onPause();					// must finish before driving super class
	}

	/*
		Add button clicked. Validate student and add if ok.
	*/
	public void Add_clicked( View v ) {
		EditText name;
		EditText sect;
		Datacache dc;
		Student st;

		name = findViewById( R.id.student_name );
		sect = findViewById( R.id.section_id );

		if( name.getText().toString().equals( "" ) || sect.getText().toString().equals( "" ) ) {
			Toast.makeText(this, "Invalid name or section", Toast.LENGTH_LONG).show();
			return;
		}

		dc = Datacache.Get_datacache( );
		if( dc == null ) {
			Toast.makeText(this, "Internal mishap: bad datacache", Toast.LENGTH_LONG).show();
			return;
		}

		if( ! dc.HasStudent( name.getText().toString()) ) {					// truly new
			Class targetc;
			Intent target;         // after add we'll return

			st = new Student( name.getText().toString(), sect.getText().toString() );	// mk and add to dc
			if( dc.DepositStudent( st ) ) {
				Toast.makeText(this, "student added.", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Internal mishap: student add failed.", Toast.LENGTH_LONG).show();
			}

			finish();
		} else {
			Toast.makeText(this, "Not added: sudent already exists.", Toast.LENGTH_LONG).show();
		}
	}

	/*
		Cancel button clicked; clear fields, and return to main menu.
	*/
	public void Cancel_clicked( View v ) {
		EditText name;
		EditText sect;
		Class targetc;
		Intent target;         // after add we'll return

		name = findViewById( R.id.student_name );
		sect = findViewById( R.id.section_id );

		name.setText( "" );
		sect.setText( "" );
		finish();
	}

}
