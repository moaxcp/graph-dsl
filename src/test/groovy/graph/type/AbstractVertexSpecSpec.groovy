package graph.type

import graph.Edge
import graph.Graph
import graph.Vertex
import graph.VertexSpec
import graph.type.undirected.UndirectedVertexSpec
import spock.lang.Specification

class AbstractVertexSpecSpec extends Specification {

    Graph graph = new Graph()

    def 'apply throws exception on invalid id'() {
        when:
        new UndirectedVertexSpec(graph, [id:''])

        then:
        thrown IllegalArgumentException
    }

    def 'apply can add vertex'() {
        setup:
        VertexSpec spec = new UndirectedVertexSpec(graph, [id:'step1'])

        when:
        Vertex vertex = spec.apply()

        then:
        graph.vertices[vertex.id] == vertex
    }

    def 'checkCondition throws IllegalStateException when id is null'() {
        given:
        AbstractVertexSpec spec = new UndirectedVertexSpec(graph, [:])
        spec.id = null

        when:
        spec.checkConditions()

        then:
        IllegalStateException e = thrown()
        e.message == 'id is not set.'
    }

    def 'checkCondition throws IllegalStateException when graph is null'() {
        given:
        AbstractVertexSpec spec = new UndirectedVertexSpec(graph, [:])
        spec.id = 'id'
        spec.graph = null

        when:
        spec.checkConditions()

        then:
        IllegalStateException e = thrown()
        e.message == 'graph is not set.'
    }

    def 'checkCondition throws IllegalStateException when vertex is null'() {
        given:
        AbstractVertexSpec spec = new UndirectedVertexSpec(graph, [:])
        spec.id = 'id'
        spec.vertex = null

        when:
        spec.checkConditions()

        then:
        IllegalStateException e = thrown()
        e.message == 'vertex is not set.'
    }

    def 'apply cannot be run twice'() {
        setup:
        VertexSpec spec = new UndirectedVertexSpec(graph, [id:'step1'])

        when:
        Vertex vertex = spec.apply()
        spec.apply()

        then:
        thrown IllegalStateException
    }

    def 'apply can rename vertex'() {
        setup:
        graph.vertex 'step1'
        VertexSpec spec = new UndirectedVertexSpec(graph, [id:'step1', changeId:'step2'])

        when:
        Vertex vertex = spec.apply()

        then:
        graph.vertices.size() == 1
        vertex.id == 'step2'
        graph.vertices[vertex.id] == vertex
    }

    def 'apply can add edges using edgesFirst'() {
        setup:
        VertexSpec spec = new UndirectedVertexSpec(graph, [id:'step1', connectsTo:['step2', 'step3']])

        when:
        spec.apply()

        then:
        graph.vertices.size() == 3
        graph.edges.find { new Edge(from:'step1', to:'step2') }
        graph.edges.find { new Edge(from:'step1', to:'step3') }
    }
}
