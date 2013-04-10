package com.incsys.tango;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.powerdata.mdleng.transmission.csvimp.GeneratingUnit;
import com.powerdata.mdleng.transmission.csvimp.Node;
import com.powerdata.mdleng.transmission.csvimp.SynchronousMachine;

public class CommonBlock
{
	public static final int GenMaxAttrib = 16;
	public final int maxGen;
	
	/* Block 1 */
	public float time;
	public float tstep;
	
	/* Block 2 */
	final public float[] pbase;
	final public float[] h;
	final public float[] r;
	final public float[] xl;
	final public float[] xd;
	final public float[] xd1;
	final public float[] xq;
	final public float[] xq1;
	final public float[] td1;
	final public float[] tq1;
	final public float[] damp;
	final public float[] c1;
	final public float[] c2;
	
	/* Block 3 */
	final public float[][] avrprm;
	final public float[] ka;
	final public float[] ke;
	final public float[] kf;
	final public float[] ta;
	final public float[] te;
	final public float[] tf;
	final public float[] vrmin;
	final public float[] vrmax;
	final public float[] ac1;
	final public float[] ac2;
	
	/* Block 5 */
	final public ComplexList vt;
	final public ComplexList ct;
	final public float[] ef;
	final public float[] pm;
	
	/* Block 6 */
	final public float[][] plug;
	final public float[][] out;
	final public float[][] save;
	
	/* Block 7 */
	public ComplexList[] y;
	
	/* Block 8 */
	final public float[] tym = new float[200];
	final public float[][] var = new float[200][6];
	public int nt, nvar;
	
	/* Block 9 */
	final public float[][] prtvar;
	
	/* Common data not present in original algorithm */
	/*
	 * A specific node order was only introduced to make comparison of the
	 * admittance matrices possible between algorithms. Sorting the nodes is
	 * not necessary and should be removed
	 * TODO:  remove the node list once debugging is finished
	 */
	public List<Node> nodelist;

	/*
	 * The algorithm expects a consistent ordering of generators. We need to
	 * store that, and a way to find the order given a specific generator node
	 */

	/** Consistent ordering of generators */
	public List<GeneratingUnit> genlist;
	/** Consistent ordering of synchronous machines (same order as units)*/
	public List<SynchronousMachine> smlist;
	/** lookup unit index by node */
	public Map<String,Integer> genndmap;
	
	public CommonBlock(int maxgen)
	{
		maxGen = maxgen;
		
		// allocate block 2
		pbase = new float[maxGen];
		h = new float[maxGen];
		r = new float[maxGen];
		xl = new float[maxGen];
		xd = new float[maxGen];
		xd1 = new float[maxGen];
		xq = new float[maxGen];
		xq1 = new float[maxGen];
		td1 = new float[maxGen];
		tq1 = new float[maxGen];
		damp = new float[maxGen];
		c1 = new float[maxGen];
		c2 = new float[maxGen];
		
		/* allocate block 3 */
		avrprm = new float[GenMaxAttrib][maxGen];
		ka = avrprm[0];
		ke = avrprm[1];
		kf = avrprm[2];
		ta = avrprm[3];
		te = avrprm[4];
		tf = avrprm[5];
		vrmin = avrprm[6];
		vrmax = avrprm[7];
		ac1 = avrprm[8];
		ac2 = avrprm[9];

		/* allocate block 5 */
		vt = new ComplexList(maxGen);
		ct = new ComplexList(maxGen);
		ef = new float[maxGen];
		pm = new float[maxGen];

		/* allocate block 6 */
		plug = new float[maxGen][GenMaxAttrib];
		out = new float[maxGen][GenMaxAttrib];
		save = new float[maxGen][GenMaxAttrib];

		/* allocate block 7 */
		int nmat = maxgen+1;
		y = new ComplexList[nmat];
		for (int i=0; i < nmat; ++i)
		{
			y[i] = new ComplexList(nmat);
		}

		/* allocate block 9 */
		prtvar = new float[maxGen][20];

	}
}
