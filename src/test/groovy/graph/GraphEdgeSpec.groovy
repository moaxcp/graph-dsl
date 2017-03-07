package graph

import spock.lang.Specification

class GraphEdgeSpec extends Specification {

    def graph = new Graph()

    def 'can add edge to graph'() {
        when:
        graph.edge 'step1', 'step2'

        then:
        graph.edges.first().one == 'step1'
        graph.edges.first().two == 'step2'
    }

    def 'can add edge to graph with closure'() {
        setup:
        def edgeOne
        def edgeTwo

        when:
        graph.edge 'step1', 'step2', {
            edgeOne = one
            edgeTwo = two
        }

        then:
        edgeOne == 'step1'
        edgeTwo == 'step2'
    }

    def 'can add edge to graph with map'() {
        when:
        graph.edge one: 'step1', two: 'step2'

        then:
        graph.edges.first().one == 'step1'
        graph.edges.first().two == 'step2'
    }

    def 'can add edge with map and closure to graph'() {
        setup:
        def edgeOne
        def edgeTwo

        when:
        graph.edge one: 'step1', two: 'step2', {
            edgeOne = one
            edgeTwo = two
        }

        then:
        edgeOne == 'step1'
        edgeTwo == 'step2'
    }

    def 'can add edge with single trait'() {
        when:
        graph.edge one: 'step1', two: 'step2', trait: Weight

        then:
        graph.edges[0].delegate instanceof Weight
    }

    def 'can add edge with map and traits to graph'() {
        when:
        graph.edge one: 'step1', two: 'step2', traits: [Weight, Value]

        then:
        graph.edges[0].delegate instanceof Weight
        graph.edges[0].delegate instanceof Value
    }

    def 'cannot add duplicate edge with the same order'() {
        setup:
        graph.edge 'step1', 'step2'

        when:
        graph.edge 'step1', 'step2'

        then:
        graph.edges.size() == 1
    }

    def 'cannot add duplicate edge with different order'() {
        setup:
        graph.edge 'step1', 'step2'

        when:
        graph.edge 'step2', 'step1'

        then:
        graph.edges.size() == 1
    }

    def 'can modify existing edge'() {
        setup:
        def edge = graph.edge 'step1', 'step2'

        when:
        def testValue = false
        def testEdge = graph.edge 'step1', 'step2', {
            testValue = delegate == edge
        }

        then:
        testValue
        edge == testEdge
    }
}
