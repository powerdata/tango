package com.incsys.tango;

public class TangoBlock5 extends RealCommonBlock
{
	protected ComplexList _vt;
	protected ComplexList _ct;
	
	public TangoBlock5(int ngen)
	{
		super(2, ngen);
		_vt = new ComplexList(ngen);
		_ct = new ComplexList(ngen);
	}

	public float[] ef() {return _block[0];}
	public float[] pm() {return _block[1];}
	
	public ComplexList vt() {return _vt;}
	public ComplexList ct() {return _ct;}
	
}
