Release Process
===============

1. Update the `CHANGELOG.md` file with relevant info and date.
2. Update version number in `gradle.properties` file.
3. Update version number in `README.md` file.
4. Commit: `git commit -am "Prepare version X.Y.Z."`
5. Tag: `git tag -a X.Y.Z -m "Version X.Y.Z"`
6. Release: `./gradlew clean assemble uploadArchives`
7. Update version number in `gradle.properties` file to next "SNAPSHOT" version.
8. Commit: `git commit -am "Prepare next development version."`
9. Push: `git push && git push --tags`

#### *TODO* Write a script for steps 2 - 9. 
