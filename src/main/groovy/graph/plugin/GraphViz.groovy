package graph.plugin

import graph.Edge
import graph.Graph

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

    private String getEdgeDot(Edge edge) {
        String string = "$edge.one ${graph.isDirected() ? '->' : '--'} $edge.two"
        Map attributes = [:]
        if(graph.isWeighted()) {
            attributes.weight = edge.weight
        }

        if(attributes) {
            string += ' [' + attributes.collect { /$it.key="$it.value"/ }.join(' ') + ']'
        }
        string
    }

    String dot() {
        StringWriter writer = new StringWriter()

        new IndentPrinter(writer).with { p ->
            p.autoIndent = true
            p.println("strict ${graph.isDirected() ? 'digraph' : 'graph'} {")
            p.incrementIndent()
            graph.edges.each {
                p.println(getEdgeDot(it))
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
