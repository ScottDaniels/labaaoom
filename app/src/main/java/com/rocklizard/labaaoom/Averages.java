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
	Mnemonic:	Averages.java
	Abstract:	This is used to manage a set of averages which are tucked into the datacache
				and allow things like evaluation and history to easily push a new score into
				the average pool, and extract information from it.
	Author:		E. Scott Daniels   edaniels7@gatech.edu for CS6460
	Date:		29 October 2017
*/

package com.rocklizard.labaaoom;

import static com.rocklizard.labaaoom.Datacache.GetDatacache;

public class Averages {
	public final static String ET_RAND = Evaluation.ET_RANDOM;	// eval types for constant use
	public final static String ET_SENT = Evaluation.ET_SENTENCE;

	private Ave_data slt_aves;				// average data for the specific section/list/type
	private Ave_data all_aves;				// all for list-type

	/*
		build an average given the triple used to save the average in the datacache.
	*/
	public Averages( String sect, String list, String etype ) {
		Datacache dc;

		//System.out.printf( ">>> loading aves for %s %s %s\n", sect, list, etype );
		dc = GetDatacache();
		slt_aves = dc.ExtractAveData( sect, list, etype );
		if(  slt_aves == null ) {
			slt_aves = new Ave_data( sect, list, etype );		// not one in the dc; initislise it
		}

		all_aves = dc.ExtractAveData( "all", list, etype );
		if( all_aves == null ) {
			all_aves = new Ave_data( "all", list, etype );		// not one in the dc; initislise it
		}
	}

	/*
		Add a value to all average data sets managed. This automatically records the update
		in the datacache; there is no need to have a separate 'stash' call.
	*/
	public void AddVal( double val ) {
		Datacache dc;

		//System.out.printf( ">>> adding value %.2f\n", val );
		dc = GetDatacache();

		slt_aves.AddValue( val );
		dc.DepositAveData( slt_aves );

		all_aves.AddValue( val );
		dc.DepositAveData( all_aves );
	}

	/*
		Return the average for either all sections of listtype (all == true) or for the
		more specific section.
	*/
	public double GetAve( boolean all ) {
		if( all ) {
			return all_aves.GetAve();
		} else {
			return slt_aves.GetAve();
		}
	}
}
