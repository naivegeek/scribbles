/*run using /usr/java/jdk1.8.0/bin/jjs -nashorn js*/
var data = [1, 3, 5, 7, 11];
var sum = data.reduce(function(x, y) {return x + y}, 0);
print(sum);