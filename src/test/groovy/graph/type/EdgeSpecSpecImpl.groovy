package graph.type

import graph.Edge
import graph.EdgeSpec
import graph.Graph
import spock.lang.Specification

class EdgeSpecSpecImpl extends Specification {
    Graph graph = new Graph()


    def 'init throws IllegalStateException if edge is set'() {
        given: 'an EdgeSpecImpl with edge set'
        EdgeSpec spec = new EdgeSpec(graph, [:])
        spec.edge = new Edge(from:'one', to:'two')

        when: 'init is called'
        spec.init()

        then: 'IllegalStateException is thrown'
        IllegalStateException e = thrown()
        e.message == 'Edge already created.'
    }

    def 'checkCondition throws IllegalStateException when edge is null'() {
        given: 'an EdgeSpec without edge set'
        EdgeSpec spec = new EdgeSpec(graph, [:])
        spec.from = 'one'
        spec.to = 'two'

        when: 'checkCondition is called'
        spec.checkConditions()

        then: 'IllegalStateException is thrown'
        IllegalStateException e = thrown()
        e.message == 'edge is not set.'
    }

    def 'checkCondition throws IllegalStateException when graph is null'() {
        given: 'an EdgeSpec without graph set'
        EdgeSpec spec = new EdgeSpec(graph, [:])
        spec.from = 'one'
        spec.to = 'two'
        spec.graph = null

        when: 'checkCondition is called'
        spec.checkConditions()

        then: 'IllegalStateException is thrown'
        IllegalStateException e = thrown()
        e.message == 'graph is not set.'
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
}
