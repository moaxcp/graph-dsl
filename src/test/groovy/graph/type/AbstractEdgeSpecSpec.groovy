package graph.type

import graph.Edge
import graph.EdgeSpec
import graph.Graph
import graph.type.undirected.UndirectedEdgeSpec
import spock.lang.Specification

class AbstractEdgeSpecSpec extends Specification {
    Graph graph = new Graph()

    class TestEdgeSpec extends AbstractEdgeSpec {

        protected TestEdgeSpec(Graph graph, Map<String, ?> map, Closure closure = null) {
            super(graph, map, closure)
        }

        @Override
        protected void applyClosure() {

        }
    }

    def 'init throws IllegalStateException if edge is set'() {
        given: 'an AbstractEdgeSpec with edge set'
        AbstractEdgeSpec spec = new TestEdgeSpec(graph, [:])
        spec.edge = new Edge(one:'one', two:'two')

        when: 'init is called'
        spec.init()

        then: 'IllegalStateException is thrown'
        IllegalStateException e = thrown()
        e.message == 'Edge already created.'
    }

    def 'checkCondition throws IllegalStateException when edge is null'() {
        given: 'an AbstractEdgeSpec without edge set'
        AbstractEdgeSpec spec = new TestEdgeSpec(graph, [:])
        spec.one = 'one'
        spec.two = 'two'

        when: 'checkCondition is called'
        spec.checkConditions()

        then: 'IllegalStateException is thrown'
        IllegalStateException e = thrown()
        e.message == 'edge is not set.'
    }

    def 'checkCondition throws IllegalStateException when graph is null'() {
        given: 'an AbstractEdgeSpec without graph set'
        AbstractEdgeSpec spec = new TestEdgeSpec(graph, [:])
        spec.one = 'one'
        spec.two = 'two'
        spec.graph = null

        when: 'checkCondition is called'
        spec.checkConditions()

        then: 'IllegalStateException is thrown'
        IllegalStateException e = thrown()
        e.message == 'graph is not set.'
    }


    def 'cannot apply without one'() {
        when:
        new TestEdgeSpec(graph, [one:null]).apply()

        then:
        thrown IllegalStateException
    }

    def 'cannot apply without two'() {
        when:
        new TestEdgeSpec(graph, [two:null]).apply()

        then:
        thrown IllegalStateException
    }

    def 'can add edge between vertices'() {
        setup:
        graph.vertex('step1')
        graph.vertex('step2')

        when:
        new TestEdgeSpec(graph, [one:'step1', two:'step2']).apply()

        then:
        graph.edges.size() == 1
        Edge edge = graph.edges.first()
        edge.one == 'step1'
        edge.two == 'step2'
    }

    def 'can add vertices and edge'() {
        when:
        new TestEdgeSpec(graph, [one:'step1', two:'step2']).apply()

        then:
        graph.vertices.size() == 2
        graph.edges.size() == 1
        Edge edge = graph.edges.first()
        edge.one == 'step1'
        edge.two == 'step2'
    }

    def 'can changeOne'() {
        setup:
        graph.edge('step1', 'step2')

        when:
        new TestEdgeSpec(graph, [one:'step1', two:'step2', changeOne:'step4']).apply()

        then:
        graph.vertices.size() == 3
        graph.vertices.step4.key == 'step4'
        graph.edges.size() == 1
        graph.edges.first().one == 'step4'
        graph.edges.first().two == 'step2'
    }

    def 'can changeTwo'() {
        setup:
        graph.edge('step1', 'step2')

        when:
        new TestEdgeSpec(graph, [one:'step1', two:'step2', changeTwo:'step4']).apply()

        then:
        graph.vertices.size() == 3
        graph.vertices.step4.key == 'step4'
        graph.edges.size() == 1
        graph.edges.first().one == 'step1'
        graph.edges.first().two == 'step4'
    }
}
