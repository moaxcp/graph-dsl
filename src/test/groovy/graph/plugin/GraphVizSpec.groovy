package graph.plugin

import graph.Graph
import graph.plugin.GraphViz
import spock.lang.Specification
import spock.lang.Unroll

class GraphVizSpec extends Specification {
    Graph graph = new Graph()
    GraphViz graphviz = new GraphViz()

    def 'edge string is -- for an undirected graph'() {
        given: 'an undirected graph with the graphviz plugin applied'
        graphviz.apply(graph)

        expect: 'edgeString in graphviz is --'
        '--' == graphviz.edgeString
    }

    def 'edge string is -> for a directed graph'() {
        given: 'a directed graph with the graphviz plugin applied'
        graph.type 'directed-graph'
        graphviz.apply(graph)

        expect: 'edgeString in graphviz is ->'
        '->' == graphviz.edgeString
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
        '234'       | '"234"'
        'two words' | '"two words"'

    }
}
