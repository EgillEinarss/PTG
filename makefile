all: PTG.class
	jar cmf META-INF/MANIFEST.MF PTG.jar *.class
	rm *.class
	cp doc/UserManual.tex UserManual.tex
	cp doc/preface.tex preface.tex
	pdflatex UserManual.tex
	pdflatex UserManual.tex
	#runs twice to generate the correct toc
	mv UserManual.pdf doc/UserManual.pdf
	rm preface.tex
	rm UserManual.*
    
PTG.class:
	javac src/*.java
	mv src/*.class .

clean:
	rm *.class