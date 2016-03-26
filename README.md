# Popular Movies
![Alt text](screenshot.png?raw=true "Popular Movies List view")
![Alt text](screenshot2.png?raw=true "Popular Movies Detail Screen")
## Installation Instructions
For the application to build properly, you need an API key from themoviedb.org
* Create an account at https://www.themoviedb.org/account/signup
* Get the API key and fill the gradle.properties TMDB_API_KEY=""
From the command Line
`git clone https://github.com/jdesesquelles/PopularMovies`
`cd PopularMovies`
`export ANDROID_HOME=/Users/ebal/Library/Android/sdk`
`read API_KEY
`echo "TMDB_API_KEY=\"$API_KEY\"" >> gradle.properties`
`./gradlew installProdDebug`
## Requirements
_minSdkVersion = 21_
**targetSdkVersion** = 23
_compileSdkVersion_ = 23
_buildToolsVersion_ = '23.0.2'