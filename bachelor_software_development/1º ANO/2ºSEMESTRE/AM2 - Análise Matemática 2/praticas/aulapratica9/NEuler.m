function [t,y] = NEuler(f,a,b,n,y0)
%NEULER Método de Euler para resolução numérica de EDO/PVI
%   y'=f(t,y), t=[a,b], y(a)=y0
%   y(i+1)=y(i)+h*f(t(i),y(i)), i=0,1,2,...,n
%INPUT:
%   f - função da EDO y'=f(t,y)
%   [a,b] - intervalo de valores da variável independente t
%   n - núnmero de subintervalos ou iterações do método
%   y0 - aproximação inicial y(a)=y0
%OUTPUT:
%   t - vetor da partição regular do intervalo [a,b]
%   y - vetor das soluções aproximadas do PVI em cada um dos t(i)
%
%   22/03/2023  Arménio Correia  armenioc@isec.pt
%   28/03/2023  Arménio Correia 

h = (b-a)/n;
t = a:h:b;
y = zeros(1,n+1);
y(1) = y0;
for i =1:n
    y(i+1) = y(i)+h*f(t(i),y(i));
end
end

