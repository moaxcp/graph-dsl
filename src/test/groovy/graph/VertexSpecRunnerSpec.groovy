package graph

import spock.lang.Specification

class VertexSpecRunnerSpec extends Specification {
    VertexSpecCodeRunner runner
    Graph graph = new Graph()
    Vertex vertex

    def setup() {
        vertex = graph.vertex('step1')
        runner = new VertexSpecCodeRunner(graph:graph, vertex:vertex)
    }

    def 'can rename'() {
        when:
        runner.rename('step2')

        then:
        vertex.name == 'step2'
        graph.vertices.size() == 1
        graph.vertices['step2'].name == 'step2'
    }

    def 'can add traits'() {
        when:
        runner.traits Mapping, Weight

        then:
        vertex.delegate instanceof Mapping
        vertex.delegate instanceof Weight
    }

    def 'can add edgesFirst'() {
        when:
        runner.edgesFirst 'step2', 'step3'
        def edges = graph.adjacentEdges('step1')

        then:
        graph.edges.size() == 2
        edges.size() == 2
        edges[0].one == 'step1'
        edges[1].one == 'step1'
    }

    def 'can add edgesSecond'() {
        when:
        runner.edgesSecond 'step2', 'step3'
        def edges = graph.adjacentEdges('step1')

        then:
        graph.edges.size() == 2
        edges.size() == 2
        edges[0].two == 'step1'
        edges[1].two == 'step1'
    }
}
