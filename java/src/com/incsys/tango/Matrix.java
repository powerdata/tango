package com.incsys.tango;

import java.io.PrintWriter;

public class Matrix
{
	protected CommonBlockSet _cblk;
	protected PrintWriter _wrtr;
	
	public Matrix(CommonBlockSet cblk, PrintWriter wrtr)
	{
		_cblk = cblk;
		_wrtr = wrtr;
	}
	public void matrix(int ngen)
	{
//	    COMPLEX YFICT,CMPLX,CONJG
		Complex yfict;
//	    INTEGER NGEN,I,J,N,M,N1
		int i, j, n, m, n1;
		/*
		 * AUGMENT Y MATRIX WITH GENERATOR BUSES AND ELIMINATE THE
		 * TERMINAL BUSES.
		 */
//	    N1=NGEN+1
		n1=ngen;

		ComplexList[] y = _cblk.getBlock7().getYMatrix();
		TangoBlock2 b2 = _cblk.getBlock2();
		float[] r = b2.r();
		float[] xd1 = b2.xd1();
		float[] xq1 = b2.xq1();
		
//	    DO 80 I=1,NGEN
		for(i=0; i<ngen; ++i)
		{
			/* MOVE TERMINAL BUS TO OUTSIDE OF MATRIX */
//	    	Y(N1,N1)=Y(I,I)
			y[n1].set(n1, y[i].get(i));
//	    	Y(I,I)=(0.0,0.0)
			y[i].set(i, Complex.Zero);
//	    	DO 75 J=1,NGEN
			for(j=0; i < ngen; ++j)
			{
				/* MOVE ROW */
//	    	    Y(N1,J)=Y(I,J)
				y[n1].set(j, y[i].get(j));
//	    	    Y(I,J)=(0.0,0.0)
				y[i].set(j, Complex.Zero);
				/* MOVE COLUMN */
//	    	    Y(J,N1)=Y(J,I)
				y[j].set(n1, y[j].get(i));
//	    	    Y(J,I)=(0.0,0.0)
				y[j].set(i, Complex.Zero);
//	    	75CONTINUE
			}
			/* ADD IN GENERATOR BUS */
//	    	YFICT=CMPLX(R(I),-(XD1(I)+XQ1(I))/2.0)/(R(I)*R(I)+XD1(I)*XQ1(I))
			yfict=new Complex(r[i],-(xd1[i]+xq1[i])/2F).divide(r[i]*r[i]+xd1[i]*xq1[i]);
//	    	Y(I,I)=YFICT
			y[i].set(i, yfict);
			/* CHECK IF TERMINAL BUS IS GROUNDED. */
//	    	IF(CABS(Y(N1,N1)) .EQ. 0.0) GO TO 80
			if(y[n1].abs(n1) == 0F) continue;
//	    	Y(N1,N1)=Y(N1,N1)+YFICT
			y[n1].add(n1, yfict);
//	    	Y(I,N1)=-YFICT
			y[i].set(n1, yfict.multiply(-1));
//	    	Y(N1,I)=-YFICT
			y[n1].set(i, yfict.multiply(-1));
			/* ELIMINATE THE TERMINAL BUS. */
//	    	DO 76 M=1,NGEN
			for(m=0; m < ngen; ++m)
			{
//	    		DO 76 N=M,NGEN
				for(n=m; n < ngen; ++n)
				{
//	    	        Y(M,N)=Y(M,N)-Y(M,N1)*Y(N1,N)/Y(N1,N1)
					y[m].subtract(n, y[m].get(n1).multiply(y[n1].get(n)).divide(y[n1].get(n1)));
//	    	        Y(N,M)=Y(M,N)
					y[n].set(m, y[m].get(n));
//	    		76CONTINUE
				}
			}
//	    80CONTINUE
		}
	}

}
