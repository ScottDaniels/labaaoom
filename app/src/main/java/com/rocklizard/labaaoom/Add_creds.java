/*
	Mnemonic:	Add_creds.java
	Abstract:	This class allows for the addition and deletion of instructor credentials.
				Currently LaBaaooM is not 'muli user' in the sense that all of the data maintained
				by the application is available to anybody who can log in, but we do support
				multiple instructor's such that each may have their own Id and password combination.

	Author:		E. Scott Daniels   edaniels7@gatech.edu for CS6460
	Date:		1 November 2017
*/

package com.rocklizard.labaaoom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.rocklizard.labaaoom.M5hash.Mk_md5;

public class Add_creds extends AppCompatActivity {

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_add_creds );
	}

	/*
		Called when add button is touched.  We verify that the user doesn't already exist, and
		that the password strings enter match.  If new, and match, then we compute the MD5 hash
		that is stored in the password file and update it in the datacache.
	*/
	public void Add_cb( View v) {
		EditText name_thing;			// view object things; new uer name
		EditText passwd_thing;			// passwd
		EditText vpasswd_thing;			// passwd verification
		String	passwd;					// strings snarfed from objects
		String 	vpasswd;
		String name;
		String result;					// the md5 string to stuff in the datacace
		Datacache dc;

		name_thing = (EditText) findViewById( R.id.add_user_name );
		passwd_thing = (EditText) findViewById( R.id.add_passwd );
		vpasswd_thing = (EditText) findViewById( R.id.add_vpasswd );
		passwd = passwd_thing.getText().toString();
		vpasswd = vpasswd_thing.getText().toString();
		name = name_thing.getText().toString();

		passwd_thing.setText( "" );										// safe to clear
		vpasswd_thing.setText( "" );

		if( passwd.equals( vpasswd ) ) {							// they match
			result = Mk_md5(  name_thing.getText().toString(), passwd );

			dc = Datacache.GetDatacache();
			if( dc != null ) {
				if( dc.AddInstructor( name, result ) ) {            // add if not there
					Toast.makeText( this, name + " added", Toast.LENGTH_SHORT ).show( );
				} else {
					Toast.makeText( this, "User already defined; delete first.", Toast.LENGTH_LONG ).show( );
				}
			} else {
				Toast.makeText( this, "Internal error, no datacache", Toast.LENGTH_LONG ).show( );
			}

			finish();
		} else {
			Toast.makeText( this, "Passwords do not match", Toast.LENGTH_LONG ).show( );
		}
	}

	/*
		Deletee the instructor name passed in provided they ack the pop-up.
	*/
	public void Delete_cb( View v) {
		EditText name_thing;			// view object things; new uer name
		EditText passwd_thing;			// passwd
		EditText vpasswd_thing;			// passwd verification
		String	passwd;					// strings snarfed from objects
		String name;
		String result;					// the md5 string to stuff in the datacace
		Datacache dc;

		name_thing = (EditText) findViewById( R.id.add_user_name );
		passwd_thing = (EditText) findViewById( R.id.add_passwd );
		vpasswd_thing = (EditText) findViewById( R.id.add_vpasswd );
		passwd = passwd_thing.getText().toString();
		name = name_thing.getText().toString();

		passwd_thing.setText( "" );										// safe to clear
		vpasswd_thing.setText( "" );

		dc = Datacache.GetDatacache();
		if( dc != null ) {
			// future -- generate pop-up to confirm
			result = Mk_md5(  name_thing.getText().toString(), passwd );
			if( dc.DelInstructor( name, result ) ) {            			// delete if password matches what was there
				Toast.makeText( this, name + " deleted", Toast.LENGTH_SHORT ).show( );
			} else {
				Toast.makeText( this, "Badd user or password: not deleted.", Toast.LENGTH_LONG ).show( );
				return;
			}
		} else {
			Toast.makeText( this, "Internal error, no datacache", Toast.LENGTH_LONG ).show( );
		}

		finish();
	}

	/*
		Called when cancel button is touched. Clear all fields, and finish.
	*/
	public void Cancel_cb( View v) {
		EditText name_thing;
		EditText passwd_thing;
		EditText vpasswd_thing;

		name_thing = (EditText) findViewById( R.id.add_user_name );
		passwd_thing = (EditText) findViewById( R.id.add_passwd );
		vpasswd_thing = (EditText) findViewById( R.id.add_vpasswd );

		passwd_thing.setText( "" );										// safe to clear
		vpasswd_thing.setText( "" );
		name_thing.setText( "" );

		finish();
	}
}
