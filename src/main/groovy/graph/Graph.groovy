package graph

class Graph {
    def vertices = [:]
    def edges = []

    def vertice(name) {
        vertices[name] = new Vertice(name: name)
    }

    def vertice(name, closure) {
        def vertice = new Vertice(name: name)
        closure.delegate = vertice
        closure.call()
        vertices[name] = vertice
    }

    def edge(one, two) {
        edges += new Edge(one: one, two: two)
    }

    def edge(one, two, closure) {
        def edge = new Edge(one: one, two: two)
        closure.delegate = edge
        closure.call()
        edges += edge
    }
}
