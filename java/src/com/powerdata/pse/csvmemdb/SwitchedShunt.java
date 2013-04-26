package com.powerdata.pse.csvmemdb;

public class SwitchedShunt extends PsrOneTermDev
{
	protected Float _mvar;
	protected Boolean _hasRegulator;
	protected String _regnode;
	protected Float _minkv;
	protected Float _maxkv;
	protected Integer _energizeDelay;
	protected Integer _deenergizeDelay;
	
	public Float getMVAr() {return _mvar;}
	public Boolean hasRegulator() {return _hasRegulator;}
	public String getRegulatedNode() {return _regnode;}
	public Float getMinKV() {return _minkv;}
	public Float getMaxKV() {return _maxkv;}
	public Integer getEnergizeDelay() {return _energizeDelay;}
	public Integer getDeenergizeDelay() {return _deenergizeDelay;}
	@Override
	public void configure(RecordReader rr)
	{
		_mvar = processFloat(rr, "mvar");
		_hasRegulator = processBoolean(rr, "hasregulator");
		_regnode = rr.getProperty("regulatednode");
		_minkv = processFloat(rr, "minkv");
		_maxkv = processFloat(rr, "maxkv");
		_energizeDelay = processInteger(rr, "energizedelay");
		_deenergizeDelay = processInteger(rr, "deenergizedelay");
		super.configure(rr);
	}
	@Override
	public String toString()
	{
		StringBuilder sb = reportString();
		sb.append(", mvar=");
		sb.append(_mvar);
		sb.append(", hasRegulator=");
		sb.append(_hasRegulator);
		sb.append(", regulatedNode=");
		sb.append(_regnode);
		sb.append(", minKV=");
		sb.append(_minkv);
		sb.append(", maxKV=");
		sb.append(_maxkv);
		sb.append(", energizeDelay=");
		sb.append(_energizeDelay);
		sb.append(", deenergizeDelay=");
		sb.append(_deenergizeDelay);
		return sb.toString();
	}
	
	
}
