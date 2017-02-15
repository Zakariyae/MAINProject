JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Graphe.java \

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
