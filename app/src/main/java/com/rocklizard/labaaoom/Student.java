package com.rocklizard.labaaoom;

/*
	Mnemonic:	Student
	Abstract:	class file that represents a student.
	Date:		17 September 2017
	Author:		E. Scott Daniels edaniels7@gatech.edu  cs6460 Fall 2017
*/

public class Student {
	private String name;
	private Eval_set norm_evals;	// list of all normal evaluations that have been done
	private Eval_set lc_evals;		// list of all low contrast evaluations that have been done

	public Student( String name ) {
		this.name = name;
		norm_evals = new Eval_set();
		lc_evals = new Eval_set();
	}

	/*
		Add an entry to one of the lists.
	*/
	private void add_name( String name ) {
		this.name = name;
	}

	/*
		Add an entry to one of the lists.
	*/
	private void add_entry( String csl, boolean norm ) {
		if( norm ) {
			norm_evals.Add_entry( csl );			// add the entry
		} else {
			lc_evals.Add_entry( csl );
		}
	}

	/*
		Given a datacache entry set (array of strings) build a student object.
		Strings are of the type:
			name:  <student name tokens>
			ne:	<normal evaluation set>
			le: <low contrast evaluation set>
	*/
	public Student FromDcEnt( String[] dc_entry ) {
		Student st;
		int i;
		String[] tokens;		// split input data

		st = new Student( "noname" );
		for( i = 0; i < dc_entry.length; i++ ) {

			tokens = dc_entry[i].split( ":", 2 );
			switch( tokens[0] ) {
				case "name":
					st.add_name( tokens[1] );
					break;

				case "ne":
					st.add_entry( tokens[1], true );
					break;

				case "le":
					st.add_entry( tokens[1], false );
					break;

				default:
					break;
			}
		}

		return st;
	}
}
