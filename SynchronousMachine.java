package com.powerdata.pse.csvmemdb;

public class SynchronousMachine extends PsrNamedObject
{
	protected String _node;
	protected String _genunit;
	protected Float _minkv;
	protected Float _maxkv;
	protected String _regnode;
	protected Float _r;
	protected Float _x;
	protected Float _inertia;
	protected String _cswitch;
	
	public String getNode() {return _node;}
	public String getGeneratingUnit() {return _genunit;}
	public Float getMinKV() {return _minkv;}
	public Float getMaxKV() {return _maxkv;}
	public String getRegulatedNode() {return _regnode;}
	public Float getR() {return _r;}
	public Float getX() {return _x;}
	public Float getInertia() {return _inertia;}
	public String getControllingSwitch() {return _cswitch;}
	@Override
	public void configure(RecordReader rr)
	{
		_node = rr.getProperty("node");
		_genunit = rr.getProperty("generatingunit");
		_minkv = processFloat(rr, "minkv");
		_maxkv = processFloat(rr, "maxkv");
		_regnode = rr.getProperty("regulatednode");
		_r = processFloat(rr, "r");
		_x = processFloat(rr, "x");
		_inertia = processFloat(rr, "inertia");
		_cswitch = rr.getProperty("controllingswitch");
		super.configure(rr);
	}
	@Override
	public String toString()
	{
		StringBuilder sb = super.reportString();
		sb.append(", node=");
		sb.append(_node);
		sb.append(", generatingUnit=");
		sb.append(_genunit);
		sb.append(", minKV=");
		sb.append(_minkv);
		sb.append(", maxKV=");
		sb.append(_maxkv);
		sb.append(", regulatedNode=");
		sb.append(_regnode);
		sb.append(", R=");
		sb.append(_r);
		sb.append(", X=");
		sb.append(_x);
		sb.append(", inertia=");
		sb.append(_inertia);
		sb.append(", controllingSwitch=");
		sb.append(_cswitch);
		return sb.toString();
	}
	
	
}
