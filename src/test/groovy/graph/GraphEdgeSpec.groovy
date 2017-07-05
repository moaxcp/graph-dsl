package graph

import spock.lang.Specification

class GraphEdgeSpec extends Specification {

    def graph = new Graph()

    def 'can add/get with edge(String, String)'() {
        when:
        Edge edge = graph.edge 'step1', 'step2'

        then:
        graph.edges.size() == 1
        graph.edges.first().is edge
        edge.one == 'step1'
        edge.two == 'step2'
    }

    def 'can get with second call to edge(String, String)'() {
        setup:
        Edge expected = graph.edge 'step1', 'step2'

        when:
        Edge result = graph.edge 'step1', 'step2'

        then:
        result.is expected
    }

    def 'can add/get with edge(VertexSpec, VertexSpec)'() {
        when:
        Edge result
        graph.with {
            result = edge step1(), step2()
        }

        then:
        graph.edges.size() == 1
        graph.vertices.size() == 2
        graph.edges.first().one == 'step1'
        graph.edges.first().two == 'step2'
    }

    def 'can add/get with edge(VertexNameSpec, VertexNameSpec)'() {
        when:
        Edge result
        graph.with {
            result = edge step1, step2
        }

        then:
        graph.edges.size() == 1
        graph.vertices.size() == 2
        graph.edges.first().one == 'step1'
        graph.edges.first().two == 'step2'
    }

    def 'can add/get edge with edge(Map)'() {
        when:
        Edge edge = graph.edge one:'step1', two:'step2'

        then:
        graph.edges.size() == 1
        graph.edges.first().is edge
        edge.one == 'step1'
        edge.two == 'step2'
    }

    def 'can get with second call to edge(Map)'() {
        setup:
        Edge expected = graph.edge one:'step1', two:'step2'

        when:
        Edge result = graph.edge one:'step1', two:'step2'

        then:
        result.is expected
    }

    def 'can add traits with edge(Map)'() {
        when:
        Edge edge = graph.edge one:'step1', two:'step2', traits:Mapping

        then:
        edge.delegate instanceof Mapping
    }

    def 'can add/get edge with edge(String, String, Closure)'() {
        when:
        Edge edge = graph.edge 'step1', 'step2', {}

        then:
        graph.edges.size() == 1
        graph.edges.first().is edge
        edge.one == 'step1'
        edge.two == 'step2'
    }

    def 'can get with second call to edge(String, String, Closure)'() {
        setup:
        Edge expected = graph.edge 'step1', 'step2', {}

        when:
        Edge result = graph.edge 'step1', 'step2', {}

        then:
        result.is expected
    }

    def 'can add/get edge with edge(VertexNameSpec, VertexNameSpec, Closure)'() {
        when:
        Edge result
        graph.with {
            result = edge(step1, step2) {}
        }

        then:
        graph.edges.size() == 1
        graph.vertices.size() == 2
        graph.edges.first().one == 'step1'
        graph.edges.first().two == 'step2'
    }

    def 'can add/get edge with edge(VertexSpec, VertexSpec, Closure)'() {
        when:
        Edge result
        graph.with {
            result = edge(step1(), step2()) {}
        }

        then:
        graph.edges.size() == 1
        graph.vertices.size() == 2
        graph.edges.first().one == 'step1'
        graph.edges.first().two == 'step2'
    }

    def 'can add/get edge with edge(String, String, Map)'() {
        when:
        Edge edge = graph.edge 'step1', 'step2', [:]

        then:
        graph.edges.size() == 1
        graph.edges.first().is edge
        edge.one == 'step1'
        edge.two == 'step2'
    }

    def 'can get with second call to edge(String, String, Map)'() {
        setup:
        Edge expected = graph.edge 'step1', 'step2', [:]

        when:
        Edge result = graph.edge 'step1', 'step2', [:]

        then:
        result.is expected
    }

    def 'can add/get with edge(VertexSpec, VertexSpec, Map)'() {
        when:
        Edge result
        graph.with {
            result = edge step1(), step2(), [:]
        }

        then:
        graph.edges.size() == 1
        graph.vertices.size() == 2
        graph.edges.first().one == 'step1'
        graph.edges.first().two == 'step2'
    }

    def 'can add/get with edge(VertexNameSpec, VertexNameSpec, Map)'() {
        when:
        Edge result
        graph.with {
            result = edge step1, step2, [:]
        }

        then:
        graph.edges.size() == 1
        graph.vertices.size() == 2
        graph.edges.first().one == 'step1'
        graph.edges.first().two == 'step2'
    }

    def 'can add/get edge with edge(Map, Closure)'() {
        when:
        Edge edge = graph.edge(one:'step1', two:'step2') {}

        then:
        graph.edges.size() == 1
        graph.edges.first().is edge
    }

    def 'can get with second call to edge(Map, Closure)'() {
        setup:
        Edge expected = graph.edge(one:'step1', two:'step2') {}

        when:
        Edge result = graph.edge(one:'step1', two:'step2') {}

        then:
        result.is expected
    }

    def 'can add/get edge with edge(String, String, Map, Closure'() {
        when:
        Edge edge = graph.edge('step1', 'step2', [:]) {}

        then:
        graph.edges.size() == 1
        graph.edges.first().is edge
        edge.one == 'step1'
        edge.two == 'step2'
    }

    def 'can get with second call to edge(String, String, Map, Closure)'() {
        setup:
        Edge expected = graph.edge('step1', 'step2', [:]) {}

        when:
        Edge result = graph.edge('step1', 'step2', [:]) {}

        then:
        result.is expected
    }

    def 'can add/get edge with edge(VertexNameSpec, VertexNameSpec, Map, Closure)'() {
        when:
        Edge result
        graph.with {
            result = edge step1, step2, [:], {}
        }

        then:
        graph.edges.size() == 1
        graph.vertices.size() == 2
        graph.edges.first().one == 'step1'
        graph.edges.first().two == 'step2'
    }

    def 'can add/get edge with edge(VertexSpec, VertexSpec, Map, Closure)'() {
        when:
        Edge result
        graph.with {
            result = edge step1(), step2(), [:], {}
        }

        then:
        graph.edges.size() == 1
        graph.vertices.size() == 2
        graph.edges.first().one == 'step1'
        graph.edges.first().two == 'step2'
    }

    def 'can add/get edge with edge(EdgeSpec)'() {
        setup:
        EdgeSpec spec = EdgeSpec.newInstance(one:'step1', two:'step2')

        when:
        Edge edge = graph.edge(spec)

        then:
        graph.edges.size() == 1
        graph.edges.first().is edge
        edge.one == 'step1'
        edge.two == 'step2'
    }

    def 'can get with second call to edge(EdgeSpec)'() {
        setup:
        EdgeSpec spec = EdgeSpec.newInstance(one:'step1', two:'step2')
        Edge expected = graph.edge(spec)

        when:
        Edge result = graph.edge(spec)

        then:
        result.is expected
    }

    def 'cannot add duplicate edge with the same order'() {
        setup:
        graph.edge 'step1', 'step2'

        when:
        graph.edge 'step1', 'step2'

        then:
        graph.edges.size() == 1
    }

    def 'cannot add duplicate edge with different order'() {
        setup:
        graph.edge 'step1', 'step2'

        when:
        graph.edge 'step2', 'step1'

        then:
        graph.edges.size() == 1
        graph.edges.first().one == 'step1'
        graph.edges.first().two == 'step2'
    }
}
