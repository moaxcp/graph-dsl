package graph.plugin.graphviz

import graph.Graph
import spock.lang.Ignore
import spock.lang.Specification
import sun.util.resources.cldr.luo.CalendarData_luo_KE

import java.awt.image.AreaAveragingScaleFilter

import static org.hamcrest.core.StringStartsWith.startsWith

class GraphVizSpec extends Specification {
    Graph graph = new Graph()

    def 'dot is for an undirected graph'() {
        given: 'an undirected graph with the graphviz plugin'
        graph.plugin 'graphviz'

        when: 'the dot dsl is created'
        String dot = graph.dot()

        then: 'it is an empty strict graph'
        '''
            strict graph {
            }
        '''.stripIndent().trim() == dot
    }

    def 'dot renders undirected edges'() {
        given: 'an undirected graph with the graphviz plugin'
        graph.plugin 'graphviz'

        and: 'vertices with an edge A, B'
        graph.edge('A', 'B')

        when: 'the dot dsl is created'
        String dot = graph.dot()

        then: 'it contains a strict graph'
        dot.startsWith('strict graph {')

        and: 'it contains the edge'
        '''
            strict graph {
              A -- B
            }
        '''.stripIndent().trim() == dot
    }

    def 'dot is for a directed graph'() {
        given: 'a directed graph with the graphviz plugin'
        graph.type 'directed-graph'
        graph.plugin 'graphviz'

        when: 'the dot dsl is created'
        String dot = graph.dot()

        then: 'it contains an empty strict digraph'
        '''
            strict digraph {
            }
        '''.stripIndent().trim() == dot
    }

    def 'dot renders directed edges'() {
        given: 'a directed graph with the graphviz plugin'
        graph.type 'directed-graph'
        graph.plugin 'graphviz'

        and: 'vertices with an edge A, B'
        graph.edge('A', 'B')

        when: 'the dot dsl is created'
        String dot = graph.dot()

        then: 'it contains a strict digraph'
        dot startsWith('strict digraph {')

        and: 'it contains the edge'
        '''
            strict digraph {
              A -> B
            }
        '''.stripIndent().trim() == dot
    }
}
