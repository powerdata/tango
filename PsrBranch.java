package com.powerdata.pse.csvmemdb;

public abstract class PsrBranch extends PsrNamedObject
{
	private Float _r;
	private Float _x;
	protected String _node1;
	protected String _node2;
	private Float _nrmlim;
	private Float _stlim;
	private Float _emlim;
	public Float getR() {return _r;}
	public Float getX() {return _x;}
	public String getNode1() {return _node1;}
	public String getNode2() {return _node2;}
	public Float getNormalOperatingLimit() {return _nrmlim;}
	public Float getShortTermLimit() {return _stlim;}
	public Float getEmergencyLimit() {return _emlim;}
	@Override
	public void configure(RecordReader rr)
	{
		_r = processFloat(rr, "r");
		_x = processFloat(rr, "x");
		_node1 = rr.getProperty("node1");
		_node2 = rr.getProperty("node2");
		_nrmlim = processFloat(rr, "normaloperatinglimit");
		_stlim = processFloat(rr, "shorttermlimit");
		_emlim = processFloat(rr, "emergencylimit");
		super.configure(rr);
	}

	@Override
	protected StringBuilder reportString()
	{
		StringBuilder sb = super.reportString();
		sb.append(", r=");
		sb.append(_r);
		sb.append(", x=");
		sb.append(_x);
		sb.append(", node1=");
		sb.append(_node1);
		sb.append(", node2=");
		sb.append(_node2);
		sb.append(", normalOperatingLimit=");
		sb.append(_nrmlim);
		sb.append(", shortTermLimit=");
		sb.append(_stlim);
		sb.append(", emergencyLimit=");
		sb.append(_emlim);
		return sb;
	}
}
