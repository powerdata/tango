package com.powerdata.pse.csvmemdb;

public class PsrNamedObject extends PsrObject
{
	protected String _name = null;
	public String getName() {return _name;}
	@Override
	public void configure(RecordReader rr)
	{
		_name = rr.getProperty("name");
		super.configure(rr);
	}
	@Override
	protected StringBuilder reportString()
	{
		StringBuilder sb = super.reportString();
		sb.append(", name=");
		sb.append(_name);
		return sb;
	}

	
}
