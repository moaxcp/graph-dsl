package graph.plugin

import graph.Graph
import graph.plugin.EdgeWeightPlugin
import graph.trait.Weight
import graph.type.directed.DirectedGraphPlugin
import spock.lang.Specification

class EdgeWeightPluginSpec extends Specification {
    Graph graph = new Graph()

    def 'can add plugin'() {
        when:
        graph.apply EdgeWeightPlugin

        then:
        graph.plugins.contains(EdgeWeightPlugin)
    }

    def 'adds Weight to all existing edges'() {
        setup:
        graph.edge 'step1', 'step2'
        graph.edge 'step2', 'step3'

        when:
        graph.apply EdgeWeightPlugin

        then:
        graph.edges.every { edge ->
            edge.delegate instanceof Weight
        }
    }

    def 'adds Weight to new edges'() {
        setup:
        graph.apply EdgeWeightPlugin

        when:
        graph.edge 'step1', 'step2'
        graph.edge 'step2', 'step3'

        then:
        graph.edges.every { edge ->
            edge.delegate instanceof Weight
        }
    }

    def 'traverseEdges sorts edges by weight'() {
        setup:
        graph.apply EdgeWeightPlugin
        graph.edge ('step1', 'step4') {
            weight { 3 }
        }
        graph.edge ('step1', 'step2') {
            weight { 1 }
        }
        graph.edge ('step1', 'step3') {
            weight { 2 }
        }

        when:
        def edges = graph.traverseEdges('step1')

        then:
        edges instanceof SortedSet
        edges[0].two == 'step2'
        edges[1].two == 'step3'
        edges[2].two == 'step4'
    }

    def 'after applying DirectedGraphPlugin traverseEdges sorts edges by weight'() {
        setup:
        graph.apply EdgeWeightPlugin
        graph.edge ('step1', 'step4') {
            weight { 3 }
        }
        graph.edge ('step1', 'step2') {
            weight { 1 }
        }
        graph.edge ('step1', 'step3') {
            weight { 2 }
        }
        graph.apply DirectedGraphPlugin

        when:
        def edges = graph.traverseEdges('step1')

        then:
        edges[0].two == 'step2'
        edges[1].two == 'step3'
        edges[2].two == 'step4'
    }

    def 'new edges after applying DirectedGraphPlugin traverseEdges sorts edges by weight'() {
        setup:
        graph.apply EdgeWeightPlugin
        graph.apply DirectedGraphPlugin
        graph.edge ('step1', 'step4') {
            weight { 3 }
        }
        graph.edge ('step1', 'step2') {
            weight { 1 }
        }
        graph.edge ('step1', 'step3') {
            weight { 2 }
        }

        when:
        def edges = graph.traverseEdges('step1')

        then:
        edges[0].two == 'step2'
        edges[1].two == 'step3'
        edges[2].two == 'step4'
    }

    def 'depthFirstTraversal is ordered by weight'() {
        setup:
        graph.apply EdgeWeightPlugin
        graph.edge('step2', 'step5') {
            weight { 4 }
        }
        graph.edge('step2', 'step4') {
            weight { 3 }
        }
        graph.edge('step1', 'step3') {
            weight { 2 }
        }
        graph.edge('step1', 'step2') {
            weight { 1 }
        }

        when:
        def vertices = []
        graph.depthFirstTraversal {
            root = 'step1'
            preorder { vertex ->
                vertices << vertex.name
            }
        }

        then:
        vertices == ['step1', 'step2', 'step4', 'step5', 'step3']
    }
}
