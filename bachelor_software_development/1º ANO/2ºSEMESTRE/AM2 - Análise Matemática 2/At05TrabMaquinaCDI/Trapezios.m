% Formula da Regra dos Trapezios
%
% INPUT:  f - funcao integranda
%         [a, b] - intervalo de integracao
%         n - numero de subintervalos
%
% OUTPUT: area - Valor da area calculada pela Regra dos Trapezios
%
% 08/06/23 
% Diogo Gomes - 2021137427
% Rodrigo Gonçalves - 2022145597
% Ricardo Rodrigues - 2022147797

function area = Trapezios(f,a,b,n)

h=(b-a)/n;                      % Valor de cada subintervalo (passo)
x=a;                            % 'x' recebe o valor de 'a' (primeira abcissa)
s=0;                            % Inicializacao da variavel 's' a 0

for i=1:n-1
    x=x+h;                      % Ao valor de 'x' e somado o passo ('h')
    s=s+f(x);                   % Ao valor de 's' e somado o valor da funcao
end
area = (h/2)*(f(a)+2*s+f(b));
