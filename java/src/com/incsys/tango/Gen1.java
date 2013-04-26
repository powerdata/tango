package com.incsys.tango;

import java.io.PrintWriter;

public class Gen1
{
	final public static float Rad2Deg = 180F/(float)Math.PI;
	CommonBlock _cb;
	float[] csat;
	
	public Gen1(CommonBlock cblk, int ngen)
	{
		_cb = cblk;
		csat = new float[ngen];
	}

	/**
	 * ENTER HERE TO CALCULATE INITIAL CONDITIONS.
	 */
	public void gen1ic(int i)
	{
		Complex curr = null, eqd;
		float del=0F, eq=0F, ed=0F;
		float xds, xqs, id, iq, eaq, ead, eat, pe;
		int j;
//	    CSAT(I)=1.0
		csat[i] = 1F;
		
//	    DO 100 J=1,3
		for(j=0; j<3; ++j)
		{
//			XDS=CSAT(I)*XD(I)+(1.0-CSAT(I))*XL(I)
			xds=csat[i]*_cb.xd[i]+(1F-csat[i])*_cb.xl[i];
//	        XQS=CSAT(I)*XQ(I)+(1.0-CSAT(I))*XL(I)
//	        IF(XQ1(I) .EQ. XQ(I))XQS=XQ(I)
			xqs=(_cb.xq1[i] == _cb.xq[i]) ? _cb.xq[i] : csat[i]*_cb.xq[i]+(1F-csat[i])*_cb.xl[i];
			/* CALCULATE ANGLE OF GENERATOR Q AXIS. */
//	        EQD=VT(I)+CMPLX(R(I),XQS)*CT(I)
			eqd=_cb.vt.get(i).add(_cb.ct.get(i).mult(_cb.r[i], xqs));
//	        DEL=ATAN2(AIMAG(EQD),REAL(EQD))
			del=(float)Math.atan2(eqd.im(), eqd.re());
			/* TRANSFORM CURRENT ONTO GENERATOR REFERENCE. */
//	        CURR=CT(I)*CMPLX(SIN(DEL),COS(DEL))
			curr=_cb.ct.get(i).mult((float)Math.sin(del),(float)Math.cos(del));
//	        ID=REAL(CURR)
			id=curr.re();
//	        IQ=AIMAG(CURR)
			iq=curr.im();
//	        EQ=CABS(EQD)-(XQS -XD1(I))*ID
			eq=eqd.abs()-(xqs-_cb.xd1[i])*id;
//	        EF(I)=(EQ+(XDS-XD1(I))*ID)/CSAT(I)
			_cb.ef[i]=(eq+(xds-_cb.xd1[i])*id)/csat[i];
//	        ED=(XQS -XQ1(I))*IQ
			ed=(xqs-_cb.xq1[i])*iq;
//	        EAQ=EQ-(XD1(I)-XL(I))*ID
			eaq=eq-(_cb.xd1[i]-_cb.xl[i])*id;
//	        EAD=ED+(XQ1(I)-XL(I))*IQ
//	        IF(XQ1(I) .EQ. XQ(I))EAD=0.0
			ead=(_cb.xq1[i] == _cb.xq[i]) ? 0F : ed+(_cb.xq1[i]-_cb.xl[i])*iq;
//	        EAT=SQRT(EAQ**2+EAD**2)
			eat=(float)Math.sqrt(eaq*eaq+ead*ead);
//	        CSAT(I)=1.0/(1.0+C1(I)*EXP(C2(I)*EAT))
			csat[i]=1F/(1F+_cb.c1[i]*(float)Math.exp(_cb.c2[i]*eat));
//100	CONTINUE
		}

//	    PE=REAL(VT(I)*CONJG(CT(I)))+CABS(CURR)**2*R(I)
		pe=_cb.vt.get(i).mult(_cb.ct.get(i).conjg()).re()+(float)Math.pow(curr.abs(), 2D)*_cb.r[i];
//	    PM(I)=PE
		_cb.pm[i] = pe;
//	    OUT(I,1)=0.0
		_cb.out[i][0]=0F;
//	    OUT(I,2)=DEL
		_cb.out[i][1]=del;
//	    OUT(I,3)=EQ
		_cb.out[i][2]=eq;
//	    OUT(I,4)=ED
		_cb.out[i][3]=ed;
	}

	public void gen1(int i)
	{
		/* enter here for each integration step */
		
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
		ome=_cb.out[i][0];
//		DEL=OUT(I,2)
		del=_cb.out[i][1];
//		EQ=OUT(I,3)
		eq=_cb.out[i][2];
//		ED=OUT(I,4)
		ed=_cb.out[i][3];
		
		/* TRANSFORM CURRENT TO MACHINE REFERENCE. */
//	    CURR=CT(I)*CMPLX(SIN(DEL),COS(DEL))
		curr=_cb.ct.get(i).mult((float)Math.sin(del), (float)Math.cos(del));
//	    ID=REAL(CURR)
		id=curr.re();
//	    IQ=AIMAG(CURR)
		iq=curr.im();
		
		/* CALCULATE GENERATOR OUTPUT PLUS LOSSES. */
//	    PE=REAL(VT(I)*CONJG(CT(I)))
		pe=_cb.vt.get(i).mult(_cb.ct.get(i).conjg()).re();
//	    QE=AIMAG(VT(I)*CONJG(CT(I)))
		qe=_cb.vt.get(i).mult(_cb.ct.get(i).conjg()).im();
//	    PL=CABS(CURR)**2*R(I)
		pl=(float)Math.pow(curr.abs(), 2)*_cb.r[i];

		/* ADJUST REACTANCES AND TIME CONSTANTS TO ACCOUNT FOR SATURATION. */
//	    XDS=CSAT(I)*XD(I)+(1.0-CSAT(I))*XL(I)
		xds=csat[i]*_cb.xd[i]+(1F-csat[i])*_cb.xl[i];
//	    XQS=CSAT(I)*XQ(I)+(1.0-CSAT(I))*XL(I)
//	    IF(XQ1(I) .EQ. XQ(I))XQS=XQ(I)
		xqs=(_cb.xq1[i]==_cb.xq[i])?_cb.xq[i]:csat[i]*_cb.xq[i]+(1F-csat[i])*_cb.xl[i];
//	    TD1S=TD1(I)*(1.0-(1.0-CSAT(I))*(XD(I)-XD1(I))/(XD(I)-XL(I)))
		td1s=_cb.td1[i]*(1F-(1F-csat[i])*(_cb.xd[i]-_cb.xd1[i])/(_cb.xd[i]-_cb.xl[i]));
//	    TQ1S=TQ1(I)*(1.0-(1.0-CSAT(I))*(XQ(I)-XQ1(I))/(XQ(I)-XL(I)))
		tq1s=_cb.tq1[i]*(1F-(1f-csat[i])*(_cb.xq[i]-_cb.xq1[i])/(_cb.xq[i]-_cb.xl[i]));
//	    EAQ=EQ-(XD1(I)-XL(I))*ID
		eaq=eq-(_cb.xd1[i]-_cb.xl[i])*id;
//	    EAD=ED+(XQ1(I)-XL(I))*IQ
//	    IF(XQ1(I) .EQ. XQ(I))EAD=0.0
		ead=(_cb.xq1[i]==_cb.xq[i])?0F:ed+(_cb.xq1[i]-_cb.xl[i])*iq;
//	    EAT=SQRT(EAQ**2+EAD**2)
		eat=(float)Math.sqrt(eaq*eaq+ead*ead);
//	    CSAT(I)=1.0/(1.0+C1(I)*EXP(C2(I)*EAT))
		csat[i]=1F/(1F+_cb.c1[i]*(float)Math.exp(_cb.c2[i]*eat));

		/* DEFINE INTEGRATOR INPUTS. */
		/* SET UP PRINTOUT VARIABLES. */
//		PLUG(I,1)=(PM(I)-PE-PL-DAMP(I)*OME)/(2.0*H(I))
		_cb.plug[i][0]=(_cb.pm[i]-pe-pl-_cb.damp[i]*ome)/(2F*_cb.h[i]);
//		PLUG(I,2)=377.0*OME
		_cb.plug[i][1]=377F*ome;
//		PLUG(I,3)=(CSAT(I)*EF(I)-EQ-(XDS-XD1(I))*ID)/TD1S
		_cb.plug[i][2]=(csat[i]*_cb.ef[i]-eq-(xds-_cb.xd1[i])*id)/td1s;
//		PLUG(I,4)=(-ED+(XQS -XQ1(I))*IQ)/TQ1S
		_cb.plug[i][3]=(-ed+(xqs-_cb.xq1[i])*iq)/tq1s;
//		PRTVAR(I,1)=DEL*180.0/PI
		_cb.prtvar[i][0]=del*(float)(180.0/Math.PI);
//		PRTVAR(I,2)=OME
		_cb.prtvar[i][1]=ome;
//		PRTVAR(I,3)=EQ
		_cb.prtvar[i][2]=eq;
//		PRTVAR(I,4)=ED
		_cb.prtvar[i][3]=ed;
//		PRTVAR(I,5)=CABS(VT(I))
		_cb.prtvar[i][4]=_cb.vt.get(i).abs();
//		PRTVAR(I,6)=PE*100.0/PBASE(I)
		_cb.prtvar[i][5]=pe*100F/_cb.pbase[i];
//		PRTVAR(I,7)=QE*100.0/PBASE(I)
		_cb.prtvar[i][6]=qe*100F/_cb.pbase[i];
//		PRTVAR(I,8)=EF(I)
		_cb.prtvar[i][7]=_cb.ef[i];
//		PRTVAR(I,9)=PM(I)*100.0/PBASE(I)
		_cb.prtvar[i][8]=_cb.pm[i]*100F/_cb.pbase[i];
//		PRTVAR(I,10)=CSAT(I)
		_cb.prtvar[i][9]=csat[i];
//		PRTVAR(I,11)=EAT
		_cb.prtvar[i][10]=eat;
	}
}
