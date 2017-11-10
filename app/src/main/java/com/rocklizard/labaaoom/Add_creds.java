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

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.rocklizard.labaaoom.M5hash.Mk_md5;
import static com.rocklizard.labaaoom.R.style.AppTheme;

public class Add_creds extends Force_login_activity {

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

		if( name.equals( "" ) || passwd.equals( "" ) ) {
			//Toast.makeText( this, "Instructor and password must not be empty", Toast.LENGTH_LONG ).show( );
			Tools.PopError( this, "Instructor and password must not be empty" );
			return;
		}

		passwd_thing.setText( "" );										// safe to clear
		vpasswd_thing.setText( "" );

		if( passwd.equals( vpasswd ) ) {							// they match
			result = Mk_md5(  name_thing.getText().toString(), passwd );

			dc = Datacache.GetDatacache();
			if( dc != null ) {
				if( dc.AddInstructor( name, result ) ) {            // add if not there
					//Toast.makeText( this, name + " added", Toast.LENGTH_SHORT ).show( );
					Tools.PopOk( this, name + " added" );
				} else {
					//Toast.makeText( this, "User already defined; delete first.", Toast.LENGTH_LONG ).show( );
					Tools.PopError( this, "User already defined; delete first." );
				}
			} else {
				//Toast.makeText( this, "Internal error, no datacache", Toast.LENGTH_LONG ).show( );
				Tools.PopError( this, "Internal error, no datacache" );
			}

			finish();
		} else {
			//Toast.makeText( this, "Passwords do not match", Toast.LENGTH_LONG ).show( );
			Tools.PopError( this, "Passwords do not match" );
		}
	}

	/*
		Toss up a specialised dialogue box and if the response is 'ok', then delete the instructor.
	*/
	private void delete_on_ok( final String instructor, final String md5 ) {
		AlertDialog notice;

		notice =new AlertDialog.Builder( this, R.style.PopUp ).create();
			notice.setTitle( "Delete instructor: " + instructor );
			notice.setMessage( "Press 'Delete!' to confirm." );
   			notice.setCancelable( false );
			notice.setButton( Dialog.BUTTON_POSITIVE, "Delete!", new DialogInterface.OnClickListener() {
					public void onClick (DialogInterface dialog,int which){
						int i;
						Datacache dc;

						dc = Datacache.GetDatacache();
						dc.DelInstructor( instructor, md5 );

						finish( );
					}
				}
			);

			notice.setButton( Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
					public void onClick ( DialogInterface dialog, int which){
					}
				}
			);

			notice.show();
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
			result = Mk_md5(  name_thing.getText().toString(), passwd );
			if( dc.ValidateInstructor( name_thing.getText().toString(), result ) ) {		// if password was right
				delete_on_ok( name_thing.getText().toString(), result );					// confirm with pop-up then delete
			} else {
				//Toast.makeText( this, "Bad user or password: not deleted.", Toast.LENGTH_LONG ).show( );
				Tools.PopError( this, "Bad user or password: not deleted." );
			}
		} else {
			//Toast.makeText( this, "Internal error, no datacache", Toast.LENGTH_LONG ).show( );
			Tools.PopError( this, "Internal error, no datacache" );
		}
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
