package graph

/**
 * Main interface for all {@link Graph} plugins. Plugins are applied to a {@link Graph} using
 * {@link Graph#apply(Class)}.
 */
interface Plugin {
    /**
     * Applies this plugin to the {@link Graph}.
     * @param graph - modified by this plugin.
     */
    void apply(Graph graph)
}
