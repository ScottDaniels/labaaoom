/*
	Mnemonic:	Run_eval.java
	Abstract:	This is the class which provides functions to the run-eval sctivity.
				It manages an evaluation from start to finish.
	Author:		E. Scott Daniels   edaniels7@gatech.edu for CS6460
	Date:		24 October 2017
*/

package com.rocklizard.labaaoom;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class Run_eval extends AppCompatActivity {
	private static final int LARGE_TEXT = 30;
	private static final int MED_TEXT = 24;
	private static final int SMALL_TEXT = 18;

	private Eval_set set;			// set of things we're displaying for this evaluation
	private long start_ts;			// timestamp of the time the first click was registered
	private	Sentence_group sg;		// the group of setences from which we will display a few
	private Student student;		// the student being evaluated
	private TextView text_thing;	// the spot where we write text on the screen
	private int		words;			// number of words that appeared in the evaluation
	private String ekind;			// kind of evaluation (sentence/random)
	private String eset;			// name of the evaluation set selected

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
		Datacache dc;
		Settings settings;
		Intent it;
		String target_name;			// name of the 'target' (student)
		int i;
		int ce;						// completed evals

		super.onResume();

		dc = Datacache.GetDatacache( );
		start_ts = 0;				// indicate not initialised
		words = 0;

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
		if( ekind == null ) {
			ekind = "sent";
		}
		eset = it.getExtras( ).getString( "eval_set" );

		dc = Datacache.GetDatacache();									// point us at the thing
		student = dc.ExtractStudent( target_name );
		if( student == null ) {
			System.out.printf( ">>> internal mishap run eval: student was null\n" );
			finish();
			Toast.makeText( this, "Internal mishap: unable to find student in datacache.", Toast.LENGTH_SHORT ).show( );
			return;
		}

		settings = student.GetSettings();				// pick up settings, and configure things based on them
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

		// future: set text style

		System.out.printf( ">>> running eval type: %s\n", ekind );
		if( ekind.equals( "sent" )  ) {
			sg = dc.ExtractSgroup( eset, true ) ;			// extract all of the sentences from dc
			ce = student.GetNEvals( Student.SENTENCES );
		} else {
			System.out.printf( ">>> extracting random word group: %s\n", eset );
			sg = dc.ExtractWgroup( eset, true ) ;			// extract all of the words from dc
			ce = student.GetNEvals( Student.RANDOM );
		}

		if( sg == null ) {
			System.out.printf( ">>> no sentence group returned; abort\n" );
			finish();
			return;
		}

		i = ce % sg.GetSize( 10 );				// get the set in the group to present
		System.out.printf( ">>>>  run_eval is rquesting set %d set has %d of size 10\n", i,  sg.GetSize( 10 ) );
		sg.Select( 10, i ); 					// select the set in the group
	}

	/*
		Called with the click of the big button to go to the next sentence/word group
	*/
	public void Next_group( View v ) {
		String out_str;
		long elapsed;						// elapsed time
		String[] tokens;					// we'll split and count
		double wpm;
		Evaluation eval;					// eval we will stash as pending in the student object
		Datacache dc;

		if( start_ts == 0 ) {                // first click
			start_ts = new Date( ).getTime( );        // current timestamp
		}

		if( sg == null ) {
			System.out.printf( ">>>> internal mishap: sg is null in run eval click function?\n" );
			finish();
			return;
		}

		if( (out_str = sg.GetNext() ) == null ) {        // eval is done; capture data, save the student with the pending info and return
			dc = Datacache.GetDatacache( );
			elapsed = (new Date( ).getTime( ) - start_ts)/1000;        	// amount of time passed (seconds)
			System.out.printf( ">>> elapsed=%d words=%d\n", elapsed, words );
			wpm  = ((double) words / (double) elapsed) * 60.0;
			text_thing.setText( "" );
			eval = new Evaluation( String.format( "%s,%s,%.2f,%d", eset, ekind, wpm, start_ts ) );
			student.AddPendingEval( eval );
			dc.DepositStudent( student ); 		// must stash now to preserve the pending information

			finish( );							// all done; go home
			return;
		}

		text_thing.setText( out_str );			// show next string
		tokens = out_str.split( " " );
		words += tokens.length;
	}

	/*
		Must override the back button to prevent accidental reverse to the start activity.
	*/
	public void onBackPressed() {
		/* do nothing */
	}
}
