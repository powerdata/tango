
FC = gfortran
FFLAGS = -g -fbounds-check
#FFLAGS = -O3

PROGRAMS = psdsim

all: $(PROGRAMS)

psdsim: avr1.o output.o

clean:
	rm -f $(PROGRAMS) *.o

