package graph

import spock.lang.Specification

class VertexSpecSpec extends Specification {

    def graph = new Graph()

    def 'can configure vertex traits with VertexSpec.config(Closure) in vertex(Closure)'() {
        when:
        Vertex vertex = graph.vertex {
            name = 'step1'
            traits Mapping, Weight
            config {
                label = 'the first step'
                weight { 100 }
            }
        }

        then:
        vertex.label == 'the first step'
        vertex.getWeight() == 100
    }

    def 'can add edges using connectsTo with applySpecToVertexAndGraph(VertexSpec, Vertex)'() {
        setup:
        Vertex vertex = graph.vertex 'step1'
        VertexSpec spec = new VertexSpec()
        spec.connectsTo 'step2', 'step3'

        when:
        spec.applyToGraphAndVertex(graph, vertex)

        then:
        graph.edges.find { new Edge(one:'step1', two:'step2') }
        graph.edges.find { new Edge(one:'step1', two:'step3') }
    }

    def 'can add traits with applyToGraphAndVertex(Graph, Vertex)'() {
        setup:
        Vertex vertex = new Vertex(name:'step1')
        VertexSpec spec = new VertexSpec()
        spec.traits Mapping, Weight

        when:
        spec.applyToGraphAndVertex(graph, vertex)

        then:
        vertex.delegate instanceof Mapping
        vertex.delegate instanceof Weight
    }

    def 'can make VertexSpec with Closure'() {
        when:
        VertexSpec spec = VertexSpec.newInstance(graph) {
            name = 'step1'
            traits Mapping, Weight
            connectsTo 'step2', 'step3'
            config {
                label = 'the first step'
                weight 100
            }
        }

        then:
        spec.name == 'step1'
        spec.traits == [Mapping, Weight] as Set<Class>
        spec.connectsTo == ['step2', 'step3'] as Set<String>
        spec.config != null
        spec.config instanceof Closure
    }

    def 'can rename with applyToGraphAndVertex(Graph, Vertex)'() {
        setup:
        graph.vertex 'step1'
        graph.vertex 'step2'
        graph.vertex 'step3'
        graph.vertex 'step4'
        graph.edge 'step1', 'step2'
        graph.edge 'step1', 'step3'
        graph.edge 'step4', 'step1'
        VertexSpec spec = new VertexSpec()
        spec.name = 'step5'

        when:
        spec.applyToGraphAndVertex(graph, graph.vertex('step1'))

        then:
        graph.adjacentEdges('step1').size() == 0
        graph.adjacentEdges('step5').size() == 3
    }

    def 'does not rename to false name applyToGraphAndVertex(Graph, Vertex)'() {
        setup:
        graph.vertex 'step1'
        graph.vertex 'step2'
        graph.vertex 'step3'
        graph.vertex 'step4'
        graph.edge 'step1', 'step2'
        graph.edge 'step1', 'step3'
        graph.edge 'step4', 'step1'
        VertexSpec spec = new VertexSpec()
        spec.name = ''

        when:
        spec.applyToGraphAndVertex(graph, graph.vertex('step1'))

        then:
        graph.adjacentEdges('step1').size() == 3
        graph.adjacentEdges('').size() == 0
    }
}
