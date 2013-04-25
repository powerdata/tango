package com.powerdata.pse.csvmemdb;

public class Transformer extends PsrNamedObject
{
	protected String _circuit;
	protected Integer _wndcnt;
	
	public String getCircuit() {return _circuit;}
	public Integer getWindingCount() {return _wndcnt;}
	@Override
	public void configure(RecordReader rr)
	{
		_circuit = rr.getProperty("circuit");
		_wndcnt = processInteger(rr, "windingcount");
		super.configure(rr);
	}
	@Override
	public String toString()
	{
		StringBuilder sb = super.reportString();
		sb.append(", circuit=");
		sb.append(_circuit);
		sb.append(", windingCount=");
		sb.append(_wndcnt);
		return sb.toString();
	}
	
	
}
