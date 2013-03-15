package com.incsys.tango;

import java.io.PrintWriter;

public class Nwsol
{
	protected CommonBlockSet _cblk;
	protected PrintWriter _wrtr;
	
	public Nwsol(CommonBlockSet cblk, PrintWriter wrtr)
	{
		_cblk = cblk;
		_wrtr = wrtr;
	}

	public void nwsol(int ngen)
	{
		TangoBlock6 b6 = _cblk.getBlock6();
		float[][] out = b6.out();
//	    COMPLEX SCALE,ROTATE
		Complex scale, rotate;
//	    REAL ID,IQ
		float id = 0, iq = 0;
//	    COMPLEX CMPLX,CONJG
//	    COMPLEX VOLD,YFICT
		Complex vold, yfict;
//	    COMPLEX EFICT(10),E(10)
		ComplexList efict = new ComplexList(10), e = new ComplexList(10);
//	    REAL DEL(10)
		float[] del = new float[10];
//	    INTEGER NGEN,I,J,ITER,NFLAG
		int i, j, iter, nflag;
//	    REAL ED,EQ,THETA,VD,VD1,VQ,VQ1
		float ed, eq, theta, vd = 0, vd1 = 0, vq = 0, vq1 = 0;
//	    DO 10 I=1,NGEN
		for(i=0; i < ngen; ++i)
		{
//	        DEL(I)=OUT(I,2)
			del[i] = out[i][2];
//	        EQ=OUT(I,3)
			eq = out[i][3];
//	        ED=OUT(I,4)
			ed = out[i][4];
			/*transform voltage to synchronous reference */
//	        THETA=DEL(I)-PI/2.0
			theta = del[i]-(float)Math.PI/2F;
//	        E(I)=CMPLX(ED,EQ)*CMPLX(COS(THETA),SIN(THETA))
			e.set(i, new Complex(ed,eq).multiply((float)Math.cos(theta), (float)Math.sin(theta)));
//	    10CONTINUE
		}

//	    ITER=0
		iter = 0;
		TangoBlock2 b2 = _cblk.getBlock2();
		TangoBlock5 b5 = _cblk.getBlock5();
		ComplexList ct = b5.ct();
		ComplexList vt = b5.vt();
		
		TangoBlock7 b7 = _cblk.getBlock7();
		ComplexList[] y = b7.getYMatrix();
		
		float[] xq = b2.xq();
		float[] xd1 = b2.xd1();
		float[] xq1 = b2.xq1();
		float[] r = b2.r();
		
		
		nflag = 1;
		while (nflag == 1 && iter < 10)
		{
//		    ITER=ITER+1
			++iter;
//		    DO 20 I=1,NGEN
			for(i=0; i < ngen; ++i)
			{
//		    	THETA=DEL(I)-PI/2.0
				theta=del[i]-(float)Math.PI/2F;
//		    	SCALE=CMPLX(0.0,(XQ1(I)-XD1(I))*0.5)/CMPLX(R(I),-(XQ1(I)+XD1(I))*0.5)
				scale=new Complex(0F,(xq1[i]-xd1[i])*.5F).divide(r[i], -(xq1[i]+xd1[i])*0.5F);
//		    	ROTATE=CMPLX(COS(2.0*THETA),SIN(2.0*THETA))
				rotate=new Complex((float)Math.cos(2F*theta), (float)Math.sin(2F*theta));
//		    	EFICT(I)=E(I)+SCALE*CONJG(E(I)-VT(I))*ROTATE
				efict.set(i, e.get(i).add(scale.multiply(e.get(i).subtract(vt.get(i)).conjugate()).multiply(rotate)));
//		    20CONTINUE
			}
			
//	        DO 30 I=1,NGEN
			for(i=0; i < ngen; ++i)
			{
//		        CT(I)=(0.0,0.0)
				Complex cti = new Complex(0,0);
//		        DO 25 J=1,NGEN
				for(j=0; j<ngen; ++j)
				{
//		            25 CT(I)=CT(I)+Y(I,J)*EFICT(J)
					cti = cti.add(y[i].get(j).multiply(efict.get(j)));
				}
				ct.set(i,cti);
//	        30CONTINUE
			}

//		    DO 40 I=1,NGEN
			for(i=0; i < ngen; ++i)
			{
//		    	YFICT=CMPLX(R(I),-(XD1(I)+XQ1(I))/2.0)/(R(I)*R(I)+XD1(I)*XQ1(I))
				yfict=new Complex(r[i],-(xd1[i]+xq1[i])/2F).divide(r[i]*r[i]+xd1[i]*xq1[i]);
//		        VT(I)=EFICT(I)-CT(I)/YFICT
				vt.set(i, efict.get(i).subtract(ct.get(i).divide(yfict)));
			}
//		    40CONTINUE
			
			/* CHECK FOR CONVERGENCE */
//		    NFLAG=0
			nflag=0;
//		    DO 50 I=1,NGEN
			for(i=0; i < ngen; ++i)
			{
//		    	EQ=OUT(I,3)
				eq=out[i][3];
//		    	ED=OUT(I,4)
				ed=out[i][4];
				/* TRANSFORM TERMINAL VOLTAGE AND CURRENT TO MACHINE REFERENCE. */
//		      	THETA=DEL(I)-PI/2.0
				theta=del[i]-(float)Math.PI/2F;
//		      	ROTATE=CMPLX(COS(THETA),-SIN(THETA))
				rotate=new Complex((float)Math.cos(theta), -(float)Math.sin(theta));
//		      	ID=REAL(CT(I)*ROTATE)
				id=ct.get(i).multiply(rotate).re();
//		      	IQ=AIMAG(CT(I)*ROTATE)
				iq=ct.get(i).multiply(rotate).im();
//		      	VD=REAL(VT(I)*ROTATE)
				vd=vt.get(i).multiply(rotate).re();
//		      	VQ=AIMAG(VT(I)*ROTATE)
				vq=vt.get(i).multiply(rotate).im();
//		      	IF(ABS(EQ-R(I)*IQ-XD1(I)*ID-VQ) .GT. .001) NFLAG=1
//		      	IF(ABS(ED-R(I)*ID+XQ1(I)*IQ-VD) .GT. .001) NFLAG=1
				if(Math.abs(eq-r[i]*iq-xd1[i]*id-vq) > .001F ||
				   Math.abs(ed-r[i]*id+xq1[i]*iq-vd) > .001F)
				{
					nflag=1;
				}
//		      	VD1=ED-R(I)*ID+XQ1(I)*IQ
				vd1=ed-r[i]*id+xq1[i]*iq;
//		      	VQ1=EQ-R(I)*IQ-XD1(I)*ID
				vq1=eq-r[i]*iq-xd1[i]*id;
//		    50CONTINUE
			}
		}
			
		if (iter >= 10)
		{
			_wrtr.println("SALIENCY ITERATIONS NOT CONVERGED");
			for (i=0; i < ngen; ++i)
			{
				_wrtr.format(" TERM %5d%10.4f%10.4f%10.4f%10.4f%10.4f%10.4f%10.4f%10.4f%10.4f%10.4f\n", 
					i+1, vt.re()[i], vt.im()[i], ct.re()[i], ct.im()[i], vd, vq, vd1, vq1, id, iq);
			}
		}
	}

}
