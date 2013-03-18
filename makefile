all: PTG.class
	jar cmf META-INF/MANIFEST.MF PTG.jar *.class
	rm *.class
	pdflatex doc/UserManual.tex
	pdflatex doc/UserManual.tex
	mv UserManual.pdf doc/UserManual.pdf
	rm UserManual.*
    
PTG.class:
	javac src/*.java
	mv src/*.class .

clean:
	rm *.class