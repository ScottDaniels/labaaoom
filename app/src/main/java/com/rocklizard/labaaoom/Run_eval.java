package com.rocklizard.labaaoom;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Run_eval extends AppCompatActivity {
	private static final int LARGE_TEXT = 30;
	private static final int MED_TEXT = 24;
	private static final int SMALL_TEXT = 18;

	private Eval_set set;			// set of things we're displaying for this evaluation
	private long start_ts;			// timestamp of the time the first click was registered

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_run_eval );
	}

	/*
		Set up the evaluation and blank the screen.  We wait for the first click to display
		the first set of words and start the timer so the 'hand off' time is not included.
	*/
	@Override
	protected void onResume( ) {
		TextView text_thing;		// the spot where we write text on the screen
		Student s;
		Settings settings;
		Intent it;
		String target_name;			// name of the 'target' (student)
		Datacache dc;
		String ekind;				// kind of evaluation (sentence/random)
		String eset;				// name of the evaluation set selected

		super.onResume();

		start_ts = 0;				// indicate not initialised

		text_thing = (TextView) findViewById( R.id.eval_text );
		text_thing.setText( "" );		// clear it

		it = getIntent();
		if( it == null ) {
			System.out.printf( ">>> internal mishap no intent passed to run_eval\n" );
			finish();
			return;
		}
		target_name = it.getExtras( ).getString( "student_name" );		// student name from the caller; we use to fetch up from dc
		if( target_name == null ) {
			System.out.printf( ">>> internal mishap no stdent name passed to run_eval\n" );
			finish();
			return;
		}

		ekind = it.getExtras( ).getString( "eval_kind" );
		eset = it.getExtras( ).getString( "eval_set" );

		dc = Datacache.GetDatacache();									// point us at the thing
		s = dc.ExtractStudent( target_name );
		if( s == null ) {
			finish();
			Toast.makeText( this, "Internal mishap: unable to find student in datacache.", Toast.LENGTH_SHORT ).show( );
			return;
		}

		settings = s.GetSettings();				// pick up settings, and configure things based on them
		if( settings.IsBackground( settings.INVERTED ) ) {
			text_thing.setBackgroundColor( Color.parseColor( "#ffffff" ) );   // black text on white background
			text_thing.setTextColor( Color.parseColor( "#000000" ) );
		} else {
			text_thing.setBackgroundColor( Color.parseColor( "#000000" ) );   // black text on white background
			text_thing.setTextColor( Color.parseColor( "#ffffff" ) );
		}

		if( settings.IsSize( settings.LARGE ) ) {
			text_thing.setTextSize( LARGE_TEXT );
		} else {
			if( settings.IsSize( settings.MED ) ) {
				text_thing.setTextSize( MED_TEXT );
			} else {
				text_thing.setTextSize( SMALL_TEXT );
			}
		}

		text_thing.setText( "TESTING "  + ekind + " " + eset );		// clear it
		// future: set text style

	}

	/*
		Called with the click of the big button to go to the next sentence/word group
	*/
	public void Next_group( View v ) {
		finish();
	}

	/*
		Must override the back button to prevent accidental reverse to the start activity.
	*/
	public void onBackPressed() {
		/* do nothing */
	}
}
