package com.rocklizard.labaaoom;

/*
	Mnemonic:  Force_login_activity.java
	Abstract:	This class extends activity and should be extended by all
				activities in the application except the main activity. The
				class provides a wrapper which forces the login screen if
				we believe the return to an activity is from 'outside' (meaning
				that the user migrated to some other activity and has returend).

	Author:		E. Scott Daniels
	Date:		22 October 2017
*/

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Force_login_activity extends Activity {
	private boolean spawned_internal;			// set to true if we spawn one of ours

	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		spawned_internal = true;						// prvent 'hit' as we travel down the entry path
	}

	@Override
	protected void onResume( ) {
		Intent target;

		super.onResume( );

		if( ! spawned_internal ) {
			//Toast.makeText( this, "### WARNING ###  return from outside detected.", Toast.LENGTH_LONG ).show( );

			target = new Intent( getApplicationContext( ), Login.class );
			spawned_internal = true;				// must set so when this runs after login is finished we pass through
			startActivity( target );
		} else {
			spawned_internal = false;
		}

	}

	/*
		This should be used by all of our activites which kick off an internal activity.
		It ensures that if we return to this activity from an internal activity we
		process normally, but if the user navigated away from here, we drive the login
		to revalidate the user credentials.
	*/
	public void Fork_internal( Intent it ) {
		spawned_internal = true;

		startActivity( it );
	}
}
