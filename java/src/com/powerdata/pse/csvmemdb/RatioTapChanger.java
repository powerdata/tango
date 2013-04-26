package com.powerdata.pse.csvmemdb;

public class RatioTapChanger extends TapChanger
{
	protected Float _stepsize;
	protected Integer _mintap;
	protected Integer _maxtap;
	protected Integer _neutraltap;
	protected Float _neutralkv;
	protected Integer _normaltap;
	protected Float _minratio;
	protected Float _maxratio;
	protected Boolean _isregulating;
	protected Float _minkv;
	protected Float _maxkv;
	protected String _regnode;
	protected Integer _initdelay;
	protected Integer _subsequentdelay;
	
	public Float getStepSize() {return _stepsize;}
	public Integer getMinTap() {return _mintap;}
	public Integer getMaxTap() {return _maxtap;}
	public Integer getNeutralTap() {return _neutraltap;}
	public Float getNeutralKV() {return _neutralkv;}
	public Integer getNormalTap() {return _normaltap;}
	public Float getMinRatio() {return _minratio;}
	public Float getMaxRatio() {return _maxratio;}
	public Boolean isRegulating() {return _isregulating;}
	public Float getMinKV() {return _minkv;}
	public Float getMaxKV() {return _maxkv;}
	public String getRegulatedNode() {return _regnode;}
	public Integer getInitialDelay() {return _initdelay;}
	public Integer getSubsequentDelay() {return _subsequentdelay;}
	@Override
	public void configure(RecordReader rr)
	{
		_stepsize = processFloat(rr, "stepsize");
		_mintap = processInteger(rr, "mintap");
		_maxtap = processInteger(rr, "maxtap");
		_neutraltap = processInteger(rr, "neutraltap");
		_neutralkv = processFloat(rr, "neutralkv");
		_normaltap = processInteger(rr, "normaltap");
		_minratio = processFloat(rr, "minratio");
		_maxratio = processFloat(rr, "maxratio");
		_isregulating = processBoolean(rr, "isregulating");
		_minkv = processFloat(rr, "minkv");
		_maxkv = processFloat(rr, "maxkv");
		_regnode = rr.getProperty("regulatednode");
		_initdelay = processInteger(rr, "initialdelay");
		_subsequentdelay = processInteger(rr, "subsequentdelay");
		super.configure(rr);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = reportString();
		sb.append(", stepSize=");
		sb.append(_stepsize);
		sb.append(", minTap=");
		sb.append(_mintap);
		sb.append(", maxTap=");
		sb.append(_maxtap);
		sb.append(", neutralTap=");
		sb.append(_neutraltap);
		sb.append(", neutralKV=");
		sb.append(_neutralkv);
		sb.append(", normalTap=");
		sb.append(_normaltap);
		sb.append(", minRatio=");
		sb.append(_minratio);
		sb.append(", maxRatio=");
		sb.append(_maxratio);
		sb.append(", isRegulating=");
		sb.append(_isregulating);
		sb.append(", minKV=");
		sb.append(_minkv);
		sb.append(", maxKV=");
		sb.append(_maxkv);
		sb.append(", regulatedNode=");
		sb.append(_regnode);
		sb.append(", initialDelay=");
		sb.append(_initdelay);
		sb.append(", subsequentDelay=");
		sb.append(_subsequentdelay);
		return sb.toString();
	}
	
	
}
