package graph.plugin

import graph.Edge
import graph.Graph
import static graph.Graph.graph
import graph.Vertex
import spock.lang.Specification
import spock.lang.Unroll

class GraphVizSpec extends Specification {
    Graph graph = new Graph()
    GraphViz graphviz = new GraphViz()

    def setup() {
        graphviz.apply(graph)
    }

    def 'edge string is -- for an undirected graph'() {
        expect: 'edgeString in graphviz is --'
        '--' == graphviz.edgeString
    }

    def 'edge string is -> for a directed graph'() {
        given: 'a directed graph with the graphviz plugin applied'
        graph.type 'directed-graph'

        expect: 'edgeString in graphviz is ->'
        '->' == graphviz.edgeString
    }

    def 'dot uses strict graph for undirected graph'() {
        expect: 'graphString is "strict graph"'
        'strict graph' == graphviz.graphString
    }

    def 'dot uses strict digraph for DirectedGraphType'() {
        given:'a directed graph with the graphviz plugin applied'
        graph.type 'directed-graph'

        expect: 'graphString is "strict digraph"'
        'strict digraph' == graphviz.graphString
    }

    @Unroll('"#id" converts to "#expected"')
    def 'ids are converted to dot ids'() {
        expect:
        graphviz.getId(id) == expected

        where:
        id          | expected
        '_'         | '_'
        'a'         | 'a'
        'A'         | 'A'
        'a9'        | 'a9'
        ' '         | '" "'
        '234'       | '234'
        'two words' | '"two words"'
        '%5d'       | '"%5d"'
    }

    def 'dot for edge with no attributes for undirected graph'() {
        given: 'an edge with no attributes'
        Edge edge = new Edge(one:'A', two:'B')

        expect: 'expected format is returned'
        'A -- B' == graphviz.getEdgeDot(edge)
    }

    def 'dot for edge with no attributes for directed graph'() {
        given: 'a directed graph with the graphviz plugin applied'
        graph.type 'directed-graph'

        and: 'an edge with no attributes'
        Edge edge = new Edge(one:'A', two:'B')

        expect: 'expected format is returned'
        'A -> B' == graphviz.getEdgeDot(edge)
    }

    def 'dot for edge with attributes for undirected graph'() {
        given: 'an edge with attributes'
        Edge edge = new Edge(one:'A', two:'B', weight:10, label:'connects to')

        expect: 'expected format is returned'
        'A -- B [weight="10" label="connects to"]' == graphviz.getEdgeDot(edge)
    }

    def 'dot for vertex with no attributes'() {
        given: 'a vertex with no attributes'
        Vertex vertex = new Vertex(key:'A')

        expect: 'expected format is returned'
        'A' == graphviz.getVertexDot(vertex)
    }

    def 'dot for vertex with attributes'() {
        given: 'a vertex with attributes'
        Vertex vertex = new Vertex(key:'A', color:'blue', label:'blue node')

        expect: 'expected format is returned'
        'A [color="blue" label="blue node"]' == graphviz.getVertexDot(vertex)
    }
    
    def 'dot for graph'() {
        given: 'a graph with vertices and edges'
        Graph graph = graph {
            edge('A', 'B', [color:'blue'])
            edge('A', 'C', [color:'red'])
            vertex('Z', [color:'black'])
        }
        graphviz.apply(graph)
        
        expect 'expected format is returned'
        '' == graphviz.dot()
    }
}
