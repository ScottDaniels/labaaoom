package com.rocklizard.labaaoom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Student_info extends AppCompatActivity {

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

		super.onResume();

		dc = Datacache.GetDatacache();
		it = getIntent();
		target_name = it.getExtras( ).getString( "student_name" );		// student name from the caller

		student = (TextView) findViewById( R.id.student_name );
		student.setText( target_name );

		s = dc.ExtractStudent( target_name );
		last_e = s.GetLastEval();

		section = (TextView) findViewById( R.id.section_name );
		section.setText( s.GetSection() );

		last_ev_score = (TextView) findViewById( R.id.last_eval_score );
		ave_score = (TextView) findViewById( R.id.ave_score );
		last_ev_date = (TextView) findViewById( R.id.last_eval_date );

		if( last_e != null ) {
			mmai = s.GetMMAI( true );
			last_ev_date.setText( last_e.GetDate() );
			ave_score.setText(  Double.toString( mmai[2] ) );
			last_ev_score.setText(  Double.toString( last_e.GetWPM() ) );
		} else {
			last_ev_date.setText( "no evalusations" );
			ave_score.setText(  "0 wpm" );
			last_ev_score.setText(  "0 wpm" );
		}
	}
}
