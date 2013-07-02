jenkins-topj
============

TopJ Jenkins plugin

The background of this plugin is to have all slaves from a Jenkins cluster be 100% independent of haveing a particular script present on the slave machine. Everytime a build is started, it pulls the scripts from a central repository, prepare the build/test environment, then it actually perform the operations.
