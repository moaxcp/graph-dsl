package graph.plugin

import graph.Graph

import static groovyx.javafx.GroovyFX.start

class GraphViz implements Plugin {
    Graph graph

    @Override
    void apply(Graph graph) {
        this.graph = graph
    }

    String dot() {
        StringWriter writer = new StringWriter()

        new IndentPrinter(writer).with { p ->
            p.autoIndent = true
            p.println("strict ${graph.isDirected() ? 'digraph' : 'graph'} {")
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

    void view() {
        println "in view ${dot()}"
        Process execute = "${graph.isDirected() ? 'dot' : 'neato'} -Tpng".execute()
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
}
