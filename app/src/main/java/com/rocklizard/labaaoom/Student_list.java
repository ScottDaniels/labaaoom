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
	Mnemonic:	Student_list.java
	Abstract:	Create and manage a list of students.  The list allows a single student name
	            to be touched which causes a follow-on activity (e.g. eval_setup) to be launched.
	Author:		E. Scott Daniels   edaniels7@gatech.edu for CS6460
	Date:		23 September 2017
*/

package com.rocklizard.labaaoom;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.View.generateViewId;

/*
    This creates a list of students form the datacache, presents it and when the
    user selects one, the student selected is passed to the target (next) activity.
*/

public class Student_list extends Force_login_activity {
    private String target_name;         // name of the activity to pass control to

    /*
        Listener funciton must be created as variable; given to each list element.
    */
    protected View.OnClickListener name_callback = new View.OnClickListener() {        // listener each item in list refers to
        public void onClick( View v ) {
            CheckedTextView clicked_thing;
            TextView selected;
            Class targetc = null;
            Intent target;
            Bundle bun;

            selected = (TextView) findViewById( v.getId() );
            selected.setBackgroundColor( Color.parseColor( "#002040" ) );   // quick comfirmation

            bun = new Bundle();
            bun.putString( "student_name", selected.getText().toString()  );			// name of the student for evaluation
            switch( target_name ) {
                case "show":
                    targetc = Student_info.class;
                    break;

                case "eval":
                    targetc = Eval_setup.class;
                    break;

                case "modify_student":
                    targetc = Modify_student.class;
                    break;

                default:
                    //Toast.makeText( getApplicationContext(), "Internal mishap in selecct list; unknown target!", Toast.LENGTH_LONG ).show();
					Tools.PopError( getApplicationContext(), "Internal mishap in selecct list; unknown target!" );
                    finish();
            }

            if( targetc != null ) {
				target = new Intent( getApplicationContext( ), targetc );
				target.putExtras( bun );
				//startActivity( target );
                Fork_internal( target );        // start the activity
			}
        }
    } ;

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_student_list );
	}

    @Override
    protected void onResume() {
        String[] slist;                 // list of students fetched from the cache
        Intent intent;
        LinearLayout list_thing;        // container holding the list on the screen
        int i;
        CheckedTextView text_thing;
		Datacache dc;

        super.onResume();

        dc = Datacache.GetDatacache();

        intent = getIntent();
        target_name = intent.getExtras( ).getString( "target_name" );


        TextView sub_title;
        sub_title = (TextView) findViewById( R.id.sub_title );

        switch( target_name )  {
            case "show":
                sub_title.setText( "Select student to show:" );
				break;

            case "eval":
                sub_title.setText( "Select student to evaluate:" );
                break;

			case "modify_student":
				sub_title.setText( "Select student to modify:" );
				break;

            default:
                sub_title.setText( "Select student to process:" );
                break;
        }

        list_thing = (LinearLayout) findViewById( R.id.student_list );
        list_thing.removeAllViews();
        slist = dc.GetStudentList( );
        if( slist != null &&  slist.length > 0 ) {
            for( i = 0; i < slist.length; i++ ) {
                text_thing = new CheckedTextView( this );   // what we will stuff in the list

                text_thing.setId( generateViewId() );       // must have an id
                text_thing.setOnClickListener( name_callback );
                text_thing.setText( slist[i] );
                text_thing.setTextSize( 30 );
				text_thing.setTextColor( Color.parseColor( "#00f040" ) );   // future: pull from values

                list_thing.addView( text_thing );           // stuff it into the list
            }
        } else {
            //Toast.makeText(this, "There are no students to display.", Toast.LENGTH_LONG).show();
			Tools.PopOk( this, "There are no students to display." );
        }
    }
}
