package com.incsys.tango;

import java.io.IOException;

public class TangoBlock7
{
	protected ComplexList[] _y;

	public void init(int ngen)
	{
		++ngen;
		_y = new ComplexList[ngen];
		for(int i=0; i < ngen; ++i)
			_y[i] = new ComplexList(ngen);
	}
	
	public void load(int ngen, PsdDataReader rdr) throws IOException
	{
		for(int ig=0; ig < ngen; ++ig)
		{
			rdr.loadComplexList(_y[ig], 0, ngen);
		}
	}

	public ComplexList[] getYMatrix()
	{
		return _y;
	}
}
