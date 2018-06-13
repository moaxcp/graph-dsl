package graph

import graph.Edge
import graph.EdgeSpec
import graph.Graph
import spock.lang.Specification

class EdgeSpecSpec extends Specification {
    Graph graph = new Graph()

    def 'cannot create with null graph'() {
        when:
        new EdgeSpec(null, null)

        then:
        IllegalArgumentException e = thrown()
        e.message == 'graph must be set.'
    }

    def 'cannot create with null map'() {
        when:
        new EdgeSpec(graph, null)

        then:
        IllegalArgumentException e = thrown()
        e.message == 'map must be set.'
    }


    def 'cannot apply without from'() {
        when:
        new EdgeSpec(graph, [from:null]).apply()

        then:
        thrown IllegalStateException
    }

    def 'cannot apply without two'() {
        when:
        new EdgeSpec(graph, [from:'A', to:null]).apply()

        then:
        thrown IllegalStateException
    }

    def 'can add edge between vertices'() {
        setup:
        graph.vertex('step1')
        graph.vertex('step2')

        when:
        new EdgeSpec(graph, [from:'step1', to:'step2']).apply()

        then:
        graph.edges.size() == 1
        Edge edge = graph.edges.first()
        edge.from == 'step1'
        edge.to == 'step2'
    }

    def 'can add vertices and edge'() {
        when:
        new EdgeSpec(graph, [from:'step1', to:'step2']).apply()

        then:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        Edge edge = graph.edges.first()
        edge.from == 'step1'
        edge.to == 'step2'
    }

    def 'can changeFrom'() {
        setup:
        graph.edge('step1', 'step2')

        when:
        new EdgeSpec(graph, [from:'step1', to:'step2', changeFrom:'step4']).apply()

        then:
        graph.vertices.size() == 3
        graph.vertices.step4.id == 'step4'
        graph.edges.size() == 1
        graph.edges.first().from == 'step4'
        graph.edges.first().to == 'step2'
    }

    def 'can changeTo'() {
        setup:
        graph.edge('step1', 'step2')

        when:
        new EdgeSpec(graph, [from:'step1', to:'step2', changeTo:'step4']).apply()

        then:
        graph.vertices.size() == 3
        graph.vertices.step4.id == 'step4'
        graph.edges.size() == 1
        graph.edges.first().from == 'step1'
        graph.edges.first().to == 'step4'
    }

    def 'cannot apply spec twice'() {
        when:
        EdgeSpec spec = new EdgeSpec(graph, [from:'A', to:'B'])
        spec.apply()

        and:
        spec.apply()

        then:
        IllegalStateException e = thrown()
        e.message == 'spec has already been applied.'
    }

    def 'missing method calls method on edge'() {
        given:
        EdgeSpec spec = new EdgeSpec(graph, [from:'A', to:'B'])
        spec.apply()

        when:
        spec.put('prop', 'value')

        then:
        graph.edges.first().prop == 'value'
    }
}
