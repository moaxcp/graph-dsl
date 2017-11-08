package dsl.undirected

import graph.Graph
import spock.lang.Specification

class BFSMethods extends Specification {
    Graph graph = new Graph()
    def defaultOrder = []
    def orderFromD = []

    def setup() {
        graph.with {
            vertex 'A', [connectsTo:['B', 'D'], connectsFrom:['D']]
            vertex 'B', [connectsTo:['C', 'D']]
            vertex 'D', [connectsTo:['C', 'E']]
        }
        graph.breadthFirstTraversal {
            visit { vertex ->
                defaultOrder << vertex.key
            }
        }
        graph.breadthFirstTraversal {
            root = 'D'
            visit { vertex ->
                orderFromD << vertex.key
            }
        }
    }

    def 'eachFbs with NameSpec'() {
        when:
        def result = []
        graph.with {
            eachBfs(D) {
                result << it.key
            }
        }

        then:
        orderFromD == result
    }

    def 'findBfs with NameSpec'() {
        when:
        def vertex
        graph.with {
            vertex = findBfs(D) {
                it.key == 'D'
            }
        }

        then:
        vertex.key == 'D'
    }

    def 'injectBfs with NameSpec'() {
        when:
        def result
        graph.with {
            result = injectBfs(D, []) { list, vertex ->
                list << vertex.key
            }
        }

        then:
        orderFromD == result
    }

    def 'findAllBfs with NameSpec'() {
        when:
        def result
        graph.with {
            result = findAllBfs(D) {
                true
            }
        }

        then:
        orderFromD == result*.key
    }

    def 'collectBfs with NameSpec'() {
        when:
        def result
        graph.with {
            result = collectBfs(D) { it.key }
        }

        then:
        orderFromD == result
    }
}
