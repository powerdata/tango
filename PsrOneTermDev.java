package com.powerdata.pse.csvmemdb;

public class PsrOneTermDev extends PsrNamedObject
{
	private String _node;
	public String getNode() {return _node;}

	@Override
	public void configure(RecordReader rr)
	{
		_node = rr.getProperty("node");
		super.configure(rr);
	}

	@Override
	protected StringBuilder reportString()
	{
		StringBuilder sb = super.reportString();
		sb.append(", node=");
		sb.append(_node);
		return sb;
	}
}