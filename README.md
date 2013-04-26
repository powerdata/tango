<p><img src=http://incsys.com/images/IncSys_Logo_web.png></p>
The program has been developed as a special purpose program for allowing detailed study of the effects of generators, excitation systems and turbine-governors on power system transient and dynamic stability.

## Organization

### fortran

Original Fortran source code.  Model, event, and control inputs are processed through standard input.
A file containing these parameters for the 2-bus test example study can be found in examplestudy.out.
The file, examplestudy.out, contains the results.

To build the executable:

    make

To run the 2-generator example study:

    make test

### java

Java-language line-by-line translation of the fortran source code into Java, used to be able to have
a solid baseline to test a later fully-featured java implementation.

The original version corresponding to the fortran code is tagged as
"initial-java" in the Github project.  

The current version has been modified to allow additional models using the PowerSimulator Model and
Case formats.  At this time, a single-bus study model is required (no switching).

To build the code:

    make

usage: 

    java -cp tango.jar  --csvdir model_csv_files --control control_properties
    --event event_csv_file --resultdir directory_for_unit_results [ --output file_name] [ --help ]

Two examples are provided in the Makefile.  To execute the routine against the 2-generator test model,
and place results in /tmp/2gen:

    make 2gen

Additionally, to execute the PALCO model and place results in /tmp/palco:

    make palco

### examples

* 2gen

    * model contains the 2-generator model as described in the Tango report.

    * results contains the expected transient stability results for both units.

    * benchmarck contains comparisons of results from the fortran, java,
and benchmarks used in original paper.

* palco

    * model contains the palco 6am basecase 

    * results contains the expected transient stability results for each unit.

See the java and fortran sections to locally generate the examples.


## More Information

Please check out [IncSys](http://incsys.com/smartgrid.htm) for more information.

## Contact

Please contact [Marck Robinson](mailto:marck@powerdata.com) for support.

