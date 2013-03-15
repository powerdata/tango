package com.incsys.tango;

import java.io.PrintWriter;

public class Avr1
{
	CommonBlockSet _blks;
	float[] se;
	float[] vref;
	PrintWriter wrtr;
	
	public Avr1(CommonBlockSet cblk, int ngen, PrintWriter pw)
	{
		_blks = cblk;
		se = new float[ngen];
		vref = new float[ngen];
		wrtr = pw;
	}

	protected void avr1ic(int i)
	{
		TangoBlock5 b5 = _blks.getBlock5();
		float[] ef = b5.ef();
		ComplexList vt = b5.vt();
		TangoBlock3 b3 = _blks.getBlock3();
		float[] ac1 = b3.ac1();
		float[] ac2 = b3.ac2();
		float[] ka = b3.ka();
		float[] ke = b3.ke();
		float[] kf = b3.kf();
		float[] tf = b3.tf();
		float[] vrmin = b3.vrmin();
		float[] vrmax = b3.vrmax();
		float[][] out = _blks.getBlock6().out();

//	    SE(I)=AC1(I)*EXP(AC2(I)*EF(I))
		se[i]=ac1[i]*(float)Math.exp(ac2[i]*ef[i]);
//	    VREF(I)=CABS(VT(I))+(KE(I)+SE(I))*EF(I)/KA(I)
		vref[i]=vt.get(i).abs()+(ke[i]+se[i])*ef[i]/ka[i];
//	    OUT(I,5)=EF(I)
		out[i][5]=ef[i];
//	    OUT(I,6)=EF(I)*(KE(I)+SE(I))
		out[i][6]=ef[i]*(ke[i]+se[i]);
//	    OUT(I,7)=EF(I)*KF(I)/TF(I)
		out[i][7]=ef[i]*kf[i]/tf[i];
//	    IF(OUT(I,6) .LT. VRMIN(I)) WRITE(6,1020) I
//	    IF(OUT(I,6) .GT. VRMAX(I)) WRITE(6,1020)
//1020  FORMAT('0**** AVR VOLTAGE LIMIT IS EXCEEDED BY INITIAL FIELD ON',
//	     1' UNIT',I3/)
		if (out[i][6] < vrmin[i] || out[i][6] > vrmax[i])
		{
			wrtr.println("**** AVR VOLTAGE LIMIT IS EXCEEDED BY INITIAL FIELD ON UNIT"+(i+1));
		}
	}

	public void avr1(int i)
	{
		TangoBlock5 b5 = _blks.getBlock5();
		float[] ef = b5.ef();
		ComplexList vt = b5.vt();
		TangoBlock3 b3 = _blks.getBlock3();
		float[] ka = b3.ka();
		float[] ke = b3.ke();
		float[] kf = b3.kf();
		float[] ta = b3.ta();
		float[] te = b3.te();
		float[] tf = b3.tf();
		float[] vrmin = b3.vrmin();
		float[] vrmax = b3.vrmax();
		
		TangoBlock6 b6 = _blks.getBlock6();
		float[][] out = b6.out();
		float[][] plug = b6.plug();

//	    REAL VREF(10),SE(10)
		float[] vref=new float[10], se=new float[10];
//	    INTEGER I,J
		int j;
//	    REAL X1,X2,X3,X4,X5,X6,X7,X8
		float x1, x2, x3, x4, x5, x6, x7, x8;
		
		/* ENTER HERE FOR EACH INTEGRATION STEP. */
		/* DEFINE INTEGRATOR OUTPUTS. */
//		X5=OUT(I,6)
		x5=out[i][6];
//		EF(I)=OUT(I,5)
		ef[i]=out[i][5];
//		X3=OUT(I,7)
		x3=out[i][7];
		/* CALCULATE INTERMEDIATE VARIABLES */
//	    X1=VREF(I)-CABS(VT(I))
		x1=vref[i]-vt.get(i).abs();
//	    X2=KF(I)/TF(I)*EF(I)-X3
		x2=kf[i]/tf[i]*ef[i]-x3;
//	    X4=X1-X2
		x4=x1-x2;
//	    X6=X5
		x6=x5;
//	    IF(X6 .LT. VRMIN(I)) X6=VRMIN(I)
		if(x6 < vrmin[i]) x6=vrmin[i];
//	    IF(X6 .GT. VRMAX(I)) X6=VRMAX(I)
		if(x6 > vrmax[i]) x6=vrmax[i];
//	    X7=SE(I)*EF(I)
		x7=se[i]*ef[i];
//	    X8=X6-X7
		x8=x6-x7;
		/* DEFINE INTEGRATOR INPUTS. */
//	    PLUG(I,5)=X8/TE(I)-KE(I)/TE(I)*EF(I)
		plug[i][5]=x8/te[i]-ke[i]/te[i]*ef[i];
//	    PLUG(I,6)=KA(I)/TA(I)*X4-X5/TA(I)
		plug[i][6]=ka[i]/ta[i]*x4-x5/ta[i];
//	    PLUG(I,7)=X2/TF(I)
		plug[i][7]=x2/tf[i];

	}
}
