# Spotify Explorer

This is a command line Spotify client. The application consumes Spotify's REST API to fetch lists of the following
Spotify music items: new album releases, featured playlists, music categories and lastly their playlists.
Correspondingly, supported user input strings (i.e. user commands) include 'new', 'featured', 'categories' and, for
example, 'playlists Rock'.
'Rock' can be replaced by any other category obtained using the 'categories' command.

Item lists recieved from Spotify are presented to the user using the standard output. The lists are split into
scrollable "pages" containing a pre-set numer of items per page (default number of items per page = 5, see
application.yml).

## <details> <summary> Table of Contents </summary>

- [Git Repository and Default Branch Names](#git-repository-and-default-branch-names)
</details>

## Git Repository and Default Branch Names

I wanted to store my app in a public GitHub repository for easy sharing with reviewers. At the same time, I was
interested in minimizing its searchability through general search. Google isn't expected to index branches other than '
master' and 'main', that's why my only branch has been called 'prime'. I also named the repository itself with a name
that is unrelated to the functionality or purpose of the app.

## System Requirements to Build the Application:

- JDK 11+
- Internet Access (for downloading dependencies by Gradle and accessing Spotify's endpoints)
- Writable application's directory (for build artifacts)

## Technologies Used

* Java
* Spring-Core
* Gradle
* JUnit, Mockito
* Gson

## To Build

Run the following in the root directory of this project:

### In Linux

```
./gradlew clean assemble
```

### In Windows

```
gradlew clean assemble 
```

## To Run Tests

Run in the root directory of this project:

### In Linux

```
./gradlew test
```

### In Windows

```
gradlew test
```

## To Run the Application from Command Line

Run in the root directory of this project:

### In Linux

```
./gradlew jar; cd build/libs; java -jar SpotifyExplorer-1.0-SNAPSHOT.jar
```

### In Windows

```
gradlew jar

cd build/libs

java -jar SpotifyExplorer-1.0-SNAPSHOT.jar
```

## Configuration

This default configurations can be updated in application.yml:

Property|Default Value|Description|Notes
---|---|---|---
app.items-per-page|5|Number of music items to display on each console output "page"| In total, up to 50 items are queried from Spotify. The obtained list of items is split into sublists, each containing 5 items.| 
server.port|9090|localhost port to which Spotify's OAuth service sends an Access Token| This value must be identical to the one set in the Spotify developer dashboard|
server.context-path|/|localhost path to which Spotify's OAuth service sends an Access Token|This value must be identical to the one set in the Spotify Developer Dashboard|
spotify.auth.client-id|_see application.yml_|Client id for authenticating the app via Spotify's OAuth service| This value must be identical to that of Spotify Developer account|
spotify.auth.client-secret|_see application.yml_|Client secret for authenticating the app via Spotify's OAuth service| This value must be identical to that of Spotify Developer account|
spotify.auth.base-uri|https://accounts.spotify.com|Spotify's OAuth endpoint||
spotify.auth.redirect-base-uri|http://localhost|uri where Spotify's OAuth service redirects user's browser after obtaining response to an Access Token request|This value must be identical to the one set in the Spotify Developer Dashboard
spotify.auth.token-path|/api/token|Path for sending Access Token request|Appended to spotify.auth.base-uri|
spotify.music.base-uri|https://api.spotify.com/v1|Spotify api endpoint for sending requests to obtain music items||
spotify.music.categories-path|/browse/categories|Path for requesting a list of Spotify Categories|Appended to spotify.music.base-uri
spotify.music.new-albums-path|/browse/new-releases|Path for requesting a list of new albums on Spotify|Appended to spotify.music.base-uri|
spotify.music.featured-path|/browse/featured-playlists|Path for requesting a list of featured playlists on Spotify|Appended to spotify.music.base-uri
spotify.music.query|?limit=50|Maximum number of items to request from Spotify (i.e. the maximum size of the obtained list)|Optional property. If omitted, Spotify will return up to 20 items|

## Running example

```
Greetings! :)
This is your Spotify Explorer

> next
Please authenticate with Spotify first ('auth')

> auth
Waiting for Access Code...
Access Code received successfully.
Access Token received successfully.
Authentication completed!

> featured
Sweet Soul Chillout
https://open.spotify.com/playlist/37i9dQZF1DXbcgQ8d7s0A0

Lo-Fi Beats
https://open.spotify.com/playlist/37i9dQZF1DWWQRwui0ExPn

Evening Acoustic
https://open.spotify.com/playlist/37i9dQZF1DXcWBRiUaG3o5

Deep Dark Indie
https://open.spotify.com/playlist/37i9dQZF1DWTtTyjgd08yp

Are & Be
https://open.spotify.com/playlist/37i9dQZF1DX4SBhb3fqCJd

--- page 1 of 3 ---

> next
Chill Hits
https://open.spotify.com/playlist/37i9dQZF1DX4WYpdgoIcn6

Gold School
https://open.spotify.com/playlist/37i9dQZF1DWVA1Gq4XHa6U

Timeless Love Songs
https://open.spotify.com/playlist/37i9dQZF1DX7rOY2tZUw1k

Jazz Rap
https://open.spotify.com/playlist/37i9dQZF1DX8Kgdykz6OKj

Nightstorms
https://open.spotify.com/playlist/37i9dQZF1DX4aYNO8X5RpR

--- page 2 of 3 ---

> next
Lush + Ethereal
https://open.spotify.com/playlist/37i9dQZF1DWZ0OzPeadl0h

Downtempo Beats
https://open.spotify.com/playlist/37i9dQZF1DWWQp0YMTvpD3

--- page 3 of 3 ---

> next
No more pages

> categories
Top Lists

2020 Wrapped

At Home

Pop

Mood

--- page 1 of 8 ---

> playlists Pop
Hot Hits Canada
https://open.spotify.com/playlist/37i9dQZF1DWXT8uSSn6PRy

Pop All Day
https://open.spotify.com/playlist/37i9dQZF1DXarRysLJmuju

It's a Bop
https://open.spotify.com/playlist/37i9dQZF1DWYMfG0Phlxx8

Today's Top Hits
https://open.spotify.com/playlist/37i9dQZF1DXcBWIGoYBM5M

New Music Friday Canada
https://open.spotify.com/playlist/37i9dQZF1DX5DfG8gQdC3F

--- page 1 of 10 ---

> exit
Terminating...
Bye bye!
```

## Testing

Unit tests for the pagination mechanism and music fetching service are available in
their [respective packages](src/test/java/net/bnijik/spotify/explorer).
