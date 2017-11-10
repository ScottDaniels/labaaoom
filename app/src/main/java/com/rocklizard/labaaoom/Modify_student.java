package com.rocklizard.labaaoom;
/*
	Mnemonic:	Modify_student
	Abstract:	Class which supports the activity that allows a student's information to be changed
				in flight.
	Author:		E. Scott Daniels   edaniels7@gatech.edu for CS6460
	Date:		21 October 2017
*/

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/*
	Provides the functions that manage things when the modify student activity is active.
	Allows the user to change certain student information such as correcting spelling of
	name, and section assignment.

	This must extend the force login activity, and use fork_internal() in order to ensure that
	return from an 'outside' activity drives the login verification.
*/
public class Modify_student extends Force_login_activity {

	private Student s;					// referenced student to click functions have access
	private EditText student;			// spot on screen where student name is updated
	private EditText section;			// section space to change

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_modify_student );
	}

	@Override
	protected void onResume( ) {
		Datacache dc;
		Intent it;
		String target_name;			// evaluation target name from the list
		Settings settings;
		RadioButton rb1;            // generic radio buttons on the screen
		RadioButton rb2;
		RadioButton rb3;

		super.onResume( );

		it = getIntent( );
		target_name = it.getExtras( ).getString( "student_name" );        // student name from the caller
		if( target_name == null ) {
			finish( );                    // likely a return from another application which we don't allow
		}

		dc = Datacache.GetDatacache( );
		s = dc.ExtractStudent( target_name );

		if( s == null ) {            // shouldn't happen, but databases suck, so it might
			//Toast.makeText( this, "Internal mishap: student not in data cache: " + target_name, Toast.LENGTH_LONG ).show( );
			Tools.PopError( this, "Internal mishap: student not in data cache: " + target_name );
			finish( );
		}


		student = (EditText) findViewById( R.id.mod_student_name );
		section = (EditText) findViewById( R.id.mod_section_id );
		student.setText( target_name );
		section.setText( s.GetSection() );
	}


	/*
		Look to see if there were changes, and if there were update the student in the dc.
	*/
	public void Mod_clicked( View v ) {
		Datacache dc;
		String old_name;			// we'll hold this and delte the old entry only after current is saved

		dc = Datacache.GetDatacache();

		old_name = s.GetName();
		if( ! student.getText().toString().equals( old_name )  ) {
			s.Rename( student.getText().toString() );
			if ( dc.DepositStudent( s ) ) {
				dc.Delete( old_name, Datacache.STUDENT );                // delete the old thing from the dc, rename what we have, then save it
			} else {
				System.err.printf( "###ERROR### saving modiied student with new name; old entry not delted\n" );
			}
		} else {
			if( ! section.getText().toString().equals( s.GetSection() ) ) {
				s.SetSection( section.getText().toString() );
				dc.DepositStudent( s );
			}
		}

		finish( );
	}

	public void Cancel_clicked( View v ) {
		finish();
	}
}
