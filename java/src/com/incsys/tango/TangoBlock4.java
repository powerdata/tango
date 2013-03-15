package com.incsys.tango;

import java.io.IOException;

public class TangoBlock4 extends RealCommonBlock
{
	public TangoBlock4(int ngen)
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
}
