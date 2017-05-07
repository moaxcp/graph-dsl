package graph

import spock.lang.Specification

class ReadMeSpec extends Specification {

    def setup() {

    }

    def 'main readme example'() {
        when:
        Graph graph = Graph.graph {
            apply DirectedGraphPlugin
            vertex a {
                edgesFirst 'b', 'd'
                edgesSecond 'd'
            }

            vertex renameMe {
                rename 'b'
                edgesFirst 'c', 'd'
            }

            vertex d([edgesFirst:'c']) {
                edgesFirst 'e'
            }

            edge 'f', 'g'
            edge 'g', 'd'

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
