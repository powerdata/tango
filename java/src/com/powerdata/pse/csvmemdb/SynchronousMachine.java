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
	protected String _cswitch;
	protected Float _inertia;
	protected Float _damping;
	protected Float _statorResistance;
	protected Float _statorLeakageReactance;
	protected Float _directSyncReactance;
	protected Float _quadSyncReactance;
	protected Float _directTransientReactance;
	protected Float _quadTransientReactance;
	protected Float _directTransientRotorTC;
	protected Float _quadTransientRotorTC;
	protected Float _c1;
	protected Float _c2;
	
	
	public String getNode() {return _node;}
	public String getGeneratingUnit() {return _genunit;}
	public Float getMinKV() {return _minkv;}
	public Float getMaxKV() {return _maxkv;}
	public String getRegulatedNode() {return _regnode;}
	public Float getR() {return _r;}
	public Float getX() {return _x;}
	public Float getInertia() {return _inertia;}
	public String getControllingSwitch() {return _cswitch;}
	public Float getDamping() {return _damping;}
	public Float getStatorResistance() {return _statorResistance;}
	public Float getStatorLeakageReactance() {return _statorLeakageReactance;}
	public Float getDirectSyncReactance() {return _directSyncReactance;}
	public Float getQuadSyncReactance() {return _quadSyncReactance;}
	public Float getDirectTransientReactance() {return _directTransientReactance;}
	public Float getQuadTransientReactance() {return _quadTransientReactance;}
	public Float getDirectTransientRotorTC() {return _directTransientRotorTC;}
	public Float getQuadTransientRotorTC() {return _quadTransientRotorTC;}
	public Float getC1() {return _c1;}
	public Float getC2() {return _c2;}

	
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
		_damping = processFloat(rr, "damping");
		_statorResistance = processFloat(rr, "statorresistance");
		_statorLeakageReactance = processFloat(rr,"statorleakagereactance");
		_directSyncReactance = processFloat(rr, "directsyncreactance");
		_quadSyncReactance = processFloat(rr, "quadsyncreactance");
		_directTransientReactance = processFloat(rr, "directtransientreactance");
		_quadTransientReactance = processFloat(rr, "quadtransientreactance");
		_directTransientRotorTC = processFloat(rr, "directtransientrotortc");
		_quadTransientRotorTC = processFloat(rr, "quadtransientrotortc");
		_c1 = processFloat(rr,"c1");
		_c2 = processFloat(rr,"c2");
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
		sb.append(", damping=");
		sb.append(_damping);
		sb.append(", statorResistance=");
		sb.append(_statorResistance);
		sb.append(", statorLeakageReactance=");
		sb.append(_statorLeakageReactance);
		sb.append(", directSyncReactance=");
		sb.append(_directSyncReactance);
		sb.append(", quadSyncReactance=");
		sb.append(_quadSyncReactance);
		sb.append(", directTransientReactance=");
		sb.append(_directTransientReactance);
		sb.append(", quadTransientReactance=");
		sb.append(_quadTransientReactance);
		sb.append(", directTransientRotorTC=");
		sb.append(_directTransientRotorTC);
		sb.append(", quadTransientRotorTC=");
		sb.append(_quadTransientRotorTC);
		sb.append(", c1=");
		sb.append(_c1);
		sb.append(", c2=");
		sb.append(_c2);
		sb.append(", controllingSwitch=");
		sb.append(_cswitch);
		return sb.toString();
	}
	
	
}
