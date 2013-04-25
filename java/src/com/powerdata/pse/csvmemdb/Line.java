package com.powerdata.pse.csvmemdb;

public class Line extends PsrBranch
{
	private String _circuit;
	private Float _bch;
	private Float _length;
	
	public String getCircuit() {return _circuit;}
	public Float getBch() {return _bch;}
	public Float getLength() {return _length;}
	@Override
	public void configure(RecordReader rr)
	{
		_circuit = rr.getProperty("circuit");
		_bch = processFloat(rr, "bch");
		_length = processFloat(rr, "length");
		super.configure(rr);
	}
	@Override
	public String toString()
	{
		StringBuilder sb = super.reportString();
		sb.append(", circuit=");
		sb.append(_circuit);
		sb.append(", bch=");
		sb.append(_bch);
		sb.append(", length=");
		sb.append(_length);
		return sb.toString();
	}
}
