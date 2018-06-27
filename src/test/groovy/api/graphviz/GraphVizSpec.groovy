package api.graphviz

import graph.Graph
import graph.plugin.GraphViz
import spock.lang.Specification

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import static org.junit.Assert.*
import static graph.ImageMatcher.*

class GraphVizSpec extends Specification {
    Graph graph = new Graph()

    def setup() {
        graph.plugin 'graphviz'
    }

    def 'dot for empty undirected graph is an empty strict graph'() {
        expect: 'dot is an empty strict graph'
        '''
            strict graph {
            }
        '''.stripIndent().trim() == graph.dot()
    }

    def 'dot renders undirected edges'() {
        given: 'vertices with an edge A, B'
        graph.edge('A', 'B')

        expect: 'dot is a strict graph with edge A, B'
        '''
            strict graph {
              A -- B
            }
        '''.stripIndent().trim() == graph.dot()
    }

    def 'dot for an empty directed graph is an empty strict digraph'() {
        given: 'a directed graph with the graphviz plugin'
        graph.type 'directed-graph'

        expect: 'dot is an empty strict digraph'
        '''
            strict digraph {
            }
        '''.stripIndent().trim() == graph.dot()
    }

    def 'dot renders directed edges'() {
        given: 'a directed graph with the graphviz plugin'
        graph.type 'directed-graph'

        and: 'vertices with an edge A, B'
        graph.edge('A', 'B')

        expect: 'dot is a strict digraph with edge A, B'
        '''
            strict digraph {
              A -> B
            }
        '''.stripIndent().trim() == graph.dot()
    }

    def 'dot renders weight for weighted graph'() {
        given: 'a weighted graph with the graphviz plugin'
        graph.type 'weighted-graph'

        expect: 'it contains an empty strict graph'
        '''
            strict graph {
            }
        '''.stripIndent().trim() == graph.dot()
    }

    def 'dot renders default weighted edges'() {
        given: 'a weighted graph with the graphviz plugin'
        graph.type 'weighted-graph'

        and: 'vertices with an edge A, B'
        graph.edge('A', 'B')

        expect: 'it contains an empty strict graph'
        '''
            strict graph {
              A -- B
            }
        '''.stripIndent().trim() == graph.dot()
    }

    def 'dot renders weighted edges'() {
        given: 'a weighted graph with the graphviz plugin'
        graph.type 'weighted-graph'

        and: 'vertices with an edge A, B'
        graph.edge('A', 'B', [weight:10])

        expect: 'it contains an empty strict graph'
        '''
            strict graph {
              A -- B [weight="10"]
            }
        '''.stripIndent().trim() == graph.dot()
    }

    def 'dot renders vertex attributes'() {
        given: 'vertices with an edge A, B'
        graph.edge 'A', 'B'

        and: 'vertex A has attributes'
        graph.vertex 'A', [color:'blue']

        expect: 'dot contains vertex A with attributes'
        '''
            strict graph {
              A -- B
              A [color="blue"]
            }
        '''.stripIndent().trim() == graph.dot()
    }

    def 'dot saves to file'() {
        given: 'graph with edge'
        graph.edge 'A', 'B'

        and: 'a file to save dot'
        Path file = Files.createTempFile('graph', '.dot')

        when:
        graph.dot(file.toString())

        then:
        '''
            strict graph {
              A -- B
            }
        '''.stripIndent().trim() == file.text
        Files.delete(file)
    }

    def 'centerOnOutput centers the image'() {
        given: '200x200 black out image'
        BufferedImage output = new BufferedImage(200, 200, BufferedImage.TYPE_4BYTE_ABGR)
        Graphics2D outg = output.createGraphics()
        outg.setColor(Color.BLACK)
        outg.fillRect(0, 0, output.width, output.height)
        outg.dispose()

        and: '150x75 white image'
        BufferedImage image = new BufferedImage(150, 100, BufferedImage.TYPE_4BYTE_ABGR)
        Graphics2D imageg = image.createGraphics()
        imageg.setColor(Color.WHITE)
        imageg.fillRect(0, 0, image.width, image.height)
        imageg.dispose()

        when: 'image is centered on out image'
        GraphViz plugin = new GraphViz()
        BufferedImage result = plugin.centerOnOutput(output, image)

        then: 'result shows image centered'
        BufferedImage expected = new BufferedImage( 200, 200, BufferedImage.TYPE_4BYTE_ABGR)
        Graphics2D expectedg = expected.createGraphics()
        expectedg.setColor(Color.BLACK)
        expectedg.fillRect(0, 0, 200, 200)
        expectedg.setColor(Color.WHITE)
        int x = (output.width - image.width) / 2
        int y = (output.height - image.height) / 2
        expectedg.fillRect(x, y, 150, 100)
        expectedg.dispose()

        assertThat(result, contentEqualTo(expected))
    }

    def 'guess background color'() {
        given: '200x200 black image'
        BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_4BYTE_ABGR)
        Graphics2D g = image.createGraphics()
        g.setColor(Color.BLACK)
        g.fillRect(0, 0, image.width, image.height)
        g.dispose()

        when: 'background color is guessed'
        GraphViz plugin = new GraphViz()
        int color = plugin.guessBackgroundColor(image)

        then: 'background was black'
        color == Color.BLACK.getRGB()
    }

    def 'can create output image'() {
        given: 'specification for a 200x200 black image'
        int background = Color.BLACK.getRGB()
        int width = 200
        int height = 200
        int type = BufferedImage.TYPE_4BYTE_ABGR

        when: 'image is created'
        GraphViz plugin = new GraphViz()
        BufferedImage image = plugin.createOutputImage(background, width, height, type)

        then: 'image matches specification'
        width == image.width
        height == image.height
        type == image.type
        background == image.getRGB(0, 0)
    }

    def 'can find max width and height of snapshots'() {
        given: 'snapshots of different sizes'
        GraphViz plugin = new GraphViz()
        plugin.snapshots.add(new BufferedImage(200, 220, BufferedImage.TYPE_4BYTE_ABGR))
        plugin.snapshots.add(new BufferedImage(210, 300, BufferedImage.TYPE_4BYTE_ABGR))
        plugin.snapshots.add(new BufferedImage(150, 200, BufferedImage.TYPE_4BYTE_ABGR))

        when: 'plugin finds max width and height'
        Map<String, Integer> result = plugin.findMaxWidthHeight()

        then: 'expected width and height is returned'
        result.width == 210
        result.height == 300
    }
}
