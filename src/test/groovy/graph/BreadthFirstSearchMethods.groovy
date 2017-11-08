package graph

import spock.lang.Specification

class BreadthFirstSearchMethods extends Specification {

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

    def 'eachBfs is in breadthFirstOrder'() {
        when:
        def result = []
        graph.eachBfs {
            result << it.key
        }

        then:
        defaultOrder == result
    }

    def 'eachBfs can start at different root'() {
        when:
        def result = []
        graph.eachBfs('D') {
            result << it.key
        }

        then:
        orderFromD == result
    }

    def 'findBfs is in breadth first order'() {
        when:
        def result = []
        graph.findBfs {
            result << it.key
            false
        }

        then:
        defaultOrder == result
    }

    def 'findBfs can start at different root'() {
        when:
        def result = []
        graph.findBfs('D') {
            result << it.key
            false
        }

        then:
        orderFromD == result
    }

    def 'findBfs can find vertex'() {
        when:
        Vertex vertex = graph.findBfs {
            it.key == 'D'
        }

        then:
        vertex.key == 'D'
    }

    def 'injectBfs is in breadth first order'() {
        when:
        def result = []
        graph.injectBfs(result) { list, vertex ->
            list << vertex.key
            list
        }

        then:
        defaultOrder == result
    }

    def 'injectBfs can start at different root'() {
        when:
        def result = []
        graph.injectBfs('D', result) { list, vertex ->
            list << vertex.key
            list
        }

        then:
        orderFromD == result
    }

    def 'findAllBfs is in breadth first order'() {
        when:
        def result = graph.findAllBfs{ true }

        then:
        defaultOrder == result*.key
    }

    def 'findAllBfs can start at different root'() {
        when:
        def result = graph.findAllBfs('D') { true }

        then:
        orderFromD == result*.key
    }

    def 'findAllBfs can find all'() {
        when:
        def result = graph.findAllBfs {
            it.key in ['A', 'C', 'E']
        }

        then:
        result*.key == ['A', 'C', 'E']
    }

    def 'collectBfs is in breadth first order'() {
        when:
        def result = graph.collectBfs { it }

        then:
        defaultOrder == result*.key
    }

    def 'collectBfs can start at different root'() {
        when:
        def result = graph.collectBfs('D') { it }

        then:
        orderFromD == result*.key
    }

    def 'collectBfs can get names'() {
        when:
        def result = graph.collectBfs { it.key }

        then:
        defaultOrder == result
    }
}
