package graph

import spock.lang.Specification

class GraphSpec extends Specification {
    def 'can detect multiple plugins'() {
        setup:
        def graph = new Graph()
        graph.apply DirectedGraph

        when:
        graph.apply DirectedGraph

        then:
        thrown IllegalArgumentException
    }
}
