function [t,u,v] = MRK2(f,g,a,b,n,u0,v0)
%NRK2 Método de Euler para resolução numérica de EDO/PVI
%   y'=f(t,y), t=[a,b], y(a)=y0
%   y(i+1)=y(i)+h*f(t(i),y(i)), i=0,1,2,...,n
%INPUT:
%   f - 1ª Função do sistema de equações diferenciais, em v, u e t
%   g - 2ª Função do sistema de equações diferenciais, em v, u e t
%   a - Limite esquerdo do intervalo
%   b - Limite direito do intervalo
%   n - Numero de sub-intervalos ou iterações do método
%   u0 - 1º Valor (condição) Inicial do PVI, quando u=0
%   v0 - 2º Valor (condição) Inicial do PVI, quando v=0
%
%OUTPUT: 
%   t - vector do X, dos passos de "a" a "b"
%   u - vector das soluções apróximadas dos deslocamentos
%   v - vector das soluções aproximadas das velocidades'
%
%   25/05/2023 Diogo Gomes
%   25/05/2023  Rodrigo Gonçalves
%   25/05/2023 Ricardo Rodrigues

h = (b-a)/n;                % Valor de cada subintervalo (passo)
t = a:h:b;                  % Alocação de memória
u = zeros(1,n+1);           % Alocação de memória
v = zeros(1,n+1);           % Alocação de memória
u(1) = u0;                  % O primeiro valor de u é sempre u0
v(1) = v0;                  % O primeiro valor de v é sempre v0
for i = 1 : n               % O número de iterações vai ser igual a n
    k1u = h*f(t(i),u(i),v(i));                  % Inclinação no início do intervalo
    k1v = h*g(t(i),u(i),v(i));                  % Inclinação no início do intervalo
    
    k2u = h*f(t(i+1),u(i)+k1u,v(i)+k1v);        % Inclinação no fim do intervalo
    k2v = h*g(t(i+1),u(i)+k1u,v(i)+k1v);        % Inclinação no fim do intervalo
    
    u(i+1) = u(i)+(k1u+k2u)/2;              % Aproximação do método de RK2 para a enésima iteração
    v(i+1) = v(i)+(k1v+k2v)/2;              % Aproximação do método de RK2 para a enésima iteração
end
end

