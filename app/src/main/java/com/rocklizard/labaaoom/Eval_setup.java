package com.rocklizard.labaaoom;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Eval_setup extends Activity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_eval_setup );
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
