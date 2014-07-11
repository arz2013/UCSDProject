export M2_HOME=/Users/rogertan/TritonFitLife/apache-maven-3.2.1
export M2=$M2_HOME/bin
export MAVEN_OPTS="-server -d64 -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+AggressiveOpts -XX:+UseFastAccessorMethods -XX:+UseBiasedLocking -XX:NewRatio=2 -Xms8G -Xmx8G -XX:-ReduceInitialCardMarks"
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0.jdk/Contents/Home
export PATH=$PATH:$M2:$JAVA_HOME
