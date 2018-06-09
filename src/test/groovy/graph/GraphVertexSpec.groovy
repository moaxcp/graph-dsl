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
        graph.vertices.step1.is vertex
        vertex.id == 'step1'
    }

    def 'can get with second call to vertex(String)'() {
        setup:
        def expected = graph.vertex 'step1'

        when:
        def result = graph.vertex 'step1'

        then:
        result.is expected
    }

    def 'can add/get vertex with vertex(Map)'() {
        when:
        def vertex = graph.vertex id:'step1'

        then:
        graph.vertices.size() == 1
        graph.vertices.step1.is vertex
        vertex.id == 'step1'
    }

    def 'can get with second call to vertex(Map)'() {
        setup:
        def expected = graph.vertex id:'step1'

        when:
        def result = graph.vertex id:'step1'

        then:
        result.is expected
    }

    def 'can add edges using connectsTo with vertex(Map)'() {
        when:
        graph.vertex id:'step1', connectsTo:['step2', 'step3']
        def edgeOne = graph.edges.find {
            it.one == 'step1' && it.two == 'step2'
        }
        def edgeTwo = graph.edges.find {
            it.one == 'step1' && it.two == 'step3'
        }

        then:
        graph.edges.size() == 2
        edgeOne != null
        edgeTwo != null
    }

    def 'can add/get vertex with vertex(Object, Closure)'() {
        when:
        def vertex = graph.vertex 'step1', {}

        then:
        graph.vertices.size() == 1
        graph.vertices.step1.is vertex
        vertex.id == 'step1'
    }

    def 'can get with second call to vertex(Object, Closure)'() {
        setup:
        def expected = graph.vertex 'step1', {}

        when:
        def result = graph.vertex 'step1', {}

        then:
        result.is expected
    }

    def 'can add/get vertex with vertex(Object, Map)'() {
        when:
        def vertex = graph.vertex 'step1', [:]

        then:
        graph.vertices.size() == 1
        graph.vertices.step1.is vertex
        vertex.id == 'step1'
    }

    def 'can get with second call to vertex(Object, Map)'() {
        setup:
        def expected = graph.vertex 'step1', [:]

        when:
        def result = graph.vertex 'step1', [:]

        then:
        result.is expected
    }

    def 'can add/get vertex with vertex(Map, Closure)'() {
        when:
        def vertex = graph.vertex(id:'step1') {}

        then:
        graph.vertices.size() == 1
        graph.vertices.step1.is vertex
        vertex.id == 'step1'
    }

    def 'can get with second call to vertex(Map, Closure)'() {
        setup:
        def expected = graph.vertex(id:'step1') {}

        when:
        def result = graph.vertex(id:'step1') {}

        then:
        result.is expected
    }

    def 'can add/get vertex with vertex(Object, Map, Closure)'() {
        when:
        def vertex = graph.vertex('step1', [:]) {}

        then:
        graph.vertices.size() == 1
        graph.vertices.step1.is vertex
        vertex.id == 'step1'
    }

    def 'can get with second call to vertex(Object, Map, Closure)'() {
        setup:
        def expected = graph.vertex('step1', [:]) {}

        when:
        def result = graph.vertex('step1', [:]) {}

        then:
        result.is expected
    }

    def 'vertices is unmodifiable'() {
        setup:
        graph.vertex 'step1'

        when:
        graph.vertices.step2 = new Vertex(id: 'step2')

        then:
        thrown(UnsupportedOperationException)
    }

    def 'can change key of vertex'() {
        setup:
        graph.vertex 'step1'
        graph.vertex 'step2'
        graph.vertex 'step3'
        graph.vertex 'step4'
        graph.edge 'step1', 'step2'
        graph.edge 'step1', 'step3'
        graph.edge 'step4', 'step1'

        when:
        graph.changeId 'step1', 'step5'

        then:
        graph.vertices['step1'] == null
        graph.vertices.size() == 4
        graph.adjacentEdges('step1').size() == 0
        graph.adjacentEdges('step5').size() == 3
    }

    def 'cannot change vertex to a false key'() {
        setup:
        graph.vertex 'step1'

        when:
        graph.changeId 'step1', ''

        and:
        graph.changeId 'step1', null

        then:
        thrown IllegalArgumentException
    }

    def 'can create multiple vertices'() {
        when:
        Set<Vertex> vertices = graph.vertex 'step1', 'step2', 'step3', 'step4'

        then:
        vertices == [
                new Vertex(id:'step1'),
                new Vertex(id:'step2'),
                new Vertex(id:'step3'),
                new Vertex(id:'step4')
        ] as Set<Vertex>
    }
}
