package graph

import spock.lang.Specification

class GraphBreadthFirstSearchSpec extends Specification {
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
        spec.colors == ['step1' : Graph.DepthFirstTraversalColor.WHITE]
        spec.visit != null
    }
}
