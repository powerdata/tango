package com.incsys.tango;

import java.io.PrintWriter;

public class Gen1
{
	final public static float Rad2Deg = 180F/(float)Math.PI;
	CommonBlockSet _cblk;
	PrintWriter _wrtr;
	float[] csat;
	
	public Gen1(CommonBlockSet cblk, int ngen, PrintWriter wrtr)
	{
		_cblk = cblk;
		_wrtr = wrtr;
		csat = new float[ngen];
	}

	/**
	 * ENTER HERE TO CALCULATE INITIAL CONDITIONS.
	 */
	public void gen1ic(int i)
	{
		TangoBlock2 b2 = _cblk.getBlock2();
		float[] xd = b2.xd();
		float[] xd1 = b2.xd1();
		float[] xl = b2.xl();
		float[] xq = b2.xq();
		float[] xq1 = b2.xq1();
		float[] r = b2.r();
		float[] c1 = b2.c1();
		float[] c2 = b2.c2();
		TangoBlock5 b5 = _cblk.getBlock5();
		ComplexList ct = b5.ct();
		ComplexList vt = b5.vt();
		float[] pm = b5.pm();
		float[] ef = b5.ef();
		Complex curr = null, eqd;
		float del=0F, eq=0F, ed=0F;
		float xds, xqs, id, iq, eaq, ead, eat, pe;
		float[][] out = _cblk.getBlock6().out();
		int j;
//	    CSAT(I)=1.0
		csat[i] = 1F;
		
//	    DO 100 J=1,3
		for(j=0; j<3; ++j)
		{
//			XDS=CSAT(I)*XD(I)+(1.0-CSAT(I))*XL(I)
			xds=csat[i]*xd[i]+(1F-csat[i])*xl[i];
//	        XQS=CSAT(I)*XQ(I)+(1.0-CSAT(I))*XL(I)
//	        IF(XQ1(I) .EQ. XQ(I))XQS=XQ(I)
			xqs=(xq1[i] == xq[i]) ? xq[i] : csat[i]*xq[i]+(1F-csat[i])*xl[i];
			/* CALCULATE ANGLE OF GENERATOR Q AXIS. */
//	        EQD=VT(I)+CMPLX(R(I),XQS)*CT(I)
			eqd=vt.get(i).add(ct.get(i).multiply(r[i], xqs));
//	        DEL=ATAN2(AIMAG(EQD),REAL(EQD))
			del=(float)Math.atan2(eqd.im(), eqd.re());
			/* TRANSFORM CURRENT ONTO GENERATOR REFERENCE. */
//	        CURR=CT(I)*CMPLX(SIN(DEL),COS(DEL))
			curr=ct.get(i).multiply((float)Math.sin(del),(float)Math.cos(del));
//	        ID=REAL(CURR)
			id=curr.re();
//	        IQ=AIMAG(CURR)
			iq=curr.im();
//	        EQ=CABS(EQD)-(XQS -XD1(I))*ID
			eq=eqd.abs()-(xqs-xd1[i])*id;
//	        EF(I)=(EQ+(XDS-XD1(I))*ID)/CSAT(I)
			ef[i]=(eq+(xds-xd1[i])*id)/csat[i];
//	        ED=(XQS -XQ1(I))*IQ
			ed=(xqs-xq1[i])*iq;
//	        EAQ=EQ-(XD1(I)-XL(I))*ID
			eaq=eq-(xd1[i]-xl[i])*id;
//	        EAD=ED+(XQ1(I)-XL(I))*IQ
//	        IF(XQ1(I) .EQ. XQ(I))EAD=0.0
			ead=(xq1[i] == xq[i]) ? 0F : ed+(xq1[i]-xl[i])*iq;
//	        EAT=SQRT(EAQ**2+EAD**2)
			eat=(float)Math.sqrt(eaq*eaq+ead*ead);
//	        CSAT(I)=1.0/(1.0+C1(I)*EXP(C2(I)*EAT))
			csat[i]=1F/(1F+c1[i]*(float)Math.exp(c2[i]*eat));
//100	CONTINUE
		}

//	    PE=REAL(VT(I)*CONJG(CT(I)))+CABS(CURR)**2*R(I)
		pe=vt.get(i).multiply(ct.get(i).conjugate()).re()+(float)Math.pow(curr.abs(), 2D)*r[i];
//	    PM(I)=PE
		pm[i] = pe;
//	    OUT(I,1)=0.0
		out[i][0]=0F;
//	    OUT(I,2)=DEL
		out[i][1]=del;
//	    OUT(I,3)=EQ
		out[i][2]=eq;
//	    OUT(I,4)=ED
		out[i][3]=ed;
	}

	public void gen1(int i)
	{
		/* enter here for each integration step */
		TangoBlock6 b6 = _cblk.getBlock6();
		float[][] out = b6.out();
		float[][] plug = b6.plug();
		TangoBlock5 b5 = _cblk.getBlock5();
		ComplexList ct = b5.ct();
		ComplexList vt = b5.vt();
		float[] pm = b5.pm();
		float[] ef = b5.ef();
		TangoBlock2 b2 = _cblk.getBlock2(); 
		float[] r = b2.r();
		float[] xl = b2.xl();
		float[] xd = b2.xd();
		float[] xq = b2.xq();
		float[] xq1 = b2.xq1();
		float[] td1 = b2.td1();
		float[] tq1 = b2.tq1();
		float[] xd1 = b2.xd1();
		float[] c1 = b2.c1();
		float[] c2 = b2.c2();
		float[] damp = b2.damp();
		float[] h = b2.h();
		float[] pbase = b2.pbase();
		
		float[][] prtvar = _cblk.getBlock9().prtvar();
		
//		REAL CSAT(10) (moved to class field)
//		COMPLEX CONJG,CMPLX,EQD,CURR
		Complex eqd, curr;
//		REAL ID,IQ,OME,DEL,EQ,ED,EAD,EAQ,EAT,PE,PL,QE
		float id, iq, ome, del, eq, ed, ead, eaq, eat, pe, pl, qe;
//		REAL TD1S,TQ1S,XDS,XQS
		float td1s, tq1s, xds, xqs;
//		INTEGER I,J
		int j;
		
		/* ENTER HERE FOR EACH INTEGRATION STEP. */
		/* DEFINE INTEGRATOR OUTPUTS. */
//		OME=OUT(I,1)
		ome=out[i][0];
//		DEL=OUT(I,2)
		del=out[i][1];
//		EQ=OUT(I,3)
		eq=out[i][2];
//		ED=OUT(I,4)
		ed=out[i][3];
		
		/* TRANSFORM CURRENT TO MACHINE REFERENCE. */
//	    CURR=CT(I)*CMPLX(SIN(DEL),COS(DEL))
		curr=ct.get(i).multiply((float)Math.sin(del), (float)Math.cos(del));
//	    ID=REAL(CURR)
		id=curr.re();
//	    IQ=AIMAG(CURR)
		iq=curr.im();
		
		/* CALCULATE GENERATOR OUTPUT PLUS LOSSES. */
//	    PE=REAL(VT(I)*CONJG(CT(I)))
		pe=vt.get(i).multiply(ct.get(i).conjugate()).re();
//	    QE=AIMAG(VT(I)*CONJG(CT(I)))
		qe=vt.get(i).multiply(ct.get(i).conjugate()).im();
//	    PL=CABS(CURR)**2*R(I)
		pl=(float)Math.pow(curr.abs(), 2)*r[i];

		/* ADJUST REACTANCES AND TIME CONSTANTS TO ACCOUNT FOR SATURATION. */
//	    XDS=CSAT(I)*XD(I)+(1.0-CSAT(I))*XL(I)
		xds=csat[i]*xd[i]+(1F-csat[i])*xl[i];
//	    XQS=CSAT(I)*XQ(I)+(1.0-CSAT(I))*XL(I)
//	    IF(XQ1(I) .EQ. XQ(I))XQS=XQ(I)
		xqs=(xq1[i]==xq[i])?xq[i]:csat[i]*xq[i]+(1F-csat[i])*xl[i];
//	    TD1S=TD1(I)*(1.0-(1.0-CSAT(I))*(XD(I)-XD1(I))/(XD(I)-XL(I)))
		td1s=td1[i]*(1F-(1F-csat[i])*(xd[i]-xd1[i])/(xd[i]-xl[i]));
//	    TQ1S=TQ1(I)*(1.0-(1.0-CSAT(I))*(XQ(I)-XQ1(I))/(XQ(I)-XL(I)))
		tq1s=tq1[i]*(1F-(1f-csat[i])*(xq[i]-xq1[i])/(xq[i]-xl[i]));
//	    EAQ=EQ-(XD1(I)-XL(I))*ID
		eaq=eq-(xd1[i]-xl[i])*id;
//	    EAD=ED+(XQ1(I)-XL(I))*IQ
//	    IF(XQ1(I) .EQ. XQ(I))EAD=0.0
		ead=(xq1[i]==xq[i])?0F:ed+(xq1[i]-xl[i])*iq;
//	    EAT=SQRT(EAQ**2+EAD**2)
		eat=(float)Math.sqrt(eaq*eaq+ead*ead);
//	    CSAT(I)=1.0/(1.0+C1(I)*EXP(C2(I)*EAT))
		csat[i]=1F/(1F+c1[i]*(float)Math.exp(c2[i]*eat));

		/* DEFINE INTEGRATOR INPUTS. */
		/* SET UP PRINTOUT VARIABLES. */
//		PLUG(I,1)=(PM(I)-PE-PL-DAMP(I)*OME)/(2.0*H(I))
		plug[i][0]=(pm[i]-pe-pl-damp[i]*ome)/(2F*h[i]);
//		PLUG(I,2)=377.0*OME
		plug[i][1]=377F*ome;
//		PLUG(I,3)=(CSAT(I)*EF(I)-EQ-(XDS-XD1(I))*ID)/TD1S
		plug[i][2]=(csat[i]*ef[i]-eq-(xds-xd1[i])*id)/td1s;
//		PLUG(I,4)=(-ED+(XQS -XQ1(I))*IQ)/TQ1S
		plug[i][3]=(-ed+(xqs-xq1[i])*iq)/tq1s;
//		PRTVAR(I,1)=DEL*180.0/PI
		prtvar[i][0]=del*(float)(180.0/Math.PI);
//		PRTVAR(I,2)=OME
		prtvar[i][1]=ome;
//		PRTVAR(I,3)=EQ
		prtvar[i][2]=eq;
//		PRTVAR(I,4)=ED
		prtvar[i][3]=ed;
//		PRTVAR(I,5)=CABS(VT(I))
		prtvar[i][4]=vt.get(i).abs();
//		PRTVAR(I,6)=PE*100.0/PBASE(I)
		prtvar[i][5]=pe*100F/pbase[i];
//		PRTVAR(I,7)=QE*100.0/PBASE(I)
		prtvar[i][6]=qe*100F/pbase[i];
//		PRTVAR(I,8)=EF(I)
		prtvar[i][7]=ef[i];
//		PRTVAR(I,9)=PM(I)*100.0/PBASE(I)
		prtvar[i][8]=pm[i]*100F/pbase[i];
//		PRTVAR(I,10)=CSAT(I)
		prtvar[i][9]=csat[i];
//		PRTVAR(I,11)=EAT
		prtvar[i][10]=eat;
	}
}
