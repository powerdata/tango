
FC = gfortran
FFLAGS = -g -fbounds-check
#FFLAGS = -O3

PROGRAMS = psdsim

all: $(PROGRAMS)

psdsim: avr1.o gen1.o int.o matrix.o nwsol.o output.o plot.o

clean:
	rm -f $(PROGRAMS) *.o

