package dsl.undirected

import graph.TraversalColor
import spock.lang.Specification
import static graph.Graph.graph

class UnvisitedChild extends Specification {

    def 'unvisited child with string'() {
        given:
        String name
        graph {
            vertex ('A') {
                connectsTo 'B'
                connectsTo 'C'
            }

            name = getUnvisitedChildId('A', ['A':TraversalColor.BLACK, 'B':TraversalColor.WHITE, 'C':TraversalColor.BLACK])
        }

        expect:
        name == 'B'
    }
}
