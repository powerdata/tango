package com.powerdata.pse.csvmemdb;

public class Node extends PsrNamedObject
{
	protected Float _nominalkv;
	protected String _substation;
	protected Boolean _isbusbar;
	protected Integer _freqsrcpri;
	
	public Float getNominalKV() {return _nominalkv;}
	public String getSubstation() {return _substation;}
	public Boolean isBusBarSection() {return _isbusbar;}
	public Integer getFrequencySourcePriority() {return _freqsrcpri;}
	@Override
	public void configure(RecordReader rr)
	{
		_nominalkv = processFloat(rr, "nominalkv");
		_substation = rr.getProperty("substation");
		_isbusbar = processBoolean(rr, "isbusbarsection");
		_freqsrcpri = processInteger(rr, "frequencysourcepriority");
		
		super.configure(rr);
	}
	@Override
	public String toString()
	{
		StringBuilder sb = super.reportString();
		sb.append(", substation=");
		sb.append(_substation);
		sb.append(", nominalkv=");
		sb.append(_nominalkv);
		sb.append(", isbusbar=");
		sb.append(_isbusbar);
		sb.append(", frequencysourcepriority=");
		sb.append(_freqsrcpri);
		return sb.toString();
	}
	
	
}
