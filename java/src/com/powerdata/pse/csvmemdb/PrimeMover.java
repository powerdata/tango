package com.powerdata.pse.csvmemdb;

public class PrimeMover extends PsrNamedObject
{
	public enum PrimeMoverType
	{
		HydroTurbine, SteamTurbine, CombustionTurbine;
		
		public static PrimeMoverType fromString(String value)
		{
			PrimeMoverType rv = SteamTurbine;
			if (value.equalsIgnoreCase("HydroTurbine"))
			{
				rv = HydroTurbine;
			}
			else if (value.equalsIgnoreCase("CombustionTurbine"))
			{
				rv = CombustionTurbine;
			}
			return rv;
		}
	};

	protected String _synchronousMachine;
	protected PrimeMoverType _type;
	
	public String getSynchronousMachine() {return _synchronousMachine;}
	public PrimeMoverType getType() {return _type;}
	@Override
	public void configure(RecordReader rr)
	{
		_synchronousMachine = rr.getProperty("synchronousmachine");
		_type = PrimeMoverType.fromString(rr.getProperty("type"));
		super.configure(rr);
	}
	@Override
	public String toString()
	{
		StringBuilder sb = reportString();
		sb.append(", synchronousMachine=");
		sb.append(_synchronousMachine);
		sb.append(", type=");
		sb.append(_type);
		return sb.toString();
	}
	
	
}
