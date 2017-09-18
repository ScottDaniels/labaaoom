package com.rocklizard.labaaoom;

/*
	Mnemonic:	Evaluation
	Abstract:	Manages one evaluation
	Date:		17 September 2017
	Author:		E. Scott Daniels edaniels7@gatech.edu  cs6460 Fall 2017
*/


public class Evaluation {
	private String	date;			// date that the evaluation occurred
	private double	wpm;			// words per minute of the eval
	private String	eval_set;		// reading set id (e.g. K-1)
	private String	eval_type;		// norm/lc or maybe somthing else

	/*
		Constructor from a new evaluation
	*/
	public Evaluation( String date, String set, String type, double wpm ) {
		this.date = date;
		this.wpm = wpm;
		eval_set = set;
		eval_type = type;
	}

	/*
		Constructor for a stirng read from the datacache (comma separated fields)
	*/
	public Evaluation( String csl ) {
		String[] tokens;

		tokens = csl.split( "," );
		if(  tokens.length >= 4 ) {
			date = tokens[ 0 ];
			eval_set = tokens[ 1 ];
			eval_type = tokens[ 2 ];
			wpm = Double.parseDouble( tokens[ 3 ] );
		}
	}

	/*
		Fetch up the evaluation's word per minute score.
	*/
	public double GetWpm( ) {
		return wpm;
	}

	/*
		Build a comma separated string with our event info; order out must match
		the order expected from the string onstructor: date, set, type, wpm.
	*/
	public String ToString( ) {
		String s;

		s = date + "," + eval_set + "," + eval_type +"," + Double.toString( wpm );
		return s;
	}
}
