package com.incsys.tango;

import java.io.IOException;

public class TangoBlock2 extends RealCommonBlock
{
	public TangoBlock2(int ngen)
	{
		super(13, ngen);
	}

	public void load(int ngen, PsdDataReader rdr) throws IOException
	{
		for(int ig=0; ig < ngen; ++ig)
		{
			rdr.loadArray(_block, 0, 8, ig);
			rdr.loadArray(_block, 8, 16, ig);
		}
	}

	public float[] pbase() {return _block[0];}
	public float[] h() {return _block[1];}
	public float[] r() {return _block[2];}
	public float[] xl() {return _block[3];}
	public float[] xd() {return _block[4];}
	public float[] xd1() {return _block[5];}
	public float[] xq() {return _block[6];}
	public float[] xq1() {return _block[7];}
	public float[] td1() {return _block[8];}
	public float[] tq1() {return _block[9];}
	public float[] damp() {return _block[10];}
	public float[] c1() {return _block[11];}
	public float[] c2() {return _block[12];}
	
}
