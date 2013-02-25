
FC = gfortran
CC = $(FC)
FFLAGS = -g -fbounds-check
#FFLAGS = -O3

#
#	Source
#
INCSRC = cblocks.inc
LIBSRC = avr1.f gen1.f int.f matrix.f nwsol.f output.f plot.f
EXESRC = psdsim.f

LIBOBJ = $(LIBSRC:%.f=%.o)
EXEOBJ = $(EXESRC:%f=%.o)
OBJ = $(EXEOBJ) $(LIBOBJ)
PROGRAMS = $(EXESRC:%.f=%)

all: $(LIBOBJ) $(PROGRAMS)

psdsim: $(LIBOBJ)

$(OBJ) : $(INCSRC)

.PHONY : test
test: psdsim
	psdsim < examplestudy.dat

.PHONY : clean
clean:
	rm -f $(PROGRAMS) *.o

