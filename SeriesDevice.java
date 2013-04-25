package com.powerdata.pse.csvmemdb;

public class SeriesDevice extends PsrBranch
{
	private String _circuit;
	public String getCircuit() {return _circuit;}

	@Override
	public void configure(RecordReader rr)
	{
		_circuit = rr.getProperty("circuit");
		super.configure(rr);
	}
	@Override
	public String toString()
	{
		StringBuilder sb = reportString();
		sb.append(", circuit=");
		sb.append(_circuit);
		return sb.toString();
	}
	
	
}
