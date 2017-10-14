package com.rocklizard.labaaoom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Eval_setup extends Activity {
	boolean settings_changed = false;
	Student target = null;					// student that we're setting up for

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_eval_setup );

		settings_changed = false;
	}

	@Override
	protected void onResume( ) {
		Datacache dc;
		Intent it;
		String target_name;		// evaluation target name from the list
		TextView student;		// spot on screen where student name is updated
		Student s;				// we'll load this from the datacache
		Settings settings;
		RadioButton rb1;			// generic radio buttons on the screen
		RadioButton rb2;
		RadioButton rb3;

		super.onResume();

		it = getIntent();
		target_name = it.getExtras( ).getString( "student_name" );		// student name from the caller
		if( target_name == null ) {
			finish( );                    // likely a return from another application which we don't allow
		}

		student = (TextView) findViewById( R.id.student_name );
		student.setText( target_name );

		dc = Datacache.GetDatacache();
		s = dc.ExtractStudent( target_name );
		if( s == null ) {            // shouldn't happen, but databases suck, so it might
			Toast.makeText( this, "Internal mishap: student not in data cache: " + target_name, Toast.LENGTH_LONG ).show( );
			finish( );
		}

		target = s;				// stash so available for reactionary functions

		settings = s.GetSettings();									// fill out the settings on the screen
		rb1 = (RadioButton) findViewById( R.id.font_serif_rb );
		rb2 = (RadioButton) findViewById( R.id.font_sans_rb );
		rb1.setChecked( settings.IsStyle( settings.SERIF ) );
		rb2.setChecked( settings.IsStyle( settings.SANS ) );

		rb1 = (RadioButton) findViewById( R.id.bg_invert_rb );
		rb2 = (RadioButton) findViewById( R.id.bg_normal_rb );
		rb1.setChecked( settings.IsBackground( settings.INVERTED ) );
		rb2.setChecked( settings.IsBackground( settings.NORMAL ) );

		rb1 = (RadioButton) findViewById( R.id.size_small_rb );
		rb2 = (RadioButton) findViewById( R.id.size_med_rb );
		rb3 = (RadioButton) findViewById( R.id.size_large_rb );
		rb1.setChecked( settings.IsSize( settings.SMALL ) );
		rb2.setChecked( settings.IsSize( settings.MED ) );
		rb3.setChecked( settings.IsSize( settings.LARGE ) );
		System.out.printf( ">>> eval setup settings large == %s  %d\n", settings.IsSize( settings.LARGE ) ? "true" : "false", settings.GetSize() );

	}

	//  ------------------ support -------------------------------------------

	/*
		If the settings have changed, stash the student information before going on.
	*/
	private void stash_if_needed( Student s ) {
		Datacache dc;

		if( ! settings_changed ) {
			return;
		}

		dc = Datacache.GetDatacache();
		dc.DepositStudent( s );
		Toast.makeText(this, "Student setting changes saved", Toast.LENGTH_LONG).show();
	}

	// ----- reaction ------------------------------------------------------------

	/*
		Called when start random button pressed. Sets up and invokes
		an evaluation on random word groups.
	*/
	public void Start_rand_eval( View v ) {
		Toast.makeText(this, "start random eval button clicked", Toast.LENGTH_LONG).show();

		stash_if_needed( target );
	}

	/*
		Called when start random button pressed. Sets up and invokes
		an evaluation on random word groups.
	*/
	public void Start_sent_eval( View v ) {
		Toast.makeText(this, "start sentence eval button clicked", Toast.LENGTH_LONG).show();

		stash_if_needed( target );
	}

	/*
		Invoked when a button in the background adjustment group is clicked
	*/
	public void Adjust_bg( View v ) {
		RadioButton rb1;					// radio buttons to query
		RadioButton rb2;
		Settings settings;

		if( target == null ) {
			return;
		}

		settings_changed = true;
		rb1 = (RadioButton) findViewById( R.id.bg_invert_rb );
		rb2 = (RadioButton) findViewById( R.id.bg_normal_rb );

		settings = target.GetSettings( );
		if( rb1.isChecked() ) {
			settings.SetBackground( settings.INVERTED );
		} else {
			if( rb2. isChecked( ) ) {
				settings.SetBackground( settings.NORMAL );
			}
		}

		//Toast.makeText(this, "radio button callback driven for bg", Toast.LENGTH_LONG).show();
	}

	/*
		Invoked when a button in the style adjustment group is clicked
	*/
	public void Adjust_style( View v ) {
		RadioButton rb1;					// radio buttons to query
		RadioButton rb2;
		Settings settings;

		if( target == null ) {
			return;
		}

		settings_changed = true;
		rb1 = (RadioButton) findViewById( R.id.font_serif_rb );
		rb2 = (RadioButton) findViewById( R.id.font_sans_rb );

		settings = target.GetSettings( );
		if( rb1.isChecked() ) {
			settings.SetStyle( settings.SERIF );
		} else {
			if( rb2. isChecked( ) ) {
				settings.SetStyle( settings.SANS );
			}
		}

		//Toast.makeText(this, "radio button callback driven for style", Toast.LENGTH_LONG).show();
	}

	/*
		Invoked when a button in the size adjustment group is clicked
	*/
	public void Adjust_size( View v ) {
		RadioButton rb1;					// radio buttons to query
		RadioButton rb2;
		RadioButton rb3;
		Settings settings;

		if( target == null ) {
			return;
		}

		settings_changed = true;
		rb1 = (RadioButton) findViewById( R.id.size_small_rb );
		rb2 = (RadioButton) findViewById( R.id.size_med_rb );
		rb3 = (RadioButton) findViewById( R.id.size_large_rb );

		settings = target.GetSettings( );
		if( rb1.isChecked() ) {
			settings.SetSize( settings.SMALL );
		} else {
			if( rb2. isChecked( ) ) {
				settings.SetSize( settings.MED );
			} else {
				settings.SetSize( settings.LARGE );
			}
		}

		//Toast.makeText(this, "radio button callback driven for size", Toast.LENGTH_LONG).show();
	}
}
