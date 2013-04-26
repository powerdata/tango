package com.powerdata.pse.csvmemdb;

public class SVC extends PsrOneTermDev
{
	protected Float _minmvar;
	protected Float _maxmvar;
	protected Float _slope;
	
	public Float getMinMVAr() {return _minmvar;}
	public Float getMaxMVAr() {return _maxmvar;}
	public Float getSlope() {return _slope;}
	
	@Override
	public void configure(RecordReader rr)
	{
		_minmvar = processFloat(rr, "minmvar");
		_maxmvar = processFloat(rr, "maxmvar");
		_slope = processFloat(rr, "slope");
		super.configure(rr);
	}
	@Override
	public String toString()
	{
		StringBuilder sb = reportString();
		sb.append(", minMVAr=");
		sb.append(_minmvar);
		sb.append(", maxMVAr=");
		sb.append(_maxmvar);
		sb.append(", slope=");
		sb.append(_slope);
		return sb.toString();
	}
	
	
}
