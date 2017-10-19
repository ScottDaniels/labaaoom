package com.rocklizard.labaaoom;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
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

public class Student_list extends AppCompatActivity {
    private String target_name;         // name of the activity to pass control to

    /*
        Listener funciton must be created as variable; given to each list element.
    */
    protected View.OnClickListener name_callback = new View.OnClickListener() {        // listener each item in list refers to
        public void onClick( View v ) {
            CheckedTextView clicked_thing;
            TextView selected;
            Class targetc;
            Intent target;
            Bundle bun;

            selected = (TextView) findViewById( v.getId() );
            selected.setBackgroundColor( Color.parseColor( "#002040" ) );

            switch( target_name ) {
                case "show":
                    //Toast.makeText( getApplicationContext(), "name selected for show: " + selected.getText().toString(), Toast.LENGTH_SHORT ).show();

                    bun = new Bundle();
                    bun.putString( "student_name", selected.getText().toString()  );			// name of the student to suss out and display

                    targetc = Student_info.class;
                    target = new Intent( getApplicationContext(), targetc );
                    target.putExtras( bun );
                    startActivity( target );
                    break;

                case "eval":
                    //Toast.makeText( getApplicationContext(), "name selected for eval: " + selected.getText().toString(), Toast.LENGTH_SHORT ).show();

                    bun = new Bundle();
                    bun.putString( "student_name", selected.getText().toString()  );			// name of the student for evaluation

                    targetc = Eval_setup.class;
                    target = new Intent( getApplicationContext(), targetc );
                    target.putExtras( bun );
                    startActivity( target );
                    break;
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
            Toast.makeText(this, "There are no students to display.", Toast.LENGTH_LONG).show();
        }
    }
}
