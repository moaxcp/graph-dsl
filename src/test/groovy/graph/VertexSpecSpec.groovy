package graph

import graph.trait.Mapping
import graph.trait.Weight
import graph.type.VertexSpec
import spock.lang.Specification

class VertexSpecSpec extends Specification {

    Graph graph = new Graph()

    def 'apply throws exception on invalid name'() {
        setup:
        VertexSpec spec = new VertexSpec(name:'')

        when:
        spec.apply(graph)

        then:
        thrown IllegalArgumentException
    }

    def 'apply can add vertex'() {
        setup:
        VertexSpec spec = new VertexSpec(name:'step1')

        when:
        Vertex vertex = spec.apply(graph)

        then:
        graph.vertices[vertex.name] == vertex
    }

    def 'apply can rename vertex'() {
        setup:
        graph.vertex 'step1'
        VertexSpec spec = new VertexSpec(name:'step1', rename:'step2')

        when:
        Vertex vertex = spec.apply(graph)

        then:
        graph.vertices.size() == 1
        vertex.name == 'step2'
        graph.vertices[vertex.name] == vertex
    }

    def 'apply can add traits'() {
        setup:
        VertexSpec spec = new VertexSpec(name:'step1', traits:[Mapping, Weight])

        when:
        Vertex vertex = spec.apply(graph)

        then:
        graph.vertices.size() == 1
        vertex.delegate instanceof Mapping
        vertex.delegate instanceof Weight
    }

    def 'apply can add edges using edgesFirst'() {
        setup:
        VertexSpec spec = new VertexSpec(name:'step1', connectsTo:['step2', 'step3'])

        when:
        spec.apply(graph)

        then:
        graph.vertices.size() == 3
        graph.edges.find { new Edge(one:'step1', two:'step2') }
        graph.edges.find { new Edge(one:'step1', two:'step3') }
    }
}
