package com.powerdata.pse.csvmemdb;

public class ReactiveCapabilityCurve extends PsrObject
{
	protected String _synchronousMachine;
	protected Float _mw;
	protected Float _minmvar;
	protected Float _maxmvar;
	
	public String getSynchronousMachine() {return _synchronousMachine;}
	public Float getMW() {return _mw;}
	public Float getMinMVAr() {return _minmvar;}
	public Float getMaxMVAr() {return _maxmvar;}
	@Override
	public void configure(RecordReader rr)
	{
		_synchronousMachine = rr.getProperty("synchronousmachine");
		_mw = processFloat(rr, "mw");
		_minmvar = processFloat(rr, "minmvar");
		_maxmvar = processFloat(rr, "maxmvar");
		super.configure(rr);
	}
	@Override
	public String toString()
	{
		StringBuilder sb = reportString();
		sb.append(", synchronousMachine=");
		sb.append(_synchronousMachine);
		sb.append(", minMVAr=");
		sb.append(_minmvar);
		sb.append(", maxMVAr=");
		sb.append(_maxmvar);
		return sb.toString();
	}
	
	
}
