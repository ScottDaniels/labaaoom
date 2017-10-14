package com.rocklizard.labaaoom;

/*
	Mnemonic:	Student
	Abstract:	class file that represents a student.
	Date:		17 September 2017
	Author:		E. Scott Daniels edaniels7@gatech.edu  cs6460 Fall 2017
*/

public class Student {
	static final boolean SENTENCES = true;
	static final boolean RANDOM = false;

	private String name;
	private String section;			// section that the kid belongs to (K1 First1 etc)
	private Eval_set sentence_evals;    // list of all evaluations using sentences
	private Eval_set rand_evals;        // list of all evaluations using random words
	private Settings settings;			// evaluation settings last used

	public Student( String name, String sect ) {
		this.name = name;
		this.section = sect;
		sentence_evals = new Eval_set( );
		rand_evals = new Eval_set( );
		settings = new Settings();			// get default settings
	}

	// ---- static methods ----------------
	/*
		Given a datacache entry set (array of strings) build a student object.
		The settings for a student are also cached in the same list, so as a part of this build
		we generate a settings object which is referenced by the student.
		Strings are of the type:
			name:<student name tokens>
			sect:<section name>
			sen:<sentence evaluation set>
			rand:<random word evaluation set>
	*/
	public Student( String[] dc_entry ) {
		int i;
		String[] tokens;        // split input data

		sentence_evals = new Eval_set( );
		rand_evals = new Eval_set( );

		if( dc_entry == null ) {			// shouldn't happen but parinoia isn't always bad
				add_name( "information missing" );
				settings = new Settings();
				return;
		}

		for( i = 0; i < dc_entry.length; i++ ) {

			tokens = dc_entry[ i ].split( ":", 2 );
			switch( tokens[ 0 ] ) {
				case "name":
					add_name( tokens[ 1 ] );
					break;

				case "sect":
					add_sect( tokens[1] );
					break;

				case "se":
					Add_evaluation( tokens[ 1 ], true );
					break;

				case "re":
					Add_evaluation( tokens[ 1 ], false );
					break;

				case "sets":			// pick up settings and build object
					settings = new Settings( tokens[1] );
					break;

				default:
					break;
			}
		}

		if( settings == null ) {
			settings = new Settings( ); 		// ensure there are defaults if not in the dc
		}
	}

	// ----------------------------------------------------------------
	/*
		Interal setters used to support reading from a datacache entry.
	*/
	private void add_name( String name ) {
		this.name = name;
	}

	private void add_sect( String sect ) {
		section = sect;
	}

	/*
		Add an evaluation to one of the lists. List is determined by the sentence bool
		flag; if true the sentence list is assumed, else the random word evaluation
		list is assumed.
	*/
	public void Add_evaluation( String csl, boolean sentence ) {
		if( sentence ) {
			sentence_evals.Add_entry( csl );            // add the entry
		} else {
			rand_evals.Add_entry( csl );
		}
	}

	/*
		Generate a datacache entry from this instance. The datacache entry is an array
		of strings with name, sentence  and random word evaluation information.
	*/
	public String[] GenDcEntry() {
		String[] strs;				// set of strings to return
		String[] evals;
		int i = 0;
		int j;

		if( sentence_evals != null ){
			i += sentence_evals.GetSize();
		}
		if( rand_evals != null ){
			i += rand_evals.GetSize();
		}

		strs = new String[i+1];
		i = 0;
		strs[i++] = "name:" + name;
		strs[i++] = "sect:" + section;
		strs[i++] = "sets:" + settings.ToString();

		if( sentence_evals != null ) {
			evals = sentence_evals.ToStrings();
			if( evals != null ) {
				for( j = 0; j < evals.length; j++ ) {
					strs[ i++ ] = "se:" + evals[ j ];
				}
			}
		}

		if( rand_evals != null ) {
			evals = rand_evals.ToStrings();
			if( evals != null ) {
				for( j = 0; j < evals.length; j++ ) {
					strs[ i++ ] = "re:" + evals[ j ];
				}
			}
		}

		return strs;
	}

	/*
		Generate a set of graph data for either the sentence (true) or random word evaluations.
	*/
	public int[] GetData( boolean sentence ) {

		if( sentence ) {
			return sentence_evals.GetData();
		}

		return rand_evals.GetData();
	}

	/*
		Find the last eval and return it or nil if no evals exist.
	*/
	public Evaluation GetLastEval() {
		Evaluation evs = null;		// last senence based evaluation
		Evaluation evr = null;		// last random word evaluation


		if( sentence_evals != null ){
			evs = sentence_evals.GetLast();
		}

		if( rand_evals != null ) {
			evr = rand_evals.GetLast();
		}

		if( evr == null ) {				// if either is nil, just return the other
			return evs;
		} else {
			if( evs == null ) {
				return evr;
			}
		}

		if( evr.GetTimestamp() > evs.GetTimestamp() ) {			// return the larger
			return evr;
		}

		return evs;
	}

	/*
		Return the stuent name.
	*/
	public String GetName() {
		return name;
	}

	/*
		Return the section that the student is in.
	*/
	public String GetSection() {
		return section;
	}

	/*
		Return the min/max/ave/instances counts for either the sentence based evaluations (true) 
		eval set or the random word (false) eval set.
	*/
	public double[] GetMMAI( boolean sentences ) {
		if( sentences ) {
			if( sentence_evals != null ) {
				return sentence_evals.GetMMAI();
			}

			return null;
		} else {
			if( rand_evals != null ) {
				return rand_evals.GetMMAI();
			}

			return null;
		}
	}
}
