% aula pratica 4
% 21/03/2023

clc
clear

syms y(t)

strF = "y*(1-t^2)"

%help function_handle

f = @(t,y) eval(vectorize(strF))

f(0,1)

a = 0
b = 2
y0 = 5

edo1 = diff(y,t) == f(t,y)
ci = y(a) == y0

%help dsolve
sExata = dsolve(edo1,ci)

fplot(sExata,[a,b])