package com.incsys.tango;

import java.io.PrintWriter;

public class Avr1
{
	CommonBlock _cb;
	float[] se;
	float[] vref;
	PrintWriter wrtr;
	
	public Avr1(CommonBlock cblk, int ngen, PrintWriter pw)
	{
		_cb = cblk;
		se = new float[10];
		vref = new float[10];
		wrtr = pw;
	}

	protected void avr1ic(int i)
	{

//	    SE(I)=AC1(I)*EXP(AC2(I)*EF(I))
		se[i]=_cb.ac1[i]*(float)Math.exp(_cb.ac2[i]*_cb.ef[i]);
//	    VREF(I)=CABS(VT(I))+(KE(I)+SE(I))*EF(I)/KA(I)
		vref[i]=_cb.vt.get(i).abs()+(_cb.ke[i]+se[i])*_cb.ef[i]/_cb.ka[i];
//	    OUT(I,5)=EF(I)
		_cb.out[i][4]=_cb.ef[i];
//	    OUT(I,6)=EF(I)*(KE(I)+SE(I))
		_cb.out[i][5]=_cb.ef[i]*(_cb.ke[i]+se[i]);
//	    OUT(I,7)=EF(I)*KF(I)/TF(I)
		_cb.out[i][6]=_cb.ef[i]*_cb.kf[i]/_cb.tf[i];
//	    IF(OUT(I,6) .LT. VRMIN(I)) WRITE(6,1020) I
//	    IF(OUT(I,6) .GT. VRMAX(I)) WRITE(6,1020)
//1020  FORMAT('0**** AVR VOLTAGE LIMIT IS EXCEEDED BY INITIAL FIELD ON',
//	     1' UNIT',I3/)
		if (_cb.out[i][5] < _cb.vrmin[i] || _cb.out[i][5] > _cb.vrmax[i])
		{
			wrtr.println("**** AVR VOLTAGE LIMIT IS EXCEEDED BY INITIAL FIELD ON UNIT"+(i+1));
		}
	}

	public void avr1(int i)
	{
//	    REAL VREF(10),SE(10) (handled as class members)
//	    INTEGER I,J
		int j;
//	    REAL X1,X2,X3,X4,X5,X6,X7,X8
		float x1, x2, x3, x4, x5, x6, x7, x8;
		
		/* ENTER HERE FOR EACH INTEGRATION STEP. */
		/* DEFINE INTEGRATOR OUTPUTS. */
//		X5=OUT(I,6)
		x5=_cb.out[i][5];
//		EF(I)=OUT(I,5)
		_cb.ef[i]=_cb.out[i][4];
//		X3=OUT(I,7)
		x3=_cb.out[i][6];
		/* CALCULATE INTERMEDIATE VARIABLES */
//	    X1=VREF(I)-CABS(VT(I))
		x1=vref[i]-_cb.vt.get(i).abs();
//	    X2=KF(I)/TF(I)*EF(I)-X3
		x2=_cb.kf[i]/_cb.tf[i]*_cb.ef[i]-x3;
//	    X4=X1-X2
		x4=x1-x2;
//	    X6=X5
		x6=x5;
//	    IF(X6 .LT. VRMIN(I)) X6=VRMIN(I)
		if(x6 < _cb.vrmin[i]) x6=_cb.vrmin[i];
//	    IF(X6 .GT. VRMAX(I)) X6=VRMAX(I)
		if(x6 > _cb.vrmax[i]) x6=_cb.vrmax[i];
//	    X7=SE(I)*EF(I)
		x7=se[i]*_cb.ef[i];
//	    X8=X6-X7
		x8=x6-x7;
		/* DEFINE INTEGRATOR INPUTS. */
//	    PLUG(I,5)=X8/TE(I)-KE(I)/TE(I)*EF(I)
		_cb.plug[i][4]=x8/_cb.te[i]-_cb.ke[i]/_cb.te[i]*_cb.ef[i];
//	    PLUG(I,6)=KA(I)/TA(I)*X4-X5/TA(I)
		_cb.plug[i][5]=_cb.ka[i]/_cb.ta[i]*x4-x5/_cb.ta[i];
//	    PLUG(I,7)=X2/TF(I)
		_cb.plug[i][6]=x2/_cb.tf[i];

	}
}
