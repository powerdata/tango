package com.incsys.tango;

public class CommonBlockSet
{
	TangoBlock1 _b1;
	TangoBlock2 _b2;
	TangoBlock3 _b3;
	TangoBlock4 _b4;
	TangoBlock5 _b5;
	TangoBlock6 _b6;
	TangoBlock7 _b7;
	TangoBlock8 _b8;
	TangoBlock9 _b9;
	
	public CommonBlockSet()
	{
		_b1 = new TangoBlock1();
		_b7 = new TangoBlock7();
		_b8 = new TangoBlock8();
	}

	public void setGeneratorCount(int ngen)
	{
		_b2 = new TangoBlock2(ngen);
		_b3 = new TangoBlock3(ngen);
		_b4 = new TangoBlock4(ngen);
		_b5 = new TangoBlock5(ngen);
		_b6 = new TangoBlock6(ngen);
		_b9 = new TangoBlock9(ngen);
	}
	
	public TangoBlock1 getBlock1() {return _b1;}
	public TangoBlock2 getBlock2() {return _b2;}
	public TangoBlock3 getBlock3() {return _b3;}
	public TangoBlock4 getBlock4() {return _b4;}
	public TangoBlock5 getBlock5() {return _b5;}
	public TangoBlock6 getBlock6() {return _b6;}
	public TangoBlock7 getBlock7() {return _b7;}
	public TangoBlock8 getBlock8() {return _b8;}
	public TangoBlock9 getBlock9() {return _b9;}
}
