package graph.type

import graph.Edge
import graph.EdgeSpec
import graph.Graph
import graph.type.undirected.UndirectedEdgeSpec
import spock.lang.Specification

class GraphEdgeSpecSpec extends Specification {
    Graph graph = new Graph()


    def 'cannot apply without one'() {
        when:
        new UndirectedEdgeSpec(graph, [one:null]).apply()

        then:
        thrown IllegalStateException
    }

    def 'cannot apply without two'() {
        when:
        new UndirectedEdgeSpec(graph, [two:null]).apply()

        then:
        thrown IllegalStateException
    }

    def 'can add edge between vertices'() {
        setup:
        graph.vertex('step1')
        graph.vertex('step2')

        when:
        new UndirectedEdgeSpec(graph, [one:'step1', two:'step2']).apply()

        then:
        graph.edges.size() == 1
        Edge edge = graph.edges.first()
        edge.one == 'step1'
        edge.two == 'step2'
    }

    def 'can add vertices and edge'() {
        when:
        new UndirectedEdgeSpec(graph, [one:'step1', two:'step2']).apply()

        then:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        Edge edge = graph.edges.first()
        edge.one == 'step1'
        edge.two == 'step2'
    }

    def 'can renameOne'() {
        setup:
        graph.edge('step1', 'step2')

        when:
        new UndirectedEdgeSpec(graph, [one:'step1', two:'step2', renameOne:'step4']).apply()

        then:
        graph.vertices.size() == 3
        graph.vertices.step4.key == 'step4'
        graph.edges.size() == 1
        graph.edges.first().one == 'step4'
        graph.edges.first().two == 'step2'
    }

    def 'can renameTwo'() {
        setup:
        graph.edge('step1', 'step2')

        when:
        new UndirectedEdgeSpec(graph, [one:'step1', two:'step2', renameTwo:'step4']).apply()

        then:
        graph.vertices.size() == 3
        graph.vertices.step4.key == 'step4'
        graph.edges.size() == 1
        graph.edges.first().one == 'step1'
        graph.edges.first().two == 'step4'
    }

    def 'apply runs runnerCode'() {
        setup:
        def ran = false
        EdgeSpec spec = new UndirectedEdgeSpec(graph, [one:'step1', two:'step2'], {ran = true})

        when:
        spec.apply()

        then:
        ran
    }
}
