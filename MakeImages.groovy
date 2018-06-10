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
        eachBfs('A') { vertex ->
            vertex.label = "${vertex.id}\\lorder:${count++}\\l"
            vertex.fillcolor = 'green'
            vertex.style = 'filled'
        }
        plugin 'graphviz'
        image 'images/breadth-first-traversal.png'
    }
}

def examplePreOrderTraversal() {
    graph {
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
            vertex.label = "${vertex.id}\\lorder:${count++}\\l"
            vertex.fillcolor = 'green'
            vertex.style = 'filled'
            CONTINUE
        }
        plugin 'graphviz'
        image 'images/pre-order-traversal.png'
    }
}

def examplePostOrderTraversal() {
    graph {
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
            vertex.label = "${vertex.id}\\lorder:${count++}\\l"
            vertex.fillcolor = 'green'
            vertex.style = 'filled'
            CONTINUE
        }
        plugin 'graphviz'
        image 'images/post-order-traversal.png'
    }
}

def exampleReversePostOrderTraversal() {
    graph {
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
        reversePostOrder('build') { vertex ->
            vertex.label = "${vertex.id}\\lorder:${count++}\\l"
            vertex.fillcolor = 'green'
            vertex.style = 'filled'
            CONTINUE
        }

        plugin 'graphviz'
        image 'images/reverse-post-order-traversal.png'
    }
}

def exampleClassifyEdges() {
    graph {
        type 'directed-graph'
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
        classifyEdges('A') {Object from, Object to, EdgeType type ->
            edge(from, to) {
                switch(type) {
                    case EdgeType.TREE_EDGE:
                        color = 'black'
                        label = 'tree'
                        break
                    case EdgeType.BACK_EDGE:
                        color = 'red'
                        label = 'back'
                        break
                    case EdgeType.FORWARD_EDGE:
                        color = 'grey'
                        label = 'forward'
                        break
                    case EdgeType.CROSS_EDGE:
                        color = 'blue'
                        label = 'cross'
                        break
                }
            }
            CONTINUE
        }
        plugin 'graphviz'
        image 'images/edge-classification.png'
    }
}

def exampleConnectedComponentUndirected() {
    graph {
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
        connectedComponent('A') { root, vertex ->
            vertex.fillcolor = colors[(root)]
            vertex.style = 'filled'
            CONTINUE
        }
        plugin 'graphviz'
        image 'images/connected-component-undirected.png'
    }
}

def exampleConnectedComponentDirected() {
    graph {
        type 'directed-graph'
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
        connectedComponent('A') { root, vertex ->
            vertex.fillcolor = colors[(root)]
            vertex.style = 'filled'
            CONTINUE
        }
        plugin 'graphviz'
        image 'images/connected-component-directed.png'
    }
}

