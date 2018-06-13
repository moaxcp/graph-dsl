package graph

import graph.Edge
import graph.Graph
import graph.Vertex
import graph.VertexSpec
import spock.lang.Specification

class VertexSpecSpec extends Specification {

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

    def 'VertexSpec must have graph'() {
        when:
        VertexSpec spec = new VertexSpec(null, [:])

        then:
        IllegalArgumentException e = thrown()
        e.message == 'invalid graph.'
    }

    def 'VertexSpec must have map'() {
        when:
        new VertexSpec(graph, null)

        then:
        IllegalArgumentException e = thrown()
        e.message == 'invalid map.'
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

    def 'apply can add edges using connectsTo'() {
        setup:
        VertexSpec spec = new VertexSpec(graph, [id:'step1', connectsTo:['step2', 'step3']])

        when:
        spec.apply()

        then:
        graph.vertices.size() == 3
        graph.edges.find { new Edge(from:'step1', to:'step2') }
        graph.edges.find { new Edge(from:'step1', to:'step3') }
    }

    def 'missing method calls method on vertex'() {
        setup:
        VertexSpec spec = new VertexSpec(graph, [id:'A'])
        spec.apply()

        when:
        spec.put('prop', 'value')

        then:
        graph.vertices.size() == 1
        graph.vertices.A.prop == 'value'
    }
}
