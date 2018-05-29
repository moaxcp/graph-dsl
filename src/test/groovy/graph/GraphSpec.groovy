package graph

import graph.type.directed.DirectedEdge
import spock.lang.Specification

class GraphSpec extends Specification {

    def 'can use graph method as expected'() {
        setup:
        def c = {
            vertex 'step1'
        }

        when:
        def graph = Graph.graph(c)

        then:
        graph.vertices.size() == 1
    }

    def 'can delete an unconnected Vertex'() {
        setup:
        Graph graph = new Graph()
        graph.vertex 'step1'

        when:
        graph.delete('step1')

        then:
        graph.vertices.size() == 0
    }

    def 'cannot delete connected vertex'() {
        setup:
        Graph graph = new Graph()
        graph.edge 'step1', 'step2'

        when:
        graph.delete('step1')

        then:
        thrown IllegalStateException
    }

    def 'can delete edge'() {
        setup:
        Graph graph = new Graph()
        graph.edge 'step1', 'step2'

        when:
        graph.deleteEdge('step1', 'step2')

        then:
        graph.edges.size() == 0
    }

    def 'can replaceEdges'() {
        setup:
        Graph graph = new Graph()
        graph.edge 'step1', 'step2'
        graph.edge 'step1', 'step3'
        graph.edge 'step2', 'step3'
        graph.edge 'step2', 'step4'

        when:
        graph.replaceEdges {
            new DirectedEdge(it)
        }

        then:
        graph.edges.size() == 4
        graph.edges.every { it instanceof DirectedEdge }
    }

    def 'can replaceEdgesSet'() {
        setup:
        Graph graph = new Graph()
        graph.edge 'step1', 'step2'
        graph.edge 'step1', 'step3'
        graph.edge 'step2', 'step3'
        graph.edge 'step2', 'step4'

        when:
        graph.replaceEdgesSet(new TreeSet<>(new OrderBy([{ it.one }, { it.two }])))

        then:
        graph.edges.size() == 4
    }

    def 'set must be empty in replaceEdgesSet'() {
        setup:
        Graph graph = new Graph()
        graph.edge 'step1', 'step2'
        graph.edge 'step1', 'step3'
        graph.edge 'step2', 'step3'
        graph.edge 'step2', 'step4'

        when:
        def set = new TreeSet<>(new OrderBy([{ it.one }, { it.two }]))
        set.add(new Edge(one:'step5', two:'step6'))
        graph.replaceEdgesSet(set)

        then:
        thrown IllegalArgumentException
    }

    def 'cannot replace vertices map unless empty'() {
        setup:
        Graph graph = new Graph()

        when:
        graph.replaceVerticesMap([step1:new Vertex(key:'step1')])

        then:
        thrown IllegalArgumentException
    }
}
