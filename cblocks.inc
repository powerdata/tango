C  ********************************************************************
C
C  Copyright (c) 2013 IncSys (http://incsys.com)
C
C  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRENTY OF ANY KIND,
C  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRENTIES
C  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
C  NONINFRENGEMENT.  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
C  BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
C  ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
C  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
C  SOFTWARE.
C
C  ********************************************************************
C  ********************************************************************
C  * PARAMETERS
C  ********************************************************************
      REAL PI
      PARAMETER (PI = 3.14159)
C  ********************************************************************
C  * COMMON BLOCKS
C  ********************************************************************
      SAVE
C  BLOCK 1
      COMMON /BLOCK1/ TIME,TSTEP
      REAL TIME,TSTEP
C  BLOCK 2
      COMMON /BLOCK2/ PBASE(10),H(10),R(10),XL(10),XD(10),XD1(10),
     1XQ(10),XQ1(10),TD1(10),TQ1(10),DAMP(10),C1(10),C2(10)
      REAL PBASE,H,R,XL,XD,XD1,XQ,XQ1,TD1,TQ1,DAMP,C1,C2
C  BLOCK 3
      COMMON /BLOCK3/ AVRPRM(10,16)
      REAL AVRPRM
C  BLOCK 4
      COMMON /BLOCK4/ TURPRM(10,16)
      REAL TURPRM
C  BLOCK 5
      COMMON /BLOCK5/ VT(10),CT(10),EF(10),PM(10)
      COMPLEX VT,CT
      REAL EF,PM
C  BLOCK 6
      COMMON /BLOCK6/ PLUG(10,16),OUT(10,16),SAVE(10,16)
      REAL PLUG,OUT,SAVE
C  BLOCK 7
      COMMON /BLOCK7/ Y(11,11)
      COMPLEX Y
C  BLOCK 8
      COMMON /BLOCK8/ TYM(200),VAR(200,6),NT,NVAR
      REAL TYM,VAR
      INTEGER NT,NVAR
C  BLOCK 9
      COMMON /BLOCK9/ PRTVAR(10,20)
      REAL PRTVAR

