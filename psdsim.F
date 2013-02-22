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
     1XQ(10),XQ1(10),TD1(10),TQ1(10),DAMP(10),C1(10),C2(10)
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
      DO 10 I=1,10
      DO 10 J=1,16
      PLUG(I,J)=0.0
      OUT(I,J)=0.0
      SAVE(I,J)=0.0
10    CONTINUE
      DO 12 I=1,10
      DO 12 J=1,20
12    PRTVAR(I,J)=0.0
      WRITE(6,990)
990   FORMAT('1',T39,'POWER  SYSTEM  RESOURCE  GROUP'//
     1T40,'UNIVERSITY  OF  SASKATCHEWAN'//
     2T32,'POWER  SYSTEM  DYNAMIC  SIMULATION  PROGRAM'//)
      READ(5,1000) NGEN,TSTEP,TPRINT
1000  FORMAT(I5,5X,2F10.4)
      WRITE(6,1005) NGEN,TSTEP,TPRINT
1005  FORMAT('0NO. OF GENERATORS',T20,I5/' TIME STEP',T20,F6.3/
     1' PRINT INTERVAL',T20,F6.3)
      WRITE(6,1008)
1008  FORMAT('0GENERATOR PARAMETERS')
C  READ GENERATOR PARAMETERS.
      DO 20 I=1,NGEN
      READ(5,1010)           PBASE(I),H(I),R(I),XL(I),XD(I),
     1XD1(I),XQ(I),XQ1(I),TD1(I),TQ1(I),DAMP(I),C1(I),C2(I)
1010  FORMAT(8F10.4)
      WRITE(6,1015) I,       PBASE(I),H(I),R(I),XL(I),XD(I),
     1XD1(I),XQ(I),XQ1(I),TD1(I),TQ1(I),DAMP(I),C1(I),C2(I)
1015  FORMAT(1X,I5,8G12.4/6X,8G12.4)
C  CONVERT DATA TO 100 MW BASE.
      C=100.0/PBASE(I)
      H(I)=H(I)/C
      R(I)=R(I)*C
      XL(I)=XL(I)*C
      XD(I)=XD(I)*C
      XD1(I)=XD1(I)*C
      XQ(I)=XQ(I)*C
      XQ1(I)=XQ1(I)*C
      DAMP(I)=DAMP(I)/C
20    CONTINUE
      STOP
      END

