C  ********************************************************************
C  *  ROTATING EXCITATION SYSTEM - IEEE TYPE 1 MODEL                  *
C  ********************************************************************
      SUBROUTINE AVR1(I)
      INCLUDE 'common.fin'
      REAL KA,KE,KF,VREF(10),SE(10)
C  ENTER HERE FOR EACH INTEGRATION STEP.
C  DEFINE INTEGRATOR OUTPUTS.
      X5=OUT(I,6)
      EF(I)=OUT(I,5)
      X3=OUT(I,7)
C  CALCULATE INTERMEDIATE VARIABLES
      X1=VREF(I)-CABS(VT(I))
      X2=KF(I)/TF(I)*EF(I)-X3
      X4=X1-X2
      X6=X5
      IF(X6 .LT. VRMIN(I)) X6=VRMIN(I)
      IF(X6 .GT. VRMAX(I)) X6=VRMAX(I)
      X7=SE(I)*EF(I)
      X8=X6-X7
C  DEFINE INTEGRATOR INPUTS.
      PLUG(I,5)=X8/TE(I)-KE(I)/TE(I)*EF(I)
      PLUG(I,6)=KA(I)/TA(I)*X4-X5/TA(I)
      PLUG(I,7)=X2/TF(I)
      RETURN
C  ENTER HERE TO CALCULATE INITIAL CONDITIONS.
      ENTRY AVR1IC(I)
      SE(I)=C1(I)*EXP(C2(I)*EF(I))
      VREF(I)=CABS(VT(I))+(KE(I)+SE(I))*EF(I)/KA(I)
      OUT(I,5)=EF(I)
      OUT(I,6)=EF(I)*(KE(I)+SE(I))
      OUT(I,7)=EF(I)*KF(I)/TF(I)
      IF(OUT(I,6) .LT. VRMIN(I)) WRITE(6,1020) I
      IF(OUT(I,6) .GT. VRMAX(I)) WRITE(6,1020)
1020  FORMAT('0**** AVR VOLTAGE LIMIT IS EXCEEDED BY INITIAL FIELD ON',
     1' UNIT',I3/)
      RETURN
      END

