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
    This creates a list of students form the datacache, presents it and when the
    user selects one, the student selected is passed to the target (next) activity.
*/
public class Multi_select_list extends Activity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_multi_select_list );

		selected = new HashMap<String, Boolean>( );
	}

	private String target_name;                 // name of the activity to pass control to
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
		String[] slist;                 // list of students fetched from the cache
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
		target_name = intent.getExtras( ).getString( "target_name" );


		TextView sub_title;
		sub_title = (TextView) findViewById( R.id.sub_title );

		switch( target_name ) {
			case "delete":
				sub_title.setText( "Select student(s) to delete:" );
				button_text = "Delete!!";
				break;

			default:
				sub_title.setText( "Select student to process:" );
				break;
		}

		list_thing = (LinearLayout) findViewById( R.id.multi_select_list );
		list_thing.removeAllViews( );
		slist = dc.GetStudentList( );
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
			Toast.makeText( this, "There are no students to display.", Toast.LENGTH_LONG ).show( );
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
							System.out.printf( ">>>> deleting student: %s\n", dlist[i] );
						}

						Toast.makeText( getApplicationContext( ), "Deleting!!!!!!!", Toast.LENGTH_SHORT ).show( );
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
		For some target actions (e.g. delete)  we confirm before invoking. If user says Go,
		then we pass the selected list and lit it rip.
	*/
	public void Go_button_click( View V ) {
		String[] selected_set;						// set of things from list to act on
		String[] slice;								// actual slice
		Iterator<Map.Entry<String, Boolean>> looper;
		Map.Entry<String, Boolean> kv;				// k,v pair
		int i = 0;

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

		switch( target_name ) {
			case "delete_student":									// build the set of selected and pass for confirmation
				delete_on_ok( slice, Datacache.STUDENT );			// question authority and delete if ok
				break;

			default:
				finish();
				break;
		}

    }
}
