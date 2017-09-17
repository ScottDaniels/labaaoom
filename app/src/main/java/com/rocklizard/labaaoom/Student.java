package com.rocklizard.labaaoom;

/*
	Mnemonic:	Student
	Abstract:	class file that represents a student.
	Date:		17 September 2017
	Author:		E. Scott Daniels edaniels7@gatech.edu  cs6460 Fall 2017
*/

public class Student {
	String name;
	Eval_set norm_evals;	// list of all normal evaluations that have been done
	Eval_set lc_evals;		// list of all low contrast evaluations that have been done

	Student( String name ) {
		this.name = name;
		norm_evals = null;
		lc_evals = null;
	}
}
