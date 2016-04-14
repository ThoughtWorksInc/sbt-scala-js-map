# sbt-scala-js-map

[![Build Status](https://travis-ci.org/ThoughtWorksInc/sbt-scala-js-map.svg?branch=master)](https://travis-ci.org/ThoughtWorksInc/sbt-scala-js-map)

**sbt-scala-js-map** is a Sbt plugin that setup source mapping for Scala.js projects hosted on Github.

## Usage

### Add the dependencies in your `project/plugins.sbt`

See [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.thoughtworks.sbt-scala-js-map/sbt-scala-js-map_2.10_0.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.thoughtworks.sbt-scala-js-map/sbt-scala-js-map_2.10_0.13) for the group ID and artifact ID.

### Enable this plugin in your Scala.js project's `build.sbt'

``` sbt
enablePlugins(ScalaJsMap)
```

### Compile your Scala files to `*.js` and `*.js.map`

```
fastOptJS
```

### Debug it!

Now browse your web page and open the debugger in your browser. You will see the Scala source files hosted under https://raw.githubusercontent.com/ and you can set break points at code lines in these Scala files.
