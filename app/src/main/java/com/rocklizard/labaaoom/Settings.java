package com.rocklizard.labaaoom;

/*
	Mnemonic:	Settings
	Abstract:	Manages the per student settings that are used for an evaulation.
	Date:		14 October 2017
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
	private String rand_group;			// evaluation group of each type last used
	private String sent_group;

	/*
		Create with defaults:
			med size, normal bg, sans.
	*/
	public Settings( ) {
		size = MED;
		style = SANS;
		background = NORMAL;
		sent_group = "grade1";
		rand_group = "grade1";
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
		sent_group = "grade1";
		rand_group = "grade1";

		tokens = s.split( "," );
		System.out.printf( ">>>> building settings from a string: %s\n", s );
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

				case "rgroup":
					rand_group = kv[1];
					System.out.printf( ">>>> setting rgroup %s\n", rand_group );
					break;

				case "sgroup":
					sent_group = kv[1];
					System.out.printf( ">>>> setting sgroup %s\n", sent_group );
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
		Obvious getter functions
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

	public String GetRandGroup( ) {
		if( rand_group != null ) {
			return rand_group;
		}

		return "";
	}

	public String GetSentGroup( ) {
		return sent_group;
	}

	/*
		Is functions allow for simile boolean testing
		Example usage:  if( settings.IsStyle( settings.SANS ) ) ...
	*/
	public boolean IsStyle( int test_value ) {
		return style == test_value;
	}

	public boolean IsSize( int test_value ) {
		return size == test_value;
	}

	public boolean IsBackground( int test_value ) {
		return background == test_value;
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

		System.out.printf( ">>> setting size %d\n", value );
		size = value;
	}

	public void SetStyle( int value ) {
		if( value < 0 || value > SERIF ) {
			return;
		}

		style = value;
	}

	public void SetRandGroup( String group ) {
		if( group != null ) {
			rand_group = group;
		}
	}

	public void SetSentGroup( String group ) {
		if( group != null ) {
			sent_group = group;
			System.out.printf( ">>>> setting sgroup %s\n", sent_group );
		}
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
		rs = dcentry[1] + "," + dcentry[2] + "," + dcentry[3] + "," + dcentry[4] + "," + dcentry[5];
		System.out.printf( ">>>> settings to string: %s\n", rs );

		return rs;
	}

	/*
		This will return an array of strings that can be added to the datacache.

		CAUTION: if order changes, the test function will report error.
	*/
	public String[] GenDcEntry( String key ) {
		String[] entry;

		entry = new String[6];
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
				entry[3] = "size:large";
				break;
		}

		entry[4] = "sgroup:" + sent_group;
		entry[5] = "rgroup:" + rand_group;

		return entry;
	}
}
