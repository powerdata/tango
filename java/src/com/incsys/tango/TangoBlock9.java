package com.incsys.tango;

public class TangoBlock9
{
	protected float[][] _prtvar;
	
	public TangoBlock9(int ngen)
	{
		_prtvar = new float[ngen][20];
	}
	
	public float[][] prtvar() {return _prtvar;}
}
