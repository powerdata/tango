package com.powerdata.pse.csvmemdb;

public class BasecaseSynchronousMachine extends PsrObject
{
	public enum SynchronousMachineOperatingMode {GEN,CON,PMP;}
	public enum AVRMode
	{
		ON,OFF;
		public static AVRMode fromString(String s)
		{
			if (s == null) return null;
			String vs = s.toUpperCase();
			char c = vs.charAt(0);
			return (c == '1' || c == 'Y' || c == 'T' || vs.equals("ON")) ? 
					AVRMode.ON : AVRMode.OFF;
		}
	}
	
	private Float _kvsetpoint;
	private SynchronousMachineOperatingMode _opmode;
	private AVRMode _avrmode;
	private Float _mvarsetpt;

	public Float getKVSetPoint() {return _kvsetpoint;}
	public SynchronousMachineOperatingMode getSynchronousMachineOperatingMode() {return _opmode;}
	public AVRMode getAVRMode() {return _avrmode;}
	public Float getMVArSetPoint() {return _mvarsetpt;}
	
	@Override
	public void configure(RecordReader rr)
	{
		_kvsetpoint = processFloat(rr, "kvsetpoint");
		String smode = rr.getProperty("synchronousmachineoperatingmode");
		_opmode = (smode == null || smode.isEmpty()) ? null :
			SynchronousMachineOperatingMode.valueOf(smode.toUpperCase());
		_avrmode = AVRMode.fromString(rr.getProperty("avrmode"));
		_mvarsetpt = processFloat(rr, "mvarsetpoint");
		super.configure(rr);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = reportString();
		sb.append(", KVSetPoint=");
		sb.append(_kvsetpoint);
		sb.append(", SynchronousMachineOperatingMode=");
		sb.append(_opmode);
		sb.append(", AVRMode=");
		sb.append(_avrmode);
		sb.append(", MVArSetPoint=");
		sb.append(_mvarsetpt);
		return sb.toString();
	}
}
