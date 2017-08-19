package graph.plugin

import graph.Graph

/**
 * Main interface for all {@link Graph} plugins. Plugins are applied to a {@link Graph} using
 * {@link Graph#apply(Class)}.
 */
interface Plugin {
    /**
     * Applies this plugin to the {@link graph.Graph}.
     * @param graph - modified by this plugin.
     */
    void apply(Graph graph)
}
