*version 9.1 571094664
u 37
R? 6
V? 2
D? 2
? 3
@libraries
@analysis
.TRAN 0 0 0 0
+0 0ns
+1 1s
+3 0.01s
.OP 1 
.LIB C:\Users\riroc\OneDrive\Ambiente de Trabalho\ISEC\1ºANO\1ºSEMESTRE\Eletrónica\Softwares e Simuladores\Schematic1.lib
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
pageloc 1 0 2354 
@status
n 0 122:10:10:16:10:48;1668096648 e 
s 2832 122:10:10:16:10:51;1668096651 e 
*page 1 0 970 720 iA
@ports
@parts
part 8 D1N4002 260 170 u
a 0 sp 11 0 17 29 hln 100 PART=D1N4002
a 0 s 0:13 0 0 0 hln 100 PKGTYPE=DO-41
a 0 s 0:13 0 0 0 hln 100 GATE=
a 0 a 0:13 0 0 0 hln 100 PKGREF=D1
a 0 ap 9 0 17 4 hln 100 REFDES=D1
a 0 sp 0 0 0 0 hln 100 MODEL=D1N4002-X
part 7 VSIN 180 190 h
a 1 u 0 0 0 0 hcn 100 DC=0
a 1 u 0 0 0 0 hcn 100 AC=0
a 1 u 0 0 0 0 hcn 100 VOFF=0
a 1 u 0 0 0 0 hcn 100 VAMPL=20
a 1 u 0 0 0 0 hcn 100 FREQ=50
a 0 a 0:13 0 0 0 hln 100 PKGREF=V1
a 1 ap 9 0 20 10 hcn 100 REFDES=V1
part 2 r 230 250 h
a 0 sp 0 0 0 10 hlb 100 PART=r
a 0 s 0:13 0 0 0 hln 100 PKGTYPE=RC05
a 0 s 0:13 0 0 0 hln 100 GATE=
a 0 a 0:13 0 0 0 hln 100 PKGREF=R1
a 0 ap 9 0 15 0 hln 100 REFDES=R1
a 0 u 13 0 15 25 hln 100 VALUE=2k
part 3 r 310 230 v
a 0 sp 0 0 0 10 hlb 100 PART=r
a 0 s 0:13 0 0 0 hln 100 PKGTYPE=RC05
a 0 s 0:13 0 0 0 hln 100 GATE=
a 0 a 0:13 0 0 0 hln 100 PKGREF=R2
a 0 ap 9 0 15 0 hln 100 REFDES=R2
a 0 u 13 0 15 25 hln 100 VALUE=0.5k
part 1 titleblk 970 720 h
a 1 s 13 0 350 10 hcn 100 PAGESIZE=A
a 1 s 13 0 180 60 hcn 100 PAGETITLE=
a 1 s 13 0 300 95 hrn 100 PAGENO=1
a 1 s 13 0 340 95 hrn 100 PAGECOUNT=1
part 31 vdiffMarker 310 180 h
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 a 0 0 6 20 hlb 100 LABEL=1
part 33 vdiffMarker 310 240 h
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 s 13 0 16 14 hlb 100 NODE=-
a 0 a 0 0 6 20 hlb 100 LABEL=1
part 35 vdiffMarker 180 190 h
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 a 0 0 6 20 hlb 100 LABEL=2
part 36 vdiffMarker 180 230 h
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 s 13 0 16 14 hlb 100 NODE=-
a 0 a 0 0 6 20 hlb 100 LABEL=2
@conn
w 14
s 260 170 310 170 13
s 310 170 310 180 15
s 310 180 310 190 32
w 18
s 310 230 310 240 17
s 270 250 310 250 29
s 310 240 310 250 34
w 10
s 180 190 180 170 9
s 180 170 230 170 11
w 25
s 230 250 180 250 24
s 180 250 180 230 26
@junction
j 180 190
+ p 7 +
+ w 10
j 230 170
+ p 8 2
+ w 10
j 260 170
+ p 8 1
+ w 14
j 310 190
+ p 3 2
+ w 14
j 180 230
+ p 7 -
+ w 25
j 270 250
+ p 2 2
+ w 18
j 230 250
+ p 2 1
+ w 25
j 310 230
+ p 3 1
+ w 18
j 310 180
+ p 31 pin1
+ w 14
j 310 240
+ p 33 pin1
+ w 18
j 180 190
+ p 35 pin1
+ p 7 +
j 180 190
+ p 35 pin1
+ w 10
j 180 230
+ p 36 pin1
+ p 7 -
j 180 230
+ p 36 pin1
+ w 25
@attributes
a 0 s 0:13 0 0 0 hln 100 PAGETITLE=
a 0 s 0:13 0 0 0 hln 100 PAGENO=1
a 0 s 0:13 0 0 0 hln 100 PAGESIZE=A
a 0 s 0:13 0 0 0 hln 100 PAGECOUNT=1
@graphics
