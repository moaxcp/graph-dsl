package graph.plugin

import graph.Edge
import graph.Graph
import graph.Vertex
import groovy.transform.Memoized
import groovy.transform.PackageScope

import javax.imageio.ImageIO
import javax.imageio.stream.FileImageOutputStream
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path

class GraphViz implements Plugin {
    Graph graph
    List<BufferedImage> snapshots = []

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
        String string = "${getId(edge.from.toString())} $edgeString ${getId(edge.to.toString())}"
        String attributes = edge.findAll {
            it.key != 'from' && it.key != 'to'
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
                if(v.size() > 1 || graph.adjacentEdges(id).size() == 0) {
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

    void snapshot(int count = 1) {
        (0..count).each {
            snapshots.add(image().image)
        }
    }

    void gif(String file, int delay = 1000, boolean loop = true) throws IOException {
        Map<String, Integer> max = findMaxWidthHeight()
        List<BufferedImage> resized = snapshots.collect {
            int b = guessBackgroundColor(it)
            BufferedImage out = createOutputImage(b, max.width, max.height, it.type)
            centerOnOutput(out, it)
        }
        writeImagesToFileAsGif(new File(file), resized, delay, loop)
    }

    void writeImagesToFileAsGif(File file, List<BufferedImage> images, int delay, boolean loop) {
        new FileImageOutputStream(file).withCloseable { out ->
            GifSequenceWriter writer = new GifSequenceWriter(out, BufferedImage.TYPE_3BYTE_BGR, delay, loop)
            images.each {
                writer.writeToSequence(it)
            }
            writer.close()
        }
    }

    BufferedImage centerOnOutput(BufferedImage output, BufferedImage image) {
        int x = (output.width - image.width) / 2
        int y = (output.height - image.height) / 2
        Graphics2D g2d = output.createGraphics()
        g2d.drawImage(image, x, y, image.width, image.height, null)
        g2d.dispose()
        output
    }

    BufferedImage createOutputImage(int background, int width, int height, int type) {
        BufferedImage out = new BufferedImage(width, height, type)
        Graphics2D g2d = out.createGraphics()
        g2d.setBackground(new Color(background))
        g2d.clearRect(0, 0, width, height)
        g2d.dispose()
        out

    }

    int guessBackgroundColor(BufferedImage image) {
        image.getRGB(0, 0)
    }

    Map<String, Integer> findMaxWidthHeight() {
        Map<String, Integer> max = [:]
        max.width = 0
        max.height = 0
        snapshots.each {
            max.width = it.width > max.width ? it.width : max.width
            max.height = it.height > max.height ? it.height : max.height
        }
        max
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
        println err
        Files.delete(file)
    }
}
