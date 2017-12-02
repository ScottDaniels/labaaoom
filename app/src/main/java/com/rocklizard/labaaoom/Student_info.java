/*

=================================================================================================
    (c) Copyright 2017 By E. Scott Daniels. All rights reserved.

    Redistribution and use in source and binary forms, with or without modification, are
    permitted provided that the following conditions are met:

        1. Redistributions of source code must retain the above copyright notice, this list of
            conditions and the following disclaimer.

        2. Redistributions in binary form must reproduce the above copyright notice, this list
            of conditions and the following disclaimer in the documentation and/or other materials
            provided with the distribution.

    THIS SOFTWARE IS PROVIDED BY E. Scott Daniels ``AS IS'' AND ANY EXPRESS OR IMPLIED
    WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
    FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL E. Scott Daniels OR
    CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
    SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
    ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
    NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
    ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

    The views and conclusions contained in the software and documentation are those of the
    authors and should not be interpreted as representing official policies, either expressed
    or implied, of E. Scott Daniels.
=================================================================================================
*/
/*
	Mnemonic:	Student_info.java
	Abstract:	Manages the display of a student's evaluation history.
	Author:		E. Scott Daniels   edaniels7@gatech.edu for CS6460
	Date:		23 September 2017
*/

package com.rocklizard.labaaoom;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


/*
	This must extend the force login activity, and use fork_internal() in order to ensure that
	return from an 'outside' activity drives the login verification.
*/
public class Student_info extends Force_login_activity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_student_info );
	}

	@Override
	protected void onResume( ) {
		TextView student;
		TextView section;
		TextView last_ev_date;
		TextView last_ev_score;
		TextView ave_score;
		String target_name;
		Intent it;
		Student s;
		Datacache dc;
		Evaluation last_e;
		double[] mmai;
		String rgroup;				// group names from the last evaluation
		String sgroup;				// used to get average lines
		Settings settings;
		int	student_rave = 0;		// student's random and sentence average score
		int student_save = 0;
		int student_rlast = 0;		// last scores
		int student_slast = 0;
		int	student_max = 0;		// student's max score for min top on graph
		Averages rave;				// average information  (overall and section)
		Averages save;
		int	raval;					// random and sentence average -- actual values
		int saval;
		int	min_top = 100;			// min top value so graphs are equalised

		super.onResume();

		dc = Datacache.GetDatacache();
		it = getIntent();
		target_name = it.getExtras( ).getString( "student_name" );		// student name from the caller
		if( target_name == null ) {
			finish( );                    // likely a return from another application which we don't allow
		}

		student = (TextView) findViewById( R.id.student_name );
		student.setText( target_name );

		s = dc.ExtractStudent( target_name );

		section = (TextView) findViewById( R.id.section_name );
		section.setText( s.GetSection() );

		last_ev_date = (TextView) findViewById( R.id.last_eval_date );
		last_e = s.GetLastEval( ) ; 											// get the most recent eval to pull date from
		if( last_e != null ) {
			last_ev_date.setText( last_e.GetDate() );
		} else {
			last_ev_date.setText( "None" );
		}

		last_e = s.GetLastEval( Evaluation.ET_SENTENCE );						// fill in sentence things
		last_ev_score = (TextView) findViewById( R.id.last_seval_score );		// dig screen things to populate
		ave_score = (TextView) findViewById( R.id.ave_sscore );

		if( last_e != null ) {
			mmai = s.GetMMAI( Student.SENTENCES );
			student_save = (int) mmai[2];										// save for graphing marker
			student_max = (int) mmai[1];

			ave_score.setText(  String.format( "%.0f wpm",  mmai[2] ) );
			last_ev_score.setText(  String.format( "%.0f wpm", last_e.GetWpm() ) );
		} else {
			ave_score.setText(  "-- wpm" );
			last_ev_score.setText(  "-- wpm" );
		}

		last_e = s.GetLastEval( Evaluation.ET_RANDOM );						// fill in random things
		last_ev_score = (TextView) findViewById( R.id.last_weval_score );		// dig screen things to populate
		ave_score = (TextView) findViewById( R.id.ave_wscore );
		last_ev_date = (TextView) findViewById( R.id.last_eval_date );

		if( last_e != null ) {
			mmai = s.GetMMAI( Student.RANDOM );									// get the random stats
			student_rave = (int) mmai[2];										// save for graphing marker
			if( student_max < mmai[1] ) {
				student_max = (int) mmai[1];
			}

			ave_score.setText(  String.format( "%.0f wpm",  mmai[2] ) );
			last_ev_score.setText(  String.format( "%.0f wpm", last_e.GetWpm() ) );
		} else {
			ave_score.setText(  "-- wpm" );
			last_ev_score.setText(  "-- wpm" );
		}


		settings = s.GetSettings( );			// get settings so we can get last eval group used
		rgroup = settings.GetRandGroup( );
		sgroup = settings.GetSentGroup( );
		rave = new Averages( s.GetSection(), rgroup, Averages.ET_RAND );	// get averages for each type
		save = new Averages( s.GetSection(), sgroup, Averages.ET_SENT );

		raval = (int) rave.GetAve( false );				// false == get just section averages
		saval = (int) save.GetAve( false );

		min_top = raval > 100 ? (raval > saval ? raval : saval) : (saval > 100 ? saval : 100);		// set min top based on class/section averages
		min_top = student_max > min_top ? student_max : min_top;			// if sutdent max is over the average, this is the min top for the graph
		if( min_top % 10 != 0 ) {
			min_top += 10 - (min_top % 10);                // round to even mult of 10
		}

		draw_graph( R.id.student_graph_left, s.GetData( Student.SENTENCES ), saval, student_save, min_top );
		draw_graph( R.id.student_graph_right, s.GetData( Student.RANDOM ), raval, student_rave, min_top );
	}

	/*
		Given a linear layout name, and a set of data, draw the graph in the layout
	*/
	private void draw_graph( int layout_id, int[] values, int sect_ave,  int student_ave, int min_top ) {
		int gh = 100;
		int gw = 200;		// defaults if layout is not found
		Graph gr;
		LinearLayout ll;

		ll = (LinearLayout) findViewById( layout_id );
		if( ll == null ) {
			return;
		}

		gr = new Graph( gw, gh, 30, 10 );
		gr.SetMinTop( min_top );					// set before data load as data load could be larger
		gr.Add_values( values );
		gr.AddMarker( sect_ave, "#ff9000" );		// section ave is orange
		gr.AddMarker( student_ave, "#ffff00" ); 	// student ave drawn on top is yellow
		gr.Set_pt_space( 4 );
		ll.setBackground( new BitmapDrawable( getResources(), gr.Paint( ) ) );
	}
}
