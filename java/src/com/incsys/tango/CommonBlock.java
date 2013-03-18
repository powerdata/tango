package com.incsys.tango;

public class CommonBlock
{
	/* Block 1 */
	public float time;
	public float tstep;
	
	/* Block 2 */
	public float[] pbase = new float[10];
	public float[] h = new float[10];
	public float[] r = new float[10];
	public float[] xl = new float[10];
	public float[] xd = new float[10];
	public float[] xd1 = new float[10];
	public float[] xq = new float[10];
	public float[] xq1 = new float[10];
	public float[] td1 = new float[10];
	public float[] tq1 = new float[10];
	public float[] damp = new float[10];
	public float[] c1 = new float[10];
	public float[] c2 = new float[10];
	
	/* Block 3 */
	public float[][] avrprm = new float[16][10];
	public float[] ka = avrprm[0];
	public float[] ke = avrprm[1];
	public float[] kf = avrprm[2];
	public float[] ta = avrprm[3];
	public float[] te = avrprm[4];
	public float[] tf = avrprm[5];
	public float[] vrmin = avrprm[6];
	public float[] vrmax = avrprm[7];
	public float[] ac1 = avrprm[8];
	public float[] ac2 = avrprm[9];
	
	/* Block 4 */
	public float[][] turprm = new float[16][10];

	/* Block 5 */
	public ComplexList vt = new ComplexList(10);
	public ComplexList ct = new ComplexList(10);
	public float[] ef = new float[10];
	public float[] pm = new float[10];
	
	/* Block 6 */
	public float[][] plug = new float[10][16];
	public float[][] out = new float[10][16];
	public float[][] save = new float[10][16];
	
	/* Block 7 */
	public ComplexList[] y;
	
	/* Block 8 */
	public float[] tym = new float[200];
	public float[][] var = new float[200][6];
	public int nt, nvar;
	
	/* Block 9 */
	public float[][] prtvar = new float[10][20];
	
	public CommonBlock()
	{
		y = new ComplexList[11];
		for (int i=0; i < 11; ++i)
		{
			y[i] = new ComplexList(11);
		}
	}
}
