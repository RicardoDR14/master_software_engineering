function [x,y] = Polares2Cartesianas(rho,theta)
%UNTITLED2 Summary of this function goes here
%   Detailed explanation goes here
if rho>=0
    x=rho*cos(theta);
    y=rho*sin(theta);
end
end