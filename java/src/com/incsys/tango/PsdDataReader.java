/*
   Copyright 2013 Incremental Systems Corporation

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.incsys.tango;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Deprecated
class PsdDataReader
{
	protected static Pattern _WS = Pattern.compile("\\S+");
	LineNumberReader _in;
	int lno;
	Matcher _m;
	String _l;
	
	public PsdDataReader(BufferedReader in) throws IOException
	{
		_in = new LineNumberReader(in);
	}

	public boolean nextLine() throws IOException
	{
		_l = _in.readLine();
		lno = _in.getLineNumber();
		if (_l == null) return true;
		_m = null;
		return false;
	}
	
	public String getNextString() throws IOException
	{
		if (_m==null) _m = _WS.matcher(_l);
		return _m.find() ? _m.group() : null;
	}
	
	public int getNextInt() throws IOException
	{
		return Integer.parseInt(getNextString().trim());
	}
	
	public float getNextFloat() throws IOException
	{
		return Float.parseFloat(getNextString().trim());
	}

	public void readArrays(int i, int maxLineVals, int narray, float[][] arrays)
		throws IOException
	{
		int nlines = (int) Math.ceil((float) narray / (float)maxLineVals);
		int ia=0;
		for (int il = 0; il < nlines; ++il)
		{
			if (!nextLine())
			{
				for (;ia < narray; ++ia)
				{
					String s = getNextString();
					if (s == null) break;
					if (arrays != null)
						arrays[ia][i] = Float.parseFloat(s);
				}
			}
		}
	}
	
	public void readArrayBlock(int i, int maxLineVals, int narray,
		float[]... arrays) throws IOException
	{
		readArrays(i, maxLineVals, narray, arrays);
	}
	
	public String readChars(int i)
	{
		int start = (_m == null) ? 0 : _m.end();
		String rv = _l.substring(start, i).trim();
		_l = _l.substring(start+i);
		_m = null;
		return rv;
	}
}