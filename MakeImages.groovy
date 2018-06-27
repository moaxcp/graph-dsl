import graph.EdgeType

import static graph.Graph.graph
import static graph.TraversalState.*

shell = new GroovyShell(this.class.classLoader)

usageBaseScriptMethod()
usageGraphMethod()
exampleBreadthFirstTraversal()
examplePreOrderTraversal()
examplePostOrderTraversal()
exampleReversePostOrderTraversal()
exampleClassifyEdges()
exampleConnectedComponentUndirected()
exampleConnectedComponentDirected()

def usageBaseScriptMethod() {
    shell.evaluate '''
            import graph.*
            @groovy.transform.BaseScript DslScript script
            plugin 'graphviz'
            edge 'A', 'B'
            assert graph.vertices.keySet() == ['A', 'B'] as Set
            assert graph.edges.size() == 1
            assert graph.edges.first() == new Edge(from:'A', to:'B')
            image 'images/base-script-method.png'
        '''
}

def usageGraphMethod() {
    shell.evaluate '''
            import graph.*
            import static graph.Graph.*
            def graph = graph {
                edge 'A', 'B'
            }

            assert graph.vertices.keySet() == ['A', 'B'] as Set
            assert graph.edges.size() == 1
            assert graph.edges.first() == new Edge(from:'A', to:'B')
            graph.plugin 'graphviz'
            graph.image 'images/graph-method.png'
        '''
}

def exampleBreadthFirstTraversal() {
    graph {
        type 'directed-graph'
        plugin 'graphviz'
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
        snapshot()
        eachBfs('A') { vertex ->
            vertex.fillcolor = 'green'
            vertex.style = 'filled'
            snapshot()
        }
        gif 'images/breadth-first-traversal.gif'
    }
}

def examplePreOrderTraversal() {
    graph {
        type 'directed-graph'
        plugin 'graphviz'
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
        snapshot()
        preOrder('A') { vertex ->
            vertex.fillcolor = 'green'
            vertex.style = 'filled'
            snapshot()
            CONTINUE
        }
        gif 'images/pre-order-traversal.gif'
    }
}

def examplePostOrderTraversal() {
    graph {
        type 'directed-graph'
        plugin 'graphviz'
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
        snapshot()
        postOrder('A') { vertex ->
            vertex.fillcolor = 'green'
            vertex.style = 'filled'
            snapshot()
            CONTINUE
        }
        gif 'images/post-order-traversal.gif'
    }
}

def exampleReversePostOrderTraversal() {
    graph {
        type 'directed-graph'
        plugin 'graphviz'
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
        snapshot()
        reversePostOrder('build') { vertex ->
            vertex.fillcolor = 'green'
            vertex.style = 'filled'
            snapshot()
            CONTINUE
        }
        gif 'images/reverse-post-order-traversal.gif'
    }
}

def exampleClassifyEdges() {
    graph {
        type 'directed-graph'
        plugin 'graphviz'
        vertex('A') {
            connectsTo('B') {
                connectsTo 'C'
                connectsTo('D') {
                    connectsTo 'A'
                    connectsFrom 'A'
                    connectsTo 'C'
                    connectsTo 'E'
                }
            }
        }
        vertex('F') {
            connectsTo('G') {
                connectsTo 'D'
            }
        }
        snapshot()
        classifyEdges('A') {Object from, Object to, EdgeType type ->
            edge(from, to) {
                switch(type) {
                    case EdgeType.TREE_EDGE:
                        color = 'green'
                        snapshot()
                        label = 'tree'
                        break
                    case EdgeType.BACK_EDGE:
                        color = 'red'
                        snapshot()
                        label = 'back'
                        break
                    case EdgeType.FORWARD_EDGE:
                        color = 'grey'
                        snapshot()
                        label = 'forward'
                        break
                    case EdgeType.CROSS_EDGE:
                        color = 'blue'
                        snapshot()
                        label = 'cross'
                        break
                }
            }
            snapshot()
            CONTINUE
        }
        snapshot(4)
        gif 'images/edge-classification.gif', 500
    }
}

def exampleConnectedComponentUndirected() {
    graph {
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
        def colors = ['A':'yellow', 'Z':'green', J:'blue']
        snapshot()
        connectedComponent('A') { root, vertex ->
            vertex.fillcolor = colors[(root)]
            vertex.style = 'filled'
            snapshot()
            CONTINUE
        }
        gif 'images/connected-component-undirected.gif'
    }
}

def exampleConnectedComponentDirected() {
    graph {
        type 'directed-graph'
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
        def colors = ['A': 'yellow', 'Z': 'green', J: 'blue']
        snapshot()
        connectedComponent('A') { root, vertex ->
            vertex.fillcolor = colors[(root)]
            vertex.style = 'filled'
            snapshot()
            CONTINUE
        }
        gif 'images/connected-component-directed.gif'
    }
}

