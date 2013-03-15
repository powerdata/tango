package com.incsys.tango;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PsdDataReader
{
	protected static Pattern _WS = Pattern.compile("\\S+");
	BufferedReader _in;
	Matcher _m;
	
	
	public PsdDataReader(BufferedReader in) throws IOException
	{
		_in = in;
	}

	public boolean nextRec() throws IOException
	{
		String l = _in.readLine();
		if (l == null) return true;
		_m = _WS.matcher(l);
		return false;
	}
	
	public String getNextString() throws IOException
	{
		return _m.find() ? _m.group() : null;
	}
	
	public int getNextInt() throws IOException
	{
		return Integer.parseInt(getNextString());
	}
	
	public float getNextFloat() throws IOException
	{
		return Float.parseFloat(getNextString());
	}

	public void loadArray(float[][] block, int slofs, int sllen, int ofs) throws IOException
	{
		if (nextRec()) return;
		for (int i=0; i < sllen; ++i, ++slofs)
		{
			String s = getNextString();
			if (s == null || slofs >= block.length) return;
			block[slofs][ofs] = Float.parseFloat(s);
		}
	}
	
	public void loadComplexList(ComplexList l, int start, int length) throws IOException
	{
		if (nextRec()) return;
		float[] lre = l.re();
		float[] lim = l.im();
		for (int i=0; i < length; ++i, ++start)
		{
			String sre = getNextString();
			if (sre == null) return;
			String sim = getNextString();
			lre[start] = Float.parseFloat(sre);
			lim[start] = Float.parseFloat(sim);
		}
	}
}