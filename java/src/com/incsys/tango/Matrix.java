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

import java.io.PrintWriter;

public class Matrix
{
	protected CommonBlock _cb;
	
	public Matrix(CommonBlock cblk)
	{
		_cb = cblk;
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

		ComplexList[] y = _cb.y;
//	    DO 80 I=1,NGEN
		for(i=0; i<ngen; ++i)
		{
			/* MOVE TERMINAL BUS TO OUTSIDE OF MATRIX */
//	    	Y(N1,N1)=Y(I,I)
			y[n1].set(n1, y[i].get(i));
//	    	Y(I,I)=(0.0,0.0)
			y[i].set(i, Complex.Zero);
//	    	DO 75 J=1,NGEN
			for(j=0; j < ngen; ++j)
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
			yfict=new Complex(_cb.r[i],-(_cb.xd1[i]+_cb.xq1[i])/2F).div(_cb.r[i]*_cb.r[i]+_cb.xd1[i]*_cb.xq1[i]);
//	    	Y(I,I)=YFICT
			y[i].set(i, yfict);
			/* CHECK IF TERMINAL BUS IS GROUNDED. */
//	    	IF(CABS(Y(N1,N1)) .EQ. 0.0) GO TO 80
			if(y[n1].abs(n1) == 0F) continue;
//	    	Y(N1,N1)=Y(N1,N1)+YFICT
			y[n1].add(n1, yfict);
//	    	Y(I,N1)=-YFICT
			y[i].set(n1, yfict.mult(-1));
//	    	Y(N1,I)=-YFICT
			y[n1].set(i, yfict.mult(-1));
			/* ELIMINATE THE TERMINAL BUS. */
//	    	DO 76 M=1,NGEN
			for(m=0; m < ngen; ++m)
			{
//	    		DO 76 N=M,NGEN
				for(n=m; n < ngen; ++n)
				{
//	    	        Y(M,N)=Y(M,N)-Y(M,N1)*Y(N1,N)/Y(N1,N1)
					y[m].sub(n, y[m].get(n1).mult(y[n1].get(n).div(y[n1].get(n1))));
//	    	        Y(N,M)=Y(M,N)
					y[n].set(m, y[m].get(n));
//	    		76CONTINUE
				}
			}
//	    80CONTINUE
		}
	}

}
