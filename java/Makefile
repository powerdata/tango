JARFILE = tango.jar

.default: $(JARFILE)

.phony: 2gen palco

JAVA = java
JAVAC = javac
JAR = jar
EX2GEN = ../examples/2gen
EXPAL = ../examples/palco

JDEP = $(shell find src -name "*.java" -print)

$(JARFILE): $(JDEP)
	rm -rf bin
	mkdir bin
	rm -f $@
	$(JAVAC) -sourcepath src -d bin src/com/incsys/tango/Tango.java
	$(JAR) cfe $@ com.incsys.tango.Tango -C bin .

clean:
	rm -f $(JARFILE)
	rm -rf bin
	rm -rf /tmp/2gen

2gen: $(JARFILE)
	$(JAVA) -jar tango.jar  --csvdir $(EX2GEN)/model --control $(EX2GEN)/model/control.prop \
		--event $(EX2GEN)/model/events.csv --resultdir /tmp/2gen

