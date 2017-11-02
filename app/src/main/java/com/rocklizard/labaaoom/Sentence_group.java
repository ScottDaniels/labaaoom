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


import java.util.Arrays;

public class Sentence_group {
	private String[] data;
	private String name;
	private int	iidx;				// we allocate with more so we can insert new ones
	private int sel_idx;			// selection index for GetNext()
	private int sel_stop;			// selection stop point
	private boolean from_words;		// if we end up extending the list and saving we want to know to split it

	/*
		Build a group from a set of strings read from the datacache. There are two
		types of data cache files: sentence files and word files. The word files
		can be read into 'sentences' by this function as well. When reading word lists
		this function will collect 10 words per 'sentence' and save the sentences for
		presentation.

		CAUTION: this does OK witl mixed sentences and words, but if it is ever saved, a mixed list
				will be saved out as words and when pulled back in it could be different.
	*/
	public Sentence_group( String[] dc_entry ) {
		int i;
		int j;
		String tokens[];
		String	partial = "";	// partial sentence
		int wcount = 0;			// number of words stuffed into partial

		from_words = false;
		iidx = dc_entry.length;
		data = new String[iidx + 50];	// allow user to add 50 sentences before expansion is needed
		sel_idx = 0;
		sel_stop = 0;

		for( j = 0, i = 0; i < iidx; i++ ) {
			tokens = dc_entry[i].split( ":", 2 );
			switch( tokens[0] ) {
				case "name":
					name = tokens[1];
					break;

				case "sentence":
					data[j++] = tokens[1];
					break;

				case "word":
					from_words = true;
					partial += " " + tokens[1];
					wcount++;
					if( wcount > 10 ) {
						data[j++] = partial;
						partial = "";
						wcount = 0;
					}
			}
		}

		if( from_words ) {				// we need to trim the array since allocation was based on records, and we reduced that by 10x
			String tdata[];

			data[j] = partial;							// capture partial
			iidx = j +1;								// insertion point
			tdata = new String[iidx + 50];				// reasonable amount
			System.arraycopy( data, 0, tdata, 0, j );
			data = tdata;								// retain the smaller
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
		Given a group size, returns the number of sets that the group contains.
	*/
	public int GetSize( int set_size ) {
		if( set_size <= 0 ) {
			return 0;
		}

		return iidx / set_size;
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

		sel_idx = group_size * group;
		sel_stop = sel_idx + group_size;

		return true;
	}
}
