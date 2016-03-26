Popular Movies
==============

![Alt text](screenshot.png?raw=true "Popular Movies List view")
![Alt text](screenshot2.png?raw=true "Popular Movies Detail Screen")

Installation Instructions
-------------------------
For the application to build properly, you will need an API key from themoviedb.org.
* Create an account at https://www.themoviedb.org/account/signup
* Get the API key and fill the gradle.properties TMDB_API_KEY=""

From the command Line:

&nbsp;&nbsp;`git clone https://github.com/jdesesquelles/PopularMovies`
  &nbsp;&nbsp;`cd PopularMovies`
&nbsp;&nbsp;`export ANDROID_HOME=/Users/ebal/Library/Android/sdk`

&nbsp;&nbsp;`read API_KEY`

&nbsp;&nbsp;`echo "TMDB_API_KEY=\"$API_KEY\"" >> gradle.properties`

&nbsp;&nbsp;`./gradlew installProdDebug`

## Requirements

_minSdkVersion = 21_

**targetSdkVersion** = 23

_compileSdkVersion_ = 23

_buildToolsVersion_ = '23.0.2'