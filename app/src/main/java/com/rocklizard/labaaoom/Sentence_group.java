package com.rocklizard.labaaoom;
/*
	Mnemonic:	Sentence_group
	Abstract:	Class manages a list of sentences.
				The list is thought of as n sets of m  sentences. A set may be selected
				starting at n from the 'top' and then a call to GetNext() will generate
				the next senence in the group until the group is exhausted.
	Date:		3 October 2017
	Author:		E. Scott Daniels edaniels7@gatech.edu  cs6460 Fall 2017
*/


public class Sentence_group {
	String[] data;
	String name;
	int	iidx;				// we allocate with more so we can insert new ones
	int sel_idx;			// selection index for GetNext()
	int sel_stop;			// selection stop point

	public Sentence_group( String[] dc_entry ) {
		int i;
		int j;
		String tokens[];

		iidx = dc_entry.length;
		data = new String[iidx + 50];
		sel_idx = 0;
		sel_stop = 0;

		for( j = 0, i = 0; i < iidx; i++ ) {
			tokens = dc_entry[i].split( ":", 2 );
			switch( tokens[0] ) {
				case "name":
					name = tokens[1];
					break;

				case "sentence":
					data[j] = tokens[1];
					break;
			}
		}
	}

	/*
		Add a new sentence to the group.
	*/
	public void AddSentence( String ns ) {
		if( iidx >= data.length ) {				// FUTURE:  extend the array
			return;
		}

		data[iidx++] = ns;
	}

	/*
		Convert this list to a set of data cache formmatted stirngs.
	*/
	public String[] GenDcEntry() {
		int i;
		int j;
		String dc_entry[];

		dc_entry = new String[iidx];

		dc_entry[0] = "name:" + name;
		for( j = 0, i = 0; i < iidx; i++ ) {
			if( data[j] != null ) {							// deleted entries could be null
				dc_entry[j++] = "sentence:" + data[ i ];
			}
		}

		return dc_entry;
	}

	/*
		Return the next string from the group selected or null if at the end of the
		group.
	*/
	public String GetNext( ) {
		if( sel_idx >= sel_stop ) {
			sel_idx = 0;
			sel_stop = 0; 		// prevent accidents
			return null;
		}

		return data[sel_idx++];
	}

	/*
		Set the current group iteration with a starting value and group size.
		Returns true unless the starting point/size cannot return size (we don't allow
		wrapping).
	*/
	public boolean Select( int group_size, int group ) {
		if( group_size + (group_size * group) >= iidx ) {
			System.out.printf( ">>>>>>>   group size not valid: %d\n", group_size + (group_size * group)  );
			return false;
		}

		System.out.printf( ">>>>>>>   group size valid: group=%d size=%d len=%d\n", group, group_size + (group_size * group), iidx  );
		sel_idx = group_size * group;
		sel_stop = sel_idx + group_size;

		System.out.printf( ">>>>>>>   return: staart=%d stop=%d\n", sel_idx, sel_stop );
		return true;
	}
}
