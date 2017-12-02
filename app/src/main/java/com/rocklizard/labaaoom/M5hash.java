
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
	Mnemonic:	M5hash
	Abstract:	Functions that make generating an MD5sum easy.
	Date:		17 September 2017
	Author:		E. Scott Daniels edaniels7@gatech.edu  cs6460 Fall 2017
*/

package com.rocklizard.labaaoom;
import java.security.MessageDigest;

public class M5hash {

    /*
        Given a string and salt, generate an MD5 sum. On failure an empty string
        is returned.
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
            return "";
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
