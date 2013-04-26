package com.powerdata.pse.csvmemdb;

/**
 * This class is added in order to get Transient stability operational. It is
 * not currently a part of the PowerSimulator model formats specification, and
 * requires significant review prior to inclusion
 * 
 * @author chris
 * 
 */
public class Exciter extends PsrNamedObject
{
	private String _sm;
	private float _ka;
	private float _ke;
	private float _kf;
	private float _ta;
	private float _te;
	private float _tf;
	private float _vrmin;
	private float _vrmax;
	private float _a;
	private float _b;
	
	public String getSynchronousMachine() {return _sm;}
	public Float getKa() {return _ka;}
	public Float getKe() {return _ke;}
	public Float getKf() {return _kf;}
	public Float getTa() {return _ta;}
	public Float getTe() {return _te;}
	public Float getTf() {return _tf;}
	public Float getVrmin() {return _vrmin;}
	public Float getVrmax() {return _vrmax;}
	public Float getA() {return _a;}
	public Float getB() {return _b;}

	public void configure(RecordReader rr)
	{
		_sm = rr.getProperty("synchronousmachine");
		_ka = processFloat(rr, "ka");
		_ke = processFloat(rr, "ke");
		_kf = processFloat(rr, "kf");
		_ta = processFloat(rr, "ta");
		_te = processFloat(rr, "te");
		_tf = processFloat(rr, "tf");
		_vrmin = processFloat(rr, "vrmin");
		_vrmax = processFloat(rr, "vrmax");
		_a = processFloat(rr, "a");
		_b = processFloat(rr, "b");
		super.configure(rr);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = super.reportString();
		sb.append(", synchronousMachine=");
		sb.append(_sm);
		sb.append(", ka=");
		sb.append(_ka);
		sb.append(", ke=");
		sb.append(_ke);
		sb.append(", kf=");
		sb.append(_kf);
		sb.append(", ta=");
		sb.append(_ta);
		sb.append(", te=");
		sb.append(_te);
		sb.append(", tf=");
		sb.append(_tf);
		sb.append(", vrmin=");
		sb.append(_vrmin);
		sb.append(", vrmax=");
		sb.append(_vrmax);
		sb.append(", a=");
		sb.append(_a);
		sb.append(", b=");
		sb.append(_b);
		
		return sb.toString();
	}

}
