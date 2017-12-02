
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
	Mnemonic:  Force_login_activity.java
	Abstract:	This class extends activity and should be extended by all
				activities in the application except the main activity. The
				class provides a wrapper which forces the login screen if
				we believe the return to an activity is from 'outside' (meaning
				that the user migrated to some other activity and has returend).

	Author:		E. Scott Daniels	edaniels7@gatech for CS6460 Fall 2017
	Date:		22 October 2017
*/

package com.rocklizard.labaaoom;

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
			System.out.printf( ">>>> on resume ext= %s starting login class! \n", spawned_internal ? "true" : "false " );
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

	public void Force_external( ) {
		System.out.printf( ">>>>> set spawned == false\n" );
		spawned_internal = false;			// for the initial activity to force the login on create
	}
}
