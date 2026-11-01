% aula pratica 4
% 21/03/2023

clc
clear

syms y(t)

while 1

    strF = input("f(t,y) = ","s");
    f = @(t,y) eval(vectorize(strF))

    try
        z = f(t,y);
        break
    catch ME
        disp("Introduça uma função em t e y")
    end
end


while 1
    a = str2num(input("a = ","s"))
    b = str2num(input("b = ","s"))
    if (isreal(a) & isreal(b) & b>a)
        break
    else
        disp("Introduza números reais e b > a")
    end
end

y0 = 5

edo1 = diff(y,t) == f(t,y)
ci = y(a) == y0

%help dsolve
sExata = dsolve(edo1,ci)

fplot(sExata,[a,b])