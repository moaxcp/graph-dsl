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
        graph.applySpecToVertexAndGraph(spec, vertex)

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
}
