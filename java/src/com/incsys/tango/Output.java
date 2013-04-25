package com.incsys.tango;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.powerdata.pse.csvmemdb.GeneratingUnit;

public class Output
{
	/** original report */
	protected PrintWriter _wrtr;
	protected CommonBlock _cb;
	/** Directory to place result CSV files */
	protected File _csvout;
	protected ArrayList<PrintWriter> _unitout;
	
	public Output(CommonBlock cblk, PrintWriter wrtr, File csvout, List<GeneratingUnit> gunit)
	{
		_cb = cblk;
		_wrtr = wrtr;
		_csvout = csvout;
		if (_csvout != null)
		{
			_csvout.mkdirs();
			setupFiles(gunit);
		}
	}
	
	protected void setupFiles(List<GeneratingUnit> gunit)
	{
		try
		{
			_unitout = new ArrayList<>(gunit.size());
			for (GeneratingUnit gu : gunit)
			{
				PrintWriter npw = new PrintWriter(new BufferedWriter(new FileWriter(new File(_csvout, gu.getName()+".csv"))));
				_unitout.add(npw);
			}
			
		}
		catch(IOException ioex)
		{
			ioex.printStackTrace();
		}
	}
	
	public void close()
	{
		for (PrintWriter pw : _unitout)
			pw.close();
	}
	

	public void output(int ngen, boolean usecoi)
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
			
			if (_csvout != null)
			{
				for(PrintWriter pw : _unitout)
				{
					pw.print("Time,Rotor Angle,Rotor Speed,EQ' Volts, ED' Volts,");
					pw.print("Term Volts,Elec Power Re,Elec Pwr Im,Fld Volts,");
					pw.println("Mech Power, Satn Factor, Air Gap Volts,Elec MW,Elec MVAr");
				}
			}
		}
		
		
//	    WRITE(6,1010) TIME
//1010  FORMAT(' ',F7.3)
		_wrtr.printf(" %7.3f\n", _cb.time);
		if (_csvout != null)
		{
			for(PrintWriter pw : _unitout)
			{
				pw.print(_cb.time);
			}
		}

		if (usecoi)
		{
			float coi = 0F;
			for (int ig = 0; ig < ngen; ++ig)
				coi += _cb.prtvar[ig][0];
			coi /= ngen;
			for (int ig = 0; ig < ngen; ++ig)
				_cb.prtvar[ig][0] -= coi;
		}
		
//	    DO 10 I=1,NGEN
		for(i=0; i < ngen; ++i)
		{
//	        WRITE(6,1000) I,(PRTVAR(I,J),J=1,11)
//1000  	FORMAT(' ',8X,I3,F8.3,13F8.4)
			_wrtr.printf("         %3d%8.3f", i+1,_cb.prtvar[i][0]);
			@SuppressWarnings("resource")
			PrintWriter pw = (_csvout == null) ? null : _unitout.get(i);
			if (_csvout != null)
			{
				pw.print(',');
				pw.print(_cb.prtvar[i][0]);
			}
			for(j=1; j < 11; ++j)
			{
				_wrtr.printf("%8.4f", _cb.prtvar[i][j]);
				if (_csvout != null)
				{
					pw.print(',');
					pw.print(_cb.prtvar[i][j]);
				}
			}
			if (_csvout != null)
			{
				pw.print(',');
				pw.print(_cb.prtvar[i][5]*_cb.pbase[i]);
				pw.print(',');
				pw.print(_cb.prtvar[i][6]*_cb.pbase[i]);
			}
			_wrtr.println();
			if (_csvout != null) pw.println();
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
