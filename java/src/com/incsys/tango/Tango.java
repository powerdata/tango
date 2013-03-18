package com.incsys.tango;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

public class Tango
{
	public static final float HalfPI = (float)Math.PI/2F;
	public static final float Deg2Rad = (float)Math.PI/180F;
	
	protected CommonBlock _cb = new CommonBlock();

	public void runTango(PsdDataReader rdr, PrintWriter wrtr) throws IOException
	{
//	    COMPLEX YFICT
		Complex yfict;
//	    COMPLEX CMPLX,CONJG
//	    REAL C,TFIN,PT,QT,TOLD,VMAG,VARG,TPRINT
		float c, tfin = 0, pt, qt, told, vmag, varg, tprint;
//	    INTEGER I,J,NGEN,NPRINT,NSTEP
		int i, j, ngen, nprint = 0, nstep = 0;
//	    NT=0
//	    TIME=0.0
//	    TFIN=0.0
//	    NSTEP=0
//	    NPRINT=0
		/* CLEAR INTEGRATOR ARRAYS */
//	    DO 10 I=1,10
//	    DO 10 J=1,16
//	    PLUG(I,J)=0.0
//	    OUT(I,J)=0.0
//	    SAVE(I,J)=0.0
//	10  CONTINUE
//	    DO 12 I=1,10
//	    DO 12 J=1,20
//	12  PRTVAR(I,J)=0.0
		wrtr.println("POWER SYSTEM DYNAMIC SIMULATION PROGRAM");
//	    READ(5,1000) NGEN,TSTEP,TPRINT
//	    1000  FORMAT(I5,5X,2F10.4)
		rdr.nextLine();
		ngen = rdr.getNextInt();
//		TangoBlock1 b1 = _blks.getBlock1();
		_cb.tstep = rdr.getNextFloat();
		tprint = rdr.getNextFloat();
//	    WRITE(6,1005) NGEN,TSTEP,TPRINT
//1005  FORMAT('0NO. OF GENERATORS',T20,I5/' TIME STEP',T20,F6.3/
//		     1' PRINT INTERVAL',T20,F6.3)
//		WRITE(6,1008)
		wrtr.printf("NO. OF GENERATORS %6d\n", ngen);
		wrtr.printf("TIME STEP         %6.3f\n", _cb.tstep);
		wrtr.printf("PRINT INTERVAL    %6.3f\n", tprint);
		wrtr.println("GENERATOR PARAMETERS");
		
		/* READ GENERATOR PARAMETERS.- */
//	    DO 20 I=1,NGEN
		for(i=0; i < ngen; ++i)
		{
//	    	READ(5,1010)           PBASE(I),H(I),R(I),XL(I),XD(I),
//	     	  1XD1(I),XQ(I),XQ1(I),TD1(I),TQ1(I),DAMP(I),C1(I),C2(I)
			rdr.readArrayBlock(i, 8, 13, _cb.pbase, _cb.h, _cb.r, _cb.xl, _cb.xd,
				_cb.xd1, _cb.xq, _cb.xq1, _cb.td1, _cb.tq1, _cb.damp,
				_cb.c1, _cb.c2);
//1010      FORMAT(8F10.4)
//	    	WRITE(6,1015) I,       PBASE(I),H(I),R(I),XL(I),XD(I),
//	     	 1XD1(I),XQ(I),XQ1(I),TD1(I),TQ1(I),DAMP(I),C1(I),C2(I)
//1015  	FORMAT(1X,I5,8G12.4/6X,8G12.4)
			wrtr.printf(
				" %5d %12.4g%12.4g%12.4g%12.4g%12.4g%12.4g%12.4g%12.4g\n",
				i + 1, _cb.pbase[i], _cb.h[i], _cb.r[i], _cb.xl[i], _cb.xd[i],
				_cb.xd1[i], _cb.xq[i], _cb.xq1[i]);
			wrtr.printf(" %5c %12.4g%12.4g%12.4g%12.4g%12.4g\n", ' ',
				_cb.td1[i], _cb.tq1[i], _cb.damp[i], _cb.c1[i], _cb.c2[i]);
			/* CONVERT DATA TO 100 MW BASE. */
//		    C=100.0/PBASE(I)
			c = 100F/_cb.pbase[i];
//		    H(I)=H(I)/C
			_cb.h[i] /= c;
//		    R(I)=R(I)*C
			_cb.r[i] *= c;
//		    XL(I)=XL(I)*C
			_cb.xl[i] *= c;
//		    XD(I)=XD(I)*C
			_cb.xd[i] *= c;
//		    XD1(I)=XD1(I)*C
			_cb.xd1[i] *= c;
//		    XQ(I)=XQ(I)*C
			_cb.xq[i] *= c;
//		    XQ1(I)=XQ1(I)*C
			_cb.xq1[i] *= c;
//		    DAMP(I)=DAMP(I)/C
			_cb.damp[i] /= c;
//20    CONTINUE
		}
		
//	    WRITE(6,1018)
//1018  FORMAT('0 EXCITATION SYSTEM PARAMETERS')
		wrtr.println("EXCITATION SYSTEM PARAMETERS");
		/* READ EXCITATION SYSTEM PARAMETERS. */
//	    DO 30 I=1,NGEN
		for(i=0; i < ngen; ++i)
		{
//	    	READ(5,1020) (AVRPRM(I,J),J=1,16)
//	1020  	FORMAT(8F10.4)
			rdr.readArrays(i, 8, 16, _cb.avrprm);
//	      	WRITE(6,1025) I,(AVRPRM(I,J),J=1,16)
//	1025  	FORMAT(1X,I5,8G12.4/6X,8G12.4)
			wrtr.printf(" %5d ", i+1);
			for (j=0; j < 16; ++j)
			{
				if (j == 8) wrtr.print("\n       ");
				wrtr.printf("%12.4g", _cb.avrprm[j][i]);
			}
			wrtr.println();
//		30   CONTINUE
		}

		/* the turbine-governor parameters are not looked at anywhere */
//	    WRITE(6,1028)
//	    1028  FORMAT('0 TURBINE-GOVERNOR PARAMETERS')
		wrtr.println("TURBINE-GOVERNOR PARAMETERS");
		/* READ TURBINE AND GOVERNOR PARAMETERS. */
//	    DO 40 I=1,NGEN
		for(i=0; i < ngen; ++i)
		{
//	    	READ(5,1030) (TURPRM(I,J),J=1,16)
//1030  	FORMAT(8F10.4)
			rdr.readArrays(i, 8, 16, _cb.turprm);
//	        WRITE(6,1035) I,(TURPRM(I,J),J=1,16)
//1035  	FORMAT(1X,I5,8G12.4/6X,8G12.4)
			wrtr.printf(" %5d ", i+1);
			for(j=0; j<16; j++)
			{
				if (j == 8) wrtr.print("\n       ");
				wrtr.printf("%12.4g", _cb.turprm[j][i]);
			}
			wrtr.println();
//40    CONTINUE
		}

//	    WRITE(6,1038)
//1038  FORMAT('0INITIAL GENERATOR TERMINAL CONDITIONS'/
//	      1T3,'GEN',T15,'MW',T26,'MVAR',T36,'VOLTS',T46,'ANGLE')
		wrtr.println("INITIAL GENERATOR TERMINAL CONDITIONS");
		wrtr.println("  GEN         MW         MVAR      VOLTS     ANGLE");
		/* READ CONDITIONS ON TERMINAL BUSES. */

//		DO 50 I=1,NGEN
		for(i=0; i < ngen; ++i)
		{
//	        READ(5,1040)   PT,QT,VMAG,VARG
//1040  	FORMAT(2P2F10.4,0P2F10.4)
			rdr.nextLine();
			pt = rdr.getNextFloat();
			qt = rdr.getNextFloat();
			vmag = rdr.getNextFloat();
			varg = rdr.getNextFloat();
//	        WRITE(6,1045) I,PT,QT,VMAG,VARG
//1045  	FORMAT(1X,I5,5X,2P2F10.4,0P2F10.5)
			wrtr.printf(" %5d     %10.4f%10.4f%10.5f%10.5f\n", i+1, pt, qt, vmag, varg);
//		    VARG=VARG*PI/180.0
			varg *= Deg2Rad;
			pt /= 100F;
			qt /= 100F;
//		    VT(I)=VMAG*CMPLX(COS(VARG),SIN(VARG))
			_cb.vt.set(i, new Complex((float)Math.cos(varg),(float)Math.sin(varg)).multiply(vmag));
//		    CT(I)=CONJG(CMPLX(PT,QT)/VT(I))
			_cb.ct.set(i, new Complex(pt,qt).divide(_cb.vt.get(i)).conjugate());
//			CT SET HERE 2
//50    CONTINUE
		}
		
		Avr1 avr1 = new Avr1(_cb, ngen, wrtr);
		Gen1 gen1 = new Gen1(_cb, ngen, wrtr);

		 /* CALL EQUIPMENT SUBROUTINES TO CALCULATE INITIAL CONDITIONS.
		 *  CALL STATMENTS MUST BE GENERATED BY USER.
		 *  *******************************************************************/
//		CALL GEN1IC(1)
		gen1.gen1ic(0);
//		CALL GEN1IC(2)
		gen1.gen1ic(1);
//		CALL AVR1IC(1)
		avr1.avr1ic(0);
		/* *******************************************************************/

		Int integrate = new Int(_cb);
		Output output = new Output(_cb, wrtr);
		Matrix matrix = new Matrix(_cb, wrtr);
		Nwsol nwsol = new Nwsol(_cb, wrtr);
		
		/* LOOP HERE FOR EACH NEW NETWORK CONDINTION */
//70    CONTINUE
		do
		{
//		    TOLD=TFIN
			told = tfin;
			/* READ THE CONTROL CARD. */
//		    READ(5,1050) TFIN
//1050  	FORMAT(F10.4)
			rdr.nextLine();
			tfin = rdr.getNextFloat();
//		    IF(TFIN .EQ. 0.0) GO TO 150
			if (tfin == 0F) continue;
//		    WRITE(6,1055) TOLD,TFIN
//1055  	FORMAT('0',T9,'TERMINAL ADMITTANCE MATRIX FROM',F8.3,' TO',F8.3,
//		     1' SECS.')
			wrtr.printf("\tTERMINAL ADMITTANCE MATRIX FROM %8.3f TO %8.3f SECS.\n",
				told, tfin);
			
			/* READ THE NEW ADMITTANCE MATRIX. */
//			DO 72 I=1,NGEN
			for (i=0; i < ngen; ++i)
			{
//				READ(5,1060) (Y(I,J),J=1,NGEN)
//1060  		FORMAT(8F10.4)
				ComplexList yi = _cb.y[i];
				float[] yire = yi.re();
				float[] yiim = yi.im();
				rdr.nextLine();
				for(j=0; j < ngen; ++j)
				{
					yire[j] = rdr.getNextFloat();
					yiim[j] = rdr.getNextFloat();
				}
//				WRITE(6,1065) (Y(I,J),J=1,NGEN)
//1065  		FORMAT((T9 ,8F10.4))
				wrtr.print('\t');
				for (j=0; j < ngen; ++j)
				{
					if (j == 4) wrtr.print("\n\t");
					wrtr.printf("%10.4f%10.4f", yire[j], yiim[j]);
				}
				wrtr.println();
//72    	CONTINUE
			}
//			CALL MATRIX(NGEN)
			matrix.matrix(ngen);
//		    WRITE(6,1076)
//1076 		FORMAT('0')
			wrtr.println();

			/* LOOP HERE FOR EACH INTEGRATION STEP. */
			while (_cb.time < tfin)
			{
				/* SOLVE THE NETWORK. */
//			    CALL NWSOL(NGEN)
				nwsol.nwsol(ngen);

				/* CALL EQUPMENT SUBROUTINES TO CALCUALTE STATE VARIABLE DERIVATIVES.
				 * CALL STATMENTS MUST BE GENERATED BY USER.
				 ************************************************************************/
//			    CALL AVR1(1)
				avr1.avr1(0);
//			    CALL GEN1(1)
				gen1.gen1(0);
//			    CALL GEN1(2)
				gen1.gen1(1);
				/* **********************************************************************/
				
				/* CHECK FOR OUTPUT. */
//			    IF(NSTEP .EQ. 0) CALL OUTPUT(NGEN)
				if (nstep == 0) output.output(ngen);
//			    IF(NPRINT*TSTEP .LT. TPRINT-.0001) GO TO 125
				if (nprint*_cb.tstep >= tprint-0.0001F)
				{
//			    	CALL OUTPUT(NGEN)
					output.output(ngen);
//			    	NPRINT=0
					nprint = 0;
//125 			CONTINUE
				}
				/* PERFORM INTEGRATION STEP. */
//			    CALL INT(NGEN)
				integrate.integrate(ngen);
//			    NSTEP=NSTEP+1
				++nstep;
//			    TIME=NSTEP*TSTEP
				_cb.time = nstep * _cb.tstep;
//			    NPRINT=NPRINT+1
				++nprint;
			}
		} while (tfin != 0F);
//	    CALL PLOT
		new Plot(_cb, rdr, wrtr).plot();
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	static public void main(String[] args) throws IOException
	{

		Reader frdr = null;
		Writer fwrtr = null;
		
		int narg = args.length;
		for (int i = 0; i < narg;)
		{
			String a = args[i++];
			if (a.startsWith("-"))
			{
				int idx = (a.charAt(1) == '-') ? 2 : 1;
				switch (a.substring(idx))
				{
				case "i":
				case "input":
					frdr = new FileReader(args[i++]);
					break;
				case "o":
				case "output":
					fwrtr = new FileWriter(args[i++]);
					break;
				case "h":
				case "help":
					showHelp();
					System.exit(0);
				default:
					System.out.println("parameter " + a + " not understood");
					showHelp();
					System.exit(0);
				}
			}
		}
		Reader rdr = (frdr == null) ? new InputStreamReader(System.in) : frdr;
		Writer wrtr = (fwrtr == null) ? new OutputStreamWriter(System.out) : fwrtr;
		
		Tango t = new Tango();
		PrintWriter pwrtr = new PrintWriter(new BufferedWriter(wrtr)); 
		t.runTango(new PsdDataReader(new BufferedReader(rdr)), pwrtr);
		pwrtr.close();
	}
	



	public static void showHelp()
	{
		System.out.println("Tango --input file_name --output file_name [ --help ]");
	}

}
