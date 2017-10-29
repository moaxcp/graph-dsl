with import <nixpkgs> {};
stdenv.mkDerivation rec {
  name = "graph-dsl-env";
  env  = buildEnv { name = name; paths = buildInputs; };
  buildInputs = [
    git
    gitAndTools.gitflow
    jetbrains.idea-community
    openjdk
    groovy
    gradle
  ];
}