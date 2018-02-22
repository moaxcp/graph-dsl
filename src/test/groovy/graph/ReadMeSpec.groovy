package graph

import graph.type.directed.DirectedGraphType
import spock.lang.Specification
import static TraversalState.*
import static Graph.graph

class ReadMeSpec extends Specification {

    def setup() {

    }

    def 'graph method'() {
        when:
        def graph = graph {
            edge 'step1', 'step2'
        }
        then:
        graph.vertices.keySet() == ['step1', 'step2'] as Set //vertices were created!
        graph.edges.size() == 1
        graph.edges.first() == new Edge(one: 'step1', two: 'step2') //edge was created!
    }

    def 'usage 3'() {
        when:
        def graph = graph {
            type DirectedGraphType
            vertex('A') {
                connectsTo 'B', 'D', 'E'
                connectsFrom 'D'
            }

            vertex('D') {
                connectsTo 'C', 'E'
                connectsFrom 'B'
            }

            edge 'B', 'C'
        }

        graph.preOrder('A') { vertex ->
            println "preorder $vertex.key"
            CONTINUE
        }

        graph.breadthFirstTraversal('A') { vertex ->
            println "bft $vertex.key"
            CONTINUE
        }

        then:
        true
    }

    def 'graphviz readme'() {
        given:
        Graph graph = graph {
            type 'directed-graph'
            plugin 'graphviz'
            vertex('A') {
                connectsTo('B') {
                    connectsTo 'C', 'D'
                }
                connectsTo('D') {
                    connectsTo 'C'
                    connectsTo 'E'
                }
                connectsFrom 'D'
            }
            vertex 'F', [connectsTo: 'G']
            edge 'G', 'D'
        }

        expect:
        true
    }
}
