package com.incsys.tango;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Plot
{
	protected CommonBlockSet _cblk;
	protected PrintWriter _wrtr;
	protected PsdDataReader _rdr;
	
	public Plot(CommonBlockSet cblk, PsdDataReader rdr, PrintWriter wrtr)
	{
		_cblk = cblk;
		_wrtr = wrtr;
		_rdr = rdr;
	}

	public void plot() throws IOException
	{
//	    INTEGER I,J,LOC,LOG
		int i, j, loc, log;
//	    REAL RANGE,SCALE
		float range, scale;
//	    CHARACTER SYMBOL(6),PLUS,NAME(6,40),ALINE(132),BLANK
//	    DATA SYMBOL/'A','B','C','D','E','F'/
//	    DATA PLUS/'+'/,BLANK/' '/
		char[] symbol = new char[] {'A','B','C','D','E','F'};
		char plus='+', blank=' ';
		String[] name = new String[6];
		char[] aline = new char[132];
//	    REAL VMAX(6),VMIN(6)
		float[] vmax = new float[6], vmin = new float[6];
//	    REAL YVAL(11)
		float[] yval = new float[11];
		/* READ GRAPH NAMES AND MINIMUM AND MAXIMUM VALUES.*/
//	    DO 3 I=1,7
		for(i=0; i<6; ++i)
		{
//	    	IF(I .EQ. 7) GO TO 5
			if(_rdr.nextRec()) break;
//3     	READ(5,1000,END=5) (NAME(I,J),J=1,40),VMIN(I),VMAX(I)
//1000  	FORMAT(40A1,F10.5,F10.5)
			name[i] = _rdr.readChars(40);
			vmin[i] = _rdr.getNextFloat();
			vmax[i] = _rdr.getNextFloat();
		}
		TangoBlock8 b8 = _cblk.getBlock8();
		float[] tym = b8.tym();
		float[][] var = b8.var();
//5     NVAR=I-1
		int nvar = i;
		b8.setNvar(nvar);
//		IF(NVAR .EQ. 0) RETURN
		if (b8.nvar() == 0) return;
//		WRITE(6,1060)
//1060  FORMAT('1')
		_wrtr.println();
		/* WRITE HEADINGS FOR EACH GRAPH. */
//	    DO 20 I=1,NVAR
		for(i=0; i<nvar; ++i)
		{
//	    	RANGE=VMAX(I)-VMIN(I)
			range=vmax[i]-vmin[i];
//	    	LOG=ALOG10(RANGE)
			log=(int)Math.log10(range);
//	    	SCALE=1.0*10.0**LOG
			scale=1F*(float)Math.pow(10F, log);
//	    	DO 10 J=1,11
			for(j=0; j < 11; ++j)
			{
//10    		YVAL(J)=(VMIN(I)+(J-1)*RANGE/10.0)/SCALE
				yval[j]=(vmin[i]+j*range/10F)/scale;
			}
//	      	WRITE(6,1010) SYMBOL(I),(NAME(I,J),J=1,40),SCALE
//1010  	FORMAT('0',T12,A1,' - ',40A1,T93,'SCALE FACTOR = ',1PE7.1)
			_wrtr.printf("\n           %c - %-75s  SCALE FACTOR = %7.1e",
				symbol[i], name[i], scale);
//	        WRITE(6,1020) (YVAL(J),J=1,11)
//1020  	FORMAT(' ',T10,11(F5.2,5X))
			_wrtr.printf("\n         ");
			for(j=0; j<11; ++j)
			{
				_wrtr.printf("%5.2f     ", yval[j]);
			}
//20    CONTINUE
		}
		/* WRITE Y AXIS. */
//	    WRITE(6,1030) (PLUS,LOC=1,101)
//1030  FORMAT('0    TIME  ',101A1)
		_wrtr.print("\n     TIME  ");
		for(loc=0; loc<101; ++loc) _wrtr.print(plus);
		_wrtr.println();
		/* PLOT GRAPHS. */
		float nt = b8.nt();
//	    DO 50 J=1,NT
		for(j=0; j<nt; ++j)
		{
//	    	DO 28 LOC=1,101
//28    	ALINE(LOC)=BLANK
			Arrays.fill(aline, blank);
//	    	ALINE(1)=PLUS
			aline[0]=plus;
//	    	IF(MOD(J,10) .NE. 1) GO TO 32
			if(j%10 == 0)
			{
				/* INCLUDE GRID POINTS. */
//	    		DO 30 LOC=1,101,10
				for(loc=0; loc<101; loc+=10)
//30    		ALINE(LOC)=PLUS
					aline[loc]=plus;
//32    	CONTINUE
			}
			/* INCLUDE GRAPH POINTS. */
//	      	DO 35 I=1,NVAR
			for(i=0; i < nvar; ++i)
			{
//	      		LOC=(VAR(J,I)-VMIN(I))/(VMAX(I)-VMIN(I))*100.0+1.0
				loc=(int) ((var[j][i]-vmin[i])/(vmax[i]-vmin[i])*100);
//	      		IF(LOC .LT. 1) LOC=1
				if (loc < 0) loc = 0;
//	      		IF(LOC .GT. 101) LOC=101
				if (loc > 100) loc=100;
//	      		ALINE(LOC)=SYMBOL(I)
				aline[loc]=symbol[i];
//35    	CONTINUE
			}
//	      	IF(MOD(J,10) .EQ. 1) WRITE(6,1040) TYM(J),(ALINE(LOC),LOC=1,101)
//			1040  FORMAT(T4,F7.3,1X,101A1)
			if(j%10 == 0)
			{
				_wrtr.printf("   %7.3f ", tym[j]);
			}
//		    IF(MOD(J,10) .NE. 1) WRITE(6,1045) (ALINE(LOC),LOC=1,101)
//1045  	FORMAT(T12,101A1)
			else
			{
				_wrtr.print("           ");
			}
			for(loc=0; loc<101; ++loc) _wrtr.print(aline[loc]);
			_wrtr.println();
//50    CONTINUE
		}
	}


}
