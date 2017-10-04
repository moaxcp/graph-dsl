package graph.type

import graph.Edge
import graph.Graph
import graph.trait.Mapping
import graph.type.undirected.GraphEdgeSpec
import spock.lang.Specification

class GraphEdgeSpecSpec extends Specification {
    Graph graph = new Graph()


    def 'cannot apply without one'() {
        when:
        GraphEdgeSpec spec = new GraphEdgeSpec(graph, [one:null])
        spec.setup()

        then:
        thrown IllegalArgumentException
    }

    def 'cannot apply without two'() {
        when:
        GraphEdgeSpec spec = new GraphEdgeSpec(graph, [two:null])
        spec.setup()

        then:
        thrown IllegalArgumentException
    }

    def 'can add edge between vertices'() {
        setup:
        graph.vertex('step1')
        graph.vertex('step2')

        when:
        GraphEdgeSpec spec = new GraphEdgeSpec(graph, [one:'step1', two:'step2'])
        spec.setup()

        then:
        graph.edges.size() == 1
        Edge edge = graph.edges.first()
        edge.one == 'step1'
        edge.two == 'step2'
    }

    def 'can add vertices and edge'() {
        when:
        GraphEdgeSpec spec = new GraphEdgeSpec(graph, [one:'step1', two:'step2'])
        spec.setup()

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
        GraphEdgeSpec spec = new GraphEdgeSpec(graph, [one:'step1', two:'step2', renameOne:'step4'])
        spec.setup()

        then:
        graph.vertices.size() == 3
        graph.vertices.step4.name == 'step4'
        graph.edges.size() == 1
        graph.edges.first().one == 'step4'
        graph.edges.first().two == 'step2'
    }

    def 'can renameTwo'() {
        setup:
        graph.edge('step1', 'step2')

        when:
        GraphEdgeSpec spec = new GraphEdgeSpec(graph, [one:'step1', two:'step2', renameTwo:'step4'])
        spec.setup()

        then:
        graph.vertices.size() == 3
        graph.vertices.step4.name == 'step4'
        graph.edges.size() == 1
        graph.edges.first().one == 'step1'
        graph.edges.first().two == 'step4'
    }

    def 'can add traits in apply'() {
        setup:
        GraphEdgeSpec spec = new GraphEdgeSpec(graph, [one:'step1', two:'step2', traits:Mapping])
        def edge = graph.edge('step1', 'step2')

        when:
        spec.setup()

        then:
        edge.delegate instanceof Mapping
    }

    def 'apply runs runnerCode'() {
        setup:
        def ran = false
        GraphEdgeSpec spec = new GraphEdgeSpec(graph, [one:'step1', two:'step2'], {ran = true})

        when:
        spec.setup()

        then:
        ran
    }
}
