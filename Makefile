
FC = gfortran
FFLAGS = -g -fbounds-check
#FFLAGS = -O3

PROGRAMS = psdsim

all: $(PROGRAMS)

clean:
	rm -f $(PROGRAMS)

