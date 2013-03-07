all: PTG.class
	jar cmf META-INF/MANIFEST.MF PTG.jar *.class
	rm *.class
    
PTG.class:
	javac src/*.java
	mv src/*.class .

clean:
	rm *.class