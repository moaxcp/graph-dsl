package graph

import spock.lang.Specification

class EdgeSpecSpec extends Specification {
    def 'can add traits'() {
        setup:
        EdgeSpec spec = new EdgeSpec()

        when:
        spec.traits(Mapping, Weight)

        then:
        [Mapping, Weight] as Set<Class> == spec.traits
    }

    def 'can config'() {
        setup:
        EdgeSpec spec = new EdgeSpec()

        when:
        spec.config {
            10
        }

        then:
        10 == spec.config.call()
    }

    def 'can add traits in applyToGraphAndEdge'() {
        setup:
        EdgeSpec spec = new EdgeSpec()
        spec.traits Mapping
        Graph graph = new Graph()
        def edge = graph.edge('one', 'two')

        when:
        spec.applyToGraphAndEdge(graph, edge)

        then:
        edge.delegate instanceof Mapping
    }

    def 'can config edge in applyToGraphAndEdge'() {
        setup:
        EdgeSpec spec = new EdgeSpec()
        spec.config = {
            label = 'hello'
        }
        Graph graph = new Graph()
        Edge edge = graph.edge('one', 'two')
        edge.delegateAs(Mapping)

        when:
        spec.applyToGraphAndEdge(graph, edge)

        then:
        edge.label == 'hello'
    }

    def 'can change one and two with applyToGraphAndEdge'() {
        setup:
        EdgeSpec spec = new EdgeSpec()
        spec.one = 'c'
        spec.two = 'd'
        Graph graph = new Graph()
        Edge edge = graph.edge('a', 'b')

        when:
        spec.applyToGraphAndEdge(graph, edge)

        then:
        edge.one == 'c'
        edge.two == 'd'
    }

    def 'can newInstance with closure'() {
        setup:
        Closure closure = {
            one = 'a'
            two = 'b'
        }

        when:
        EdgeSpec spec = EdgeSpec.newInstance(new Graph(), closure)

        then:
        spec.one == 'a'
        spec.two == 'b'
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
}
