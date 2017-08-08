package graph

import graph.plugins.directedgraph.DirectedEdge
import graph.plugins.directedgraph.DirectedGraphPlugin
import spock.lang.Specification

class DirectedGraphSpec extends Specification {
    def graph = new Graph()

    def setup() {
        graph.apply DirectedGraphPlugin
    }

    def 'can get out edges'() {
        setup:
        def expected = [] as LinkedHashSet<DirectedEdge>
        graph.with {
            expected << edge('step1', 'step2')
            expected << edge('step1', 'step3')
            edge 'step4', 'step1'
            edge 'step4', 'step3'
        }

        when:
        def out = graph.outEdges 'step1'

        then:
        out == expected
    }

    def 'can get out degree'() {
        setup:
        graph.with {
            edge('step1', 'step2')
            edge('step1', 'step3')
        }

        when:
        def out = graph.outDegree 'step1'

        then:
        out == 2
    }

    def 'traverseEdges are outEdges'() {
        setup:
        def expected = [] as LinkedHashSet<DirectedEdge>
        graph.with {
            expected << edge('step1', 'step2')
            expected << edge('step1', 'step3')
            edge 'step4', 'step1'
            edge 'step4', 'step3'
        }

        when:
        def out = graph.outEdges 'step1'
        def adjacent = graph.traverseEdges 'step1'

        then:
        out == expected
        adjacent == expected
    }

    def 'can get inEdges'() {
        setup:
        def expected = [] as LinkedHashSet<DirectedEdge>
        graph.with {
            expected << edge('step2', 'step1')
            expected << edge('step3', 'step1')
            edge 'step1', 'step4'
            edge 'step3', 'step4'
        }

        when:
        def inEdges = graph.inEdges 'step1'

        then:
        inEdges == expected
    }

    def 'can get in degree'() {
        setup:
        graph.with {
            edge('step2', 'step1')
            edge('step3', 'step1')
        }

        when:
        def inEdges = graph.inDegree 'step1'

        then:
        inEdges == 2
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

    def 'can get topological sort'() {
        setup:
        graph.with {
            vertex('A', [connectsTo:['B', 'D', 'E']])
            vertex('B', [connectsTo:['D', 'C']])
            vertex('C', [connectsTo:'D'])
            vertex('D', [connectsTo:'G'])
            vertex('G', [connectsTo:'F'])
        }

        when:
        def sort = graph.reversePostOrderSort()

        then:
        sort == ['A', 'E', 'B', 'C', 'D', 'G', 'F']
    }

    def 'can perform reversePostOrder'() {
        setup:
        graph.with {
            vertex('A', [connectsTo:['B', 'D', 'E']])
            vertex('B', [connectsTo:['D', 'C']])
            vertex('C', [connectsTo:'D'])
            vertex('D', [connectsTo:'G'])
            vertex('G', [connectsTo:'F'])
        }
        def rpo = []

        when:
        graph.reversePostOrder { vertex ->
            rpo << vertex.name
        }

        then:
        rpo == ['A', 'E', 'B', 'C', 'D', 'G', 'F']
    }
}
