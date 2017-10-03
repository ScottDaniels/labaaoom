package com.rocklizard.labaaoom;

/*
	Mnemonic:	Evaluation
	Abstract:	Manages one evaluation
	Date:		17 September 2017
	Author:		E. Scott Daniels edaniels7@gatech.edu  cs6460 Fall 2017
*/


import java.text.SimpleDateFormat;
import java.util.Date;

public class Evaluation {
	//@Deprecated
	//private String	date;			// date that the evaluation occurred
	private long	timestamp;		// timestamp cooresponding to date
	private double	wpm;			// words per minute of the eval
	private String	eval_id;		// reading set id (e.g. K-1)
	private String	eval_type;		// norm/lc or maybe somthing else
	private boolean accepted;		// instructor has accepted the eval as valid

	/*
		Constructor from a new evaluation

		FUTURE:  fix this as we dont ever epect to save a formatted date
	*/
	public Evaluation( String date, String set, String type, double wpm ) {
		timestamp = new Date().getTime();
		//this.date = date;				// date will be used as is; timestamp for sorting
		this.wpm = wpm;
		eval_id = set;
		eval_type = type;
	}

	/*
		Constructor from a new evaluation, use current date/time as the date.
	*/
	public Evaluation( String id, String type, double wpm ) {

		long cur_ts;

		cur_ts = new Date().getTime();		// current timestamp

		//this.date = null;					// date will be formatted when needed
		this.wpm = wpm;
		eval_id = id;
		eval_type = type;
	}


	/*
		Constructor for a stirng read from the datacache (comma separated fields)
		<date>|missing,<eval-set>,<eval_type>,<wpm>,<timestamp>,<accepted>
	*/
	public Evaluation( String csl ) {
		String[] tokens;

		tokens = csl.split( "," );
		if(  tokens.length > 5 ) {			// old form had date at beginning
			//date = tokens[ 0 ];
			//if( date.equals( "missing" ) ) {
				//date = null;
			//}

			eval_id = tokens[ 1 ];
			eval_type = tokens[ 2 ];
			wpm = Double.parseDouble( tokens[ 3 ] );
			timestamp = Long.parseLong( tokens[ 4 ] );
			if( tokens.length >= 6 )  {
				accepted = tokens[5].equals( "true" );
			} else {
				accepted = false;
			}
		} else {
			eval_id = tokens[ 0 ];
			eval_type = tokens[ 1 ];
			wpm = Double.parseDouble( tokens[ 2 ] );
			timestamp = Long.parseLong( tokens[ 3 ] );
			accepted = tokens[4].equals( "true" );
		}
	}

	/*
		Fetch up the evaluation's word per minute score.
	*/
	public double GetWpm( ) {
		return wpm;
	}

	/*
		Return the timestamp associated with the event
	*/
	public long GetTimestamp() {
		return timestamp;
	}

	/*
		Returns the event's date.  If the date string is not nil, then we return
		it, else we return a string generated from the timestamp.
	*/
	public String GetDate() {
		SimpleDateFormat sdf;

		sdf = new SimpleDateFormat( "MMM d yyyy" );
		return sdf.format( timestamp );
	}

	/*
		Return the score for this evaluation.
	*/
	public double GetWPM() {
		return wpm;
	}

	/*
		Build a comma separated string with our event info; order out must match
		the order expected from the string onstructor: date, set, type, wpm, accepted.
	*/
	public String ToString( ) {
		String s;

		s = eval_id + "," + eval_type +"," + Double.toString( wpm ) + "," + Long.toString( timestamp );
		s += accepted ? "true" : "false";

		return s;
	}
}
