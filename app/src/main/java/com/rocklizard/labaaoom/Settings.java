package com.rocklizard.labaaoom;

/*
	Mnemonic:	Settings
	Abstract:	Manages the per student settings that are used for an evaulation.
	Date:		17 September 2017
	Author:		E. Scott Daniels edaniels7@gatech.edu  cs6460 Fall 2017
*/


public class Settings {
	public final int SMALL = 0;		// constants returned by get funcitons
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
		so it's background:bgvalue,style:stylevalue,size:szvalue, but we'll
		handle short strings using defaults for missing/unrecognised bits.
	*/
	public Settings( String s ) {
		String[] tokens;			// overall list of key/values
		String[] kv;				// single key/value pair
		int i;

		size = MED;					// defaults if missing something
		style = SANS;
		background = NORMAL;

		tokens = s.split( "," );
		for( i = 0; i < tokens.length; i++ ) {
			kv = tokens[i].split( ":", 2 );
			if( kv.length != 2 ) {
				continue;
			}

			switch( kv[0] ) {
				case "background":
					if( kv[1].equals( "inverted" ) ) {
						background = INVERTED;
					}
					break;

				case "size":
					if( kv[1].equals( "small" ) ) {
						size = SMALL;
					} else {
						if( kv[1].equals( "large" ) ) {
							size = LARGE;
						}
					}
					break;

				case "style":
					if( kv[1].equals( "serif" ) ) {
						style = SERIF;
					}
					break;
			}
		}
	}

	/*
		Test functions allow the user to test for setting values.
		The return value is one of our public constats.
	*/
	public int GetSize() {
		return size;
	}

	public int GetStyle() {
		return style;
	}

	public int GetBackground() {
		return background;
	}

	/*
		Is functions allow for simile boolean testing
		Example usage:  if( settings.IsStyle( settings.SANS ) ) ...
	*/
	public boolean IsStyle( int test_type ) {
		return style == test_type;
	}

	public boolean IsSize( int test_type ) {
		return size == test_type;
	}

	public boolean IsBackground( int test_type ) {
		return background == test_type;
	}


	/*
		Various setters allowing changes.
		Silent failure if out of range.
	*/
	public void SetBackground( int value ) {
		if( value < 0 || value > INVERTED ) {
			return;
		}

		background = value;
	}

	public void SetSize( int value ) {
		if( value < 0 || value > LARGE ) {
			return;
		}

		size = value;
	}

	public void SetStyle( int value ) {
		if( value < 0 || value > SERIF ) {
			return;
		}

		style = value;
	}

	/*
		Return a string that something like student can cache on its own without having to
		know internal bits, and can give to a constructor. String is a comma sep list of

		CAUTION: if order changes, the test function will report error.
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

		CAUTION: if order changes, the test function will report error.
	*/
	public String[] GenDcEntry( String key ) {
		String[] entry;

		entry = new String[4];
		entry[0] = "settings:" + key;
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
