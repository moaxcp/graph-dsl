package graph.type.directed

import graph.Graph
import spock.lang.Specification

import static graph.TraversalState.CONTINUE

class DirectedGraphSpec extends Specification {
    def graph = new Graph()

    def setup() {
        graph.type DirectedGraphType
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

    def 'preOrder traverse is directed'() {
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

        def preOrderList = []

        when:
        graph.preOrder { vertex ->
            preOrderList << vertex.id
            CONTINUE
        }

        then:
        preOrderList == ['step1', 'step3', 'step5', 'step2', 'step4']
    }

    def 'postOrder traverse is directed'() {
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

        def postOrderList = []

        when:
        graph.postOrder {vertex ->
            postOrderList << vertex.id
            CONTINUE
        }

        then:
        postOrderList == ['step2', 'step5', 'step3', 'step4', 'step1']
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
        def sort = graph.topologicalSort('A')

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
            rpo << vertex.id
        }

        then:
        rpo == ['A', 'E', 'B', 'C', 'D', 'G', 'F']
    }
}
