package com.powerdata.pse.csvmemdb;

public class BasecaseLoad extends PsrObject
{
	protected Float _mw;
	protected Float _mvar;
	public Float getMW() {return _mw;}
	public Float getMVAr() {return _mvar;}

	@Override
	public void configure(RecordReader rr)
	{
		_mw = processFloat(rr, "mw");
		_mvar = processFloat(rr, "mvar");
		super.configure(rr);
	}
	@Override
	public String toString()
	{
		StringBuilder sb = reportString();
		sb.append(", MW=");
		sb.append(_mw);
		sb.append(", MVAr=");
		sb.append(_mvar);
		return sb.toString();
	}
	
	
}
