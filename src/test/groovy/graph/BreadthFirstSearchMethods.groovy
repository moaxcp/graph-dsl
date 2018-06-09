package graph

import spock.lang.Specification
import static TraversalState.*

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
        graph.breadthFirstTraversal { vertex ->
            defaultOrder << vertex.id
            CONTINUE
        }
        graph.breadthFirstTraversal('D') { vertex ->
            orderFromD << vertex.id
            CONTINUE
        }
    }

    def 'eachBfs is in breadthFirstOrder'() {
        when:
        def result = []
        graph.eachBfs {
            result << it.id
        }

        then:
        defaultOrder == result
    }

    def 'eachBfs can start at different root'() {
        when:
        def result = []
        graph.eachBfs('D') {
            result << it.id
        }

        then:
        orderFromD == result
    }

    def 'findBfs is in breadth first order'() {
        when:
        def result = []
        graph.findBfs {
            result << it.id
            false
        }

        then:
        defaultOrder == result
    }

    def 'findBfs can start at different root'() {
        when:
        def result = []
        graph.findBfs('D') {
            result << it.id
            false
        }

        then:
        orderFromD == result
    }

    def 'findBfs can find vertex'() {
        when:
        Vertex vertex = graph.findBfs {
            it.id == 'D'
        }

        then:
        vertex.id == 'D'
    }

    def 'injectBfs is in breadth first order'() {
        when:
        def result = []
        graph.injectBfs(result) { list, vertex ->
            list << vertex.id
            list
        }

        then:
        defaultOrder == result
    }

    def 'injectBfs can start at different root'() {
        when:
        def result = []
        graph.injectBfs('D', result) { list, vertex ->
            list << vertex.id
            list
        }

        then:
        orderFromD == result
    }

    def 'findAllBfs is in breadth first order'() {
        when:
        def result = graph.findAllBfs{ true }

        then:
        defaultOrder == result*.id
    }

    def 'findAllBfs can start at different root'() {
        when:
        def result = graph.findAllBfs('D') { true }

        then:
        orderFromD == result*.id
    }

    def 'findAllBfs can find all'() {
        when:
        def result = graph.findAllBfs {
            it.id in ['A', 'C', 'E']
        }

        then:
        result*.id == ['A', 'C', 'E']
    }

    def 'collectBfs is in breadth first order'() {
        when:
        def result = graph.collectBfs { it }

        then:
        defaultOrder == result*.id
    }

    def 'collectBfs can start at different root'() {
        when:
        def result = graph.collectBfs('D') { it }

        then:
        orderFromD == result*.id
    }

    def 'collectBfs can get names'() {
        when:
        def result = graph.collectBfs { it.id }

        then:
        defaultOrder == result
    }
}
