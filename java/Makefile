.default: tango.jar

JAVA = java
JAVAC = javac
JAR = jar

JDEP = $(shell find src -name "*.java" -print)

tango.jar: $(JDEP)
	rm -rf bin
	mkdir bin
	rm -f $@
	$(JAVAC) -sourcepath src -d bin src/com/incsys/tango/Tango.java
	$(JAR) cfe $@ com.incsys.tango.Tango -C bin .

clean:
	rm -f tango.jar
	rm -rf bin
