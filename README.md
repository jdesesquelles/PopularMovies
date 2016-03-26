Popular Movies
==============
<table border="0" align="center">
    <th border="0">
        <img src="screenshot.png" width="120" height="200" />
        <img src="screenshot2.png" width="120" height="200" />
    </th>
    <th align="left">
        <li> Browse popular movies
        <br><br>
        <li> View trailers and reviews
        <br><br>
        <li> Favorite your preferred movies
    </th>
</table>


Installation Instructions
-------------------------
An API key from themoviedb.org is required in gradle.properties to build the app
<ul>
<li>Create an <A href=https://www.themoviedb.org/account/signup>account</A>
<li>In the root directory, create the gradle.properties file
<li>Set TMDB_API_KEY="your API Key" in gradle.properties
</ul>

Building from the command Line
------------------------------
<ul>
<code>git clone https://github.com/jdesesquelles/PopularMovies</code><br/>
<code>cd PopularMovies</code><br/>
<code>export ANDROID_HOME=/Library/Android/sdk</code><br/>
<code>read API_KEY</code><br/>
<code>echo "TMDB_API_KEY=\"$API_KEY\"" >> gradle.properties</code><br/>
<code>./gradlew installProdDebug</code><br/>
</ul>

Requirements
------------
<ul>
<li> minSdkVersion 21
<li> targetSdkVersion 23
<li> compileSdkVersion 23
<li> buildToolsVersion 23.0.2
</ul>