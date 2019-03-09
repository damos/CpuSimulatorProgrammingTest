# CpuSimulatorProgrammingTest

This is a simple 8-bit CPU. It contains two registers A and B and each is 8 bits long. A register is a simple container that holds a numerical value. There’s also a memory of size 256 (i.e. can hold a maximum of 256 values). Memory indexing is 0-based and each element is also 8 bits wide.

Example Instructions:

SET A,9<br/>
SET B,46<br/>
ADD A,B
WRITE A,20
SET B,90
ADD B,A
WRITE B,120
READ B,20
SUBT B,A
LSHIFT A,3
READ A,120
SUBT A,B
ADD A,25
ADD B,A
ADD B,A
ADD A,A
WRITE A,255
ADD B,89
READ A,120
ADD A,B
