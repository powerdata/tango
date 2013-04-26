package com.powerdata.pse.csvmemdb;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import com.powerdata.tools.utils.StringParse;

public class RecordReader
{
	protected String[] _cols;
	protected String[] _rec;
	HashMap<String,Integer> _colmap;
	protected int _counter = 0;
	
	public void prepCSV(BufferedReader rdr) throws IOException
	{

		/* read col names */
		String l = rdr.readLine().toLowerCase();
		StringParse sp = new StringParse(l, ",", false);
		_cols = sp.getTokens();
		int ncols = _cols.length;
		_colmap = new HashMap<String,Integer>(ncols);
		for (int i=0; i < ncols; i++)
		{
			_colmap.put(_cols[i], i);
		}
	}

	public boolean prepRec(BufferedReader rdr) throws IOException
	{
		String l = rdr.readLine();
		boolean rv = false;
		if (l != null)
		{
			_rec = new StringParse(l, ",", false).getTokens();
			rv = true;
		}
		++_counter;
		return rv;
	}
	
	public int getCount() {return _counter;}
	
	public String getProperty(String name)
	{
		Integer i = _colmap.get(name);
		String rv = null;
		if (i != null && _rec.length > i)
		{
			rv = _rec[i];
			if (rv.isEmpty()) rv = null;
		}
		return rv;
	}
}
