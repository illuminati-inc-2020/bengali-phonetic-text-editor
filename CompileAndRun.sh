clear
echo "compiling..."
cd src/
javac -d .. Bangla/Bangla.java
cd ..
echo "creating jar file..."
jar -cfm Bangla.jar manifest.mf Bangla resources src
rm -rf class/*
mv Bangla class
echo "running..."
java -jar Bangla.jar $*
