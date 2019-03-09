# CpuSimulatorProgrammingTest

This is a simple 8-bit CPU. It contains two registers A and B and each is 8 bits long. A register is a simple container that holds a numerical value. There’s also a memory of size 256 (i.e. can hold a maximum of 256 values). Memory indexing is 0-based and each element is also 8 bits wide.

Example Instructions:

SET A,9<br/>
SET B,46<br/>
ADD A,B<br/>
WRITE A,20<br/>
SET B,90<br/>
ADD B,A<br/>
WRITE B,120<br/>
READ B,20<br/>
SUBT B,A<br/>
LSHIFT A,3<br/>
READ A,120<br/>
SUBT A,B<br/>
ADD A,25<br/>
ADD B,A<br/>
ADD B,A<br/>
ADD A,A<br/>
WRITE A,255<br/>
ADD B,89<br/>
READ A,120<br/>
ADD A,B<br/>
