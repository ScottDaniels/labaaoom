
package com.rocklizard.labaaoom;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/*
	Mnemonic:	Graph.java
    Abastract:	Implements a basic graph class (bar graph default). The graph is painted as a
				bitmap that can easily be displayed in a linear container as the 'background'.
	Author:		E. Scott Daniels
	Date:		24 September 2017

	Notes:		https://developer.android.com/reference/android/graphics/Canvas.html
*/

public class Graph {
    private int height;                 // graph dimensions
    private int width;
    private int cheight;                // canvas dimensions
    private int cwidth;
    private int[] values;
    private int vidx = 0;               // index into value for next insertion
    private boolean value_wrap = false; // true if values have wrapped
    private int nvalues;
    private int line_colour;
    private int grid_colour;
    private int max_value;              // max value in the data set
    private int min_topv = 100;         // minimum top value
    private int padx;                   // padding on left
    private int pady;                   // padding on top/bottom
    private int point_sp = 15;           // space between points

    private boolean gap = true;         // gap between bars

    private Paint painter;
    private Canvas canvas;
    private Bitmap bmap;

    // ---- internal helper stuff -------
    private void paint_grid( ) {
        int i;
        int yoffset;        // offset of next grid line
        int yspace;         // space between grid lines
        int delta;
        int tv;

        painter.setColor( grid_colour );
        canvas.drawLine(  padx,  cheight-(pady-1),  width+padx,  cheight-pady, painter );       // xaxis
        canvas.drawLine(  padx,  cheight-(pady-1),  padx,  pady, painter );                  // yaxis

        tv = max_value > min_topv ? max_value : min_topv;
        delta = tv / 4;

        yoffset = 0;
        yspace = height / 4;
        for( i = 0; i < 4; i++ ) {
            canvas.drawLine(  padx-5,  pady+yoffset,  width+padx+5,  pady+yoffset, painter );
            canvas.drawText(  Integer.toString( tv ), 0, pady+yoffset+4, painter );
            yoffset += yspace;

            tv -= delta;
        }
        canvas.drawText(  "0", 0, pady+yoffset+5, painter );
    }

    // -----------------------------------------------------------------------------

    /*
           Create a graph. Height and width are the size of the canvas and the
           x/y dimensions of the graph will be computed using the padx/y values.
    */
    public Graph( int width, int height, int padx, int pady ) {

        if( height < 10 ) {
            height = 100;
        }
        if( width <  10 ) {
            width = 200;
        }
        if( padx < 0 ) {
            padx = 10;
        }
        if( pady < 0 ) {
            pady = 10;
        }
        this.cheight = height;
        this.cwidth = width;
        this.padx = padx;
        this.pady = pady;

        this.height = cheight - (2 * pady);
        this.width = cwidth - padx;

        nvalues = this.width;
        values = new int[nvalues];
        line_colour = Color.parseColor( "#0090e0" );
        grid_colour = Color.parseColor( "#c0c0c0" );

        // set up painter things
        painter= new Paint();
        bmap = Bitmap.createBitmap( cwidth, cheight, Bitmap.Config.ARGB_8888 );
        canvas = new Canvas( bmap );
    }

    /*
        Allow user to set the space between points
    */
    public void Set_pt_space( int n ) {
        if( n < 1 ) {
            point_sp = 1;
        } else {
            if( n > width ) {
                point_sp = width;
            } else {
                point_sp = n;
            }
        }
    }

    /*
        Add a value to the dataset. We handle wrapping.
    */
    public void  Add_value( int v ) {
        int i;
        if( v >= max_value ) {                           // this beats current, just snarf it
            max_value = v;
        } else {
            if( value_wrap && values[vidx] == max_value ) {           // scan for new max if we are replacing the old one
                max_value = 0;
                for( i = 0; i < nvalues; i++ ) {
                   if( max_value < values[i] ) {
                        max_value = values[i];
                   }
                }
            }
        }

        values[vidx++] = v;
        if( vidx  >= nvalues ) {
            vidx = 0;
            value_wrap = true;
        }
    }

	/*
		Add an array of values to the graph.
	*/
	public void Add_values( int[] vals ) {
		int i;
		int sidx;			// source index

		if( vals.length >= values.length ) {		// this is a complete replacement, only process the last n as that is all that is needed
			max_value = 0;
			sidx = vals.length - values.length;		// starting location into vals for copy

			for( i = 0; i < values.length; i++ ) {
				if( max_value < vals[sidx] ) {
					max_value = vals[sidx];
				}
				values[i] = vals[sidx++];
			}

			vidx = 0;								// next insert will start to overwrite
		} else {									// partial replacement, easier to stuff in using add
			sidx = 0;
			for( i = 0; i < vals.length; i++ ) {
				Add_value( vals[sidx++] );
			}
		}
	}

    /*
        Draw the graph into a bitmap and return the bitmap to be rendered into a
        container as the background.
    */
    public Bitmap Paint( ) {
        int i;
        int idx;                // index into the data
        float scale;
        int n2paint;
        int rx;                 // value for right x of a point

        //painter.setColor( Color.parseColor( "#404040" ) );      // grey canvas for debugging
        painter.setColor( Color.parseColor( "#000000" ) );      // blank what was drawn before
        canvas.drawPaint( painter );

        if( min_topv > max_value ) {
            scale =  ((float) height) / min_topv;
        } else {
            scale =  ((float) height) / max_value;
        }


        paint_grid( );                      // add axis and grid lines

        painter.setColor( line_colour );
        n2paint = width /  point_sp;
        idx = vidx - n2paint;
        if ( idx < 0 ) {
            idx = value_wrap ? nvalues + idx : 0;
        }
        if( idx >= nvalues ) {
            idx = 0;
        }

        for( i = 0; i < n2paint; i++ ){
            rx = padx+(i*point_sp)+point_sp - (gap ? 1 : 0);
            if( values[idx] < 0 ) {
                painter.setColor( Color.parseColor( "#ff0000"  ) );
            }
            canvas.drawRect( padx+(i*point_sp), (cheight-pady) - (values[idx++] * scale), rx, cheight-pady, painter );
            if( idx >= nvalues ) {
                idx = 0;
            }
        }

        return bmap;
    }

    /*
        Reset the graph by clearing all of the data.
    */
    public void Reset( ) {
        values = new int[nvalues];
        vidx = 0;
        value_wrap = false;
    }

    /*
        Allow user to set the minimum top value.
    */
    public void Set_min_topv( int mt ) {
        if( mt > 0 ) {
            min_topv = mt;
        }
    }
 }
