package graph

import spock.lang.Specification
import static Graph.graph

class ReadMeSpec extends Specification {

    def setup() {

    }

    def 'usage 1'() {
        when:
        def graph = graph {
            edge step1, step2
        }
        then:
        graph.vertices.keySet() == ['step1', 'step2'] as Set //vertices were created!
        graph.edges.size() == 1
        graph.edges.first() == new Edge(one:'step1', two:'step2') //edge was created!
    }

    def 'usage 2'() {
        when:
        def graph = graph {

        }

        then:
        true
    }

    def 'usage 3'() {
        when:
        def graph = graph {
            apply DirectedGraphPlugin
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
                println "preorder $vertex.name"
            }
        }

        graph.breadthFirstTraversal {
            root = 'A'
            visit { vertex ->
                println "bft $vertex.name"
            }
        }

        then:
        true
    }

    def 'main readme example'() {
        when:
        Graph graph = Graph.graph {
            apply DirectedGraphPlugin
            vertex a {
                connectsTo 'b', 'd'
                connectsFrom 'd'
            }

            vertex renameMe {
                rename 'b'
                connectsTo 'c', 'd'
            }

            vertex d([connectsTo:'c']) {
                connectsTo 'e'
            }

            edge 'f', 'g'
            edge g, d

            println collectBfs { it.name }
        }

        then:
        graph != null
    }

    def '0.13.0 added codeRunner to VertexSpec'() {
        when:
        def graph = Graph.graph {
            vertex step1 {
                traits Mapping
                message = 'hello from step1'
                queue = [] as LinkedList
                traits Weight
                weight { queue.size() }
            }
        }
        def vertex = graph.findBfs { it.name == 'step1' }

        then:
        graph != null
        vertex != null
        vertex.delegate instanceof Mapping
        vertex.delegate instanceof Weight
        graph.vertex('step1').message == 'hello from step1'
        graph.vertex('step1').weight == 0
    }

    def '0.14.0 added codeRunner to EdgeSpec'() {
        when:
        def graph = Graph.graph {
            edge (step1, step2) {
                traits Mapping
                message = "hello from edge between $one and $two"
                queue = [] as LinkedList
                traits Weight
                weight { queue.size() }
            }
        }
        def edge = graph.edges.first()

        then:
        graph != null
        edge != null
        edge.delegate instanceof Mapping
        edge.delegate instanceof Weight
        edge.message == "hello from edge between step1 and step2"
        edge.weight == 0
    }

    def 'test traits'() {
        setup:
        def object = new Object()

        when:
        object = object.withTraits(Mapping)
        object.key = 'value'
        object = object.withTraits(Weight)
        object.weight { 100 }

        then:
        object.key == 'value'
        object.weight == 100
    }
}
