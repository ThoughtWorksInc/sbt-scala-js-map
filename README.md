# sbt-scala-js-map <a href="http://thoughtworks.com/"><img align="right" src="https://www.thoughtworks.com/imgs/tw-logo.png" title="ThoughtWorks" height="15"/></a>

[![Build Status](https://travis-ci.org/ThoughtWorksInc/sbt-scala-js-map.svg)](https://travis-ci.org/ThoughtWorksInc/sbt-scala-js-map)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.thoughtworks.sbt-scala-js-map/sbt-scala-js-map/badge.svg)](http://central.maven.org/maven2/com/thoughtworks/sbt-scala-js-map/)

**sbt-scala-js-map** is a Sbt plugin that configures source mapping for Scala.js projects hosted on Github.

## Motivation

I have some Scala.js libraries hosted on Github ([Binding.scala](https://github.com/ThoughtWorksInc/Binding.scala) and some private libraries). Then, other Scala.js applications would depend on these libraries. When I debug the Scala.js application in a browser, I want to see the Scala source files of the original libraries.

However, by default, the generated `*.js.map` files maps the generated JavaScript to the absolute Scala source path where the original library compiled. For me, the path is on a Travis CI worker, like `/home/travis/build/ThoughtWorksInc/.../Binding.scala`. The path obviously does not exist on my local computer that debugs the application. Too bad.

This sbt plugin detects if a library is hosted on Github repository and let source map point to https://raw.githubusercontent.com/ instead of a local file path.

### Showcase

Browse http://todomvc.com/examples/binding-scala/ then open the inspector of your browser. You will be able to debug the Scala.js code in your browser now.

![Debugging Scala.js](https://github.com/ThoughtWorksInc/sbt-scala-js-map/raw/master/README.png)

### Alternative options

An alternative option is specifying Scala.js's relative source mappings flag.

However, this approach enforce library users cloning the library's source files into their local file system before debugging their applications.

On the other hand, libraries published with this plugin enable the library users to view the source (of correct revision), automatically.

## Usage

### Step 1: Add the dependencies in your Scala.js library's `project/plugins.sbt`

``` sbt
addSbtPlugin("com.thoughtworks.sbt-scala-js-map" % "sbt-scala-js-map" % "latest.release")
```

Note that sbt-scala-js-map 2.x requires sbt 0.13.x, sbt-api-mappings 3.x requires sbt 1.x.


### Step 2: Publish your Scala.js library

Execute the release command if you have setup [sbt-release](https://github.com/sbt/sbt-release) correctly。

```
sbt release
```

### Step 3: Debug it!

Now switch your Scala.js application to the newly published Scala.js library, build it, browse your web page, and open the debugger in your browser. You will see the Scala source files hosted under https://raw.githubusercontent.com/ and you can set break points at code lines in these Scala files.
