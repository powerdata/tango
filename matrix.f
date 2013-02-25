C  ********************************************************************
C  * SUBROUTINE TO CALCULATE EQUIVALENT Y MATRIX FOR INTERNAL
C  * GENERATOR BUSES.
C  ********************************************************************
      SUBROUTINE MATRIX(NGEN)
      IMPLICIT NONE
      INCLUDE 'cblocks.inc'
      COMPLEX YFICT,CMPLX,CONJG
      INTEGER NGEN,I,J,N,M,N1
C  AUGMENT Y MATRIX WITH GENERATOR BUSES AND ELIMINATE THE
C  TERMINAL BUSES.
      N1=NGEN+1
      DO 80 I=1,NGEN
C  MOVE TERMINAL BUS TO OUTSIDE OF MATRIX
      Y(N1,N1)=Y(I,I)
      Y(I,I)=(0.0,0.0)
      DO 75 J=1,NGEN
C  MOVE ROW
      Y(N1,J)=Y(I,J)
      Y(I,J)=(0.0,0.0)
C  MOVE COLUMN
      Y(J,N1)=Y(J,I)
      Y(J,I)=(0.0,0.0)
75    CONTINUE
C  ADD IN GENERATOR BUS
      YFICT=CMPLX(R(I),-(XD1(I)+XQ1(I))/2.0)/(R(I)*R(I)+XD1(I)*XQ1(I))
      Y(I,I)=YFICT
C  CHECK IF TERMINAL BUS IS GROUNDED.
      IF(CABS(Y(N1,N1)) .EQ. 0.0) GO TO 80
      Y(N1,N1)=Y(N1,N1)+YFICT
      Y(I,N1)=-YFICT
      Y(N1,I)=-YFICT
C  ELIMINATE THE TERMINAL BUS.
      DO 76 M=1,NGEN
      DO 76 N=M,NGEN
      Y(M,N)=Y(M,N)-Y(M,N1)*Y(N1,N)/Y(N1,N1)
      Y(N,M)=Y(M,N)
76    CONTINUE
80    CONTINUE
      RETURN
      END

