package graph

import spock.lang.Specification
import static graph.Graph.TraversalColor.*

class GraphBreadthFirstSpec extends Specification {
    def 'can breadthFirstTraversalSpec'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
        }

        when:
        def spec = graph.breadthFirstTraversalSpec {
            visit {
                //do nothing
            }
        }

        then:
        spec.root == 'step1'
        spec.colors == ['step1' : graph.Graph.TraversalColor.WHITE]
        spec.visit != null
    }

    def 'breadthFirstTraversalConnected visit'() {
        setup:
        def graph = new Graph()
        graph.vertex 'step1'
        graph.vertex 'step2'
        graph.vertex 'step3'
        graph.vertex 'step4'
        graph.edge 'step1', 'step2'
        graph.edge 'step1', 'step4'
        graph.edge 'step2', 'step3'

        def visitList = []

        def spec = new BreadthFirstTraversalSpec()
        spec.colors = graph.makeColorMap()
        spec.visit { vertex ->
            visitList << vertex.name
        }

        when:
        def traversal = graph.breadthFirstTraversalConnected 'step1', spec

        then:
        traversal != Graph.Traversal.STOP
        spec.colors == [
            step1: BLACK, step2: BLACK, step3: BLACK, step4: BLACK
        ]
        visitList == ['step1', 'step2', 'step4', 'step3']
    }
}
