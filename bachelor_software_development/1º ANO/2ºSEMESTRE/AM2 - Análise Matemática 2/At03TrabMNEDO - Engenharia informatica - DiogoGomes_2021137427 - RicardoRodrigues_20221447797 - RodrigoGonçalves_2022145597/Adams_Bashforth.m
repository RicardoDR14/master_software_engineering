function [t,y] = Adams_Bashforth(f, a, b, n, y0)
% Método de 2 passos Adams & Bashforth para EDO/PVI.
%   y = NAdams(f, a, b, n, y0) Método numérico para a resolução de um PVI
%   y' = f(t, y) com t = [a, b] e y(a) = y0 condição inicial
%
% INPUT:
%   f - função da EDO y'=f(t,y);
%   [a, b] - intervalo de valores da variável independente t;
%   n - número de subintervalos ou iterações do método;
%   y0 - aproximação inicial y(a)=y0;
%
% OUTPUT: 
%   y - vetor das soluções aproximadas do PVI em cada um dos t(i);
%   y(i + 2) = y(i + 1) + (3/2) * h * f(t(i + 1), y(i + 1)) - (1/2) * h * f(t(i), y(i)),
%   i = 0, 1, ..., n-1;
%
%   06/05/2023 Diogo Gomes   a2021137427@isec.pt
%   06/05/2023 Ricardo Rodrigues   a2022147797@isec.pt
%   06/05/2023  Rodrigo Gonçalves   a2022145597@isec.pt


h = (b - a) / n;
t = a: h: b;
y(1) = y0;
y(2) = y(1) + h * f(t(1), y(1));

for i = 1: n - 1

    y(i + 2) = y(i + 1) + (3/2) * h * f(t(i + 1), y(i + 1)) - (1/2) * h * f(t(i), y(i));
    
end
end