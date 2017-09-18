package com.rocklizard.labaaoom;

/*
	Mnemonic:	M5hash
	Abstract:	Functions that make generating an MD5sum easy.
	Date:		17 September 2017
	Author:		E. Scott Daniels edaniels7@gatech.edu  cs6460 Fall 2017
*/

import java.security.MessageDigest;

public class M5hash {

    /*
        Given a string and salt, generate an MD5 sum.
    */
    public static String Mk_md5( String src, String salt ) {
        byte[] bsrc;                        // full src in bytes
        byte[] md5;                         // output from digesting
        MessageDigest md;
        StringBuilder md5sb;                // output string
        int i;

        try {
            md = MessageDigest.getInstance( "MD5" );
        } catch( Exception e ) {
            return null;
        }

        bsrc = (src + salt).getBytes();
        md.reset();                             // parinoid, probably not needed
        md.update( bsrc );                      // process bytes
        md5 = md.digest();                      // finalise the crunching

        md5sb =  new StringBuilder( md5.length );
        for( i = 0; i < md5.length; i++ ) {
            if( (md5[i] & 0xff) < 0x10 ) {
                md5sb.append( '0' );
            }
            md5sb.append( Integer.toHexString(( md5[i] & 0xff ) ) );
        }

        //System.out.printf( "%s + %s = %s\n", src, salt, md5sb.toString() );
        return md5sb.toString();
    }

}
