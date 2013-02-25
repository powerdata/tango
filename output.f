C  ********************************************************************
C  *  SUBROUTINE TO PRINTOUT SYSTEM VARIABLES                         *
C  ********************************************************************
      SUBROUTINE OUTPUT(NGEN)
      IMPLICIT NONE
      INCLUDE 'cblocks.inc'
      INTEGER NGEN,I,J
      IF(TIME .EQ. 0.0) WRITE(6,2000)
2000  FORMAT('1'//T42,'SIMULATED RESPONSES',//T4,'TIME  GEN  ROTOR   ',
     1'ROTOR    EQ''     ED''    TERM     ELEC-POWER     FIELD   ',
     2'MECH    SATN   AIR GAP'/T4,'SECS   NO  ANGLE   SPEED    ',
     3'VOLTS   VOLTS  VOLTS    REAL   IMAG    VOLTS   POWER  ',
     4'FACTOR   VOLTS'/)
      WRITE(6,1010) TIME
1010  FORMAT(' ',F7.3)
      DO 10 I=1,NGEN
      WRITE(6,1000) I,(PRTVAR(I,J),J=1,11)
1000  FORMAT(' ',8X,I3,F8.3,13F8.4)
10    CONTINUE
C  SET UP THE VARIABLES TO BE PLOTTED.
      NT=NT+1
      TYM(NT)=TIME
      VAR(NT,1)=PRTVAR(1,1)
      VAR(NT,2)=PRTVAR(1,5)
      VAR(NT,3)=PRTVAR(1,6)
      VAR(NT,4)=PRTVAR(1,8)
      RETURN
      END

