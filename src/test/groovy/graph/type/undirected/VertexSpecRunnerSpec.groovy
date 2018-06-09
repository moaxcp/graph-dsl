package graph.type.undirected

import graph.Graph
import graph.Vertex
import spock.lang.Specification

class VertexSpecRunnerSpec extends Specification {
    VertexSpecCodeRunner runner
    Graph graph = new Graph()
    Vertex vertex

    def setup() {
        vertex = graph.vertex('step1')
        runner = new VertexSpecCodeRunner(graph, vertex)
    }

    def 'can rename'() {
        when:
        runner.changeId('step2')

        then:
        vertex.id == 'step2'
        graph.vertices.size() == 1
        graph.vertices['step2'].id == 'step2'
    }

    def 'can add with connectsTo'() {
        when:
        runner.connectsTo 'step2', 'step3'
        def edges = graph.adjacentEdges('step1')

        then:
        graph.edges.size() == 2
        edges.size() == 2
        edges[0].one == 'step1'
        edges[1].one == 'step1'
    }
}
