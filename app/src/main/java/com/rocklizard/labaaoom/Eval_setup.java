
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
	Mnemonic:	Eval_setup.java
	Abstract:	Supports the new (add) student activity. It must extend the force login activity
				in order to ensure that login is driven should the user navigate away while
				this activity is running.
	Author:		E. Scott Daniels	edaniels7@gatech.edu CS6460 Fall 2017
	Date:		12 October 2017
*/

package com.rocklizard.labaaoom;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import static android.view.View.generateViewId;
import static com.rocklizard.labaaoom.Datacache.GetDatacache;

public class Eval_setup extends Force_login_activity {
	static final int LT_RAND = 0;				// list types
	static final int LT_SENT = 1;

	boolean settings_changed = false;
	Student target = null;                    // student that we're setting up for
	TextView selected_rand = null;                    // what is currently selected
	TextView selected_sent = null;
	AlertDialog notice;

	/*
		Callback when an item in the random list is selected. We assume a selction
		changes what was the default and will cause settings to be saved.
	*/
	protected View.OnClickListener rand_touch_cb = new View.OnClickListener( ) {        // listener each item in list refers to
		public void onClick( View v ) {
			Settings settings = null;

			if( selected_rand != null ) {
				selected_rand.setBackgroundColor( Color.parseColor( "#000000" ) );       // clear the previously selected thing
			}
			selected_rand = (TextView) findViewById( v.getId( ) );
			selected_rand.setBackgroundColor( Color.parseColor( "#002040" ) );
			if( target != null ) {
				settings = target.GetSettings( );
				if( settings != null ) {
					settings.SetRandGroup( selected_rand.getText().toString() );
					settings_changed = true;
				}
			}
		}
	};

	/*
		Callback when an item in the sentence list is selected. We assume a selction
		changes what was the default and will cause settings to be saved.
	*/
	protected View.OnClickListener sent_touch_cb = new View.OnClickListener( ) {        // listener each item in list refers to
		public void onClick( View v ) {
			Settings settings = null;

			if( selected_sent != null ) {
				selected_sent.setBackgroundColor( Color.parseColor( "#000000" ) );       // clear the previously selected thing
			}
			selected_sent = (TextView) findViewById( v.getId( ) );
			selected_sent.setBackgroundColor( Color.parseColor( "#002040" ) );
			if( target != null ) {
				settings = target.GetSettings( );
				if( settings != null ) {
					settings.SetSentGroup( selected_sent.getText().toString() );
					settings_changed = true;
				}
			}
		}
	};

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_eval_setup );

		settings_changed = false;
		notice = null;
	}

	private void add2list( LinearLayout list_thing, String item, int list_type, View.OnClickListener ocl, Boolean selected ) {
		CheckedTextView text_thing;					// droid thing to add

		text_thing =new CheckedTextView( this );   // what we will stuff in the list

		text_thing.setId( generateViewId( ) );       // must have an id
		text_thing.setOnClickListener( ocl );
		text_thing.setText( item );
		text_thing.setTextSize( 20 );
		text_thing.setTextColor( Color.parseColor( "#00f040" ) );   // future: pull from values
		if( selected  ) {
			text_thing.setBackgroundColor( Color.parseColor( "#002040" ) );
			switch( list_type ) {
				case LT_RAND:
					selected_rand = text_thing;
					break;

				case LT_SENT:
					selected_sent = text_thing;
					break;
			}
		}

		list_thing.addView( text_thing );           // stuff it into the list
	}

	@Override
	protected void onPause( ) {
		if( notice != null ) {
			notice.dismiss();
		}

		super.onPause();
	}

	@Override
	protected void onResume( ) {
		Datacache dc;
		Intent it;
		String target_name;		// evaluation target name from the list
		TextView student;		// spot on screen where student name is updated
		Student s = null;		// we'll load this from the datacache
		Settings settings;
		RadioButton rb1;		// generic radio buttons on the screen
		RadioButton rb2;
		RadioButton rb3;
		LinearLayout list_thing;
		CheckedTextView text_thing;
		String[] slist;			// selection list from the datacace
		int i;
		String hold;

		super.onResume();

		it = getIntent();
		target_name = it.getExtras( ).getString( "student_name" );		// student name from the caller
		if( target_name == null ) {
			System.out.printf( ">>>> internal mishap:  eval setup called and no target name\n" );
			finish( );                    // likely a return from another application which we don't allow
			return;
		}

		student = (TextView) findViewById( R.id.student_name );
		student.setText( target_name );

		dc = GetDatacache();
		s = dc.ExtractStudent( target_name );
		if( s == null ) {            // shouldn't happen, but databases suck, so it might
			System.out.printf( ">>>> internal mishap:  eval setup: student not in dc\n" );
			//Toast.makeText( this, "Internal mishap: student not in data cache: " + target_name, Toast.LENGTH_LONG ).show( );
			Tools.PopError( this, "Internal mishap: student not in data cache: " + target_name );
			finish( );
			return;
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

		// ------------- populate the selection lists -------------------------------------
		list_thing = (LinearLayout) findViewById( R.id.random_list_layout );
		list_thing.removeAllViews( );
		slist = dc.GetRgroupList();			// get the random group

		add2list( list_thing, settings.GetRandGroup(), LT_RAND, rand_touch_cb, true ) ;
		if( slist != null && slist.length > 0 ) {
			for( i = 0; i < slist.length; i++ ) {
				if( ! slist[i].equals( settings.GetRandGroup() )  ) {
					add2list( list_thing, slist[i], LT_RAND, rand_touch_cb, false );
				}
			}
		}

		list_thing = (LinearLayout) findViewById( R.id.sent_list_layout );
		list_thing.removeAllViews( );
		slist = dc.GetSgroupList();			// get the sentence group list

		add2list( list_thing, settings.GetSentGroup( ), LT_SENT, sent_touch_cb, true );
		if( slist != null && slist.length > 0 ) {
			for( i = 0; i < slist.length; i++ ) {
				if( !slist[ i ].equals( settings.GetSentGroup( ) ) ) {
					add2list( list_thing, slist[ i ], LT_SENT, sent_touch_cb, false );
				}
			}
		}


		// Finally, check for a pending eval and ask to accept/reject it
		if( target.HasPendingEval( ) ) {
			accept_pending( );                // drive the pop up which takes action based on response
		}
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

		dc = GetDatacache();
		dc.DepositStudent( s );
	}


	// --------- popup stuff --------------------------------------------------------
	/*
		Toss up a specialised dialogue box and if the response is 'ok', then accept the pending eval
		else reject it.
	*/
	private void accept_pending(  ) {
		Evaluation eval;

		final String student_name;			// final vals for closuers
		final double wpm;
		final String etype;
		final String elist;

		notice = null;
		eval = target.GetPendingEval();
		if( eval == null ) {			// no pending, no prompt
			return;
		}
		wpm = eval.GetWpm();
		elist= eval.GetID();
		etype = eval.GetType();

		student_name = target.GetName();					// get name as we need to look up object in closure

		notice =new AlertDialog.Builder( this, R.style.PopUp ).create();
		notice.setTitle( "Pending Evaluation Exists" );
		notice.setMessage( "Name: " + student_name + "\n" + eval.PrettyPrint() );
		notice.setCancelable( false );
		notice.setButton( Dialog.BUTTON_POSITIVE, "Accept", new DialogInterface.OnClickListener() {
				public void onClick (DialogInterface dialog,int which){
					Datacache dc;
					Student	s;
					Averages aves;			// update averages when accepted

					dc = Datacache.GetDatacache();
					s = dc.ExtractStudent( student_name );
					if( s != null ) {
						System.out.printf( ">>>> accepting the evaluation saving pending eval\n" );
						s.AcceptPendingEval();				// accept it
						dc.DepositStudent( s );				// and tuck it away again

						System.out.printf( ">>>> accepting the evaluation getting averages\n" );
						aves = new Averages( s.GetSection(), elist, etype );
						aves.AddVal( wpm );
					}
				}
			}
		);

		notice.setButton( Dialog.BUTTON_NEGATIVE, "Reject", new DialogInterface.OnClickListener() {
				public void onClick ( DialogInterface dialog, int which){
					Datacache dc;
					Student	s;

					dc = Datacache.GetDatacache();
					s = dc.ExtractStudent( student_name );
					if( s != null ) {
						s.RejectPendingEval();				// drop it
						dc.DepositStudent( s );				// and tuck it away again
					}
				}
			}
		);

		notice.show();
	}

	// ----- reaction ------------------------------------------------------------

	/*
		Called when start random go_button pressed. Sets up and invokes
		an evaluation on random word groups.
	*/
	public void Start_rand_eval( View v ) {
		Class targetc;
		Intent it;
		Bundle bun;

		stash_if_needed( target );

		bun = new Bundle();
		bun.putString( "eval_kind", Evaluation.ET_RANDOM );					// send along the settings
		bun.putString( "eval_set", target.GetSettings().GetRandGroup() );
		bun.putString( "student_name", target.GetName() );

		targetc = Run_eval.class;
		it = new Intent( this, targetc );
		it.putExtras( bun );
		startActivity( it );					// MUST use startActivity() so that when it finishes we hit the login
	}

	/*
		Called when start random go_button pressed. Sets up and invokes
		an evaluation on random word groups.
	*/
	public void Start_sent_eval( View v ) {
		Class targetc;
		Intent it;
		Bundle bun;

		stash_if_needed( target );

		bun = new Bundle();
		bun.putString( "eval_kind", Evaluation.ET_SENTENCE );					// send along the settings
		bun.putString( "eval_set", target.GetSettings().GetSentGroup() );
		bun.putString( "student_name", target.GetName() );

		targetc = Run_eval.class;
		it = new Intent( this, targetc );
		it.putExtras( bun );
		startActivity( it );					// MUST use startActivity() so that when it finishes we hit the login
	}

	/*
		Invoked when a go_button in the background adjustment group is clicked
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
			System.out.printf( ">>>> adjusting bg invert checked\n" );
			settings.SetBackground( settings.INVERTED );
		} else {
			System.out.printf( ">>>> adjusting bg normal checked\n" );
			settings.SetBackground( settings.NORMAL );
		}
	}

	/*
		Invoked when a go_button in the style adjustment group is clicked
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
	}

	/*
		Invoked when a go_button in the size adjustment group is clicked
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
	}
}
