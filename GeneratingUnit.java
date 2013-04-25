package com.powerdata.pse.csvmemdb;

public class GeneratingUnit extends PsrNamedObject
{
	public enum GeneratingUnitType
	{
		Hydro, Thermal;
		public static GeneratingUnitType fromString(String val)
		{
			GeneratingUnitType rv = null;
			if (val != null)
			{
				if (val.equalsIgnoreCase("hydro"))
				{
					rv = Hydro;
				}
				else if (val.equalsIgnoreCase("thermal"))
				{
					rv = Thermal;
				}
			}
			return rv;
		}
	}
	public enum GenControlMode
	{
		Setpoint, Pulsed;
		public static GenControlMode fromString(String val)
		{
			GenControlMode rv = Setpoint;
			if (val != null && val.equals("pulsed"))
				rv = Pulsed;
			return rv;
		}
	}
	
	protected GeneratingUnitType _type;
	protected Float _minopmw;
	protected Float _maxopmw;
	protected Float _ratedgrossminmw;
	protected Float _ratedgrossmaxmw;
	protected Float _controldeadband;
	protected Float _controlResponseRate;
	protected Float _stepChange;
	protected Float _spinReserveRamp;
	protected GenControlMode _controlMode;
	protected Float _govscd;
	protected Float _govmpl;

	public GeneratingUnitType getGeneratingUnitType() {return _type;}
	public Float getMinOperatingMW() {return _minopmw;}
	public Float getMaxOperatingMW() {return _maxopmw;}
	public Float getRatedGrossMinMW() {return _ratedgrossminmw;}
	public Float getRatedGrossMaxMW() {return _ratedgrossmaxmw;}
	public Float getControlDeadband() {return _controldeadband;}
	public Float getControlResponseRate() {return _controlResponseRate;}
	public Float getStepChange() {return _stepChange;}
	public Float getSpinReserveRamp() {return _spinReserveRamp;}
	public GenControlMode getGenControlMode() {return _controlMode;}
	public Float getGovernorSCD() {return _govscd;}
	public Float getGovernorMPL() {return _govmpl;}
	@Override
	public void configure(RecordReader rr)
	{
		_type = GeneratingUnitType.fromString(rr.getProperty("generatingunittype"));
		_minopmw = processFloat(rr, "minoperatingmw");
		_maxopmw = processFloat(rr, "maxoperatingmw");
		_ratedgrossminmw = processFloat(rr, "ratedgrossminmw");
		_ratedgrossmaxmw = processFloat(rr, "ratedgrossmaxmw");
		_controldeadband = processFloat(rr, "controldeadband");
		_controlResponseRate = processFloat(rr, "controlresponserate");
		_stepChange = processFloat(rr, "stepchange");
		_spinReserveRamp = processFloat(rr, "spinreserveramp");
		_controlMode = GenControlMode.fromString(rr.getProperty("gencontrolmode"));
		_govscd = processFloat(rr, "governorscd");
		_govmpl = processFloat(rr, "governormpl");
		super.configure(rr);
	}
	@Override
	public String toString()
	{
		StringBuilder sb = super.reportString();
		sb.append(", type=");
		sb.append(_type);
		sb.append(", minOperatingMW=");
		sb.append(_minopmw);
		sb.append(", maxOperatingMW=");
		sb.append(_maxopmw);
		sb.append(", ratedGrossMinMW=");
		sb.append(_ratedgrossminmw);
		sb.append(", ratedGrossMaxMW=");
		sb.append(_ratedgrossmaxmw);
		sb.append(", controlDeadband=");
		sb.append(_controldeadband);
		sb.append(", controlResponseRate=");
		sb.append(_controlResponseRate);
		sb.append(", stepChange");
		sb.append(_stepChange);
		sb.append(", spinReserveRamp=");
		sb.append(_spinReserveRamp);
		sb.append(", genControlMode=");
		sb.append(_controlMode.toString());
		sb.append(", governorSCD=");
		sb.append(_govscd);
		sb.append(", governorMPL=");
		sb.append(_govmpl);
		return sb.toString();
	}
	
	
}

