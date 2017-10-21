
/*
	Mnemonic:	Datacache.java
	Abstract:	The class that provides the datacache for LaBaaooM.
				The datacache is a singleton class and so it's not created by 
				a 'new' call, but a call to the Mk_dtacache() static function
				that returns the object creating it if needed.

	Date:		17 September 2017
	Author:		E. Scott Daniels edaniels7@gatech for CS6460 Fall 2017
*/

package com.rocklizard.labaaoom;
import android.content.Context;
import android.content.res.AssetManager;

//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import com.rocklizard.labaaoom.Student;

public class Datacache {
	public static final int STUDENT = 0;				// kinds for delete

	private	static Datacache db = null;			// singleton

	private HashMap<String,Boolean> student_map;
	private HashMap<String,Boolean> sections_map;		// allow for mutiple sections in the same grade level
	private HashMap<String,Boolean> sgroups_map;		// map of sentance groups
	private HashMap<String,Boolean> wgroups_map;		// random word groups
	private Context ctx;								// the application's context

	/*
		Constructor that only we can invoke. We must have the context to
		save application private files.
	*/
	private Datacache( Context ctx ) {
		student_map  = new HashMap<String,Boolean>();
		sections_map  = new HashMap<String,Boolean>();
		sgroups_map  = new HashMap<String,Boolean>();
		wgroups_map  = new HashMap<String,Boolean>();
		this.ctx = ctx;

		load_maps( );				// Populate maps based on what we have on disk
	}

	// ------------------ private things ---------------------------------------------------

	/*
		Accepts a prefix, and a name and builds a file name that has all 
		spaces replaced with underbars (_). Prefix should NOT contain a trailing
		underbar.
	*/
	private String build_fname( String prefix, String name ) {
		String fname;
		int i;
		String[] tokens;

		fname = prefix; 		// seed
		tokens = name.split( " " );
		for( i = 0; i < tokens.length; i++ ) {
			fname += "_" + tokens[i];
		}

		//System.out.printf( ">>> filename built (%s)\n", fname );
		return fname;
	}

	/*
		Accepts a name and builds a key string that has all spaces replaced with underbars (_).
	*/
	private String build_key( String name ) {
		String key;
		int i;
		String[] tokens;

		tokens = name.split( " " );
		key = tokens[0]; 							// seed first frist token
		for( i = 1; i < tokens.length; i++ ) {		// add remaining tokens
			key += "_" + tokens[i];
		}

		return key;
	}

	/*
		Accepts a key and returns a name (underbars replaced with spaces).
	*/
	private String key2fname( String key ) {
		String fname;
		int i;
		String[] tokens;

		tokens = key.split( "_" );
		fname = tokens[0]; 							// seed first frist token
		for( i = 1; i < tokens.length; i++ ) {		// add remaining tokens
			fname += " " + tokens[i];
		}

		return fname;
	}


	/*
		Add the student 'key' to the hash.  Name may contain spaces and will be normalised
		to remove them.
	*/
	private void add_student( String name ) {
		if( student_map.containsKey( name ) ) {
			return;
		}

		student_map.put( name, true );
	}

	/*
		Open up the directoy and look for all important, map related, file names.  Mark
		the corresponding map[key] to serve as our index when needed.

		For sentance and word group lists which are supplied as a default set with the application
		we must suss files from the assets/groups directory in addition to reading the datacache
		directory.  To the rest of the application, these appear as one large set.
	*/
	private void load_maps( ) {
		File[] files;
		String[] tokens;
		int i;
		String[] agroups;

		if( ctx != null ) {				// no ctx, probably testing
			files = ctx.getFilesDir().listFiles();
			System.out.printf( ">>>>> datacache: %d files in directory\n", files.length );

			for( i = 0; i < files.length; i++ ) {
				System.out.printf( ">>>>> datacache: loading %d %s\n", i, files[i].toString() );

				tokens = files[i].getName().split( "_", 2 );	// all interesting map files are xxx_something

				if( tokens.length > 1 ) {						// ignore if not interesting
					switch( tokens[0] ) {
						case "student":
							System.out.printf( ">>>>> datacache: adding student to map %d %s\n", i, tokens[1] );
							student_map.put( tokens[1], true );
							break;

						case "sect":
							sections_map.put( tokens[1], true );
							break;

						case "sgrp":							// these are user created groups
							sgroups_map.put( tokens[1], true );
							break;

						case "wgrp":
							wgroups_map.put( tokens[1], true );
							break;

						default:				// ignore the unrecognised/unimportant
							break;
					}
				}
			}
		} else {
			System.out.printf( ">>>> datacache: load maps fails, no context!\n" );
		}

		try {		// load the groups provided as an asset by the application
			agroups = ctx.getAssets().list( "groups" );

			System.err.printf( ">>>>> load maps, found %d groups\n", agroups.length );
			for( i = 0; i < agroups.length; i++ ) {
				tokens = agroups[i].split( "_" );
				switch( tokens[0] ) {
					case "sgroup":
						sgroups_map.put( tokens[1], true );
						break;

					case "wgroup":
						wgroups_map.put( tokens[1], true );
						break;
				}
			}
		} catch(  IOException e ) {
			System.out.printf( "unable to list assessts in groups\n" );
		}
	}

	/*
		Writes an entry to the datacache.  The entry is a series of strings which are 
		written as newline terminated records.  The prefix is something like "student_".
		Data may be in any order, however element 0 is assumed to be the 'key' which is 
		used to create the flie name:  e.g.  name: Fred Flintstone  would be converted to
		prefix_Fred_flintsone  as the filename.  Returns the  hash 'key' (student name
		with blanks removed.
	*/
	private String stash_in_dc( String prefix, String[] data ){
		FileOutputStream f;	// if we have ctx then this writes in a private space
		String[] tokens;
		String fname;						// file name
		String key;							// key to return (name with blanks gone)
		int i;

		if( data == null || ctx == null ) {
			System.out.printf( ">>>> datacaache: stash in dc ctx or data was nil\n" );
			return null;
		}

		if( data.length < 1 ) {				// must have at least the student name 
			System.out.printf( ">>>> datacaache: stash in dc length was < 1 \n" );
			return null;
		}

		tokens = data[0].split( ":", 2 );		// pull the key and prep the remaining string to convert to filename (remove blanks)
		if( tokens.length < 2 ) {
			System.out.printf( ">>>> datacaache: element 0 didn't have two tokens\n" );
			return null;
		}

		fname = build_fname( prefix, tokens[1] );		// replace spaces with underbars and add prefix
		key = build_key( tokens[1] );

		try {
			f =	 ctx.openFileOutput( fname, Context.MODE_PRIVATE );  // cant get context generated in test
		} catch(  IOException e ) {
			e.printStackTrace();
			return null;
		}

		System.out.printf( ">>>> datacaache: attempting to write entry\n" );
		try {
			Writer writer = new OutputStreamWriter(f);
			for( i = 0; i < data.length; i++ ) {
				if( data[i] != null ) {
					System.out.printf( ">>>> datacaache: writing: %s\n", data[i] );
					writer.write( data[i] + "\n" );
					writer.flush();
				}
			}
			System.out.printf( ">>>> datacaache: write %d items\n", i );

			writer.close(); 				// closes all sub objects too
		} catch( IOException e ) {
			System.out.printf( ">>>> datacaache: write failure trapped\n" );
			return null;
		}

		System.out.printf( ">>>> datacaache: write finishing normally\n" );
		return key;
	}

	/*
		Generic remove function; hides underlying storage type
	*/
	private void remove_from_dc( String fname ) {
		if( ! ctx.deleteFile( fname ) ) {
			System.out.printf( ">>>>> remove from dc failed: %s\n", fname );
		} else {
			System.out.printf( ">>>>> remove from dc OK: %s\n", fname );
		}
	}

	/*
		Open the named file and read all records (new line terminated) into an array
		and return the array. Nil is returned on error.
	*/
	private String[] read_from_dc( String fname ) {
		String[] data;			// date read from the file
		byte[] rbuf;			// read buffer
		byte[] tbuf;			// trimmed buffer; excluding unfilled characters
		String stuff;			// stuff read converted to string
		FileInputStream f;
		int i;
		int rlen;

		rbuf = new byte[4096];				// first cut; one read with a max of 4k

		try {
			f =	 ctx.openFileInput( fname );  // cant get context generated in test
		} catch(  IOException e ) {
			e.printStackTrace();
			return null;
		}

		try {
			if( (rlen = f.read( rbuf )) < 0 ) {
				f.close();
				System.out.printf( ">>>> read from dc fails for %s\n", fname );
				return null;
			}

			//System.out.printf( ">>>> read %d bytes from dc\n", rlen );
			tbuf = Arrays.copyOfRange( rbuf, 0, rlen );
			stuff = new String( tbuf );

			// bloody java split gives us a bad token when a properly terminated record exists; add a dummy bit to prevent junk
			stuff += "dummy:junk";
			data = stuff.split( "\n" );

			f.close();
		} catch( IOException e ) {
			return null;
		}

		//System.out.printf( ">>>> read from dc has %d things to work with\n", data.length );
		//for( i = 0; i < data.length; i++ ) {
			//System.out.printf( ">>>> read from dc returns [%d] %s\n", i, data[i] );
		//}
		return data;
	}

	/*
		Opens and reads all strings from an asset file. These are 'datacache' files
		which are bundled with the application (e.g. sentence and word groups).
	*/
	private String[] read_from_asset( String fname ) {
		String[] data;			// date read from the file
		byte[] rbuf;			// read buffer
		byte[] tbuf;			// trimmed buffer; excluding unfilled characters
		String stuff;			// stuff read converted to string
		InputStream f;
		int i;
		int rlen;

		rbuf = new byte[4096];				// first cut; one read with a max of 4k

		try {
			f =	 ctx.getAssets().open( fname  );  // cant get context generated in test
		} catch(  IOException e ) {
			e.printStackTrace();
			return null;
		}

		try {
			if( (rlen = f.read( rbuf )) < 0 ) {
				f.close();
				System.out.printf( ">>>> read from dc fails for %s\n", fname );
				return null;
			}

			//System.out.printf( ">>>> read %d bytes from dc\n", rlen );
			tbuf = Arrays.copyOfRange( rbuf, 0, rlen );
			stuff = new String( tbuf );

			// bloody java split gives us a bad token when a properly terminated record exists; add a dummy bit to prevent junk
			stuff += "dummy:junk";
			data = stuff.split( "\n" );

			f.close();
		} catch( IOException e ) {
			return null;
		}

		System.out.printf( ">>>> read from asset has %d things to work with\n", data.length );
		for( i = 0; i < data.length; i++ ) {
			System.out.printf( ">>>> read from asset returns [%d] %s\n", i, data[i] );
		}
		return data;
	}

	// ------ public things ------------------------------------

	/*
		User application will call this to get a copy of the datacache
		instance.  The instance is created on the first call.
	*/
	public static Datacache Mk_datacache( Context ctx ) {
		if( db == null ) {
			db = new Datacache( ctx );			// private constructor
		}

		return db;
	}

	/*
		Used to get the database (after creation) when contect isn't available.
	*/
	public static Datacache GetDatacache( ) {
		return db;
	}

	// ----------------- generic delete --------------------------------------------------------

	/*
		Generic delete of a specific kind of something.
	*/
	public void Delete( String name, int kind ) {
		String key;					// must convert name into a hash key (no spaces)

		key = build_key( name );
		switch( kind ) {
			case STUDENT:			// delete student
				if( student_map.containsKey( key ) ) {
					student_map.remove( key );
					remove_from_dc( build_fname( "student", key ) );
				}
		}
	}

	// ---------------- sentence group things --------------------------------------------------

	/*
		Writes the senence group to the datacache. Return value true means all
		was well, and false not so good.
	*/
	public boolean DepositSgroup( Sentence_group sg  ) {

		if( sg == null ) {
			return false;
		}

		return stash_in_dc( "sgrp", sg.GenDcEntry() ) == null;
	}

	/*
		Extracts the sentence named group and if all is true extracts the asset group
		with the same name and adds it to the group in the datacache making it appear
		that they are a single unit.
	*/
	public Sentence_group ExtractSgroup( String name, boolean all ) {
		String[] arecs = null;		// records from the asset
		String[] urecs;				// records from the user created list
		String[] recs;				// concatinated lists
		int rlen = 0;
		Sentence_group sg;

		if( ! sgroups_map.containsKey( name ) ) {
			return null;
		}

		if( all ) {
			if( (arecs = read_from_asset( "sgroup_" + name )) !=
				null ) {        // read the two sets of strings
				rlen = arecs.length;
			}
		}

		if( (urecs = read_from_dc( "sgroup_" + name  )) != null ) {
			rlen += urecs.length;
		}

		if( rlen <= 0 ) {				// both null, return null object
			return null;
		}

		recs = new String[rlen];		// cat together if we have both
		rlen = 0;
		if( arecs != null ) {
			System.arraycopy( arecs, 0, recs, 0, arecs.length );
			rlen = arecs.length;
		}

		if( urecs != null ) {
			System.arraycopy( urecs, 0, recs, rlen, urecs.length );
		}

		sg = new Sentence_group( recs );
		recs = read_from_asset( "sgroup_" + name  );

		return sg;
	}

	/*
		Returns an array of sentence group IDs. We pull this list from the map
		we loaded from the filesystem at start and assume that when new ones
		are added they are added to the map so we don't need to refresh.
	*/
	public String[] GetSgroupList( ) {
		String[] snames;
		Set<String> raw_keys;
		int i;

		raw_keys = sgroups_map.keySet();
		snames = raw_keys.toArray( new String[raw_keys.size()] );

		for( i = 0; i < snames.length; i++ ) {
			snames[i] = key2fname( snames[i] );
		}

		Arrays.sort( snames );
		return snames;
	}

	// ---------------- student things ---------------------------------------------------------
	/*
		Writes student information out. Return of true means things were
		good; false, not so much. (java really needs the concept of error
		return and multi-value return; 'throwing' an execption is just messy.
	*/
	public boolean DepositStudent( Student s ) {
		String sid;

		if( s == null ){
			System.out.printf( ">>>> datacache deposit student -- student was nil!\n" );
			return false;
		}
		sid = stash_in_dc( "student", s.GenDcEntry() );		// drop the info into the cache

		if( sid  == null ){
			System.out.printf( ">>>> datacache deposit student -- stash failed\n" );
			return false;
		}

		add_student( sid );					// add to our hash
		return true;
	}

	/*
		Returns true if we know about this student.
		Key passed in assuemd to be "name name".
	*/
	public boolean HasStudent(  String skey ) {
		if( skey == null ) {
			return false;
		}

		return student_map.containsKey( build_key( skey ) );
	}


	/*
		Given the student name (key) find the student data and build a student object
		from it.  Returns nil if not found.

		Name may contain spaces which will be replaced with underbars.
	*/
	public Student ExtractStudent( String name ) {
		String fname; 			// flie name to extract info from

		System.out.printf( ">>>> datacache is attempting to extract %s  in dc==%s \n", name, HasStudent( name ) ? "true" : "false" );
		if( name == null || !HasStudent( name )){
			return null;
		}

		fname = build_fname( "student", name );		// replace spaces with underbars and add prefix
		return new Student( read_from_dc( fname ) );
	}

	/*
		Returns an array of student IDs. We pull this list from the map
		we loaded from the filesystem at start and assume that when new ones
		are added they are added to the map so we don't need to refresh.
	*/
	public String[] GetStudentList( ) {
		String[] sids;
		Set<String> raw_keys;
		int i;

		raw_keys = student_map.keySet();
		sids = raw_keys.toArray( new String[raw_keys.size()] );

		for( i = 0; i < sids.length; i++ ) {
			sids[i] = key2fname( sids[i] );
		}

		Arrays.sort( sids );
		return sids;
	}

	/*
		This will return a list of the students in the named section.
		Right now we don't have an index of sections with a student list so
		we will have to look at each student.  Future development for
		performance needed to maintain an index of each section.
	*/
	public String[] GetStudentsInSection( String section ) {
		String[] all_sids;					// entire list
		String[] sids;						// pruned list;
		Set<String> raw_keys;
		Student s;
		int i;
		int j = 0;

		if( section.equals( "all" ) ) {
			return GetStudentList();
		}

		raw_keys = student_map.keySet();
		all_sids = raw_keys.toArray( new String[raw_keys.size()] );
		sids = new String[all_sids.length];

		for( i = 0; i < all_sids.length; i++ ) {
			s = ExtractStudent( all_sids[i] );					// pull in this student
			if( s.GetSection().equals( section ) ) {
				sids[j++] = key2fname( all_sids[i] );
			}
		}

		sids = Arrays.copyOfRange( sids, 0, j );				// slice the array down
		Arrays.sort( sids );

		return sids;
	}
}
