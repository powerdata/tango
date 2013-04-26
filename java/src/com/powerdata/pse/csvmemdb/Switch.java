package com.powerdata.pse.csvmemdb;

public class Switch extends PsrNamedObject
{
	protected String _node1;
	protected String _node2;
	protected String _switchtype;
	
	public String getNode1() {return _node1;}
	public String getNode2() {return _node2;}
	public String getSwitchType() {return _switchtype;}
	@Override
	public void configure(RecordReader rr)
	{
		_node1 = rr.getProperty("node1");
		_node2 = rr.getProperty("node2");
		_switchtype = rr.getProperty("switchtype");
		super.configure(rr);
	}
	@Override
	public String toString()
	{
		StringBuilder sb = reportString();
		sb.append(", node1=");
		sb.append(_node1);
		sb.append(", node2=");
		sb.append(_node2);
		sb.append(", switchType=");
		sb.append(_switchtype);
		return sb.toString();
	}
	
	
}
