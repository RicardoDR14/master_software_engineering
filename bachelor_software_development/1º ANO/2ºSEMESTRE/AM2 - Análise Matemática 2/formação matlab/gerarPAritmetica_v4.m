function a = gerarPAritmetica_v4(a_1,r,n)
%GERARPARITMENTICA_v3 Gerar uma Progressão Aritmética
%   a = gerarPAritmetica_v1(a_1,r,n)
%   a(i) = a(1)+(i-1)*r
%
%INPUT:
%   a_11 - primeiro elemento da progressão aritmética
%   r - razão da progressão aritmética
%   n - número de elementos da progressão aritmética 
%OUTPUT: 
%   a - vetor com os elementos da progressão aritmética
%
%   21/04/2021 - Arménio Correia  armenioc@isec.pt
%   29/03/2023 - Ricardo Rodrigues a2022147797@isec.com
a = a_1:r:a_1+(n-1)*r;
end