
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
	Mnemonic:	tools.java
	Abstract:	A set of static functions that might be fairly useful.
	Author:		E. Scott Daniels	edaniels7@gatech.edu CS6460 Fall 2017
	Date:		9 November 2019
*/

package com.rocklizard.labaaoom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Tools {

	/*
		Create and show a toast message, but with controled colours.
		FG and bg are rgb integers (e.g. 0xa000f0).
	*/
	public static void PopMessage( Context caller, String msg, boolean good_msg ) {
		View view;
		TextView text;
		Toast t;

		t = Toast.makeText( caller, "   " + msg + "    ", Toast.LENGTH_LONG );
		view = t.getView( );
		text = (TextView) view.findViewById( android.R.id.message );
		if( text != null ) {
			text.setTextSize( 18 );
			if( view != null ) {
				if( good_msg ) {
					view.setBackgroundResource( R.drawable.popup_good );
				} else{
					view.setBackgroundResource( R.drawable.popup_bad );
				}
			}
		}
		t.show( );
	}

	/*
		Predefined msg types with colours.
	*/
	public static void PopError( Context caller, String msg ) {
		PopMessage( caller, msg, false );
	}

	public static void PopOk( Context caller, String msg ) {
		PopMessage( caller, msg, true );
	}
}
