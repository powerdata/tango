package com.powerdata.pse.csvmemdb;

public abstract class TapChanger extends PsrNamedObject
{
	protected String _txwinding;
	protected String _tapnode;
	public String getTransformerWinding() {return _txwinding;}
	public String getTapNode() {return _tapnode;}
	@Override
	public void configure(RecordReader rr)
	{
		_txwinding = rr.getProperty("transformerwinding");
		_tapnode = rr.getProperty("tapnode");
		super.configure(rr);
	}
	@Override
	protected StringBuilder reportString()
	{
		StringBuilder sb = super.reportString();
		sb.append(", tapNode=");
		sb.append(_tapnode);
		sb.append(", transformerWinding=");
		sb.append(_txwinding);
		return sb;
	}

}
