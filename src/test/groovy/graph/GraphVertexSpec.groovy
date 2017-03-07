package graph

import spock.lang.Specification

/**
 * contains tests for GraphVertex methods. These do not include methods that deal with the graph and visit nodes.
 */
class GraphVertexSpec extends Specification {

    def graph = new Graph()

    def 'can add vertex to graph'() {
        when:
        graph.vertex 'step1'

        then:
        graph.vertices.step1 != null
    }

    def 'can add vertex with closure to graph'() {
        setup:
        def vertexName

        when:
        graph.vertex 'step1', {
            vertexName = name
        }

        then:
        vertexName == 'step1'
    }

    def 'can add vertex with map to graph'() {

        when:
        graph.vertex name: 'step1'

        then:
        graph.vertices.step1 != null
    }

    def 'can add vertex with single trait to graph'() {
        when:
        graph.vertex name: 'step1', trait: Weight

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
        def vertex = graph.vertex 'step1'

        when:
        def testValue = false
        def testVertex = graph.vertex 'step1', {
            testValue = delegate == vertex
        }

        then:
        testValue
        vertex == testVertex
    }
}
