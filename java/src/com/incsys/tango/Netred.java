package com.incsys.tango;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.powerdata.mdleng.transmission.csvimp.BasecaseLoad;
import com.powerdata.mdleng.transmission.csvimp.BasecaseNode;
import com.powerdata.mdleng.transmission.csvimp.CsvMemoryStore;
import com.powerdata.mdleng.transmission.csvimp.Line;
import com.powerdata.mdleng.transmission.csvimp.Load;
import com.powerdata.mdleng.transmission.csvimp.Node;
import com.powerdata.mdleng.transmission.csvimp.SeriesDevice;
import com.powerdata.mdleng.transmission.csvimp.SwitchedShunt;
import com.powerdata.mdleng.transmission.csvimp.TransformerWinding;

//'     NETWORK REDUCTION PROGRAM
public class Netred
{
	protected CommonBlock _cb;
	protected CsvMemoryStore _model;
	
	public Netred(CommonBlock cblk, CsvMemoryStore model)
	{
		_cb = cblk;
		_model = model;
	}

	// Sub Netred(NFAULT, PBUSA, QBUSA, LINENAMEA)
	public void netred(String nfault, String pbusa, String qbusa,
		String linenamea)
	{
		Map<String,TransformerWinding> txwnd = _model.getTransformerWinding();
		Map<String,Load> load = _model.getLoad();
		Map<String,SwitchedShunt> shunt = _model.getSwitchedShunt();
		Map<String,Line> line = _model.getLine();
		Map<String,SeriesDevice> serdev = _model.getSeriesDevice();
		
		Map<String,BasecaseNode> bcnode = _model.getBasecaseNode();
		Map<String,BasecaseLoad> bcload = _model.getBasecaseLoad();
		
		/** keep a consistent node order for this routine */
		List<Node> nodelist = new ArrayList<>(_model.getNode().values());
		int nbus = nodelist.size();

//		Dim E(100) As Cmplx
		ComplexList e = new ComplexList(nbus);
//		Dim EMAG(100) As Single
		float[] emag = new float[nbus];
//		Dim BTYPE(100) As Integer
		int[] btype = new int[nbus];
//		Dim GBUS(20) As Integer
		int[] gbus = new int[nbus];
//		Dim YSHUNT As Cmplx
		Complex yshunt;
//		Dim YIJ As Cmplx
		Complex yij;
//		Dim ZIJ As Cmplx
		Complex zij;
//		Dim YII As Cmplx
		Complex yii;
//		Dim YJJ As Cmplx
		Complex yjj;
//		Dim CURR As Cmplx
		Complex curr;
//		Dim CURR1 As Cmplx
		Complex curr1;
//		Dim S As Cmplx
		Complex s;
//		Dim YINV As Cmplx
		Complex yinv;
//		Dim PROD1 As Cmplx
		Complex prod1;
//		Dim PROD2 As Cmplx
		Complex prod2;
//		Dim EA As Cmplx
		Complex ea;
//		Dim IMAT(1000) As Integer
//
//
//	'   INITIALIZE VARIABLES
//
//	    For I = 1 To 100
//		      
//			E(I).Re = 0
//			E(I).Im = 0
//
//			BTYPE(I) = 0
//				For J = 1 To 100
//					Y(I, J).Re = 0
//					Y(I, J).Im = 0
//				Next J
//		Next I
//		       
		
//
//		nbus = Worksheets("Control").Cells(2, 2)
//		nload = Worksheets("Control").Cells(3, 2)
		int nload = load.size();
//		NGEN = Worksheets("Control").Cells(4, 2)
		int ngen;
//		nline = Worksheets("Control").Cells(5, 2)
		int nline = line.size();
//		ntran = Worksheets("Control").Cells(6, 2)
		int ntran = txwnd.size();
//		nshunt = Worksheets("Control").Cells(7, 2)
		int nshunt = shunt.size();

		ComplexList[] y = new ComplexList[nbus];

		/* index the bus id's */
		HashMap<String,Integer> nodeid = new HashMap<>(nbus);
		for(int i=0; i < nbus; ++i)
		{
			nodeid.put(nodelist.get(i).getID(), i);
			y[i] = new ComplexList(nbus);
		}
		
//		float[] ndshunt = new float[nbus];
//		for(SwitchedShunt sh : shunt.values())
//		{
//			int ibus = nodeid.get(sh.getNode());
//			ndshunt[ibus] += sh.getMVAr() / 100F;
//		}
		
//
//
//	 '  LOOP HERE FOR EACH BUS
//		N = 0
		int n = 0;
//
//		For I = 1 To nbus
		for (int i=0; i < nbus; ++i)
		{
//		    pbus = Worksheets("Buses").Cells(I + 1, 1)
			Node pbus = nodelist.get(i);
			BasecaseNode casend = bcnode.get(pbus.getID());
//		    NTYPE = Worksheets("Buses").Cells(I + 1, 8)
			BasecaseNode.Type ntype = casend.getType();
//		    EMAG(I) = Worksheets("Buses").Cells(I + 1, 11)
//			emag[i] = casend.getVm() / pbus.getNominalKV();
			emag[i] = casend.getVm();
//		    ARG = Worksheets("Buses").Cells(I + 1, 12)
			float arg = casend.getVa();
//		    pga = Worksheets("Buses").Cells(I + 1, 7)
			float pga = 0F;
//		    qga = Worksheets("Buses").Cells(I + 1, 8)
			float qga = 0F;
//		    GSHUNT = Worksheets("Buses").Cells(I + 1, 9)
//			float gshunt = 0F;
////		    bshunt = Worksheets("Buses").Cells(I + 1, 10)
//			float bshunt = ndshunt[i];
//		    ARG = ARG * 3.14159 / 180#
			arg *= Math.PI/180F;
//		    IMAT(pbus) = I
//	'       Y(I, I) = Y(I, I) + Cmplx(GSHUNT, BSHUNT) + Cmplx(PL, -QL) / (EMAG * EMAG)
//		    Y(I, I).Re = Y(I, I).Re + GSHUNT
//		    Y(I, I).Im = Y(I, I).Im + bshunt
//			y[i].add(i, gshunt, bshunt); ** shunt processing is done twice, don't do it here */
//		    BTYPE(I) = NTYPE
			switch (ntype)
			{
			case PQ:
				btype[i] = 0;
				break;
			case PV:
				btype[i] = 1;
				break;
			case SL:
				btype[i] = 2;
				break;
			default:
				btype[i] = -1;
			}
//		    E(I).Re = EMAG(I) * Cos(ARG)
//		    E(I).Im = EMAG(I) * Sin(ARG)
			e.set(i, emag[i]*(float)Math.cos(arg), emag[i]*(float)Math.sin(arg));
//		    If BTYPE(I) <> 0 Then
			if (btype[i] != 0)
			{
//		    	N = N + 1
//		        GBUS(N) = I
				gbus[n++] = i;
//		    End If
			}
			
//		Next I
		}

//		NGEN = N
		/*
		 * TODO: This routine assumes (and Tango in general?) that there is a
		 * single generator on a node. Resolve issues for when that is not the
		 * case.
		 */
		ngen = n;


//		For K = 1 To nload
		for(Load ld : load.values())
		{
			BasecaseLoad bld = bcload.get(ld.getID());
//		    pbus = Worksheets("Loads").Cells(K + 1, 2)
//		    PLA = Worksheets("Loads").Cells(K + 1, 8) / 100
			float pla = bld.getMW()/100F;
//		    QLA = Worksheets("Loads").Cells(K + 1, 9) / 100
			float qla = bld.getMVAr()/100F;
		        
//		    I = IMAT(pbus)
			int i = nodeid.get(ld.getNode());

//		    Y(I, I).Re = Y(I, I).Re + PLA / (EMAG(I) * EMAG(I))
//		    Y(I, I).Im = Y(I, I).Im - QLA / (EMAG(I) * EMAG(I))
			float em = emag[i];
			float em2 = em*em;
			y[i].add(i, pla/em2, -qla/em2); 

//		Next K
		}
//		    
//		    For K = 1 To nshunt
		for (SwitchedShunt ss : shunt.values())
		{
//		    
//		        STAT = Worksheets("Switched Shunts").Cells(K + 1, 2)
//		        
//		        pbus = Worksheets("Switched Shunts").Cells(K + 1, 3)
//		        I = IMAT(pbus)
			int i = nodeid.get(ss.getNode());
			float em = emag[i];
			float em2 = em*em;
//		        
//		        bshunt = Worksheets("Switched Shunts").Cells(K + 1, 10) / 100
//			float bshunt = ss.getMVAr()/100F*em2;
			float bshunt = ss.getMVAr()/100F;
//		        
//		        If STAT = 1 Then Y(I, I).Im = Y(I, I).Im + bshunt
			y[i].add(i, 0F, bshunt);
//		    
//		    Next K
		}
//
//
//		      
//		For K = 1 To nline
		for(Line l : line.values())
		{
//		    
//		    pbus = Worksheets("Lines").Cells(K + 1, 2)
			String pbus = l.getNode1();
//		    qbus = Worksheets("Lines").Cells(K + 1, 3)
			String qbus = l.getNode2();
//		    linename = Worksheets("Lines").Cells(K + 1, 4)
			String linename = l.getName();
//		        
//		    status = Worksheets("Lines").Cells(K + 1, 8)
//		        
//		    If status = 0 Then GoTo 100
//		                 
//		        
//		    If PBUSA = pbus And QBUSA = qbus And LINENAMEA = linename Then
//		        
//		        GoTo 100
//		        
//		    End If
			if (pbusa != null && pbusa.equals(pbus) && qbusa != null && qbusa.equals(qbus) && linenamea != null && linenamea.equals(linename))
				continue;
//		        
//		    R = Worksheets("Lines").Cells(K + 1, 5)
			float r = l.getR();
//		    x = Worksheets("Lines").Cells(K + 1, 6)
			float x = l.getX();
//		    BCH = Worksheets("Lines").Cells(K + 1, 7)
			float bch = l.getBch();
//		        
//		    I = IMAT(pbus)
			int i = nodeid.get(pbus);
//		    J = IMAT(qbus)
			int j = nodeid.get(qbus);
//		        
//		    ZIJ.Re = R
//		    ZIJ.Im = x
			zij = new Complex(r, x);
//		        
//		    Call Invert(ZIJ, YIJ)
			float bch2 = bch/2F;
			yij = zij.inv();
//
//		        
//		    Y(I, I).Re = Y(I, I).Re + YIJ.Re
//		    Y(I, I).Im = Y(I, I).Im + YIJ.Im + BCH / 2
			y[i].add(i, yij.add(0, bch2));
//		        
//		    Y(J, J).Re = Y(J, J).Re + YIJ.Re
//		    Y(J, J).Im = Y(J, J).Im + YIJ.Im + BCH / 2
			y[j].add(j, yij.add(0,bch2));
//		        
//		              
//		    Y(I, J).Re = Y(I, J).Re - YIJ.Re
//		    Y(I, J).Im = Y(I, J).Im - YIJ.Im
			y[i].sub(j, yij);
//		        
//		    Y(J, I).Re = Y(J, I).Re - YIJ.Re
//		    Y(J, I).Im = Y(J, I).Im - YIJ.Im
			y[j].sub(i, yij);
//		      
//		100    Next K
		}

		for(SeriesDevice sd : serdev.values())
		{
			String pbus = sd.getNode1();
			String qbus = sd.getNode2();
			String linename = sd.getName();
			if (pbusa != null && pbusa.equals(pbus) && qbusa != null && qbusa.equals(qbus) && linenamea != null && linenamea.equals(linename))
				continue;

			float r = sd.getR();
			float x = sd.getX();
			int i = nodeid.get(pbus);
			int j = nodeid.get(qbus);
			zij = new Complex(r, x);
			yij = zij.inv();
			y[i].add(i, yij);
			y[j].add(j, yij);
			y[i].sub(j, yij);
			y[j].sub(i, yij);
		}
		

//		For K = 1 To ntran
		for(TransformerWinding w : txwnd.values())
		{
//		        pbus = Worksheets("Transformers").Cells(K + 1, 1)
			String pbus = w.getNode1();
//		    qbus = Worksheets("Transformers").Cells(K + 1, 2)
			String qbus = w.getNode2();

//		    R = Worksheets("Transformers").Cells(K + 1, 16)
			float r = w.getR();
//		    x = Worksheets("Transformers").Cells(K + 1, 17)
			float x = w.getX();
//		    ratio = 1#
			/* TODO:  correct integration should yield tap ratio */
			float ratio = 1;

//		    ZIJ.Re = R
//		    ZIJ.Im = x
			zij = new Complex(r,x);
// TODO:  does not seem to handle a fault on a transformer
//		    I = IMAT(pbus)
			int i = nodeid.get(pbus);
//		    J = IMAT(qbus)
			int j = nodeid.get(qbus);
//		        
//      '   YIJ=(1.0,0.0)/ZIJ
//		      
//		    Call Invert(ZIJ, YIJ)
			yij = zij.inv();
//		        
//		    YII.Re = YIJ.Re * (1# / ratio - 1#) / ratio
//		    YII.Im = YIJ.Im * (1# / ratio - 1#) / ratio
			yii = yij.mult((1F/ratio - 1F)/ratio);
//		              
//		    YJJ.Re = YIJ.Re * (1# - 1# / ratio)
//		    YJJ.Im = YIJ.Im * (1# - 1# / ratio)
			yjj = yij.mult(1F-1F/ratio);
//		        
//		    YIJ.Re = YIJ.Re / ratio
//		    YIJ.Im = YIJ.Im / ratio
			yij = yij.div(ratio);
//		        
//		    Y(I, I).Re = Y(I, I).Re + YII.Re + YIJ.Re
//		    Y(I, I).Im = Y(I, I).Im + YII.Im + YIJ.Im
			y[i].add(i, yii.add(yij));
//		        
//		    Y(J, J).Re = Y(J, J).Re + YJJ.Re + YIJ.Re
//		    Y(J, J).Im = Y(J, J).Im + YJJ.Im + YIJ.Im
			y[j].add(j, yjj.add(yij));
//		        
//		    Y(I, J).Re = Y(I, J).Re - YIJ.Re
//		    Y(I, J).Im = Y(I, J).Im - YIJ.Im
			y[i].sub(j, yij);
//		        
//		    Y(J, I).Re = Y(J, I).Re - YIJ.Re
//		    Y(J, I).Im = Y(J, I).Im - YIJ.Im
			y[j].sub(i, yij);
//		    
//		Next K
		}
//
//
//
//		If NFAULT <> 0 Then
		if (nfault != null && !nfault.isEmpty())
		{
//		      
//		'   ZERO ROW AND COLUMN AT THE FAULTED BUS

//		    For I = 1 To nbus
			for (int i=0; i < nbus; ++i)
			{
//		        IMATF = IMAT(NFAULT)
				int imatf = nodeid.get(nfault);
//		          
//		        Y(I, IMATF).Re = 0
//		        Y(I, IMATF).Im = 0
				y[i].set(imatf, Complex.Zero);
//		            
//		        Y(IMATF, I).Re = 0
//		        Y(IMATF, I).Im = 0
				y[imatf].set(i, Complex.Zero);
				
//		     Next I
			}
//
//		End If
		}
//
//	  ' PERFORM KRON ELIMINATION ON LOAD BUSES.
//
//		For m = 1 To nbus
		int nfaultidx = (nfault == null || nfault.isEmpty()) ? -1 : nodeid.get(nfault);
		
		for(int m=0; m < nbus; ++m)
		{
//		    If m = NFAULT Then GoTo 60
			if (m == nfaultidx) continue;
//		    If BTYPE(m) = 1 Or BTYPE(m) = 2 Then GoTo 60
			if (btype[m] == 1 || btype[m] == 2) continue;
//		    If Y(m, m).Re = 0# And Y(m, m).Im = 0 Then GoTo 60
			if (y[m].get(m).equals(Complex.Zero)) continue;
//		        
//		    BTYPE(m) = -1
			btype[m] = -1;
//		      
//		    For I = 1 To nbus
			for(int i=0; i < nbus; ++i)
			{
//		      ' ONLY PROCESS ROWS AND COLUMNS WHICH HAVE NOT BEEN ELIMINATED.
//		        If BTYPE(I) = -1 Then GoTo 50
				if (btype[i] == -1) continue;
//		        
//		        For J = 1 To nbus
				for(int j=0; j < nbus; ++j)
				{
//		                If BTYPE(J) <> -1 Then
					if (btype[j] != -1)
					{
//		          '     Y(I, J) = Y(I, J) - Y(I, M) * Y(M, J) / Y(M, M)
//		                Call Mult(Y(I, m), Y(m, J), PROD1)
						prod1 = y[i].get(m).mult(y[m].get(j));
//		                Call Invert(Y(m, m), YINV)
						yinv = y[m].get(m).inv();
//		                Call Mult(PROD1, YINV, PROD2)
						prod2 = prod1.mult(yinv);
//		                   
//		                Y(I, J).Re = Y(I, J).Re - PROD2.Re
//		                Y(I, J).Im = Y(I, J).Im - PROD2.Im
						y[i].sub(j, prod2);
//		                
//		                End If
					}
//		              
//		            Next J
				}
//
//		50      Next I
			}
			
//60    Next m
		}
//
//
//	'   CALCULATE GENERATOR POWERS TO CHECK REDUCTION
//		For I = 1 To NGEN
		for(int i=0; i < ngen; ++i)
		{
//		    CURR.Re = 0
//		    CURR.Im = 0
			curr = Complex.Zero;
//		        
//		    For J = 1 To NGEN
			for(int j=0; j < ngen; ++j)
			{
//        '     CURR = CURR + Y(GBUS(I), GBUS(J)) * E(GBUS(J))

//		        YIJ.Re = Y(GBUS(I), GBUS(J)).Re
//		        YIJ.Im = Y(GBUS(I), GBUS(J)).Im
				yij = y[gbus[i]].get(gbus[j]);
//		       
//		        Call Mult(YIJ, E(GBUS(J)), PROD1)
				prod1 = yij.mult(e.get(gbus[j]));
//		        CURR.Re = CURR.Re + PROD1.Re
//		        CURR.Im = CURR.Im + PROD1.Im
				curr = curr.add(prod1);
//		    Next J
			}
//		        
//	      ' S = E(GBUS(I))*CONJG(CURR)
//		        
//			Call Conj(CURR, CURR1)
			curr1 = curr.conjg();

//		    Call Mult(E(GBUS(I)), CURR1, S)
			s = e.get(gbus[i]).mult(curr1);
//		        
//		    pga = S.Re
			float pga = s.re();
//		    qga = S.Im
			float qga = s.im();
//		        
//		    Worksheets("Terminals").Cells(I + 1, 11) = I
//		    pbus = Worksheets("Buses").Cells(GBUS(I) + 1, 1)
//		        
//		    Worksheets("Terminals").Cells(I + 1, 12) = pbus
//		    Worksheets("Terminals").Cells(I + 1, 13) = pga * 100
//		    Worksheets("Terminals").Cells(I + 1, 14) = qga * 100
			
//		Next I
		}
		
		
		/*
		 * TODO: This loop doesn't work correctly if for some reason Tango has
		 * more than one unit on a bus. Clean this up.
		 */
		//		For I = 1 To NGEN
		for(int i=0; i < ngen; ++i)
		{
			Node gi = nodelist.get(gbus[i]);
			int gindx = _cb.genndmap.get(gi.getID());
//		    For J = 1 To NGEN
			for(int j=0; j < ngen; ++j)
			{
//		        YG(I, J).Re = Y(GBUS(I), GBUS(J)).Re
//		        YG(I, J).Im = Y(GBUS(I), GBUS(J)).Im
				Node gj = nodelist.get(gbus[j]);
				int gjndx = _cb.genndmap.get(gj.getID());
				_cb.y[gindx].set(gjndx, y[gbus[i]].get(gbus[j]));

//		            Worksheets("Matrix").Cells(I, J) = YG(I, J).Re
//		            Worksheets("Matrix").Cells(I + 20, J) = YG(I, J).Im
//		            
//		    Next J
			}
//		Next I
		}
//		    
//
//		End Sub
	}

}
