package graph

import graph.type.directed.DirectedGraphType
import spock.lang.Specification
import static Graph.graph
import graph.EdgeClassification.EdgeType
import static graph.EdgeClassification.EdgeType.*

class ReadMeSpec extends Specification {

    def setup() {

    }

    def 'graph method'() {
        when:
        def graph = graph {
            edge step1, step2
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
            vertex A {
                connectsTo 'B', 'D', 'E'
                connectsFrom 'D'
            }

            vertex D {
                connectsTo 'C', 'E'
                connectsFrom 'B'
            }

            edge B, C
        }

        graph.depthFirstTraversal {
            root = 'A'
            preorder { vertex ->
                println "preorder $vertex.key"
            }
        }

        graph.breadthFirstTraversal {
            root = 'A'
            visit { vertex ->
                println "bft $vertex.key"
            }
        }

        then:
        true
    }

    def 'main readme example'() {
        when:
        Graph graph = Graph.graph {
            type DirectedGraphType
            vertex a {
                connectsTo 'b', 'd'
                connectsFrom 'd'
            }

            vertex d([connectsTo:'c']) {
                connectsTo 'e'
            }

            edge 'f', 'g'
            edge g, d

            println collectBfs { it.key }
        }

        then:
        graph != null
    }

    def 'graphviz readme'() {
        given:
        Graph graph = graph {
            type 'directed-graph'
            plugin 'graphviz'
            vertex A {
                connectsTo B {
                    connectsTo C, D
                }
                connectsTo D {
                    connectsTo C
                    connectsTo E
                }
                connectsFrom D
            }
            vertex F, [connectsTo:G]
            edge G, D
        }

        expect:
        true
    }
}
