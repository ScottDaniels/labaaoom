package com.rocklizard.labaaoom;

/*
	Mnemonic:	New_student.java
	Abstract:	Supports the new (add) student activity.
	Author:		E. Scott Daniels
	Date:		22 September 2017
*/

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/*
	Manage the creation of a new student.

	This must extend the force login activity, and use fork_internal() in order to ensure that
	return from an 'outside' activity drives the login verification.
*/
public class New_student extends Force_login_activity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_new_student );
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
			//Toast.makeText(this, "Invalid name or section", Toast.LENGTH_LONG).show();
			Tools.PopError( this, "Invalid name or section" );
			return;
		}

		dc = Datacache.GetDatacache( );
		if( dc == null ) {
			//Toast.makeText(this, "Internal mishap: bad datacache", Toast.LENGTH_LONG).show();
			Tools.PopError( this, "Internal mishap: bad datacache" );
			return;
		}

		if( ! dc.HasStudent( name.getText().toString()) ) {					// truly new
			Class targetc;
			Intent target;         // after add we'll return

			st = new Student( name.getText().toString(), sect.getText().toString() );	// mk and add to dc
			if( dc.DepositStudent( st ) ) {
				//Toast.makeText(this, "student added.", Toast.LENGTH_LONG).show();
				Tools.PopOk( this, "student added." );
			} else {
				//Toast.makeText(this, "Internal mishap: student add failed.", Toast.LENGTH_LONG).show();
				Tools.PopError( this, "Internal mishap: student add failed." );
			}

			finish();
		} else {
			//Toast.makeText(this, "Not added: sudent already exists.", Toast.LENGTH_LONG).show();
			Tools.PopError( this, "Not added: sudent already exists." );
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
