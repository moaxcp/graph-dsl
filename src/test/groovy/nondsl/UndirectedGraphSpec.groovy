package nondsl

import graph.Graph

import java.awt.image.BufferedImage

class UndirectedGraphSpec extends GraphBaseSpec {

    def 'dot is for an undirected graph'() {
        when:
        String dot = graph.dot()

        then:
        '''
            strict graph {
            }
        '''.stripIndent().trim() == dot
    }

    def 'dot renders undirected edges'() {
        given:
        graph.edge('A', 'B')

        when:
        String dot = graph.dot()

        then:
        '''
            strict graph {
              A -- B
            }
        '''.stripIndent().trim() == dot
    }
}
