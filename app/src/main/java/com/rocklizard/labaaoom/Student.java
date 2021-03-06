
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
	Mnemonic:	Student
	Abstract:	class file that represents a student.
	Date:		17 September 2017
	Author:		E. Scott Daniels edaniels7@gatech.edu  cs6460 Fall 2017
*/

package com.rocklizard.labaaoom;
public class Student {
	static final boolean SENTENCES = true;
	static final boolean RANDOM = false;

	private String name;
	private String section;				// section that the kid belongs to (K1 First1 etc)
	private Eval_set sentence_evals;    // list of all evaluations using sentences
	private Eval_set rand_evals;        // list of all evaluations using random words
	private Evaluation pending;			// a pending evaluation that hasn't been accepted
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
		settings = new Settings();

		if( dc_entry == null ) {			// shouldn't happen but parinoia isn't always bad
				add_name( "information missing" );
				return;
		}

		for( i = 0; i < dc_entry.length; i++ ) {
			System.out.printf( ">>>> student info: %s\n", dc_entry[i] );

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

				case "pending":
					if( tokens[1].equals( "null" ) ) {
						pending = null;
					} else {
						pending = new Evaluation( tokens[ 1 ] );
					}
					break;

				case "sets":			// deprecated
				case "setngs":			// pick up settings and build object
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

	// ---- private functions -----------------------------------------
	/*
		Stick an evaluation on one of the lists.
	*/
	private void shove_eval( Evaluation eval, String etype ) {
		switch( etype ) {
			case Evaluation.ET_SENTENCE:
				sentence_evals.Add_entry( eval.ToString() );            // add the entry
				break;

			case Evaluation.ET_RANDOM:
				rand_evals.Add_entry( eval.ToString() );
				break;
		}
	}

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
		Captures a pending evaluation and hangs on to it.
	*/
	public void AddPendingEval( Evaluation eval ) {
		pending = eval;
	}

	/*
		Accept the pending eval by moving it to the proper list.
	*/
	public void AcceptPendingEval( ) {
		if( pending == null ) {
			return;
		}

		shove_eval( pending, pending.GetType() );			// add to the correct list
		pending = null;
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

		strs = new String[i+4];
		i = 0;
		strs[i++] = "name:" + name;
		strs[i++] = "sect:" + section;
		strs[i++] = "setngs:" + settings.ToString();
		if( pending != null ) {
			strs[ i++ ] = "pending:" + pending.ToString( );
		} else {
			strs[ i++ ] = "pending:null";
		}

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

		/*
		for( i = 0; i < strs.length; i++ ) {
			System.out.printf( ">>> saving student [%d] %s\n", i, strs[i] );
		}
		*/

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
		Return the last eval of a specific type and return it or nil if no evals of that type  exist.
		Etype is one of the Evaluation.ET_ constants.
	*/
	public Evaluation GetLastEval( String etype ) {
		Evaluation ev = null;		// last evaluation for return

		switch( etype ) {
			case Evaluation.ET_SENTENCE:
				if( sentence_evals != null ){
					return sentence_evals.GetLast();
				}
				break;

			case Evaluation.ET_RANDOM:
				if( rand_evals != null ) {
					return rand_evals.GetLast();
				}
				break;
		}

		return null;
	}

	/*
		Return the stuent name.
	*/
	public String GetName() {
		return name;
	}

	/*
		Return the number of evals of the type indicated
	*/
	public int GetNEvals( Boolean gtype ) {
		if( gtype ) {
			return sentence_evals.GetSize();
		} else {
			return rand_evals.GetSize();
		}
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

	/*
		Return the pending evaluation.
	*/
	public Evaluation GetPendingEval( ) {
		return pending;
	}

	/*
		Return the current settings structucture.
	*/
	public Settings GetSettings( ) {
		if( settings == null ) {
			settings = new Settings();		// shouldn't be the case, but ide thinks it is possible
		}

		return settings;
	}

	/*
		Returns true if there is a pending eval.
	*/
	public Boolean HasPendingEval() {
		return pending != null;
	}

	/*
		Will delete the pending evaluation without saving it.
	*/
	public void RejectPendingEval() {
		pending = null;
	}

	/*
		Change the student name associated with this instance.
	*/
	public void Rename( String new_name ) {
		name = new_name;
	}

	/*
		Allow owner to reset the section name.
	*/
	public void SetSection( String new_sect ) {
		section = new_sect;
	}
}
