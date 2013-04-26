package com.powerdata.pse.csvmemdb;

public class TransformerWinding extends PsrBranch
{
	protected String _transformer;
	protected Float _bmag;
	
	public String getTransformer() {return _transformer;}
	public Float getBmag() {return _bmag;}
	@Override
	public void configure(RecordReader rr)
	{
		_transformer = rr.getProperty("transformer");
		_bmag = processFloat(rr, "bmag");
		super.configure(rr);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = super.reportString();
		sb.append(", transformer=");
		sb.append(_transformer);
		sb.append(", bmag=");
		sb.append(_bmag);
		return sb.toString();
	}
}
