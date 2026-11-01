*version 9.1 2132871254
u 40
D? 2
R? 3
V? 3
? 9
@libraries
@analysis
.TRAN 1 0 0 0
+0 0ns
+1 2ms
+3 0.02ms
.OP 0 
.LIB C:\Users\riroc\OneDrive\Ambiente de Trabalho\ISEC\1ºANO\1ºSEMESTRE\Eletrónica\Softwares e Simuladores\meu teste.lib
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
pageloc 1 0 2597 
@status
c 122:10:10:15:18:40;1668093520
n 0 122:10:10:15:18:43;1668093523 e 
s 2832 122:10:10:15:18:47;1668093527 e 
*page 1 0 970 720 iA
@ports
port 39 GND_ANALOG 110 190 h
@parts
part 2 D1N4002 190 100 u
a 0 sp 11 0 17 29 hln 100 PART=D1N4002
a 0 s 0:13 0 0 0 hln 100 PKGTYPE=DO-41
a 0 s 0:13 0 0 0 hln 100 GATE=
a 0 a 0:13 0 0 0 hln 100 PKGREF=D1
a 0 ap 9 0 17 4 hln 100 REFDES=D1
a 0 sp 0 0 0 0 hln 100 MODEL=D1N4002-X
part 5 VSIN 110 130 h
a 0 a 0:13 0 0 0 hln 100 PKGREF=V1
a 1 ap 9 0 20 10 hcn 100 REFDES=V1
a 1 u 0 0 0 0 hcn 100 DC=0
a 1 u 0 0 0 0 hcn 100 AC=0
a 1 u 0 0 0 0 hcn 100 VOFF=0
a 1 u 0 0 0 0 hcn 100 VAMPL=20
a 1 u 0 0 0 0 hcn 100 FREQ=50
part 3 r 150 190 h
a 0 sp 0 0 0 10 hlb 100 PART=r
a 0 s 0:13 0 0 0 hln 100 PKGTYPE=RC05
a 0 s 0:13 0 0 0 hln 100 GATE=
a 0 a 0:13 0 0 0 hln 100 PKGREF=R1
a 0 ap 9 0 15 0 hln 100 REFDES=R1
a 0 u 13 0 15 25 hln 100 VALUE=2K
part 4 r 240 170 v
a 0 sp 0 0 0 10 hlb 100 PART=r
a 0 s 0:13 0 0 0 hln 100 PKGTYPE=RC05
a 0 s 0:13 0 0 0 hln 100 GATE=
a 0 a 0:13 0 0 0 hln 100 PKGREF=R2
a 0 ap 9 0 15 0 hln 100 REFDES=R2
a 0 u 13 0 15 25 hln 100 VALUE=0.5K
part 1 titleblk 970 720 h
a 1 s 13 0 350 10 hcn 100 PAGESIZE=A
a 1 s 13 0 180 60 hcn 100 PAGETITLE=
a 1 s 13 0 340 95 hrn 100 PAGECOUNT=1
a 1 s 13 0 300 95 hrn 100 PAGENO=1
part 28 vdiffMarker 110 110 h
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 a 0 0 6 20 hlb 100 LABEL=5
part 30 vdiffMarker 110 180 h
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 s 13 0 16 14 hlb 100 NODE=-
a 0 a 0 0 6 20 hlb 100 LABEL=5
part 37 vdiffMarker 240 100 h
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 a 0 0 6 20 hlb 100 LABEL=8
part 38 vdiffMarker 240 190 h
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 s 0 0 0 0 hln 100 PROBEVAR=
a 0 s 13 0 16 14 hlb 100 NODE=-
a 0 a 0 0 6 20 hlb 100 LABEL=8
@conn
w 20
s 160 100 110 100 19
s 110 100 110 110 26
s 110 110 110 130 29
w 16
s 240 100 190 100 17
s 240 130 240 100 33
w 12
s 190 190 240 190 11
s 240 190 240 170 13
w 8
s 110 170 110 180 7
s 110 190 150 190 9
s 110 180 110 190 31
@junction
j 110 170
+ p 5 -
+ w 8
j 150 190
+ p 3 1
+ w 8
j 190 190
+ p 3 2
+ w 12
j 240 170
+ p 4 1
+ w 12
j 240 130
+ p 4 2
+ w 16
j 190 100
+ p 2 1
+ w 16
j 160 100
+ p 2 2
+ w 20
j 110 130
+ p 5 +
+ w 20
j 110 110
+ p 28 pin1
+ w 20
j 110 180
+ p 30 pin1
+ w 8
j 240 100
+ p 37 pin1
+ w 16
j 240 190
+ p 38 pin1
+ w 12
j 110 190
+ s 39
+ w 8
@attributes
a 0 s 0:13 0 0 0 hln 100 PAGETITLE=
a 0 s 0:13 0 0 0 hln 100 PAGENO=1
a 0 s 0:13 0 0 0 hln 100 PAGESIZE=A
a 0 s 0:13 0 0 0 hln 100 PAGECOUNT=1
@graphics
