f = open("./RawInput.txt")
lines = f.read().split("\n")
nodes = {}
edges = []
for line in lines:
    line = line.split(" ")
    nodes[line[0]] = True
    nodes[line[1]] = True
    edges.append((line[0], line[1]))
    edges.append((line[1], line[0]))
edges = list(set(edges))

total = 0
for v in nodes:
    for i in nodes:
        for j in nodes:
            if(((i, j) in edges) and ((i, v) in edges) and ((v, j) in edges)):
                total += 1

print(total/6)