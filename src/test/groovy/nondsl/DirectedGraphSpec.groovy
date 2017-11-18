package nondsl

import spock.lang.Specification

class DirectedGraphSpec extends GraphBaseSpec {
    def setup() {
        graph.type('directed-graph')
    }

    def 'dot is for a directed graph'() {
        when:
        String dot = graph.dot()

        then:
        '''
            strict digraph {
            }
        '''.stripIndent().trim() == dot
    }

    def 'dot renders directed edges'() {
        given:
        graph.edge('A', 'B')

        when:
        String dot = graph.dot()

        then:
        '''
            strict digraph {
              A -- B
            }
        '''.stripIndent().trim() == dot
    }
}
