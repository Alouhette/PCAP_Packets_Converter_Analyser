NetP: A BeepBeep palette for network packets
============================================

This project is an extension to the [BeepBeep
3](https://liflab.github.io/beepbeep-3), event stream processing engine,
called a *palette*, that provides functionalities for processing network
captures.

Building this palette
---------------------

To compile the palette, make sure you have the following:

- The Java Development Kit (JDK) to compile. The palette complies
  with Java version 6; it is probably safe to use any later version.
- [Ant](http://ant.apache.org) to automate the compilation and build process

The palette also requires the following Java libraries:

- The latest version of [BeepBeep 3](https://liflab.github.io/beepbeep-3)
- The latest version of [JNetPcap](https://sourceforge.net/projects/jnetpcap)

These dependencies can be automatically downloaded and placed in the
`dep` folder of the project by typing:

    ant download-deps

From the project's root folder, the sources can then be compiled by simply
typing:

    ant

This will produce a file called `netp.jar` in the folder. This file
is *not* runnable and stand-alone. It is meant to be used in a Java project
alongside `beepbeep-3.jar`

Contributors
------------

The first version of this palette has been written in 2016 by [Pierre-Louis
Faure](https://www.linkedin.com/in/plfaure) and [Théo
Ménard](https://www.linkedin.com/in/theomenard). Further tweaks have been
added by [Sylvain Hallé](https://leduotang.ca/sylvain), Full Professor at
[Université du Québec à Chicoutimi](http://www.uqac.ca) and head of
[Laboratoire d'informatique formelle](http://liflab.ca) (LIF).

<!-- :maxLineLen=76: -->