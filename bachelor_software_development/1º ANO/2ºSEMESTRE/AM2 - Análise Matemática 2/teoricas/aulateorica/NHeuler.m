function y = NHeuler(f,a,b,n,y0)

h = (b-a)/n;
t(1) = a;
y(1) = y0;

for i=1 : n
    y(1+i) = y(i) + h*f(t(i),y(i));
    t(1+i) = t(i) + h;
end
end