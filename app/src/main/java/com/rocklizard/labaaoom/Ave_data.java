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
	Mnemonic:	Ave_data.java
	Abstract:	Manages the average data for one specific section/list/etype. Provides
				the ability to stuff into the datacache, and be extracted from the datacache.
	Author:		E. Scott Daniels   edaniels7@gatech.edu for CS6460
	Date:		29 October 2017
*/
package com.rocklizard.labaaoom;


public class Ave_data {
	private double	sum;				// sum of the collected data
	private long	elements;			// number of elements contributing to the sum
	private String	id;					// the section_list_etype  id

	public Ave_data( String sect, String list, String etype ) {
		sum  = 0.0;
		elements = 0;
		id = String.format( "%s_%s_%s", sect, list, etype );
	}

	/*
		Build an instance from a datacache list of strings.
	*/
	public Ave_data( String[] dclist ) {
		String[] tokens;
		int i;

		sum  = 0.0;
		elements = 0;			// these can default
		id = "missing";

		if( dclist == null ) {
			return;
		}

		for( i = 0; i < dclist.length; i++ ) {
			System.out.printf( ">>>> adata parsing [%d] %s\n", i, dclist[i] );
			tokens = dclist[i].split( ":" );
			switch( tokens[0] ) {
				case "name":
					id = tokens[1];
					break;

				case "sum":
					sum = Double.parseDouble( tokens[1] );
					break;

				case "elements":
					elements = Long.parseLong( tokens[1] );
					break;
			}
		}
	}

	/*
		Allow an ID to be added after creation
	*/
	public void AddID( String sect, String list, String etype ) {
		id = String.format( "%s_%s_%s", sect, list, etype );
	}
	public void AddID( String id ) {
		this.id = id;
	}

	/*
		Adds a value to the set of data.
	*/
	public void AddValue( double val ) {
		sum += val;
		elements++;
	}

	/*
		Generate an entry that the datachace can stuff into its hole.  This is a
		list of strings with list[0] being the id by convention; the others may
		appear in any order.
	*/
	public String[] GenDcEntry( ) {

		String[] dclist;

		dclist = new String[3];
		dclist[0] = "name:" + id;
		dclist[1] = "sum:" + String.format( "%.6f", sum );
		dclist[2] = "elements:" + String.format( "%d", elements );

		return dclist;
	}

	/*
		Get the average.
	*/
	public Double GetAve( ) {
		System.out.printf( ">>>> getting average sum=%.2f ele=%d\n", sum, elements );
		if( elements > 0 ) {
			return sum / (double) elements;
		}

		return 0.0;
	}
}
