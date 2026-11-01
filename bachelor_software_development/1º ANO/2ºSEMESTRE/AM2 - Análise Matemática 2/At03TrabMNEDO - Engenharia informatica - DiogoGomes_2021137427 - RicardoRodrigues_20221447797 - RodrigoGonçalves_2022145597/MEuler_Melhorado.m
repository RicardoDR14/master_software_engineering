function [t,y] = MEuler_Melhorado(f,a,b,n,y0)
% MEULER Método de Euler melhorado para resolução numérica de EDO/PVI
%   y'=f(t,y), t=[a,b], y(a)=y0
%   y(i+1)=y(i)+h*f(t(i),y(i)), i=0,1,2,...,n
%
% INPUT:
%   f - função da EDO y'=f(t,y);
%   [a,b] - intervalo de valores da variável independente t1;
%   n - número de subintervalos ou iterações do método;
%   y0 - aproximação inicial y(a)=y0;
%
% OUTPUT:
%   t - vetor da partição regular de [a,b];
%   y - vetor das soluções aproximadas do PVI em cada um dos t(i);
%
%   06/05/2023 Diogo Gomes   a2021137427@isec.pt
%   06/05/2023 Ricardo Rodrigues   a2022147797@isec.pt
%   06/05/2023  Rodrigo Gonçalves   a2022145597@isec.pt

h = (b-a)/n;
t = a:h:b;
y = zeros(1,n+1);
y(1) = y0;
for i =1:n
    y(i+1) = y(i)+h*f(t(i),y(i));
end
end

