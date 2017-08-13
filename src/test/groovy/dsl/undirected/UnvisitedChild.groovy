package dsl.undirected

import graph.Graph
import graph.Graph.TraversalColor
import spock.lang.Specification
import static graph.Graph.graph

class UnvisitedChild extends Specification {

    def 'unvisited child with string'() {
        given:
        String name
        graph {
            vertex A {
                connectsTo B
                connectsTo C
            }

            name = getUnvisitedChildName('A', ['A':TraversalColor.BLACK, 'B':TraversalColor.WHITE, 'C':TraversalColor.BLACK])
        }

        expect:
        name == 'B'
    }

    def 'unvisited child with NameSpec'() {
        given:
        String name
        graph {
            vertex A {
                connectsTo B
                connectsTo C
            }

            name = getUnvisitedChildName(A, ['A':TraversalColor.BLACK, 'B':TraversalColor.WHITE, 'C':TraversalColor.BLACK])
        }

        expect:
        name == 'B'
    }

    def 'unvisited child with ConfigSpec'() {
        given:
        String name
        graph {
            vertex A {
                connectsTo B
                connectsTo C
            }

            name = getUnvisitedChildName A(['A':TraversalColor.BLACK, 'B':TraversalColor.WHITE, 'C':TraversalColor.BLACK])
        }

        expect:
        name == 'B'
    }
}
