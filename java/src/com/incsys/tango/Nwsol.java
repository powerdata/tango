/*
   Copyright 2013 Incremental Systems Corporation

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.incsys.tango;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Nwsol
{
	protected CommonBlock _cb;
	protected PrintWriter _wrtr;
	
	public Nwsol(CommonBlock cblk, PrintWriter wrtr)
	{
		_cb = cblk;
		_wrtr = wrtr;
	}

	public void nwsol(int ngen)
	{
//	    COMPLEX SCALE,ROTATE
		Complex scale, rotate;
//	    REAL ID,IQ
		float id = 0, iq = 0;
//	    COMPLEX CMPLX,CONJG
//	    COMPLEX VOLD,YFICT
		Complex vold, yfict;
//	    COMPLEX EFICT(10),E(10)
		ComplexList efict = new ComplexList(_cb.maxGen), e = new ComplexList(_cb.maxGen);
//	    REAL DEL(10)
		float[] del = new float[_cb.maxGen];
//	    INTEGER NGEN,I,J,ITER,NFLAG
		int i, j, iter, nflag;
//	    REAL ED,EQ,THETA,VD,VD1,VQ,VQ1
		float ed, eq, theta, vd = 0, vd1 = 0, vq = 0, vq1 = 0;
//	    DO 10 I=1,NGEN
		for(i=0; i < ngen; ++i)
		{
//	        DEL(I)=OUT(I,2)
			del[i] = _cb.out[i][1];
//	        EQ=OUT(I,3)
			eq = _cb.out[i][2];
//	        ED=OUT(I,4)
			ed = _cb.out[i][3];
			/*transform voltage to synchronous reference */
//	        THETA=DEL(I)-PI/2.0
			theta = del[i]-(float)Math.PI/2F;
//	        E(I)=CMPLX(ED,EQ)*CMPLX(COS(THETA),SIN(THETA))
			e.set(i, new Complex(ed,eq).mult((float)Math.cos(theta), (float)Math.sin(theta)));
//	    10CONTINUE
		}

//	    ITER=0
		iter = 0;
		
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
				scale=new Complex(0F,(_cb.xq1[i]-_cb.xd1[i])*.5F).div(_cb.r[i], -(_cb.xq1[i]+_cb.xd1[i])*0.5F);
//		    	ROTATE=CMPLX(COS(2.0*THETA),SIN(2.0*THETA))
				rotate=new Complex((float)Math.cos(2F*theta), (float)Math.sin(2F*theta));
//		    	EFICT(I)=E(I)+SCALE*CONJG(E(I)-VT(I))*ROTATE
				efict.set(i, e.get(i).add(scale.mult(e.get(i).sub(_cb.vt.get(i)).conjg()).mult(rotate)));
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
					cti = cti.add(_cb.y[i].get(j).mult(efict.get(j)));
				}
				_cb.ct.set(i,cti);
//				CT SET HERE 1
//	        30CONTINUE
			}

//		    DO 40 I=1,NGEN
			for(i=0; i < ngen; ++i)
			{
//		    	YFICT=CMPLX(R(I),-(XD1(I)+XQ1(I))/2.0)/(R(I)*R(I)+XD1(I)*XQ1(I))
				yfict=new Complex(_cb.r[i],-(_cb.xd1[i]+_cb.xq1[i])/2F).div(_cb.r[i]*_cb.r[i]+_cb.xd1[i]*_cb.xq1[i]);
//		        VT(I)=EFICT(I)-CT(I)/YFICT
				_cb.vt.set(i, efict.get(i).sub(_cb.ct.get(i).div(yfict)));
			}
//		    40CONTINUE
			
			/* CHECK FOR CONVERGENCE */
//		    NFLAG=0
			nflag=0;
//		    DO 50 I=1,NGEN
			for(i=0; i < ngen; ++i)
			{
//		    	EQ=OUT(I,3)
				eq=_cb.out[i][2];
//		    	ED=OUT(I,4)
				ed=_cb.out[i][3];
				/* TRANSFORM TERMINAL VOLTAGE AND CURRENT TO MACHINE REFERENCE. */
//		      	THETA=DEL(I)-PI/2.0
				theta=del[i]-(float)Math.PI/2F;
//		      	ROTATE=CMPLX(COS(THETA),-SIN(THETA))
				rotate=new Complex((float)Math.cos(theta), -(float)Math.sin(theta));
//		      	ID=REAL(CT(I)*ROTATE)
				id=_cb.ct.get(i).mult(rotate).re();
//		      	IQ=AIMAG(CT(I)*ROTATE)
				iq=_cb.ct.get(i).mult(rotate).im();
//		      	VD=REAL(VT(I)*ROTATE)
				vd=_cb.vt.get(i).mult(rotate).re();
//		      	VQ=AIMAG(VT(I)*ROTATE)
				vq=_cb.vt.get(i).mult(rotate).im();
//		      	IF(ABS(EQ-R(I)*IQ-XD1(I)*ID-VQ) .GT. .001) NFLAG=1
//		      	IF(ABS(ED-R(I)*ID+XQ1(I)*IQ-VD) .GT. .001) NFLAG=1
				if(Math.abs(eq-_cb.r[i]*iq-_cb.xd1[i]*id-vq) > .001F ||
				   Math.abs(ed-_cb.r[i]*id+_cb.xq1[i]*iq-vd) > .001F)
				{
					nflag=1;
				}
//		      	VD1=ED-R(I)*ID+XQ1(I)*IQ
				vd1=ed-_cb.r[i]*id+_cb.xq1[i]*iq;
//		      	VQ1=EQ-R(I)*IQ-XD1(I)*ID
				vq1=eq-_cb.r[i]*iq-_cb.xd1[i]*id;
//		    50CONTINUE
			}
		}
			
		if (iter >= 10)
		{
			PrintWriter pw = (_wrtr == null) ?
					new PrintWriter(new OutputStreamWriter(System.err)) : _wrtr;
			pw.println("SALIENCY ITERATIONS NOT CONVERGED");
			for (i=0; i < ngen; ++i)
			{
				pw.format(" TERM %5d%10.4f%10.4f%10.4f%10.4f%10.4f%10.4f%10.4f%10.4f%10.4f%10.4f\n", 
					i+1, _cb.vt.re()[i], _cb.vt.im()[i], _cb.ct.re()[i], _cb.ct.im()[i], vd, vq, vd1, vq1, id, iq);
			}
		}
	}

}
