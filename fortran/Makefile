
FC = gfortran
CC = $(FC)
FFLAGS = -g -fbounds-check
#FFLAGS = -O3

#
#	Source
#
INCSRC = cblocks.fpp
LIBSRC = avr1.f gen1.f int.f matrix.f nwsol.f output.f plot.f
EXESRC = tango.f

LIBOBJ = $(LIBSRC:%.f=%.o)
EXEOBJ = $(EXESRC:%f=%.o)
OBJ = $(EXEOBJ) $(LIBOBJ)
PROGRAMS = $(EXESRC:%.f=%)

all: $(LIBOBJ) $(PROGRAMS)

tango: $(LIBOBJ)

$(OBJ) : $(INCSRC)

.PHONY : test
test: tango
	./tango < examplestudy.dat

.PHONY : clean
clean:
	rm -f $(PROGRAMS) *.o

