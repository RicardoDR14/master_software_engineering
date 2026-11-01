% Formúla das Diferenças Progressivas (2 pontos)
%
% f'(x(i))=(f(x(i+1))-f(x(i))/h
%
% INPUT:  f - função
%         [a, b] - intervalo de derivação
%         h - passo da discretização
%         y - imagens x vs y
%
% OUTPUT: [x, y] - malha de pontos
%         dydx - derivada de f 

%  08/06/23 
% Diogo Gomes - 2021137427
% Rodrigo Gonçalves - 2022145597
% Ricardo Rodrigues - 2022147797



function [x,y,dydx] = DF2_P(f,a,b,h,y)

x=a:h:b;                                % Alocação de memória

n=length(x);                            % Número de pontos (tamanho do vetor de abcissas)

if nargin==4
    y=f(x);                             % y é a função f(x)
end

dydx=zeros(1,n);                        % Alocação de memória

for i=1:n-1
    dydx(i)=(y(i+1)-y(i))/h;            % Derivada (aproximada) de f no ponto atual
end

dydx(n)=(y(n)-y(n-1))/h;                % Derivada (aproximada) de f no ponto atual