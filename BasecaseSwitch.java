package com.powerdata.pse.csvmemdb;

public class BasecaseSwitch extends PsrObject
{
	public enum SwitchPosition
	{
		Open, Closed;
		public static SwitchPosition fromString(String s)
		{
			if (s == null) return null;
			char c = s.toLowerCase().charAt(0);
			return (c == 'c' || c == '0') ? Closed : Open;
		}
	}

	private SwitchPosition _swpos;
	public SwitchPosition getSwitchPosition() {return _swpos;}
	
	@Override
	public void configure(RecordReader rr)
	{
		_swpos = SwitchPosition.fromString(rr.getProperty("switchposition"));
		super.configure(rr);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = reportString();
		sb.append(", switchPosition=");
		sb.append(_swpos);
		return sb.toString();
	}
	
	
}
