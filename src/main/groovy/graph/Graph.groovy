package graph

class Graph {
    def vertices = [:]
    def edges = [] as Set

    def vertice(name) {
        vertices[name] = new Vertex(name: name)
    }

    def vertice(name, closure) {
        def vertice = new Vertex(name: name)
        closure.delegate = vertice
        closure.call()
        vertices[name] = vertice
    }

    def edge(one, two) {
        def edge = new Edge(one: one, two: two)
        def added = edges.add edge
        if(!added) {
            throw new IllegalArgumentException("Edge already exists between $edge.one and $edge.two")
        }
    }

    def edge(one, two, closure) {
        def edge = new Edge(one: one, two: two)
        closure.delegate = edge
        closure.call()
        def added = edges.add edge
        if(!added) {
            throw new IllegalArgumentException("Edge already exists between $edge.one and $edge.two")
        }
    }
}
