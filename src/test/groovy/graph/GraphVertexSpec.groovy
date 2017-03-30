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
        graph.vertices.step1 == vertex
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
        graph.vertices.step1 == vertex
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

    def 'can add/get vertex with vertex(Map)'() {
        when:
        def vertex = graph.vertex name:'step1'

        then:
        graph.vertices.size() == 1
        graph.vertices.step1 == vertex
        vertex.name == 'step1'
    }

    def 'can get with second call to vertex(Map)'() {
        setup:
        def expected = graph.vertex name:'step1'

        when:
        def result = graph.vertex name:'step1'

        then:
        result == expected
        result.name == 'step1'
    }

    def 'can add traits with vertex(Map)'() {
        when:
        def vertex = graph.vertex name:'step1', traits: [Mapping, Weight]

        then:
        vertex.name == 'step1'
        vertex.delegate instanceof Mapping
        vertex.delegate instanceof Weight
    }

    def 'can add edges using connectsTo with vertex(Map)'() {
        when:
        graph.vertex name:'step1', connectsTo:['step2', 'step3']

        then:
        graph.edges.size() == 2
        graph.edges.contains(new Edge(one:'step1', two:'step2'))
        graph.edges.contains(new Edge(one:'step1', two:'step2'))
    }

    def 'can configure vertex traits with VertexSpec.config(Closure) in vertex(Map)'() {
        when:
        Vertex vertex = graph.vertex name:'step1', traits:[Mapping, Weight],
            config:{
                label = 'the first step'
                weight { 100 }
            }

        then:
        vertex.label == 'the first step'
        vertex.getWeight() == 100
    }

    def 'can add/get vertex with vertex(String, Closure)'() {
        when:
        def vertex = graph.vertex 'step1', {}

        then:
        graph.vertices.size() == 1
        graph.vertices.step1 == vertex
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

    def 'can add/get vertex with vertex(String, Map)'() {
        when:
        def vertex = graph.vertex 'step1', [:]

        then:
        graph.vertices.size() == 1
        graph.vertices.step1 == vertex
        vertex.name == 'step1'
    }

    def 'can get with second call to vertex(String, Map)'() {
        setup:
        def expected = graph.vertex 'step1', [:]

        when:
        def result = graph.vertex 'step1', [:]

        then:
        result == expected
        result.name == 'step1'
    }

    def 'can add/get vertex with vertex(Map, Closure)'() {
        when:
        def vertex = graph.vertex(name:'step1') {}

        then:
        graph.vertices.size() == 1
        graph.vertices.step1 == vertex
        vertex.name == 'step1'
    }

    def 'can get with second call to vertex(Map, Closure)'() {
        setup:
        def expected = graph.vertex(name:'step1') {}

        when:
        def result = graph.vertex(name:'step1') {}

        then:
        result == expected
        result.name == 'step1'
    }

    def 'can add/get vertex with vertex(String, Map, Closure)'() {
        when:
        def vertex = graph.vertex('step1', [:]) {}

        then:
        graph.vertices.size() == 1
        graph.vertices.step1 == vertex
        vertex.name == 'step1'
    }

    def 'can get with second call to vertex(String, Map, Closure)'() {
        setup:
        def expected = graph.vertex('step1', [:]) {}

        when:
        def result = graph.vertex('step1', [:]) {}

        then:
        result == expected
        result.name == 'step1'
    }

    def 'vertices is unmodifiable'() {
        setup:
        graph.vertex 'step1'

        when:
        graph.vertices.step2 = new Vertex(name: 'step2')

        then:
        thrown(UnsupportedOperationException)
    }

    def 'can rename vertex'() {
        setup:
        graph.vertex 'step1'
        graph.vertex 'step2'
        graph.vertex 'step3'
        graph.vertex 'step4'
        graph.edge 'step1', 'step2'
        graph.edge 'step1', 'step3'
        graph.edge 'step4', 'step1'

        when:
        graph.rename 'step1', 'step5'

        then:
        graph.adjacentEdges('step1').size() == 0
        graph.adjacentEdges('step5').size() == 3
    }

    def 'cannot rename vertex to a false name'() {
        setup:
        graph.vertex 'step1'
        graph.vertex 'step2'
        graph.vertex 'step3'
        graph.vertex 'step4'
        graph.edge 'step1', 'step2'
        graph.edge 'step1', 'step3'
        graph.edge 'step4', 'step1'

        when:
        graph.rename 'step1', ''

        then:
        thrown IllegalArgumentException
    }
}
