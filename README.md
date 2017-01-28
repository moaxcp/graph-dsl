# graph-dsl

[![Build Status](https://travis-ci.org/moaxcp/graph-dsl.svg?branch=master)](https://travis-ci.org/moaxcp/graph-dsl)

work around for closed merge

```
//use the -k flag to keep the feature branch
git flow feature finish test -k
//after all the merging is done, the PR should be marked as 'merged', so you can delete the remote branch
git flow feature delete test -r
```

