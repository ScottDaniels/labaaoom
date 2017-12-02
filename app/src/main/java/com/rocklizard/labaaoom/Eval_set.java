
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
	Mnemonic:	Eval_set
	Abstract:	Manages  a set of evaluations.
	Date:		17 September 2017
	Author:		E. Scott Daniels edaniels7@gatech.edu  cs6460 Fall 2017
*/

package com.rocklizard.labaaoom;

public class Eval_set {
	private Evaluation[]	evals;		// each tracked eval
	private int idx;					// insert point

	public Eval_set( ) {
		evals = new Evaluation[512];		// assuming 1/week this is about 20 years worth :)
		idx = 0;
	}

	/*
		Convert a comma sep list of eval info into an entry and add it to the list.
		The csl string must be in the order that the evaluation expects:
			date, set-name, type-name, words per min
	*/
	public void Add_entry( String csl ) {
		if( idx >= evals.length ) {
				return;					// likely never to happen, so be silent
		}

		evals[idx++] = new Evaluation( csl );
	}

	/*
		Return an array of strings which are the entries in a datacache savable format.
	*/
	public String[] ToStrings( ) {
		String[] strs;
		int i;

		if( idx == 0 ) {
			return null;
		}

		strs = new String[idx];
		for( i = 0; i < idx; i++ ) {
			strs[i] = evals[i].ToString();
		}

		return strs;
	}

	/*
		Return the total number of entries.
	*/
	public int GetSize() {
		return idx;
	}

	/*
		Build an array of graphable (integer) data from the evaluations in
		this set.
	*/
	public int[] GetData( ) {
		int[] values;
		int i;

		if( idx > 0 ) {
			values = new int[idx];
		} else {
			values = new int[1];
			values[0] = 0;
			return values;
		}

		for( i = 0; i < idx; i++ ) {
			values[i] = (int) evals[i].GetWpm();
		}

		return values;
	}

	/*
		Return the evaluation with the most recent timestamp. While they should be in
		order, we don't trust the underlying dc not to change, so we search.
	*/
	public Evaluation GetLast() {
		long ts = 0;				// current largest timestamp
		long nts = 0;				// next time stamp examined
		int	li = -1;				// index of the last one
		int i;

		for( i = 0; i < idx; i++ ) {
			if( (nts = evals[i].GetTimestamp()) > ts ) {
				ts = nts;
				li = i;
			}
		}

		if( li >= 0 ) {
			return evals[li];
		}

		return null;
	}

	/*
		Return a 4-tuple:  min, max, average and number of instances.
 	*/
	public double[] GetMMAI( ) {
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

		for( i = 1; i < idx; i++ ) {
			if( rv[0] > evals[i].GetWpm() ) {
				rv[0] = evals[i].GetWpm();
			}
			if( rv[1] < evals[i].GetWpm() ) {
				rv[1] = evals[i].GetWpm();
			}

			rv[2] += evals[i].GetWpm();		// sum values during loop
			rv[3]++;
		}

		if( rv[3] > 0 ) {
			rv[2] /= rv[3];
		}
		return rv;
	}
}
