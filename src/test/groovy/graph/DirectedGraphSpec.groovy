package graph

import spock.lang.Specification

class DirectedGraphSpec extends Specification {
    def graph = new Graph()

    def setup() {
        graph.apply DirectedGraph
    }

    def 'traverse is directed'() {
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

        def spec = new DepthFirstTraversalSpec()
        spec.postorder { vertex ->
            postorderList << vertex.name
        }
        spec.preorder { vertex ->
            preorderList << vertex.name
        }

        when:
        def traversal = graph.depthFirstTraversalConnected 'step1', spec

        then:
        preorderList == ['step1', 'step3', 'step5', 'step2', 'step4']
        postorderList == ['step2', 'step5', 'step3', 'step4', 'step1']
    }
}
