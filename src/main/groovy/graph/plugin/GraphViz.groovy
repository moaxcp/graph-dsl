package graph.plugin

import graph.Edge
import graph.Graph
import graph.Vertex
import groovy.transform.Memoized
import groovy.transform.PackageScope

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path

class GraphViz implements Plugin {
    Graph graph

    @Override
    void apply(Graph graph) {
        this.graph = graph
    }

    @PackageScope
    String getEdgeString() {
        graph.isDirected() ? '->' : '--'
    }

    @PackageScope
    String getGraphString() {
        "${graph.isMultiGraph() ? '' : 'strict'} ${graph.isDirected() ? 'digraph' : 'graph'}"
    }

    @Memoized
    @PackageScope
    String getId(String id) {
        if(id ==~ '[-]?(.[0-9]+|[0-9]+(.[0-9]*)? )') {
            return id
        } else if(id ==~ '[_a-zA-Z\200-\377][_0-9a-zA-Z\200-\377]*') {
            return id
        } else {
            return "\"$id\""
        }
    }

    @PackageScope
    String getEdgeDot(Edge edge) {
        String string = "${getId(edge.one.toString())} $edgeString ${getId(edge.two.toString())}"
        String attributes = edge.findAll {
            it.key != 'one' && it.key != 'two'
        }.collect {
            /$it.key="$it.value"/
        }.join(' ')
        if(attributes) {
            return "$string [$attributes]"
        }
        string
    }

    @PackageScope
    String getVertexDot(Vertex vertex) {
        String string =getId(vertex.id.toString())
        String attributes = vertex.findAll {
            it.key != 'id'
        }.collect {
            /$it.key="$it.value"/
        }.join(' ')
        if(attributes) {
            return "$string [$attributes]"
        }
        string
    }

    String dot() {
        StringWriter writer = new StringWriter()

        new IndentPrinter(writer).with { p ->
            p.autoIndent = true
            p.println("$graphString {")
            p.incrementIndent()
            graph.edges.each {
                p.println(getEdgeDot(it))
            }
            graph.vertices.each { Object id, Vertex v ->
                if(v.size() > 1) {
                    p.println(getVertexDot(v))
                }
            }

            p.decrementIndent()
            p.print('}')
            p.flush()
        }

        writer.toString()
    }

    void dot(String file) {
        new File(file).write(dot())
    }

    Map image() {
        Process execute = "${graph.isDirected() ? 'dot' : 'neato'} -Tpng".execute()
        execute.outputStream.withWriter { writer ->
            writer.write(dot())
        }
        StringBuilder err = new StringBuilder()
        execute.consumeProcessErrorStream(err)

        BufferedImage image = ImageIO.read(execute.inputStream)
        execute.waitFor()

        return [image:image, error:err.toString()]
    }

    void image(String file) {
        new File(file).withOutputStream { out ->
            ImageIO.write(image().image, 'PNG', out)
        }
    }

    void view() {
        Path file = Files.createTempFile('graph', '.png')
        image(file.toString())
        Process execute = "xdg-open $file".execute()
        StringBuilder err = new StringBuilder()
        execute.consumeProcessErrorStream(err)
        execute.waitFor()
        print err
        Files.delete(file)
    }
}
