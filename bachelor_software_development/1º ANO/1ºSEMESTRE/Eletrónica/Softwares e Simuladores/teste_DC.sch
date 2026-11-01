*version 9.1 396483540
u 32
V? 2
R? 4
I? 2
@libraries
@analysis
@targets
@attributes
@translators
a 0 u 13 0 0 0 hln 100 PCBOARDS=PCB
a 0 u 13 0 0 0 hln 100 PSPICE=PSPICE
a 0 u 13 0 0 0 hln 100 XILINX=XILINX
@setup
unconnectedPins 0
connectViaLabel 0
connectViaLocalLabels 0
NoStim4ExtIFPortsWarnings 1
AutoGenStim4ExtIFPorts 1
@index
pageloc 1 0 2145 
@status
n 0 122:10:10:07:50:02;1668066602 e 
s 2832 122:10:10:14:58:29;1668092309 e 
*page 1 0 970 720 iA
@ports
port 30 GND_ANALOG 250 210 h
@parts
part 3 r 210 110 h
a 0 sp 0 0 0 10 hlb 100 PART=r
a 0 s 0:13 0 0 0 hln 100 PKGTYPE=RC05
a 0 s 0:13 0 0 0 hln 100 GATE=
a 0 a 0:13 0 0 0 hln 100 PKGREF=R1
a 0 ap 9 0 15 0 hln 100 REFDES=R1
part 5 r 400 110 u
a 0 sp 0 0 0 10 hlb 100 PART=r
a 0 s 0:13 0 0 0 hln 100 PKGTYPE=RC05
a 0 s 0:13 0 0 0 hln 100 GATE=
a 0 a 0:13 0 0 0 hln 100 PKGREF=R3
a 0 ap 9 0 15 0 hln 100 REFDES=R3
part 4 r 300 190 v
a 0 sp 0 0 0 10 hlb 100 PART=r
a 0 s 0:13 0 0 0 hln 100 PKGTYPE=RC05
a 0 s 0:13 0 0 0 hln 100 GATE=
a 0 a 0:13 0 0 0 hln 100 PKGREF=R2
a 0 ap 9 0 15 0 hln 100 REFDES=R2
part 2 VDC 150 150 h
a 0 sp 0 0 22 37 hln 100 PART=VDC
a 0 a 0:13 0 0 0 hln 100 PKGREF=V1
a 1 ap 9 0 24 7 hcn 100 REFDES=V1
a 1 u 13 0 -11 18 hcn 100 DC=1V
part 6 IDC 460 140 h
a 0 sp 11 0 0 50 hln 100 PART=IDC
a 0 a 0:13 0 0 0 hln 100 PKGREF=I1
a 1 ap 9 0 20 10 hcn 100 REFDES=I1
a 1 u 13 0 -9 21 hcn 100 DC=1mA
part 1 titleblk 970 720 h
a 1 s 13 0 350 10 hcn 100 PAGESIZE=A
a 1 s 13 0 180 60 hcn 100 PAGETITLE=
a 1 s 13 0 340 95 hrn 100 PAGECOUNT=1
a 1 s 13 0 300 95 hrn 100 PAGENO=1
@conn
w 8
a 0 up 0:33 0 0 0 hln 100 V=
s 210 110 150 110 7
a 0 up 33 0 180 109 hct 100 V=
s 150 110 150 150 9
w 14
a 0 up 0:33 0 0 0 hln 100 V=
s 400 110 460 110 13
a 0 up 33 0 430 109 hct 100 V=
s 460 110 460 140 15
w 12
a 0 up 0:33 0 0 0 hln 100 V=
s 250 110 300 110 11
s 300 110 360 110 19
a 0 up 33 0 330 109 hct 100 V=
s 300 150 300 110 17
w 21
a 0 up 0:33 0 0 0 hln 100 V=
s 150 190 150 210 20
s 150 210 250 210 22
s 300 210 300 190 24
s 300 210 460 210 26
a 0 up 33 0 380 209 hct 100 V=
s 460 210 460 180 28
s 250 210 300 210 31
@junction
j 150 150
+ p 2 +
+ w 8
j 360 110
+ p 5 2
+ w 12
j 400 110
+ p 5 1
+ w 14
j 300 150
+ p 4 2
+ w 12
j 300 110
+ w 12
+ w 12
j 150 190
+ p 2 -
+ w 21
j 300 190
+ p 4 1
+ w 21
j 300 210
+ w 21
+ w 21
j 250 210
+ s 30
+ w 21
j 460 140
+ p 6 +
+ w 14
j 460 180
+ p 6 -
+ w 21
j 210 110
+ p 3 1
+ w 8
j 250 110
+ p 3 2
+ w 12
@attributes
a 0 s 0:13 0 0 0 hln 100 PAGETITLE=
a 0 s 0:13 0 0 0 hln 100 PAGENO=1
a 0 s 0:13 0 0 0 hln 100 PAGESIZE=A
a 0 s 0:13 0 0 0 hln 100 PAGECOUNT=1
@graphics
