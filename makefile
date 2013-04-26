all: PTG.class
	jar cmf META-INF/MANIFEST.MF PTG.jar *.class
	rm *.class
	#mv doc/UserManual.tex UserManual.tex
	#pdflatex UserManual.tex
	#pdflatex UserManual.tex
	#runs twice to generate the correct toc
	#mv UserManual.tex doc/UserManual.tex
	#mv UserManual.pdf doc/UserManual.pdf
	#rm UserManual.*
    
PTG.class:
	javac src/*.java
	mv src/*.class .

clean:
	rm *.class