package com.rocklizard.labaaoom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Eval_setup extends Activity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_eval_setup );
	}

	@Override
	protected void onResume( ) {
		Intent it;
		String target_name;
		TextView student;

		super.onResume();

		it = getIntent();
		target_name = it.getExtras( ).getString( "student_name" );		// student name from the caller
		if( target_name == null ) {
			finish( );                    // likely a return from another application which we don't allow
		}

		student = (TextView) findViewById( R.id.student_name );
		student.setText( target_name );
	}

	/*
		Called when start random button pressed. Sets up and invokes
		an evaluation on random word groups.
	*/
	public void Start_rand_eval( View v ) {
		Toast.makeText(this, "start random eval button clicked", Toast.LENGTH_LONG).show();
	}

	/*
		Called when start random button pressed. Sets up and invokes
		an evaluation on random word groups.
	*/
	public void Start_sent_eval( View v ) {
		Toast.makeText(this, "start sentence eval button clicked", Toast.LENGTH_LONG).show();
	}

	/*
		Invoked when a button in the background adjustment group is clicked
	*/
	public void Adjust_bg( View v ) {
		Toast.makeText(this, "radio button callback driven for bg", Toast.LENGTH_LONG).show();
	}

	/*
		Invoked when a button in the style adjustment group is clicked
	*/
	public void Adjust_style( View v ) {
		Toast.makeText(this, "radio button callback driven for style", Toast.LENGTH_LONG).show();
	}

	/*
		Invoked when a button in the size adjustment group is clicked
	*/
	public void Adjust_size( View v ) {
		Toast.makeText(this, "radio button callback driven for size", Toast.LENGTH_LONG).show();
	}
}
