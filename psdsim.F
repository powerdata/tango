C  ******************************************************************
C  *                                                                *
C  *             POWER SYSTEM DYNAMI SIMULATION PROGRAM             *
C  *                                                                *
C  ******************************************************************
C
C  MAINLINE ROUTINE.
      COMPLEX VT,CT,Y,YFICT
      COMPLEX CMPLX,CONJG
      COMMON /BLOCK1/ TIME,TSTEP
      COMMON /BLOCK2/ PBASE(10),H(10),R(10),XL(10),XD(10),XD1(10),
     1XQ(10),XQ1(10),TD1(10),TO1(10),DAMP(10),C1(10),C2(10)
      COMMON /BLOCK3/ AVRPRN(10,16)
      COMMON /BLOCK4/ TURPRM(10,16)
      COMMON /BLOCK5/ VT(10),CT(10),EF(10),PM(10)
      COMMON /BLOCK6/ PLUG(10,16),OUT(10,16),SAVE(10,16)
      COMMON /BLOCK7/ Y(11,11)
      COMMON /BLOCK8/ TYM(200),VAR(200,6),NT,NVAR
      COMMON /BLOCK9/ PRTVAR(10,20)
      NT=0
      TIME=0.0
      TFIN=0.0
      NSTEP=0
      NPRINT=0
C  CLEAR INTEGRATOR ARRAYS

      WRITE(6,990)
990   FORMAT('1',T39,'POWER  SYSTEM  RESOURCE  GROUP'//
     1T40,'UNIVERSITY  OF  SASKATCHEWAN'//
     2T32,'POWER  SYSTEM  DYNAMIC  SIMULATION  PROGRAM'//)
      STOP
      END

