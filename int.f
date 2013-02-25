C  ********************************************************************
C  * SUBROUTINE TO CALCULATE STATE VARIABLES FOR NEXT INCREMENT IN TIME
C  ********************************************************************
      SUBROUTINE INT(NGEN)
      SAVE
      COMMON /BLOCK1/ TIME,TSTEP
      COMMON /BLOCK6/ PLUG(10,16),OUT(10,16),SAVE(10,16)
      IF(TIME .EQ. 0.0) GO TO 20
      DO 10 I=1,NGEN
      DO 10 J=1,16
10    SAVE(I,J)=PLUG(I,J)
      NFLAG=1
20    CONTINUE
      DO 30 I=1,NGEN
      DO 30 J=1,16
      OUT(I,J)=OUT(I,J)+PLUG(I,J)*TSTEP+(PLUG(I,J)-SAVE(I,J))*0.5*TSTEP
30    SAVE(I,J)=PLUG(I,J)
      RETURN
      END

