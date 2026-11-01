function [t,y] = MRK4(f,a,b,n,y0)
% MRK4 Método Runge-Kutta de ordem 4 para resolução numérica de EDO/PVI
%   y = MRK4(f,a,b,n,y0)
%
% INPUT:
%   f - função da EDO y'=f(t,y);
%   [a,b] - intervalo de valores correspondentes à variável independente t;
%   n - número de subintervalos ou iterações do método;
%   y0 - aproximação inicial y(a)=y0;
%
% OUTPUT:
%   y - vetor das soluções aproximadas do PVI em cada t(i);
%
%   06/05/2023 Diogo Gomes   a2021137427@isec.pt
%   06/05/2023 Ricardo Rodrigues   a2022147797@isec.pt
%   06/05/2023  Rodrigo Gonçalves   a2022145597@isec.pt


h = (b-a)/n;
t = a:h:b;                     
y = zeros(1,n+1);
y(1) = y0;

for i =1:n

    k1 = h*f(t(i),y(i));
    
    k2 = h*f(t(i) + (h/2) , y(i) + (k1/2) );
    
    k3 = h*f(t(i) + (h/2) , y(i) + (k2/2) );
    
    k4 = h*f(t(i+1) , y(i) + k3);
    
    y(i+1) = y(i) + (k1 + 2*k2 + 2*k3 + k4)/6;
    
end
end