package com.powerdata.pse.csvmemdb;

public class Load extends PsrNamedObject
{
	protected String _node;
	
	public String getNode() {return _node;}

	@Override
	public void configure(RecordReader rr)
	{
		_node = rr.getProperty("node");
		super.configure(rr);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = super.reportString();
		sb.append(", node=");
		sb.append(_node);
		return sb.toString();
	}
	
}
