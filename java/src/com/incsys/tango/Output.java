package com.incsys.tango;

import java.io.PrintWriter;

public class Output
{
	protected PrintWriter _wrtr;
	protected CommonBlock _cb;
	
	public Output(CommonBlock cblk, PrintWriter wrtr)
	{
		_cb = cblk;
		_wrtr = wrtr;
	}

	public void output(int ngen)
	{
//	    INTEGER NGEN,I,J
		int i, j;
//	    IF(TIME .EQ. 0.0) WRITE(6,2000)
//2000  FORMAT('1'//T42,'SIMULATED RESPONSES',//T4,'TIME  GEN  ROTOR   ',
//	     1'ROTOR    EQ''     ED''    TERM     ELEC-POWER     FIELD   ',
//	     2'MECH    SATN   AIR GAP'/T4,'SECS   NO  ANGLE   SPEED    ',
//	     3'VOLTS   VOLTS  VOLTS    REAL   IMAG    VOLTS   POWER  ',
//	     4'FACTOR   VOLTS'/)
		if (_cb.time == 0F)
		{
			_wrtr.println("SIMULATED RESPONSES\n");
			_wrtr.println("   TIME  GEN  ROTOR   ROTOR    EQ'     ED'    TERM     ELEC-POWER     FIELD   MECH    SATN   AIR GAP");
			_wrtr.println("   SECS   NO  ANGLE   SPEED    VOLTS   VOLTS  VOLTS    REAL   IMAG    VOLTS   POWER  FACTOR   VOLTS\n");
		}
//	    WRITE(6,1010) TIME
//1010  FORMAT(' ',F7.3)
		_wrtr.printf(" %7.3f\n", _cb.time);
//	    DO 10 I=1,NGEN
		for(i=0; i < ngen; ++i)
		{
//	        WRITE(6,1000) I,(PRTVAR(I,J),J=1,11)
//1000  	FORMAT(' ',8X,I3,F8.3,13F8.4)
			_wrtr.printf("         %3d%8.3f", i+1,_cb.prtvar[i][0]);
			for(j=1; j < 11; ++j)
			{
				_wrtr.printf("%8.4f", _cb.prtvar[i][j]);
			}
			_wrtr.println();
//10    CONTINUE
		}
		
		/* SET UP THE VARIABLES TO BE PLOTTED. */
//	    NT=NT+1
//	    TYM(NT)=TIME
		_cb.tym[_cb.nt]=_cb.time;
//	    VAR(NT,1)=PRTVAR(1,1)
		_cb.var[_cb.nt][0]=_cb.prtvar[0][0];
//	    VAR(NT,2)=PRTVAR(1,5)
		_cb.var[_cb.nt][1]=_cb.prtvar[0][4];
//	    VAR(NT,3)=PRTVAR(1,6)
		_cb.var[_cb.nt][2]=_cb.prtvar[0][5];
//	    VAR(NT,4)=PRTVAR(1,8)
		_cb.var[_cb.nt][3]=_cb.prtvar[0][7];
		++_cb.nt;
	}


}
