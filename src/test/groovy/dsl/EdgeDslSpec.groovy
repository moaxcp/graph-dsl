package dsl

import graph.Graph
import spock.lang.Specification
import static graph.Graph.graph

class EdgeDslSpec extends Specification {
    def 'edge renameOne in closure with VertexNameSpec'() {
        given:
        Graph graph = graph {
            edge(A, B) {
                renameOne C
            }
        }

        expect:
        graph.edges.size() == 1
        graph.edge('C', 'B')
    }

    def 'edge renameTwo in closure with VertexNameSpec'() {
        given:
        Graph graph = graph {
            edge(A, B) {
                renameTwo C
            }
        }

        expect:
        graph.edges.size() == 1
        graph.edge('A', 'C')
    }
}
