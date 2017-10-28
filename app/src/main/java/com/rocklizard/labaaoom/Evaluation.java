package com.rocklizard.labaaoom;

/*
	Mnemonic:	Evaluation
	Abstract:	Manages one evaluation
	Date:		17 September 2017
	Author:		E. Scott Daniels edaniels7@gatech.edu  cs6460 Fall 2017

	Mods:		15 Oct 2017 - Add datacache stash/load functions.
*/


import java.text.SimpleDateFormat;
import java.util.Date;

public class Evaluation {
	private long	timestamp;		// timestamp cooresponding to date
	private double	wpm;			// words per minute of the eval
	private String	eval_id;		// reading set id (e.g. K-1)
	private String	eval_type;		// sentence/random etc.

	/*
		Constructor from a new evaluation, use current date/time as the date.
	*/
	public Evaluation( String id, String type, double wpm ) {

		long cur_ts;

		cur_ts = new Date().getTime();		// current timestamp

		this.wpm = wpm;
		eval_id = id;
		eval_type = type;
	}


	/*
		Constructor for a stirng read from the datacache (comma separated fields)
		<eval-set>,<eval_type>,<wpm>,<timestamp>
	*/
	public Evaluation( String csl ) {
		String[] tokens;

		tokens = csl.split( "," );
		if(  tokens.length > 5 ) {			// old form had date at beginning
			eval_id = tokens[ 1 ];
			eval_type = tokens[ 2 ];
			wpm = Double.parseDouble( tokens[ 3 ] );
			timestamp = Long.parseLong( tokens[ 4 ] );
		} else {
			eval_id = tokens[ 0 ];
			eval_type = tokens[ 1 ];
			wpm = Double.parseDouble( tokens[ 2 ] );
			timestamp = Long.parseLong( tokens[ 3 ] );
		}
	}

	/*
		Generate an evaluation instance based on a datacache entry of strings.
	*/
	public Evaluation( String[] entry ) {
		int i;
		String[] tokens;

		for( i = 0; i < entry.length; i++ ) {
			tokens = entry[i].split( ":" );
			switch( tokens[0] ) {
				case "timestamp":
					timestamp = Long.parseLong( tokens[1] );
					break;

				case "wpm":
					wpm = Double.parseDouble( tokens[1] );
					break;

				case "id":
					eval_id = tokens[1];
					break;

				case "type":
					eval_type = tokens[1];
					break;
			}
		}
	}

	/*
		Generate a datacache entry that is used when saving a pending evaluation.
		Returns an array of strings that will be put into the cache.
	*/
	public String[] GenDcEntry( ) {
		String[] entry;

		entry = new String[4];
		entry[0] = "timestamp:" + Long.toString( timestamp );
		entry[1] = "wpm:" + Double.toString( wpm );
		entry[2] = "id:" + eval_id;
		entry[3] = "type:" + eval_type;

		return entry;
	}

	/*
		Returns the event's date.  If the date string is not nil, then we return
		it, else we return a string generated from the timestamp.
	*/
	public String GetDate() {
		SimpleDateFormat sdf;

		sdf = new SimpleDateFormat( "MM dd yyyy" );
		return sdf.format( timestamp );
	}

	/*
		Returns the id of eval (e.g. grade1 )
	*/
	public String GetID( ) {
		return eval_id;
	}

	/*
		Return the timestamp associated with the event
	*/
	public long GetTimestamp() {
		return timestamp;
	}

	/*
		Returns the type of eval (e.g. random)
	*/
	public String GetType( ) {
		return eval_type;
	}

	/*
		Fetch up the evaluation's word per minute score.
	*/
	public double GetWpm( ) {
		return wpm;
	}

	/*
		Build a comma separated string with our event info; order out must match
		the order expected from the string constructor: set, type, wpm, timestamp
	*/
	public String ToString( ) {
		String s;

		s = eval_id + "," + eval_type +"," + Double.toString( wpm ) + "," + Long.toString( timestamp );

		return s;
	}

	/*
		Return a string representation in pretty printed format.
	*/
	public String PrettyPrint( ) {
		return String.format( "Eval id: %s\nEval type: %s\nWPM: %.2f\nDate: %s\n",  eval_id, eval_type, wpm, GetDate() );

	}
}
