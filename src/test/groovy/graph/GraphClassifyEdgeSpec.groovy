package graph

import spock.lang.Specification

class GraphClassifyEdgeSpec extends Specification {
    def 'can classify edges'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'A'
            vertex 'B'
            edge 'A', 'B'
        }

        when:
        def ec = graph.classifyEdges { edge, from, to, type ->

        }

        then:
        ec.forrest.edges.size() == 1
        ec.forrest.vertices.size() == 2
        ec.backEdges.size() == 1
        ec.treeEdges.size() == 1
        ec.forwardEdges.size() == 0
        ec.crossEdges.size() == 0

    }
}
