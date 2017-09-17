package com.rocklizard.labaaoom;

/*
	Mnemonic:	Evaluation
	Abstract:	Manages one evaluation
	Date:		17 September 2017
	Author:		E. Scott Daniels edaniels7@gatech.edu  cs6460 Fall 2017
*/


public class Evaluation {
	String	date;			// date that the evaluation occurred
	double	wpm;			// words per minute of the eval
	String	eval_set;		// reading set id (e.g. K-1)
	String	eval_type;		// norm/lc or maybe somthing else

	/*
		Constructor from a new evaluation
	*/
	Public Evaluation( String date, double wpm, String set ) {
		this.date = date;
		this.wpm = wpm;
		eval_set = set;
		eval_type = type;
	}

	/*
		Constructor for a stirng read from the datacache (comma separated fields)
	*/
	public Evaluation( String csepfields ) {
		String[] tokens;

		tokens = csepfields.split( "," );
		if(  tokens.lengh < 4 ) {
			return null;
		}

		date = tokens[0];
		eval_set = tokens[1];
		eval_type = tokens[2];
		wpm = Double.parseDouble( tokens[3] );
	}

	public double GetWPM( ) {
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
