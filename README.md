Popular Movies
==============
<table border="0">
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
</table> </p>


Build and Install
-------------------------
An API key from themoviedb.org is required in gradle.properties to build the app
<ul>
<li>Create an <A href=https://www.themoviedb.org/account/signup>account</A></li>
<li>From the command line:</li>
<br/>
&nbsp;&nbsp;&nbsp;<code margin-left="40px">git clone https://github.com/jdesesquelles/PopularMovies</code><br/>
&nbsp;&nbsp;&nbsp;<code>cd PopularMovies</code><br/>
&nbsp;&nbsp;&nbsp;<code>export ANDROID_HOME=$HOME/Library/Android/sdk</code><br/>
&nbsp;&nbsp;&nbsp;<code>read API_KEY</code><br/>
&nbsp;&nbsp;&nbsp;<code>echo "TMDB_API_KEY=\"$API_KEY\"" >> gradle.properties</code><br/>
&nbsp;&nbsp;&nbsp;<code>./gradlew installProdDebug</code><br/>
</ul>

Requirements
------------
<ul>
<li> minSdkVersion 21
<li> targetSdkVersion 23
<li> compileSdkVersion 23
<li> buildToolsVersion 23.0.2
</ul>