package graph

import graph.type.EdgeSpec
import spock.lang.Specification

class EdgeSpecSpec extends Specification {
    EdgeSpec spec = new EdgeSpec()
    Graph graph = new Graph()

    def setup() {

    }

    def 'can add traits'() {
        when:
        spec.traits(Mapping, Weight)

        then:
        [Mapping, Weight] as Set<Class> == spec.traits
    }

    def 'can add runnerCode'() {
        when:
        spec.runnerCode {
            10
        }

        then:
        10 == spec.runnerCode.call()
    }

    def 'cannot apply without one'() {
        when:
        spec.one = null
        spec.apply(graph)

        then:
        thrown IllegalArgumentException
    }

    def 'cannot apply without two'() {
        when:
        spec.two = null
        spec.apply(graph)

        then:
        thrown IllegalArgumentException
    }

    def 'can add edge between vertices'() {
        setup:
        graph.vertex('step1')
        graph.vertex('step2')

        when:
        spec.one = 'step1'
        spec.two = 'step2'
        spec.apply(graph)

        then:
        graph.edges.size() == 1
        Edge edge = graph.edges.first()
        edge.one == 'step1'
        edge.two == 'step2'
    }

    def 'can add vertices and edge'() {
        when:
        spec.one = 'step1'
        spec.two = 'step2'
        spec.apply(graph)

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
        spec.one = 'step1'
        spec.two = 'step2'
        spec.renameOne = 'step4'

        when:
        spec.apply(graph)

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
        spec.one = 'step1'
        spec.two = 'step2'
        spec.renameTwo = 'step4'

        when:
        spec.apply(graph)

        then:
        graph.vertices.size() == 3
        graph.vertices.step4.name == 'step4'
        graph.edges.size() == 1
        graph.edges.first().one == 'step1'
        graph.edges.first().two == 'step4'
    }

    def 'can add traits in apply'() {
        setup:
        spec.traits Mapping
        spec.one = 'step1'
        spec.two = 'step2'
        def edge = graph.edge('step1', 'step2')

        when:
        spec.apply(graph)

        then:
        edge.delegate instanceof Mapping
    }

    def 'apply runs runnerCode'() {
        setup:
        spec.one = 'step1'
        spec.two = 'step2'
        def ran = false
        spec.runnerCode {
            ran = true
        }

        when:
        spec.apply(graph)

        then:
        ran
    }

    def 'can newInstance with map'() {
        setup:
        Map map = [one:'a', two:'b']

        when:
        EdgeSpec spec = EdgeSpec.newInstance(map)

        then:
        spec.one == 'a'
        spec.two == 'b'
    }

    def 'overlay with null'() {
        setup:
        EdgeSpec first = new EdgeSpec()
        EdgeSpec second = new EdgeSpec()
        second.one = 'step1'
        second.two = 'step2'
        second.renameOne = 'step3'
        second.renameTwo = 'step4'
        second.traits Mapping
        second.runnerCode {}

        when:
        EdgeSpec result = first.overlay(second)

        then:
        result.one == 'step1'
        result.two == 'step2'
        result.renameOne == 'step3'
        result.renameTwo == 'step4'
        result.traits == [Mapping] as Set<Class>
        result.runnerCode != null
    }
}
