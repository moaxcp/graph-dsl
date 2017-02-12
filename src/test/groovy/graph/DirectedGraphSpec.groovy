package graph

import spock.lang.Specification

class DirectedGraphSpec extends Specification {
    def graph = new Graph()

    def setup() {
        graph.apply DirectedGraphPlugin
    }

    def 'can get outedges and adjacent edges'() {
        setup:
        def expected = [] as LinkedHashSet
        graph.with {
            vertex 'step1'
            expected << edge('step1', 'step2')
            expected << edge('step1', 'step3')
            edge 'step4', 'step1'
            edge 'step4', 'step3'
        }
        when:
        def out = graph.outEdges 'step1'
        def adjacent = graph.adjacentEdges 'step1'

        then:
        out == expected
        adjacent == expected
    }

    def 'traverse is directed'() {
        setup:
        graph.with {
            vertex 'step1'
            vertex 'step2'
            vertex 'step3'
            vertex 'step4'
            vertex 'step5'
            edge 'step2', 'step1'
            edge 'step5', 'step2'
            edge 'step1', 'step3'
            edge 'step3', 'step5'
            edge 'step1', 'step4'
            edge 'step4', 'step5'
            edge 'step5', 'step3'
        }

        def preorderList = []
        def postorderList = []

        when:
        def traversal = graph.depthFirstTraversal {
            preorder { vertex ->
                preorderList << vertex.name
            }
            postorder { vertex ->
                postorderList << vertex.name
            }
        }

        then:
        preorderList == ['step1', 'step3', 'step5', 'step2', 'step4']
        postorderList == ['step2', 'step5', 'step3', 'step4', 'step1']
    }
}
