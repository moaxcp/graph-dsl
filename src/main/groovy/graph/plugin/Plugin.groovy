package graph.plugin

import graph.Graph

interface Plugin {
    void apply(Graph graph)
    void setGraph(Graph graph)
}
