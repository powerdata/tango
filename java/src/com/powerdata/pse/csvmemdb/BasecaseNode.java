package com.powerdata.pse.csvmemdb;

public class BasecaseNode extends PsrObject
{
	public enum Type {PQ,PV,SL;}
	private Float _va;
	private Float _vm;
	private Type _type;
	
	final public Float getVa() {return _va;}
	final public Float getVm() {return _vm;}
	final public Type getType() {return _type;}

	@Override
	public void configure(RecordReader rr)
	{
		_va = processFloat(rr, "va");
		_vm = processFloat(rr, "vm");
		_type = Type.valueOf(rr.getProperty("bustype"));
		super.configure(rr);
	}
	@Override
	public String toString()
	{
		StringBuilder sb = reportString();
		sb.append(", VA=");
		sb.append(_va);
		sb.append(", VM=");
		sb.append(_vm);
		sb.append(", Type=");
		sb.append(_type);
		return sb.toString();
	}
	
	
	
}
