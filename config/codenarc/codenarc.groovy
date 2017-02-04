ruleset {
    description 'Rules Sample Groovy Gradle Project'

    ruleset('rulesets/basic.xml')
    ruleset('rulesets/braces.xml')
    ruleset('rulesets/concurrency.xml')
    ruleset('rulesets/convention.xml')
    ruleset('rulesets/design.xml')
    ruleset('rulesets/dry.xml')
    //ruleset('rulesets/enhanced.xml')
    ruleset('rulesets/exceptions.xml')
    ruleset('rulesets/formatting.xml')
    ruleset('rulesets/generic.xml')
    //ruleset('rulesets/grails.xml')
    ruleset('rulesets/groovyism.xml')
    ruleset('rulesets/imports.xml')
    //ruleset('rulesets/jdbc.xml')
    //ruleset('rulesets/junit.xml')
    ruleset('rulesets/logging.xml') {
        'Println' priority: 1
        'PrintStackTrace' priority: 1
    }
    ruleset('rulesets/naming.xml')
    ruleset('rulesets/security.xml')
    ruleset('rulesets/serialization.xml')

    // rulesets/size.xml
    //AbcComplexity   // DEPRECATED: Use the AbcMetric rule instead. Requires the GMetrics jar
    AbcMetric   // Requires the GMetrics jar
    ClassSize
    //CrapMetric   // Requires the GMetrics jar and a Cobertura coverage file
    CyclomaticComplexity   // Requires the GMetrics jar
    MethodCount
    MethodSize
    NestedBlockDepth
    ParameterCount

    ruleset('rulesets/unnecessary.xml')
    ruleset('rulesets/unused.xml')
}