package graph

class EdgeWeightPlugin implements Plugin {

    @Override
    void apply(Graph graph) {
        graph.edges.each { edge ->
            edge.delegateAs(Weight)
        }

        Comparator comparator = { a, b ->
            a.weight <=> b.weight
        }

        graph.replaceEdgesSet(new TreeSet<>(comparator))
    }
}
