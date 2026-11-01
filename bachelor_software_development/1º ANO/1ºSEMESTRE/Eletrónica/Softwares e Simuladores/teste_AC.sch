*version 9.1 4222952367
u 49
V? 3
R? 4
I? 2
D? 2
? 3
@libraries
@analysis
.TRAN 1 0 0 0
+0 0ns
+1 100ms
+3 10us
.OP 0 
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
pageloc 1 0 2141 
@status
n 0 122:10:10:07:50:52;1668066652 e 
s 2832 122:10:10:07:50:55;1668066655 e 
*page 1 0 970 720 iA
@ports
port 30 GND_ANALOG 250 210 h
@parts
part 4 r 300 190 v
a 0 sp 0 0 0 10 hlb 100 PART=r
a 0 s 0:13 0 0 0 hln 100 PKGTYPE=RC05
a 0 s 0:13 0 0 0 hln 100 GATE=
a 0 a 0:13 0 0 0 hln 100 PKGREF=R2
a 0 ap 9 0 15 0 hln 100 REFDES=R2
part 37 D1N4002 210 110 h
a 0 sp 11 0 17 29 hln 100 PART=D1N4002
a 0 s 0:13 0 0 0 hln 100 PKGTYPE=DO-41
a 0 s 0:13 0 0 0 hln 100 GATE=
a 0 a 0:13 0 0 0 hln 100 PKGREF=D1
a 0 ap 9 0 17 4 hln 100 REFDES=D1
part 32 VSIN 150 150 h
a 0 a 0:13 0 0 0 hln 100 PKGREF=V2
a 1 ap 9 0 20 10 hcn 100 REFDES=V2
a 1 u 0 0 0 0 hcn 100 DC=0
a 1 u 0 0 0 0 hcn 100 AC=0
a 1 u 0 0 0 0 hcn 100 VOFF=0
a 1 u 13 27 -40 15 hcn 100 VAMPL=12
a 1 u 13 13 -45 25 hcn 100 FREQ=50
part 1 titleblk 970 720 h
a 1 s 13 0 350 10 hcn 100 PAGESIZE=A
a 1 s 13 0 180 60 hcn 100 PAGETITLE=
a 1 s 13 0 300 95 hrn 100 PAGENO=1
a 1 s 13 0 340 95 hrn 100 PAGECOUNT=1
part 41 vdiffMarker 300 140 h
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 a 0 0 6 20 hlb 100 LABEL=1
part 43 vdiffMarker 300 200 h
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 s 13 0 16 14 hlb 100 NODE=-
a 0 a 0 0 6 20 hlb 100 LABEL=1
part 45 vdiffMarker 150 140 h
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 a 0 0 6 20 hlb 100 LABEL=2
part 47 vdiffMarker 150 200 h
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 s 13 0 16 14 hlb 100 NODE=-
a 0 a 0 0 6 20 hlb 100 LABEL=2
@conn
w 34
a 0 up 0:33 0 0 0 hln 100 V=
s 300 150 300 140 17
s 240 110 300 110 39
s 300 140 300 110 42
w 40
a 0 up 0:33 0 0 0 hln 100 V=
s 150 110 210 110 35
s 150 150 150 140 33
s 150 140 150 110 46
w 21
a 0 up 0:33 0 0 0 hln 100 V=
s 150 190 150 200 20
s 150 210 250 210 22
s 300 210 300 200 24
s 250 210 300 210 31
s 300 200 300 190 44
s 150 200 150 210 48
@junction
j 300 190
+ p 4 1
+ w 21
j 250 210
+ s 30
+ w 21
j 150 190
+ p 32 -
+ w 21
j 150 150
+ p 32 +
+ w 40
j 300 150
+ p 4 2
+ w 34
j 210 110
+ p 37 1
+ w 40
j 240 110
+ p 37 2
+ w 34
j 300 140
+ p 41 pin1
+ w 34
j 300 200
+ p 43 pin1
+ w 21
j 150 140
+ p 45 pin1
+ w 40
j 150 200
+ p 47 pin1
+ w 21
@attributes
a 0 s 0:13 0 0 0 hln 100 PAGETITLE=
a 0 s 0:13 0 0 0 hln 100 PAGENO=1
a 0 s 0:13 0 0 0 hln 100 PAGESIZE=A
a 0 s 0:13 0 0 0 hln 100 PAGECOUNT=1
@graphics
