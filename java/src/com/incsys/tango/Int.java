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


public class Int
{
	protected CommonBlock _cb;
	
	public Int(CommonBlock cblk)
	{
		_cb = cblk;
	}
	public void integrate(int ngen)
	{
//      INTEGER I,J,NGEN,NFLAG
		int i, j, nflag;
		
//      IF(TIME .EQ. 0.0) GO TO 20
		if (_cb.time != 0F)
		{
//     		DO 10 I=1,NGEN
			for (i=0; i < ngen; ++i)
			{
//		      	DO 10 J=1,16
				for (j=0; j < CommonBlock.GenMaxAttrib; ++j)
				{
					_cb.save[i][j] = _cb.plug[i][j];
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
			for(j=0; j < CommonBlock.GenMaxAttrib; ++j)
			{
//			    OUT(I,J)=OUT(I,J)+PLUG(I,J)*TSTEP+(PLUG(I,J)-SAVE(I,J))*0.5*TSTEP
				_cb.out[i][j] += _cb.plug[i][j]*_cb.tstep+(_cb.plug[i][j]-_cb.save[i][j])*0.5F*_cb.tstep;
//30			SAVE(I,J)=PLUG(I,J)
				_cb.save[i][j] = _cb.plug[i][j];
			}
		}
	}
}
