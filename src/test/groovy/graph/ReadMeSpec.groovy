package graph

import graph.TraversalState
import graph.type.directed.DirectedGraphType
import spock.lang.Specification

import static Graph.graph
import static graph.TraversalState.CONTINUE

class ReadMeSpec extends Specification {
    GroovyShell shell = new GroovyShell(this.class.classLoader)

    def 'base script method'() {
        expect:
        Graph graph = shell.evaluate '''
            import graph.*
            @groovy.transform.BaseScript DslScript script
            plugin 'graphviz'
            edge 'A', 'B'
            assert graph.vertices.keySet() == ['A', 'B'] as Set
            assert graph.edges.size() == 1
            assert graph.edges.first() == new Edge(one:'A', two:'B')
            image 'images/base-script-method.png'
        '''
    }

    def 'graph method'() {
        expect:
        Graph graph = shell.evaluate '''
            import graph.*
            import static graph.Graph.*
            def graph = graph {
                edge 'A', 'B'
            }

            assert graph.vertices.keySet() == ['A', 'B'] as Set
            assert graph.edges.size() == 1
            assert graph.edges.first() == new Edge(one:'A', two:'B')
            graph.plugin 'graphviz'
            graph.image 'images/graph-method.png'
        '''
    }
    
    def 'breadth first traversal'() {
        expect:
        def graph = graph {
            type 'directed-graph'
            vertex('A') {
                connectsTo('B') {
                    connectsTo('C')
                    connectsTo('D')
                }
                connectsFrom('D') {
                    connectsTo('C')
                    connectsTo('E')
                }
                connectsTo('E')
            }
            def count = 1
            breadthFirstTraversal('A') { vertex ->
                vertex.label = "${vertex.key}\\lorder:${count++}\\l"
                vertex.fillcolor = 'green'
                vertex.style = 'filled'
                TraversalState.CONTINUE
            }
            plugin 'graphviz'
            image 'images/breadth-first-traversal.png'
            
        }
    }
    
    def 'preOrder traversal'() {
        expect:
        def graph = graph {
            type 'directed-graph'
            vertex('A') {
                connectsTo('B') {
                    connectsTo('C')
                    connectsTo('D')
                }
                connectsFrom('D') {
                    connectsTo('C')
                    connectsTo('E')
                }
                connectsTo('E')
            }
            def count = 1
            preOrder('A') { vertex ->
                vertex.label = "${vertex.key}\\lorder:${count++}\\l"
                vertex.fillcolor = 'green'
                vertex.style = 'filled'
                TraversalState.CONTINUE
            }
            plugin 'graphviz'
            image 'images/pre-order-traversal.png'
            
        }
    }
    
    def 'postOrder traversal'() {
        expect:
        def graph = graph {
            type 'directed-graph'
            vertex('A') {
                connectsTo('B') {
                    connectsTo('C')
                    connectsTo('D')
                }
                connectsFrom('D') {
                    connectsTo('C')
                    connectsTo('E')
                }
                connectsTo('E')
            }
            def count = 1
            postOrder('A') { vertex ->
                vertex.label = "${vertex.key}\\lorder:${count++}\\l"
                vertex.fillcolor = 'green'
                vertex.style = 'filled'
                TraversalState.CONTINUE
            }
            plugin 'graphviz'
            image 'images/post-order-traversal.png'
        }
    }
    
    def 'reverse post order'() {
        expect:
        def graph = graph {
            type 'directed-graph'
            edge 'compileJava', 'classes'
            edge 'processResources', 'classes'
            vertex('classes') {
                connectsTo 'javadoc'
                connectsTo('compileTestJava') {
                    connectsTo('testClasses') {
                        connectsFrom('processTestResources')
                        connectsTo('test') {
                            connectsFrom 'classes'
                            connectsTo('check') {
                                connectsTo 'build'
                            }
                        }
                    }
                }
                connectsTo('jar') {
                    connectsTo 'uploadArchives'
                    connectsTo('assemble') {
                        connectsTo 'build'
                    }
                }
            }
            def count = 1
            reversePostOrder('javadoc') { vertex ->
                vertex.label = "${vertex.key}\\lorder:${count++}\\l"
                vertex.fillcolor = 'green'
                vertex.style = 'filled'
                CONTINUE
            }
            
            plugin 'graphviz'
            image 'images/reverse-post-order-traversal.png'
        }
    }

    def 'usage 3'() {
        when:
        def graph = graph {
            type DirectedGraphType
            vertex('A') {
                connectsTo 'B', 'D', 'E'
                connectsFrom 'D'
            }

            vertex('D') {
                connectsTo 'C', 'E'
                connectsFrom 'B'
            }

            edge 'B', 'C'
        }

        graph.preOrder('A') { vertex ->
            println "preorder $vertex.key"
            CONTINUE
        }

        graph.breadthFirstTraversal('A') { vertex ->
            println "bft $vertex.key"
            CONTINUE
        }

        then:
        true
    }

    def 'graphviz readme'() {
        given:
        Graph graph = graph {
            type 'directed-graph'
            plugin 'graphviz'
            vertex('A') {
                connectsTo('B') {
                    connectsTo 'C', 'D'
                }
                connectsTo('D') {
                    connectsTo 'C'
                    connectsTo 'E'
                }
                connectsFrom 'D'
            }
            vertex 'F', [connectsTo: 'G']
            edge 'G', 'D'
        }

        expect:
        true
    }
}
