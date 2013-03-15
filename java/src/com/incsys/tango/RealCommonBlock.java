package com.incsys.tango;

public class RealCommonBlock
{
	protected float[][] _block;
	protected int _nfld;

	public RealCommonBlock(int nfld, int size)
	{
		_block = (size == 0) ? null : new float[nfld][size];
		_nfld = nfld;
	}

	public float[][] block() {return _block;}
	public int getNumFields() {return _nfld;}
}
