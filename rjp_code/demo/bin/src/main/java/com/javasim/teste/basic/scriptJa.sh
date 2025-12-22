#!/bin/bash
for i in $(seq 1 20); do
	/usr/bin/env /usr/lib/jvm/java-11-openjdk-amd64/bin/java @/tmp/cp_32xswpf7um3ekgte6x6w66r1p.argfile com.javasim.teste.basic.Main $i >> saidaJava.txt
done
