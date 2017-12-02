package nondsl.graphviz

import graph.Graph
import spock.lang.Ignore
import spock.lang.Specification
import sun.util.resources.cldr.luo.CalendarData_luo_KE

import java.awt.image.AreaAveragingScaleFilter

import static org.hamcrest.core.StringStartsWith.startsWith

class GraphVizSpec extends Specification {
    Graph graph = new Graph()

    def setup() {
        graph.plugin 'graphviz'
    }

    def 'dot for empty undirected graph is an empty strict graph'() {
        expect: 'dot is an empty strict graph'
        '''
            strict graph {
            }
        '''.stripIndent().trim() == graph.dot()
    }

    def 'dot renders undirected edges'() {
        given: 'vertices with an edge A, B'
        graph.edge('A', 'B')

        expect: 'dot is a strict graph with edge A, B'
        '''
            strict graph {
              A -- B
            }
        '''.stripIndent().trim() == graph.dot()
    }

    def 'dot for an empty directed graph is an empty strict digraph'() {
        given: 'a directed graph with the graphviz plugin'
        graph.type 'directed-graph'

        expect: 'dot is an empty strict digraph'
        '''
            strict digraph {
            }
        '''.stripIndent().trim() == graph.dot()
    }

    def 'dot renders directed edges'() {
        given: 'a directed graph with the graphviz plugin'
        graph.type 'directed-graph'

        and: 'vertices with an edge A, B'
        graph.edge('A', 'B')

        expect: 'dot is a strict digraph with edge A, B'
        '''
            strict digraph {
              A -> B
            }
        '''.stripIndent().trim() == graph.dot()
    }

    def 'dot renders weight for weighted graph'() {
        given: 'a weighted graph with the graphviz plugin'
        graph.type 'weighted-graph'

        expect: 'it contains an empty strict graph'
        '''
            strict graph {
            }
        '''.stripIndent().trim() == graph.dot()
    }

    def 'dot renders default weighted edges'() {
        given: 'a weighted graph with the graphviz plugin'
        graph.type 'weighted-graph'

        and: 'vertices with an edge A, B'
        graph.edge('A', 'B')

        expect: 'it contains an empty strict graph'
        '''
            strict graph {
              A -- B
            }
        '''.stripIndent().trim() == graph.dot()
    }

    def 'dot renders weighted edges'() {
        given: 'a weighted graph with the graphviz plugin'
        graph.type 'weighted-graph'

        and: 'vertices with an edge A, B'
        graph.edge('A', 'B', [weight:10])

        expect: 'it contains an empty strict graph'
        '''
            strict graph {
              A -- B [weight="10"]
            }
        '''.stripIndent().trim() == graph.dot()
    }

    def 'dot renders vertex attributes'() {
        given: 'vertices with an edge A, B'
        graph.edge 'A', 'B'

        and: 'vertex A has attributes'
        graph.vertex 'A', [color:'blue']

        expect: 'dot contains vertex A with attributes'
        '''
            strict graph {
              A -- B
              A [color="blue"]
            }
        '''.stripIndent().trim() == graph.dot()
    }
}
