package com.incsys.tango;

import java.io.IOException;

public class TangoBlock3 extends RealCommonBlock
{
	public TangoBlock3(int ngen)
	{
		super(16, ngen);
	}

	public void load(int ngen, PsdDataReader rdr) throws IOException
	{
		for(int ig=0; ig < ngen; ++ig)
		{
			rdr.loadArray(_block, 0, 8, ig);
			rdr.loadArray(_block, 8, 16, ig);
		}
	}

	public float[] ka() {return _block[0];}
	public float[] ke() {return _block[1];}
	public float[] kf() {return _block[2];}
	public float[] ta() {return _block[3];}
	public float[] te() {return _block[4];}
	public float[] tf() {return _block[5];}
	public float[] vrmin() {return _block[6];}
	public float[] vrmax() {return _block[7];}
	public float[] ac1() {return _block[8];}
	public float[] ac2() {return _block[9];}
}
