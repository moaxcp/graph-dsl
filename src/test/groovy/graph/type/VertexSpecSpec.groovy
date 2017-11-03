package graph.type

import graph.Edge
import graph.Graph
import graph.Vertex
import graph.VertexSpec
import graph.type.undirected.UndirectedVertexSpec
import spock.lang.Specification

class VertexSpecSpec extends Specification {

    Graph graph = new Graph()

    def 'apply throws exception on invalid name'() {
        setup:
        VertexSpec spec = new UndirectedVertexSpec(graph, [name:''])

        when:
        spec.apply()

        then:
        thrown IllegalStateException
    }

    def 'apply can add vertex'() {
        setup:
        VertexSpec spec = new UndirectedVertexSpec(graph, [name:'step1'])

        when:
        Vertex vertex = spec.apply()

        then:
        graph.vertices[vertex.key] == vertex
    }

    def 'apply cannot be run twice'() {
        setup:
        VertexSpec spec = new UndirectedVertexSpec(graph, [name:'step1'])

        when:
        Vertex vertex = spec.apply()
        spec.apply()

        then:
        thrown IllegalStateException
    }

    def 'apply can rename vertex'() {
        setup:
        graph.vertex 'step1'
        VertexSpec spec = new UndirectedVertexSpec(graph, [name:'step1', rename:'step2'])

        when:
        Vertex vertex = spec.apply()

        then:
        graph.vertices.size() == 1
        vertex.key == 'step2'
        graph.vertices[vertex.key] == vertex
    }

    def 'apply can add edges using edgesFirst'() {
        setup:
        VertexSpec spec = new UndirectedVertexSpec(graph, [name:'step1', connectsTo:['step2', 'step3']])

        when:
        spec.apply()

        then:
        graph.vertices.size() == 3
        graph.edges.find { new Edge(one:'step1', two:'step2') }
        graph.edges.find { new Edge(one:'step1', two:'step3') }
    }
}
