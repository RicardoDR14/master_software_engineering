function P = MHorner(a,x)
%MHoner Método de Horner ou dos parêntises encaixados
%input:
%   a: vetor dos coeficientes do polinómios a=[a0,a1,...,an]
%   x: valor onde determinar o polinómio
%output:
%   P: valor do polinómio em x
%   07/03/2023 Ricardo Rodrigues a2022147797@isec.pt


n = length(a);
P = a(n);

    for i = n-1:-1:1
        P = P.*x+a(i);
    end
end