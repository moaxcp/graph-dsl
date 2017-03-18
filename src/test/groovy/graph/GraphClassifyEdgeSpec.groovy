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

        def index = 0
        def froms = []
        def tos = []
        def types = []

        when:
        def ec = graph.classifyEdges { from, to, type ->
            froms[index] = from
            tos[index] = to
            types[index] = type
            index++
        }

        then:
        froms.size() == 2
        tos.size() == 2
        types.size() == 2
        froms[0] == 'A'
        tos[0] == 'B'
        types[0] == EdgeClassification.EdgeType.TREE_EDGE
        froms[1] == 'B'
        tos[1] == 'A'
        types[1] == EdgeClassification.EdgeType.BACK_EDGE
        ec.forrest.edges.size() == 1
        ec.forrest.vertices.size() == 2
        ec.backEdges.size() == 1
        ec.treeEdges.size() == 1
        ec.forwardEdges.size() == 0
        ec.crossEdges.size() == 0

    }
}
