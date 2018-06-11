package graph.type

import graph.Edge
import graph.Graph
import graph.Vertex
import graph.VertexSpec
import spock.lang.Specification

class VertexSpecSpecImpl extends Specification {

    Graph graph = new Graph()

    def 'apply throws exception on invalid id'() {
        when:
        new VertexSpec(graph, [id:''])

        then:
        thrown IllegalArgumentException
    }

    def 'apply can add vertex'() {
        setup:
        VertexSpec spec = new VertexSpec(graph, [id:'step1'])

        when:
        Vertex vertex = spec.apply()

        then:
        graph.vertices[vertex.id] == vertex
    }

    def 'apply cannot be run twice'() {
        setup:
        VertexSpec spec = new VertexSpec(graph, [id:'step1'])

        when:
        Vertex vertex = spec.apply()
        spec.apply()

        then:
        thrown IllegalStateException
    }

    def 'apply can rename vertex'() {
        setup:
        graph.vertex 'step1'
        VertexSpec spec = new VertexSpec(graph, [id:'step1', changeId:'step2'])

        when:
        Vertex vertex = spec.apply()

        then:
        graph.vertices.size() == 1
        vertex.id == 'step2'
        graph.vertices[vertex.id] == vertex
    }

    def 'apply can add edges using edgesFirst'() {
        setup:
        VertexSpec spec = new VertexSpec(graph, [id:'step1', connectsTo:['step2', 'step3']])

        when:
        spec.apply()

        then:
        graph.vertices.size() == 3
        graph.edges.find { new Edge(from:'step1', to:'step2') }
        graph.edges.find { new Edge(from:'step1', to:'step3') }
    }
}
