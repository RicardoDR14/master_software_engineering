function a = gerarPAritmetica_v2(a_1,r,n)
%GERARPARITMENTICA_v2 Gerar uma Progressão Aritmética
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
a = zeros(1,n);
a(1) = a_1;
i = 2;
while i <= n
    a(i) = a(1)+(i-1)*r
    i = i+1;
end

end