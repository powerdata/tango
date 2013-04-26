package com.powerdata.pse.csvmemdb;

public class BasecaseControlArea extends PsrObject
{
	public enum ACEControlMode
	{
		TieLineBias, ConstantNetInterchange, ConstantFrequency;

		public static ACEControlMode fromString(String s)
		{
			if (s == null) return null;
			switch(s.toLowerCase())
			{
				case "tielinebias":
					return TieLineBias;
				case "constantnetinterchange":
					return ConstantNetInterchange;
				case "constantfrequency":
					return ConstantFrequency;
				default:
					return null;
			}
		}
	}
	
	private Float _netinterchange;
	private ACEControlMode _acecontrolmode;
	
	public Float getNetInterchange() {return _netinterchange;}
	public ACEControlMode getACEControlMode() {return _acecontrolmode;}
	
	@Override
	public void configure(RecordReader rr)
	{
		_netinterchange = processFloat(rr, "netinterchange");
		_acecontrolmode = ACEControlMode.fromString(
			rr.getProperty("acecontrolmode"));
		super.configure(rr);
	}
	@Override
	public String toString()
	{
		StringBuilder sb = reportString();
		sb.append(", netInterchange=");
		sb.append(_netinterchange);
		sb.append(", ACEControlMode=");
		sb.append(_acecontrolmode);
		return sb.toString();
	}
	
}
