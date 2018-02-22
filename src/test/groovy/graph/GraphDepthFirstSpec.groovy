package graph

import spock.lang.Specification
import static TraversalColor.*
import static TraversalState.*

class GraphDepthFirstSpec extends Specification {


    def 'can get adjacent edges'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
            vertex 'step2'
            vertex 'step3'
            edge 'step1', 'step2'
            edge 'step1', 'step3'
        }

        when:
        def edges = graph.adjacentEdges('step1')

        then:
        edges.size() == 2
        edges.contains(new Edge(one: 'step1', two: 'step2'))
        edges.contains(new Edge(one: 'step1', two: 'step3'))
    }

    def 'can make color map'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
            vertex 'step2'
            vertex 'step3'
        }

        when:
        def colors = graph.makeColorMap()

        then:
        colors == [
                'step1': WHITE,
                'step2': WHITE,
                'step3': WHITE
        ]
    }
}
