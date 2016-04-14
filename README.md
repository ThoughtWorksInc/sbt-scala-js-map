# sbt-scala-js-map

[![Build Status](https://travis-ci.org/ThoughtWorksInc/sbt-scala-js-map.svg?branch=master)](https://travis-ci.org/ThoughtWorksInc/sbt-scala-js-map)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.thoughtworks.sbt-scala-js-map/sbt-scala-js-map/badge.svg)](http://central.maven.org/maven2/com/thoughtworks/sbt-scala-js-map/)

**sbt-scala-js-map** is a Sbt plugin that setup source mapping for Scala.js projects hosted on Github.

## Motivation

I have some Scala.js libraries hosted on Github ([Binding.scala](https://github.com/ThoughtWorksInc/Binding.scala) and some private libraries). Then, other Scala.js applications would depend on these libraries. When I debug the Scala.js application in a browser, I want to see the Scala source file of the original libraries.

However, by default, the generated `*.js.map` files maps the generated JavaScript to the absolute Scala source path where the original library compiled. For me, the path is on a Travis CI worker, like `/home/travis/build/ThoughtWorksInc/.../Binding.scala`. The path obviously does not exist my local computer debugging the application. Too bad.

This sbt plugin detects if a library is hosted on Github repository and let source map point to https://raw.githubusercontent.com/ instead of a local file path.

## Usage

### Step 1: Add the dependencies in your Scala.js library's `project/plugins.sbt`

``` sbt
addSbtPlugin("com.thoughtworks.sbt-scala-js-map" % "sbt-scala-js-map" % "latest.release")
```

### Step 2: Enable this plugin in your Scala.js library's `build.sbt'

``` sbt
enablePlugins(ScalaJsMap)
```

## Step 3: Publish your Scala.js library

Execute the release command if you have setup [sbt-release](https://github.com/sbt/sbt-release) correctly。

```
sbt release
```

### Step 4: Debug it!

Now switch your Scala.js application to the newly published Scala.js library, build it, browse your web page, and open the debugger in your browser. You will see the Scala source files hosted under https://raw.githubusercontent.com/ and you can set break points at code lines in these Scala files.
