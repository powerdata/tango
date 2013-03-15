package com.incsys.tango;

public class Int
{
	protected CommonBlockSet _cblk;
	
	public Int(CommonBlockSet cblk)
	{
		_cblk = cblk;
	}
	public void integrate(int ngen)
	{
		TangoBlock1 b1 = _cblk.getBlock1();
		TangoBlock6 b6 = _cblk.getBlock6();
		float[][] save = b6.save();
		float[][] plug = b6.plug();
		float[][] out = b6.out();
//      INTEGER I,J,NGEN,NFLAG
		int i, j, nflag;
		
//      IF(TIME .EQ. 0.0) GO TO 20
		if (b1.time() != 0F)
		{
//     		DO 10 I=1,NGEN
			for (i=0; i < ngen; ++i)
			{
//		      	DO 10 J=1,16
				for (j=0; j < 16; ++j)
				{
					save[i][j] = plug[i][j];
				}
			}
		}
//	    NFLAG=1
		nflag = 1;
//20    CONTINUE
//      DO 30 I=1,NGEN
		for (i=0; i < ngen; ++i)
		{
//		    DO 30 J=1,16
			for(j=0; j < 16; ++j)
			{
//			    OUT(I,J)=OUT(I,J)+PLUG(I,J)*TSTEP+(PLUG(I,J)-SAVE(I,J))*0.5*TSTEP
				out[i][j] += plug[i][j]*b1.tstep()+(plug[i][j]-save[i][j])*0.5F*b1.tstep();
//30			SAVE(I,J)=PLUG(I,J)
				save[i][j] = plug[i][j];
			}
		}
	}
}
