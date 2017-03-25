package graph

import spock.lang.Specification

/**
 * contains tests for GraphVertex methods. These do not include methods that deal with the graph and visit nodes.
 */
class GraphVertexSpec extends Specification {

    def graph

    def setup() {
        graph = new Graph()
    }

    def 'cannot add !name with vertex(String)'() {
        when:
        graph.vertex ''

        then:
        thrown IllegalArgumentException
    }

    def 'can add/get with vertex(String)'() {
        when:
        def vertex = graph.vertex 'step1'

        then:
        graph.vertices.size() == 1
        vertex.name == 'step1'
    }

    def 'can get with second call to vertex(String)'() {
        setup:
        def expected = graph.vertex 'step1'

        when:
        def result = graph.vertex 'step1'

        then:
        result == expected
        result.name == 'step1'
    }

    def 'can add/get with vertex(Closure)'() {
        when:
        def vertex = graph.vertex {
            name = 'step1'
        }

        then:
        graph.vertices.size() == 1
        vertex.name == 'step1'
    }

    def 'can get with second call to vertex(Closure)'() {
        setup:
        def expected = graph.vertex {
            name = 'step1'
        }

        when:
        def result = graph.vertex {
            name = 'step1'
        }

        then:
        result == expected
        result.name == 'step1'
    }

    def 'can make VertexSpec with makeVertexSpec(Closure)'() {
        when:
        VertexSpec spec = graph.makeVertexSpec {
            name = 'step1'
            traits Mapping, Weight
            connectsTo 'step2', 'step3'
        }

        then:
        spec.name == 'step1'
        spec.traits == [Mapping, Weight] as Set<Class>
        spec.connectsTo == ['step2', 'step3'] as Set<String>
    }

    def 'can add traits with applySpecToVertex(VertexSpec, Vertex)'() {
        setup:
        Vertex vertex = new Vertex(name:'step1')
        VertexSpec spec = new VertexSpec()
        spec.traits Mapping, Weight

        when:
        graph.applySpecToVertexAndGraph(spec, vertex)

        then:
        vertex.delegate instanceof Mapping
        vertex.delegate instanceof Weight
    }

    def 'can add edges using connectsTo with applySpecToVertexAndGraph(VertexSpec, Vertex)'() {
        setup:
        Vertex vertex = graph.vertex 'step1'
        VertexSpec spec = new VertexSpec()
        spec.connectsTo 'step2', 'step3'

        when:
        graph.applySpecToVertexAndGraph(spec, vertex)

        then:
        graph.edges.find { new Edge(one:'step1', two:'step2') }
        graph.edges.find { new Edge(one:'step1', two:'step3') }
    }

    def 'can add traits with vertex(Closure)'() {
        when:
        def vertex = graph.vertex {
            name = 'step1'
            traits Weight, Mapping
        }

        then:
        vertex.name == 'step1'
        vertex.delegate instanceof Mapping
        vertex.delegate instanceof Weight
    }

    def 'can add edges using connectsTo with vertex(Closure)'() {
        when:
        graph.vertex {
            name = 'step1'
            connectsTo 'step2', 'step3'
        }

        then:
        graph.edges.size() == 2
        graph.edges.contains(new Edge(one:'step1', two:'step2'))
        graph.edges.contains(new Edge(one:'step1', two:'step2'))
    }

    def 'can add/get vertex with vertex(String, Closure)'() {
        when:
        def vertex = graph.vertex 'step1', {}

        then:
        graph.vertices.size() == 1
        vertex.name == 'step1'
    }

    def 'can get with second call to vertex(String, Closure)'() {
        setup:
        def expected = graph.vertex 'step1', {}

        when:
        def result = graph.vertex 'step1', {}

        then:
        result == expected
        result.name == 'step1'
    }

    def 'can add vertex with map to graph'() {

        when:
        graph.vertex name: 'step1'

        then:
        graph.vertices.step1 != null
    }

    def 'can add vertex with single trait to graph'() {
        when:
        graph.vertex name: 'step1', traits: Weight

        then:
        graph.vertices.step1.delegate instanceof Weight
    }

    def 'can add vertex with traits to graph'() {
        when:
        graph.vertex name: 'step1', traits: [Weight, Value]

        then:
        graph.vertices.step1.delegate instanceof Weight
        graph.vertices.step1.delegate instanceof Value
    }

    def 'vertices is unmodifiable'() {
        setup:
        graph.vertex 'step1'

        when:
        graph.vertices.step2 = new Vertex(name: 'step2')

        then:
        thrown(UnsupportedOperationException)
    }

    def 'can modify existing vertex'() {
        setup:
        def from = graph.vertex 'step1'

        when:
        def testValue = false
        def testVertex = graph.vertex 'step1', {
            testValue = vertex == from
        }

        then:
        testValue
        from == testVertex
    }
}
