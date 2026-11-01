function [t,y] = ODE45(f,a,b,n,y0)
% Utilização da função do matlab ODE45 para resolução numérica de EDO/PVI
%
% INPUT:
%   f - função da EDO y'=f(t,y);
%   [a,b] - intervalo de valores correspondentes à variável independente t;
%   n - número de subintervalos ou iterações do método;
%   y0 - aproximação inicial y(a)=y0;
%
% OUTPUT:
%  y - vetor das soluções aproximadas do PVI em cada t(i);
%
%   06/05/2023 Diogo Gomes   a2021137427@isec.pt
%   06/05/2023 Ricardo Rodrigues   a2022147797@isec.pt
%   06/05/2023  Rodrigo Gonçalves   a2022145597@isec.pt


h = (b-a)/n;
t = a:h:b;       

[~,y] = ode45(f, t, y0);            % Aproximação através da função ODE45 e colocação dos valores no vetor y
y = y';                             % Mudança da orientação do vetor porque na ODE45 é nos devolvido a solução em coluna e
                                    % queremos em linha
end