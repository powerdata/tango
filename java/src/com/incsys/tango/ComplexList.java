package com.incsys.tango;

public class ComplexList
{
	protected float[] _re;
	protected float[] _im;
	
	public ComplexList(int size)
	{
		_re = new float[size];
		_im = new float[size];
	}

	public Complex get(int i)
	{
		return new Complex(_re[i], _im[i]);
	}
	
	public float[] re() {return _re;}
	public float[] im() {return _im;}

	public void set(int i, Complex v)
	{
		_re[i] = v.re();
		_im[i] = v.im();
	}
	
	public float abs(int idx)
	{
		float re = _re[idx];
		float im = _im[idx];
		return (float) Math.sqrt(re*re+im*im);
	}
	
	public void add(int idx, Complex v)
	{
		_re[idx] += v.re();
		_im[idx] += v.im();
	}

	public void subtract(int idx, Complex v)
	{
		_re[idx] -= v.re();
		_im[idx] -= v.im();
	}
}
