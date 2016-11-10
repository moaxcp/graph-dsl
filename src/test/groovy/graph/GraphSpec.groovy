package graph

import spock.lang.Specification

class GraphSpec extends Specification {

    def graph = new Graph()

    def 'can add vertex to graph'() {
        when:
        graph.vertex 'step1'

        then:
        graph.vertices.step1
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

    def 'can add edge to graph'() {
        when:
        graph.edge 'step1', 'step2'

        then:
        graph.edges.first().one == 'step1'
        graph.edges.first().two == 'step2'
    }

    def 'can add edge with closure to graph'() {
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

    def 'cannot add duplicate edge with the same order'() {
        setup:
        graph.edge 'step1', 'step2'

        when:
        graph.edge 'step1', 'step2'

        then:
        thrown(IllegalArgumentException)
        graph.edges.size() == 1
    }

    def 'cannot add duplicate edge with different order'() {
        setup:
        graph.edge 'step1', 'step2'

        when:
        graph.edge 'step2', 'step1'

        then:
        thrown(IllegalArgumentException)
        graph.edges.size() == 1
    }
}
