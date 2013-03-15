package com.incsys.tango;

public class TangoBlock8
{
	protected float[] _tym;
	protected float[][] _var;
	protected int _nt;
	protected int _nvar;
	
	public TangoBlock8()
	{
		_tym = new float[200];
		_var = new float[200][6];
	}
	
	public float[] tym() {return _tym;}
	public float[][] var() {return _var;}
	public int nt() {return _nt;}
	public int nvar() {return _nvar;}
	public void setNt(int v) {_nt = v;}
	public void setNvar(int v) {_nvar = v;}
	
}
