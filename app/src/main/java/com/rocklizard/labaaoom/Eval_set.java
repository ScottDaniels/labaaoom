package com.rocklizard.labaaoom;

/*
	Mnemonic:	Eval_set
	Abstract:	Manages  a set of evaluations.
	Date:		17 September 2017
	Author:		E. Scott Daniels edaniels7@gatech.edu  cs6460 Fall 2017
*/

public class Eval_set {
	private Evaluation[]	evals;		// each tracked eval
	private int idx;					// insert point

	public Eval_set( ) {
		evals = new Evaluation[1024];		// assuming 1/week this is about 40 years worth :)
		idx = 0;
	}

	/*
		Convert a comma sep list of eval info into an entry and add it to the list.
	*/
	public void Add_entry( String csl ) {
		if( idx >= evals.length ) {
				return;					// likely never to happen, so be silent
		}

		evals[idx++] = new Evaluation( csl );
	}

	/*
		Return a 4-tuple:  min, max, average and number of instances.
 	*/
	public double[] mmai( ) {
		double[] rv;
		int i;

		rv = new double[4];				// return vector (min, max, mean, i)
		if( idx < 1 ) {
			return rv;
		}

		rv[0] =  evals[0].GetWpm();
		rv[1] = evals[0].GetWpm();
		rv[2] = evals[0].GetWpm();
		rv[3] = 1;

		for( i = 0; i < idx; i++ ) {
			if( rv[0] > evals[i].GetWpm() ) {
				rv[0] = evals[i].GetWpm();
			}
			if( rv[1] < evals[i].GetWpm() ) {
				rv[1] = evals[i].GetWpm();
			}

			rv[3]++;
		}

		rv[2] /= idx;
		return rv;
	}
}
