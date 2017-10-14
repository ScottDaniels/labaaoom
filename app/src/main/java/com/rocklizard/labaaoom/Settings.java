package com.rocklizard.labaaoom;

/*
	Mnemonic:	Settings
	Abstract:	Manages the per student settings that are used for an evaulation.
	Date:		17 September 2017
	Author:		E. Scott Daniels edaniels7@gatech.edu  cs6460 Fall 2017
*/


public class Settings {
	public final int SMALL = 0;
	public final int MED = 1;
	public final int LARGE = 2;

	public final int INVERTED = 0;
	public final int NORMAL = 1;

	public final int SANS = 0;
	public final int SERIF = 1;

	private int background;
	private int style;
	private int size;

	/*
		Create with defaults:
			med size, normal bg, sans.
	*/
	public Settings( ) {
		size = MED;
		style = SANS;
		background = NORMAL;
	}

	/*
		Build one from a string. Assumed string generated  by our function
		so it's bg, style, size, but we'll handle short strings using defaults
		for missing/unrecognised bits.
	*/
	public Settings( String s ) {
		String[] tokens;

		size = MED;					// defaults if missing something
		style = SANS;
		background = NORMAL;
		tokens = s.split( "," );
		switch( tokens.length ) {
			case 3:						// bg, style, size
				if( tokens[2].equals( "small" ) ) {
					size = SMALL;
				} else {
					if( tokens[1].equals( "large" ) ) {
						size = LARGE;
					}
				}
				// fall through

			case 2:
				if( tokens[1].equals( "serif" ) ) {
					style = SERIF;
				}
				// fall through

			case 1:
				if( tokens[0].equals( "inverted" ) ) {
					background = INVERTED;
				}
		}
	}

	/*
		Return a string that something like student can cache on its own without having to
		know internal bits, and can give to a constructor. String is a comma sep list of

	*/
	public String ToString( ) {
		String rs;
		String[] dcentry;

		dcentry = GenDcEntry( "foo" );			// generate a dummy entry, we'll just cat them
		rs = dcentry[1] + "," + dcentry[2] + "," + dcentry[3];

		return rs;
	}

	/*
		This will return an array of strings that can be added to the datacache.
	*/
	public String[] GenDcEntry( String key ) {
		String[] entry;

		entry = new String[4];
		entry[0] =  "settings:" + key;
		entry[1] = "background:" + (background == NORMAL ? "normal" : "inverted");
		entry[2] = "style:" + (style == SANS ? "sans" : "serif");
		switch( size ) {
			case SMALL:
				entry[3] = "size:small";
				break;

			case MED:
				entry[3] = "size:medium";
				break;

			case LARGE:
				entry[3] = "size:larage";
				break;
		}

		return entry;
	}
}
