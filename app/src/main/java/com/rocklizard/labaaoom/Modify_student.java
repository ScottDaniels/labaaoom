package com.rocklizard.labaaoom;

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
*/
public class Modify_student extends Activity {

	private Student s;					// referenced student to click functions have access

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
		EditText student;			// spot on screen where student name is updated
		EditText section;			// section space to change
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
			Toast.makeText( this, "Internal mishap: student not in data cache: " +
								  target_name, Toast.LENGTH_LONG ).show( );
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
		finish( );
	}

	public void Can_clicked( View v ) {
		finish();
	}
}
