package com.powerdata.pse.csvmemdb;

public class Substation extends PsrNamedObject
{
	protected String _organization;
	protected String _controlarea;
	
	public String getOrganization() {return _organization;}
	public String getControlArea() {return _controlarea;}
	@Override
	public void configure(RecordReader rr)
	{
		_organization = rr.getProperty("organization");
		_controlarea = rr.getProperty("controlarea");
		super.configure(rr);
	}
	@Override
	public String toString()
	{
		StringBuilder sb = super.reportString();
		sb.append(", organization=");
		sb.append(_organization);
		sb.append(", controlarea=");
		sb.append(_controlarea);
		return sb.toString();
	}


}
