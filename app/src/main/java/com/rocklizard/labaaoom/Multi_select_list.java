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
	Mnemonic:	Multi_select_list.java
	Abstract:	Class which supports creating and managing a list that allows multiple items
				to be selected and then passed to a follow-on activity.
	Author:		E. Scott Daniels   edaniels7@gatech.edu for CS6460
	Date:		19 October 2017
*/
package com.rocklizard.labaaoom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.view.View.generateViewId;


/*
	Creates a list of students from the datacache, and presents it. It allows mutiple
	sutdents to be selected and when the go button is pressed at the bottom, the indicated
	action (e.g. delete) is executed.   The text on the 'go' button is dynamic to reflect
	the resulting action, as is the title.  For some actions, a confirmation popup is generated
	and the user must ACK before going forward with the distruction.

	This must extend the force login activity, and use fork_internal() in order to ensure that
	return from an 'outside' activity drives the login verification.

	Future: add a select all option.
*/
public class Multi_select_list extends Force_login_activity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_multi_select_list );

		selected = new HashMap<String, Boolean>( );
	}

	private String target_action;                 // name of the activity to pass control to
	private HashMap<String, Boolean> selected;

	/*
        Listener funciton must be created as variable; given to each list element.
        When clicked, we check the selected map and if there is an entry that is true
        we set it to false and return the normal colour. If there is no entry, or it
        is false, we set it to true and set the text in the selected colour.
    */
	protected View.OnClickListener touch_callback = new View.OnClickListener( ) {        // listener each item in list refers to
		public void onClick( View v ) {
			TextView clicked_thing;     // item in the list that was touched
			String item_text;           // text that was displayed (student name etc)

			clicked_thing = (TextView) findViewById( v.getId( ) );
			item_text = clicked_thing.getText( ).toString( );
			if( selected.containsKey( item_text ) ) {                       // touched before
				selected.put( item_text, !selected.get( item_text ) );      // invert current setting
			} else {
				selected.put( item_text, true );
			}

			if( selected.get( item_text ) ) {
				clicked_thing.setBackgroundColor( Color.parseColor( "#002040" ) );       // if selected colour
			} else {
				clicked_thing.setBackgroundColor( Color.parseColor( "#000000" ) );       // if !selected remove colour
			}
		}
	};

	@Override
	protected void onResume() {
		String[] slist = null;			// list of things fetched from the cache for user to select from
		Intent intent;
		LinearLayout list_thing;        // container holding the list on the screen
		int i;
		CheckedTextView text_thing;
		Datacache dc;
		String button_text = "mishap";  // text written on button based on action to be taken
		Button butt_thing;

		super.onResume( );

		dc = Datacache.GetDatacache( );

		intent = getIntent( );
		target_action = intent.getExtras( ).getString( "target_action" );

		TextView sub_title;
		sub_title = (TextView) findViewById( R.id.sub_title );

		switch( target_action ) {
			case "delete_student":
				sub_title.setText( "Select student(s) to delete:" );
				slist = dc.GetStudentList( );
				button_text = "Delete!";
				break;

			// future: delete section, sentence group etc

			default:
				sub_title.setText( "Select student to process:" );
				break;
		}

		butt_thing = (Button) findViewById( R.id.multi_go_button );
		butt_thing.setText( button_text );

		list_thing = (LinearLayout) findViewById( R.id.multi_select_list );
		list_thing.removeAllViews( );
		if( slist != null && slist.length > 0 ) {
			for( i = 0; i < slist.length; i++ ) {
				text_thing = new CheckedTextView( this );   // what we will stuff in the list

				text_thing.setId( generateViewId( ) );       // must have an id
				text_thing.setOnClickListener( touch_callback );
				text_thing.setText( slist[ i ] );
				text_thing.setTextSize( 30 );
				text_thing.setTextColor( Color.parseColor( "#00f040" ) );   // future: pull from values

				list_thing.addView( text_thing );           // stuff it into the list
			}
		} else {
			//Toast.makeText( this, "There is nothing to display.", Toast.LENGTH_LONG ).show( );
			Tools.PopOk( this, "There is nothing to display." );
		}
	}

	/*
        Nothing really to do other than to finish which should return display to caller activity
    */
	public void Can_button_click( View V ) {
		finish( );
	}

	/*
		Toss up a specialised dialogue box and if the response is 'ok', then delete the list.
	*/
	private void delete_on_ok( final String[] dlist, final int kind ) {
		AlertDialog notice;

		notice =new AlertDialog.Builder( this ).

		create();
			notice.setTitle( "Delete the " + Integer.toString( dlist.length ) + " selected items?" );
			notice.setMessage( "Press 'Delete!' to confirm." );
   			notice.setCancelable( false );
			notice.setButton( Dialog.BUTTON_POSITIVE, "Delete!", new DialogInterface.OnClickListener() {
					public void onClick (DialogInterface dialog,int which){
						int i;
						Datacache dc;

						dc = Datacache.GetDatacache();

						for( i = 0; i < dlist.length; i++ ) {
							dc.Delete( dlist[i], kind );
						}

						finish( );
					}
				}
			);

			notice.setButton( Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
					public void onClick (DialogInterface dialog,int which){
					}
				}
			);

			notice.show();
	}

	/*
		React to the 'go' button being pressed.
		For some actions we invoke the 'act'_on_ok related function which tosses up a confirmation
		popup and if the user ACKs that, it goes ahead.  Other functions are just done on command.
	*/
	public void Go_button_click( View V ) {
		String[] selected_set;						// set of things from list to act on
		String[] slice;								// actual slice
		Iterator<Map.Entry<String, Boolean>> looper;
		Map.Entry<String, Boolean> kv;				// k,v pair
		int i = 0;

		if( selected.size() == 0 ) {		// there wasn't a list, so cant go forward on it
			finish();
		}

		selected_set = new String[selected.size()];		// at most there will be this many
		looper= selected.entrySet().iterator();
		while( looper.hasNext() ) {
			kv = looper.next();
			if( kv.getValue() ) {
				selected_set[i++] = kv.getKey(); 			// key is the student name
			}
		}

		slice = new String[i];			// take a slice (shame that java can't just pass a slice)
		for( i--; i >= 0; i-- ) {
			slice[i] = selected_set[i];
		}

		switch( target_action ) {
			case "delete_student":									// build the set of selected and pass for confirmation
				delete_on_ok( slice, Datacache.STUDENT );			// question authority and delete if ok
				break;

			default:
				finish();
				break;
		}

    }
}
