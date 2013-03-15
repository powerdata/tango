package com.incsys.tango;

public class TangoBlock6
{
	public final static int MaxPerGen = 16;
	protected float[][] _plug;
	protected float[][] _out;
	protected float[][] _save;
	
	public TangoBlock6(int ngen)
	{
		++ngen;
		_plug = new float[ngen][MaxPerGen];
		_out = new float[ngen][MaxPerGen];
		_save = new float[ngen][MaxPerGen];
	}
	
	public float[][] plug() {return _plug;}
	public float[][] out() {return _out;}
	public float[][] save() {return _save;}
}
