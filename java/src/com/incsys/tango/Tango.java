package com.incsys.tango;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.powerdata.pse.csvmemdb.BasecaseGeneratingUnit;
import com.powerdata.pse.csvmemdb.BasecaseNode;
import com.powerdata.pse.csvmemdb.BasecaseSynchronousMachine;
import com.powerdata.pse.csvmemdb.CsvMemoryStore;
import com.powerdata.pse.csvmemdb.Exciter;
import com.powerdata.pse.csvmemdb.GeneratingUnit;
import com.powerdata.pse.csvmemdb.Node;
import com.powerdata.pse.csvmemdb.SynchronousMachine;

public class Tango
{
	public static final float HalfPI = (float)Math.PI/2F;
	public static final float Deg2Rad = (float)Math.PI/180F;

	protected CommonBlock _cb;
	protected CsvMemoryStore _csv;
	protected PrintWriter _wrtr;
	protected File _resdir;
	
	public Tango(CsvMemoryStore rdr, PrintWriter wrtr, File resdir)
	{
		_csv = rdr;
		_wrtr = wrtr;
		_resdir = resdir;
	}

	public void runTango(List<TangoEvent> event, TangoControl tc) throws IOException
	{
		int ngen = _csv.getGeneratingUnit().size();
		
//	    COMPLEX YFICT
		Complex yfict;
//	    COMPLEX CMPLX,CONJG
//	    REAL C,TFIN,PT,QT,TOLD,VMAG,VARG,TPRINT
		float c, tfin = 0, pt, qt, told, vmag, varg, tprint;
//	    INTEGER I,J,NGEN,NPRINT,NSTEP
		int i, j, nprint = 0, nstep = 0;
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
		_wrtr.println("POWER SYSTEM DYNAMIC SIMULATION PROGRAM");
//	    READ(5,1000) NGEN,TSTEP,TPRINT
//	    1000  FORMAT(I5,5X,2F10.4)
		_cb = new CommonBlock(ngen);
		
		/*
		 * Generate some lists to track nodes and generators in lists in a
		 * fashion expected by this routine
		 */
		prepCsv();
		
		_cb.tstep = tc.getIntegrationTimeStep();
		tprint = tc.getOutputTimeStep();
		int pstep = (int) (tprint / _cb.tstep);
//	    WRITE(6,1005) NGEN,TSTEP,TPRINT
//1005  FORMAT('0NO. OF GENERATORS',T20,I5/' TIME STEP',T20,F6.3/
//		     1' PRINT INTERVAL',T20,F6.3)
//		WRITE(6,1008)
		_wrtr.printf("NO. OF GENERATORS %6d\n", ngen);
		_wrtr.printf("TIME STEP         %6.3f\n", _cb.tstep);
		_wrtr.printf("PRINT INTERVAL    %6.3f\n", tprint);
		_wrtr.println("GENERATOR PARAMETERS");
		
		/* READ GENERATOR PARAMETERS.- */
//	    DO 20 I=1,NGEN
		for(i=0; i < ngen; ++i)
		{
//	    	READ(5,1010)           PBASE(I),H(I),R(I),XL(I),XD(I),
//	     	  1XD1(I),XQ(I),XQ1(I),TD1(I),TQ1(I),DAMP(I),C1(I),C2(I)
			
			GeneratingUnit gu = _cb.genlist.get(i);
			SynchronousMachine sm = _cb.smlist.get(i);
			_cb.pbase[i] = gu.getMaxOperatingMW();
			//TODO:  add inertia to the CSV files, we do store that in the CIM
			_cb.h[i] = testNull(sm.getInertia(), GeneratorDefaults.h);
			_cb.r[i] = testNull(sm.getStatorResistance(), GeneratorDefaults.ra);
			_cb.xl[i] = testNull(sm.getStatorLeakageReactance(), GeneratorDefaults.xl);
			_cb.xd[i] = testNull(sm.getDirectSyncReactance(), GeneratorDefaults.xd);
			_cb.xd1[i] = testNull(sm.getDirectTransientReactance(), GeneratorDefaults.xd1);
			_cb.xq[i] = testNull(sm.getQuadSyncReactance(), GeneratorDefaults.xq);
			_cb.xq1[i] = testNull(sm.getQuadTransientReactance(), GeneratorDefaults.xq1);
			_cb.td1[i] = testNull(sm.getDirectTransientRotorTC(), GeneratorDefaults.td1);
			_cb.tq1[i] = testNull(sm.getQuadTransientRotorTC(), GeneratorDefaults.tq1);
			_cb.damp[i] = testNull(sm.getDamping(), GeneratorDefaults.d);
			_cb.c1[i] = testNull(sm.getC1(), GeneratorDefaults.c1);
			_cb.c2[i] = testNull(sm.getC2(), GeneratorDefaults.c2);
			
//1010      FORMAT(8F10.4)
//	    	WRITE(6,1015) I,       PBASE(I),H(I),R(I),XL(I),XD(I),
//	     	 1XD1(I),XQ(I),XQ1(I),TD1(I),TQ1(I),DAMP(I),C1(I),C2(I)
//1015  	FORMAT(1X,I5,8G12.4/6X,8G12.4)
			_wrtr.printf(
				" %5d %12.4g%12.4g%12.4g%12.4g%12.4g%12.4g%12.4g%12.4g\n",
				i + 1, _cb.pbase[i], _cb.h[i], _cb.r[i], _cb.xl[i], _cb.xd[i],
				_cb.xd1[i], _cb.xq[i], _cb.xq1[i]);
			_wrtr.printf(" %5c %12.4g%12.4g%12.4g%12.4g%12.4g,%s\n", ' ',
				_cb.td1[i], _cb.tq1[i], _cb.damp[i], _cb.c1[i], _cb.c2[i], gu.getName());
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
		_wrtr.println("EXCITATION SYSTEM PARAMETERS");
		/* READ EXCITATION SYSTEM PARAMETERS. */
		Map<String, Exciter> exmap = _csv.getExciter();
		HashMap<String,Exciter> exbysm = new HashMap<>(exmap.size());
		for(Exciter ex : exmap.values())
			exbysm.put(ex.getSynchronousMachine(), ex);
//	    DO 30 I=1,NGEN
		for(i=0; i < ngen; ++i)
		{
//	    	READ(5,1020) (AVRPRM(I,J),J=1,16)
//	1020  	FORMAT(8F10.4)
//	      	WRITE(6,1025) I,(AVRPRM(I,J),J=1,16)
//	1025  	FORMAT(1X,I5,8G12.4/6X,8G12.4)
			SynchronousMachine sm = _cb.smlist.get(i);
			Exciter ex = exbysm.get(sm.getID());
			if (ex != null)
			{
				_cb.ka[i] = ex.getKa();
				_cb.ke[i] = ex.getKe();
				_cb.kf[i] = ex.getKf();
				_cb.ta[i] = ex.getTa();
				_cb.te[i] = ex.getTe();
				_cb.tf[i] = ex.getTf();
				_cb.vrmin[i] = ex.getVrmin();
				_cb.vrmax[i] = ex.getVrmax();
				_cb.ac1[i] = ex.getA();
				_cb.ac2[i] = ex.getB();
			}
			
			_wrtr.printf(" %5d ", i+1);
			for (j=0; j < CommonBlock.GenMaxAttrib; ++j)
			{
				if (j == 8) _wrtr.print("\n       ");
				_wrtr.printf("%12.4g", _cb.avrprm[j][i]);
			}
			_wrtr.println();
//		30   CONTINUE
		}

		/*
		 * Turbine parameters not ever seen in this code, so are read only to
		 * preserve input stream order
		 */
		//	    WRITE(6,1028)
//	    1028  FORMAT('0 TURBINE-GOVERNOR PARAMETERS')
//		wrtr.println("TURBINE-GOVERNOR PARAMETERS");
		/* READ TURBINE AND GOVERNOR PARAMETERS. */
//	    DO 40 I=1,NGEN
//		for(i=0; i < ngen; ++i)
//        {
//	    	READ(5,1030) (TURPRM(I,J),J=1,16)
//1030  	FORMAT(8F10.4)
			
//			rdr.readArrays(i, 8, CommonBlock.GenMaxAttrib, null);
//	        WRITE(6,1035) I,(TURPRM(I,J),J=1,16)
//1035  	FORMAT(1X,I5,8G12.4/6X,8G12.4)
//40    CONTINUE
//        }

//	    WRITE(6,1038)
//1038  FORMAT('0INITIAL GENERATOR TERMINAL CONDITIONS'/
//	      1T3,'GEN',T15,'MW',T26,'MVAR',T36,'VOLTS',T46,'ANGLE')
		_wrtr.println("INITIAL GENERATOR TERMINAL CONDITIONS");
		_wrtr.println("  GEN         MW         MVAR      VOLTS     ANGLE");
		/* READ CONDITIONS ON TERMINAL BUSES. */
		Map<String,BasecaseSynchronousMachine> mapbcsm = _csv.getBasecaseSynchronousMachine();
		Map<String,BasecaseGeneratingUnit> mapbcgu = _csv.getBasecaseGeneratingUnit();
		Map<String,BasecaseNode> mapbcnode = _csv.getBasecaseNode();
		Map<String,Node> mapnode = _csv.getNode();
//		DO 50 I=1,NGEN
		for(i=0; i < ngen; ++i)
		{
//	        READ(5,1040)   PT,QT,VMAG,VARG
//1040  	FORMAT(2P2F10.4,0P2F10.4)
			SynchronousMachine sm = _cb.smlist.get(i);
			BasecaseSynchronousMachine bcsm = mapbcsm.get(sm.getID());
			BasecaseGeneratingUnit bcgu = mapbcgu.get(_cb.genlist.get(i).getID());
			String snode = sm.getNode();
			BasecaseNode bcnd = mapbcnode.get(snode);
			Node node = mapnode.get(snode);
			pt = bcgu.getMW();
			qt = bcsm.getMVAr();
			vmag = bcnd.getVm() / node.getNominalKV();
			varg = bcnd.getVa();
//	        WRITE(6,1045) I,PT,QT,VMAG,VARG
//1045  	FORMAT(1X,I5,5X,2P2F10.4,0P2F10.5)
			_wrtr.printf(" %5d     %10.4f%10.4f%10.5f%10.5f\n", i+1, pt, qt, vmag, varg);
//		    VARG=VARG*PI/180.0
			varg *= Deg2Rad;
			pt /= 100F;
			qt /= 100F;
//		    VT(I)=VMAG*CMPLX(COS(VARG),SIN(VARG))
			_cb.vt.set(i, new Complex((float)Math.cos(varg),(float)Math.sin(varg)).mult(vmag));
//		    CT(I)=CONJG(CMPLX(PT,QT)/VT(I))
			_cb.ct.set(i, new Complex(pt,qt).div(_cb.vt.get(i)).conjg());
//			CT SET HERE 2
//50    CONTINUE
		}
		
		Avr1 avr1 = new Avr1(_cb, ngen, _wrtr);
		Gen1 gen1 = new Gen1(_cb, ngen, _wrtr);

		 /* CALL EQUIPMENT SUBROUTINES TO CALCULATE INITIAL CONDITIONS.
		 *  CALL STATMENTS MUST BE GENERATED BY USER.
		 *  *******************************************************************/
//		CALL GEN1IC(1)
//		CALL GEN1IC(2)
//		CALL AVR1IC(1)
		/* *******************************************************************/

		for(int igen=0; igen < ngen; ++igen)
			gen1.gen1ic(igen);

		for(int igen=0; igen < ngen; ++igen)
		{
			SynchronousMachine sm = _cb.smlist.get(igen);
			Exciter ex = exbysm.get(sm.getID());
			if (ex != null)
			{
				avr1.avr1ic(igen);
			}
	
		}
		
		Int integrate = new Int(_cb);
		Output output = new Output(_cb, _wrtr, _resdir, _cb.genlist);
		Matrix matrix = new Matrix(_cb, _wrtr);
		Nwsol nwsol = new Nwsol(_cb, _wrtr);
		
		
		
		/* LOOP HERE FOR EACH NEW NETWORK CONDINTION */
//70    CONTINUE
			
		Netred netred = new Netred(_cb, _csv);
		Iterator<TangoEvent> it = event.iterator();

		do
		{
//		    TOLD=TFIN
			told = tfin;
			/* READ THE CONTROL CARD. */
//		    READ(5,1050) TFIN
//1050  	FORMAT(F10.4)
			TangoEvent ev = null;
			if (it.hasNext())
			{
				ev = it.next();
				tfin = ev.getFinishTime();
			}
			else
			{
				tfin = 0;
			}
//		    IF(TFIN .EQ. 0.0) GO TO 150
			if (tfin == 0F) continue;
//		    WRITE(6,1055) TOLD,TFIN
//1055  	FORMAT('0',T9,'TERMINAL ADMITTANCE MATRIX FROM',F8.3,' TO',F8.3,
//		     1' SECS.')
			_wrtr.printf("\tTERMINAL ADMITTANCE MATRIX FROM %8.3f TO %8.3f SECS.\n",
				told, tfin);
			
			/* READ THE NEW ADMITTANCE MATRIX. */
			
			netred.netred(ev.getFaultBus(), ev.getFromBus(), ev.getToBus(), ev.getCircuit());
//			DO 72 I=1,NGEN
			for (i=0; i < ngen; ++i)
			{
//				READ(5,1060) (Y(I,J),J=1,NGEN)
//1060  		FORMAT(8F10.4)
				ComplexList yi = _cb.y[i];
				float[] yire = yi.re();
				float[] yiim = yi.im();
//				WRITE(6,1065) (Y(I,J),J=1,NGEN)
//1065  		FORMAT((T9 ,8F10.4))
				_wrtr.print('\t');
				for (j=0; j < ngen; ++j)
				{
					_wrtr.printf("%10.4f%10.4f", yire[j], yiim[j]);
				}
				_wrtr.println();
//72    	CONTINUE
			}
//			CALL MATRIX(NGEN)
			matrix.matrix(ngen);
//		    WRITE(6,1076)
//1076 		FORMAT('0')
			_wrtr.println();

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
//			    CALL GEN1(1)
//			    CALL GEN1(2)
				/* **********************************************************************/
				for(int igen=0; igen < ngen; ++igen)
				{
					SynchronousMachine sm = _cb.smlist.get(igen);
					Exciter ex = exbysm.get(sm.getID());
					if (ex != null)
					{
						avr1.avr1(igen);
					}
				}
				
				for(int igen=0; igen < ngen; ++igen)
					gen1.gen1(igen);

				
				
				/* CHECK FOR OUTPUT. */
//			    IF(NSTEP .EQ. 0) CALL OUTPUT(NGEN)
//				if (nstep == 0) output.output(ngen);
//			    IF(NPRINT*TSTEP .LT. TPRINT-.0001) GO TO 125
//				if (nprint*_cb.tstep >= tprint-0.0001F)
				if ((nstep % pstep) == 0)
				{
//			    	CALL OUTPUT(NGEN)
					output.output(ngen, tc.useCenterOfInertia());
//			    	NPRINT=0
					nprint++;
//125 			CONTINUE
				}
				/* PERFORM INTEGRATION STEP. */
//			    CALL INT(NGEN)
				integrate.integrate(ngen);
//			    NSTEP=NSTEP+1
				++nstep;
//			    TIME=NSTEP*TSTEP
				int ttm = Math.round((nstep * _cb.tstep)*1000F);
				_cb.time = (float) ttm / 1000F;
//			    NPRINT=NPRINT+1
//				++nprint;
			}
		} while (tfin != 0F);
//	    CALL PLOT
//		new Plot(_cb, rdr, _wrtr).plot();
		output.close();
	}
	
	private float testNull(Float val, float defval)
	{
		return (val == null) ? defval : val;
	}

	private void prepCsv()
	{
		
		/* make a specific order to the units */
		Map<String,SynchronousMachine> smap = _csv.getSynchronousMachine();
		int ngen = smap.size();
		
		ArrayList<SynchronousMachine> smlist = new ArrayList<>(smap.values());
		
		// sorting the list has a convenient side effect of producing output in the same order.  Not a long-term solution.
		Collections.sort(smlist, new Comparator<SynchronousMachine>()
		{
			@Override
			public int compare(SynchronousMachine arg0, SynchronousMachine arg1)
			{
				return arg0.getIndex() - arg1.getIndex();
			}
		});
		
		
		ArrayList<GeneratingUnit> genlist = new ArrayList<>(ngen);
		Map<String,GeneratingUnit> gumap = _csv.getGeneratingUnit();
		HashMap<String,Integer> smndmap = new HashMap<>(ngen);
		
		for(int i=0; i < ngen; ++i)
		{
			SynchronousMachine sm = smlist.get(i);
			genlist.add(gumap.get(sm.getGeneratingUnit()));
			smndmap.put(sm.getNode(), i);
		}
		_cb.smlist = smlist;
		_cb.genlist = genlist;
		_cb.genndmap = smndmap;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	static public void main(String[] args) throws Exception
	{
		String scsvdir = System.getProperty("user.dir");
		String outname = null;
		String sevent = null;
		String scontrol = null;
		String resdir = null;
		
		int narg = args.length;
		for (int i = 0; i < narg;)
		{
			String a = args[i++];
			if (a.startsWith("-"))
			{
				int idx = (a.charAt(1) == '-') ? 2 : 1;
				switch (a.substring(idx))
				{
				case "csvdir":
					scsvdir = args[i++];
					break;
				case "output":
					outname = args[i++];
					break;
				case "control":
					scontrol = args[i++];
					break;
				case "event":
					sevent = args[i++];
					break;
				case "resultdir":
					resdir = args[i++];
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
		
		
		CsvMemoryStore cms = new CsvMemoryStore();
		cms.readModel(new File(scsvdir));
		PrintWriter out =new PrintWriter(new BufferedWriter(
				(outname == null) ? new OutputStreamWriter(System.out)
					: new FileWriter(outname)));
		
		if (sevent == null)
		{
			System.err.println("valid --event parameter required");
			showHelp();
			System.exit(1);
		}
		if (scontrol == null)
		{
			System.err.println("valid --scontrol required");
			showHelp();
			System.exit(1);
		}
		List<TangoEvent> evlist = loadEvents(sevent);
		
		if (resdir == null) resdir = System.getProperty("user.dir");
		Tango t = new Tango(cms, out, new File(resdir));
		t.runTango(evlist, new TangoControl(scontrol));

		if (outname==null)
			out.close();
		else
			out.flush();
	}
	
	private static List<TangoEvent> loadEvents(String sevent) throws IOException
	{
		ArrayList<TangoEvent> rv = new ArrayList<>();
		BufferedReader in = new BufferedReader(new FileReader(sevent));
		/* skip header, assumed fixed columns for now */
		in.readLine();
		String l = in.readLine();
		while (l != null)
		{
			rv.add(new TangoEvent(l));
			l = in.readLine();
		}
		in.close();
		return rv;
	}

	public static void showHelp()
	{
		System.out.println("usage: --csvdir model_csv_files --control control_properties ");
		System.out.println("--event event_csv_file [ --output file_name] [ --help ]");
	}

}

class TangoEvent
{
//	private int period;
	private float finishTime;
	private String faultbus;
	private String frombus;
	private String tobus;
	private String circuit;
//	public final int getPeriod() {return period;}
	public final float getFinishTime() {return finishTime;}
	public final String getFaultBus() {return faultbus;}
	public final String getFromBus() {return frombus;}
	public final String getToBus() {return tobus;}
	public final String getCircuit() {return circuit;}
	public TangoEvent() {}
	public TangoEvent(String csvline)
	{
		String[] tok = csvline.split(",");
		finishTime = Float.parseFloat(tok[0].trim());
		faultbus = parsefld(tok, 1);
		frombus = parsefld(tok, 2);
		tobus = parsefld(tok, 3);
		circuit = parsefld(tok, 4);
		
	}
	private String parsefld(String[] tok, int idx)
	{
		String t = (idx < tok.length) ? tok[idx] : null;
		return (t == null) ? null : t.trim();
	}
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("{finishTime=");
		sb.append(finishTime);
		sb.append(", faultbus=");
		sb.append(faultbus);
		sb.append(", frombus=");
		sb.append(frombus);
		sb.append(", tobus=");
		sb.append(tobus);
		sb.append(", circuit=");
		sb.append(circuit);
		sb.append('}');
		return sb.toString();
	}
}

class TangoControl
{
	private static TangoControl _ControlDefaults = new TangoControl(0.01F, 0.02F, true);
	
	private float _integrationTimeStep;
	private float _outputTimeStep;
	private boolean _useCenterOfInertia;
	public TangoControl(float tsint, float tsout,  boolean coi)
	{
		_integrationTimeStep = tsint;
		_outputTimeStep = tsout;
		_useCenterOfInertia = coi;
	}
	public TangoControl(String scontrol) throws IOException
	{
		float tsint = _ControlDefaults.getIntegrationTimeStep();
		float tsout = _ControlDefaults.getOutputTimeStep();
		boolean coi = _ControlDefaults.useCenterOfInertia();
		if (scontrol != null && !scontrol.isEmpty())
		{
			Properties p = new Properties();
			p.load(new BufferedReader(new FileReader(scontrol)));
			for(Entry<Object,Object> e : p.entrySet())
			{
				String k = e.getKey().toString().toLowerCase();
				String v = e.getValue().toString();
				switch(k)
				{
				case "integrationtimestep":
					tsint = Float.parseFloat(v);
					break;
				case "outputtimestep":
					tsout = Float.parseFloat(v);
					break;
				case "usecenterofinertia":
					char c = v.toLowerCase().charAt(0);
					coi = (c == 'y' || c == 't' || c == '1');
					break;
				default:
				}
			}
		}
		_integrationTimeStep = tsint;
		_outputTimeStep = tsout;
		_useCenterOfInertia = coi;
	}

	final public float getIntegrationTimeStep() {return _integrationTimeStep;}
	final public float getOutputTimeStep() {return _outputTimeStep;}
	final public boolean useCenterOfInertia() {return _useCenterOfInertia;}
}

final class GeneratorDefaults
{
	public static float h = 3.5F;
	public static float ra = 0F;
	public static float xl = 0F;
	public static float xd = 0.3F;
	public static float xd1 = 0.3F;
	public static float xq = 0.3F;
	public static float xq1 = 0.3F;
	public static float td1 = 10F;
	public static float tq1 = 10F;
	public static float d = 5F;
	public static float c1 = 0F;
	public static float c2 = 0F;
}

final class ExciterDefaults
{
	public static float ka = 25F;
	public static float ke = -0.0445F;
	public static float kf = 0.16F;
	public static float ta = 0.06F;
	public static float te = 0.5F;
	public static float tf = 1F;
	public static float vrmin = -1F;
	public static float vrmax = 1F;
	public static float a = 0.0016F;
	public static float b = 1.456F;
}

