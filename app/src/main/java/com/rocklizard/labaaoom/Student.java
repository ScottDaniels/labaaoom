package com.rocklizard.labaaoom;

/*
	Mnemonic:	Student
	Abstract:	class file that represents a student.
	Date:		17 September 2017
	Author:		E. Scott Daniels edaniels7@gatech.edu  cs6460 Fall 2017
*/

public class Student {
	private String name;
	private String section;			// section that the kid belongs to (K1 First1 etc)
	private Eval_set norm_evals;    // list of all normal evaluations that have been done
	private Eval_set lc_evals;        // list of all low contrast evaluations that have been done

	public Student( String name, String sect ) {
		this.name = name;
		this.section = sect;
		norm_evals = new Eval_set( );
		lc_evals = new Eval_set( );
	}

	// ---- static methods ----------------
	/*
		Given a datacache entry set (array of strings) build a student object.
		Strings are of the type:
			name:<student name tokens>
			ne:<normal evaluation set>
			le:<low contrast evaluation set>
	*/
	//public static Student FromDcEntry( String[] dc_entry ) {
	public Student( String[] dc_entry ) {
		int i;
		String[] tokens;        // split input data

		norm_evals = new Eval_set( );
		lc_evals = new Eval_set( );

		for( i = 0; i < dc_entry.length; i++ ) {

			tokens = dc_entry[ i ].split( ":", 2 );
			switch( tokens[ 0 ] ) {
				case "name":
					add_name( tokens[ 1 ] );
					break;

				case "ne":
					Add_evaluation( tokens[ 1 ], true );
					break;

				case "le":
					Add_evaluation( tokens[ 1 ], false );
					break;

				default:
					break;
			}
		}
	}

	// ----------------------------------------------------------------
	/*
		Add an entry to one of the lists.
	*/
	private void add_name( String name ) {
		this.name = name;
	}

	/*
		Add an evaluation to one of the lists. List is determined by the norm bool
		flag; if true the normal list is assumed, else the low contrast list is assumed.
	*/
	public void Add_evaluation( String csl, boolean norm ) {
		if( norm ) {
			norm_evals.Add_entry( csl );            // add the entry
		} else {
			lc_evals.Add_entry( csl );
		}
	}

	/*
		Generate a datacache entry from this instance. The datacache entry is an array
		of strings with name, normal evaluation and low contrast evaluation information.
	*/
	public String[] GenDcEntry() {
		String[] strs;				// set of strings to return
		String[] evals;
		int i = 0;
		int j;

		if( norm_evals != null ){
			i += norm_evals.GetSize();
		}
		if( lc_evals != null ){
			i += lc_evals.GetSize();
		}

		strs = new String[i+1];
		strs[0] = "name:" + name;
		i = 1;
		if( norm_evals != null ) {
			evals = norm_evals.ToStrings();
			if( evals != null ) {
				for( j = 0; j < evals.length; j++ ) {
					strs[ i++ ] = "ne:" + evals[ j ];
				}
			}
		}
		if( lc_evals != null ) {
			evals = lc_evals.ToStrings();
			if( evals != null ) {
				for( j = 0; j < evals.length; j++ ) {
					strs[ i++ ] = "le:" + evals[ j ];
				}
			}
		}

		return strs;
	}

	/*
		Return the stuent name.
	*/
	public String GetName() {
		return name;
	}
}
