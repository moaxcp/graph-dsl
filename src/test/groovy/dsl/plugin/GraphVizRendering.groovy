package dsl.plugin

import graph.Graph
import static graph.Graph.graph
import spock.lang.Specification

class GraphVizRendering extends Specification {

    def 'dot renders separate components of graph'() {
        given: 'graph with components'
        Graph graph = graph {
            plugin 'graphviz'
            vertex('A') {
                connectsTo('B') {
                    connectsTo 'C'
                }
                connectsTo 'C'
            }

            vertex('Z') {
                connectsTo('X') {
                    connectsTo('Y')
                }
                connectsTo('W') {
                    connectsTo 'X'
                }
            }
            vertex 'J'
        }

        expect: 'dot renders all components'
        '''
            strict graph {
              A -- B
              B -- C
              A -- C
              Z -- X
              X -- Y
              Z -- W
              W -- X
              J
            }
        '''.stripIndent().trim() == graph.dot()
    }
}
