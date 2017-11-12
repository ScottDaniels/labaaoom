
/*
	Mnemonic:	tools.java
	Abstract:	A set of static functions that might be fairly useful.
	Author:		E. Scott Daniels
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
