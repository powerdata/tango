package com.powerdata.pse.csvmemdb;

public class SwitchType extends PsrNamedObject
{
	protected Boolean _openUnderLoad;
	protected Boolean _closeUnderLoad;
	
	public Boolean getOpenUnderLoad() {return _openUnderLoad;}
	public Boolean getCloseUnderLoad() {return _closeUnderLoad;}
	@Override
	public void configure(RecordReader rr)
	{
		_openUnderLoad = processBoolean(rr, "openunderload");
		_closeUnderLoad = processBoolean(rr, "closeunderload");
		super.configure(rr);
	}
	@Override
	public String toString()
	{
		StringBuilder sb = reportString();
		sb.append(", openUnderLoad=");
		sb.append(_openUnderLoad);
		sb.append(", closeUnderLoad=");
		sb.append(_closeUnderLoad);
		return sb.toString();
	}
	
}
