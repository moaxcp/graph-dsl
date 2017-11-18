package graph.type.undirected

import graph.ConfigSpec
import graph.Edge
import graph.Graph
import graph.EdgeSpec
import graph.Vertex
import graph.VertexSpec
import graph.type.Type

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

import static groovyx.javafx.GroovyFX.start

/**
 * Implements an undirected graph. There can only be one {@link Edge} between any two vertices. When traversing a graph
 * an {@link Edge} is adjacent to a {@link Vertex} if it's one or two property equals the name of the {@link Vertex}.
 */
class GraphType implements Type {

    Graph graph

    @Override
    Edge newEdge(Object one, Object two, Map delegate = null) {
        if (delegate) {
            new Edge(one: one, two: two, delegate: delegate)
        } else {
            new Edge(one: one, two: two)
        }
    }

    @Override
    Vertex newVertex(Object key, Map delegate = null) {
        if (delegate) {
            new Vertex(key: key, delegate: delegate)
        } else {
            new Vertex(key: key)
        }
    }

    @Override
    EdgeSpec newEdgeSpec(Map<String, ?> map) {
        new UndirectedEdgeSpec(graph, map)
    }

    @Override
    EdgeSpec newEdgeSpec(ConfigSpec spec) {
        new UndirectedEdgeSpec(graph, spec.map, spec.closure)
    }

    @Override
    VertexSpec newVertexSpec(Map<String, ?> map) {
        new UndirectedVertexSpec(graph, map)
    }

    @Override
    VertexSpec newVertexSpec(ConfigSpec spec) {
        new UndirectedVertexSpec(graph, spec.map, spec.closure)
    }

    @Override
    String dot() {
        StringWriter writer = new StringWriter()

        new IndentPrinter(writer).with { p ->
            p.autoIndent = true
            p.println("strict ${isDirected() ? 'digraph' : 'graph'} {")
            p.incrementIndent()
            graph.edges.each {
                p.println(it.one + ' -- ' + it.two)
            }
            p.decrementIndent()
            p.print('}')
            p.flush()
        }

        writer.toString()
    }

    @Override
    void view() {
        println "in view ${dot()}"
        Process execute = "${isDirected() ? 'dot' : 'neato'} -Tpng".execute()
        execute.outputStream.withWriter { writer ->
            writer.write(dot())
        }
        StringBuilder err = new StringBuilder()
        execute.consumeProcessErrorStream(err)

        start {
            stage(title: 'Graph', visible: true) {
                scene {
                    imageView(image(execute.inputStream))
                    //todo add any error text from graphviz process
                }
            }
        }

        execute.waitFor()
    }

    @Override
    boolean canConvert() {
        if (graph.edges.size() == 0) {
            return true
        }
        Set<Edge> edges = [] as Set
        !graph.edges.find { Edge current ->
            Edge edge = new Edge(one: current.one, two: current.two)
            !edges.add(edge)
        }
    }

    @Override
    void convert() {
        if (!canConvert()) {
            throw new IllegalArgumentException("Cannot convert to ${getClass().simpleName}")
        }
        graph.replaceEdges { Edge edge ->
            newEdge(edge.one, edge.two, edge.delegate)
        }

        graph.replaceEdgesSet(new LinkedHashSet<? extends Edge>())

        graph.replaceVertices { String name, Vertex vertex ->
            [vertex.key, newVertex(vertex.key, vertex.delegate)]
        }

        graph.replaceVerticesMap([:])
    }

    @Override
    boolean isDirected() {
        false
    }

    /**
     * Returns edges from vertex that should be traversed.
     * @param key
     * @return
     */
    Set<? extends Edge> traverseEdges(Object key) {
        graph.adjacentEdges(key)
    }
}
