package com.powerdata.pse.csvmemdb;

public class BasecaseGeneratingUnit extends PsrObject
{
	public enum GeneratorOperatingMode {OFF, MAN, AGC, EDC, LFC;}
	
	protected Float _mw;
	protected GeneratorOperatingMode _genopmode;
	public Float getMW() {return _mw;}
	public GeneratorOperatingMode getGeneratorOperatingMode() {return _genopmode;}
	@Override
	public void configure(RecordReader rr)
	{
		_mw = processFloat(rr, "mw");
		String smode = rr.getProperty("generatoroperatingmode");
		if (smode == null || smode.isEmpty())
		{
			_genopmode = null;
		}
		else
		{
			_genopmode = GeneratorOperatingMode.valueOf(smode.toUpperCase());
		}
		super.configure(rr);
	}
	@Override
	public String toString()
	{
		StringBuilder sb = reportString();
		sb.append(", mw=");
		sb.append(_mw);
		sb.append(", generatorOperatingMode=");
		sb.append(_genopmode);
		return sb.toString();
	}
	
	
}
