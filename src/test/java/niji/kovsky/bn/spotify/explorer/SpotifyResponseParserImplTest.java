package niji.kovsky.bn.spotify.explorer;

import niji.kovsky.bn.spotify.explorer.model.Album;
import niji.kovsky.bn.spotify.explorer.model.Category;
import niji.kovsky.bn.spotify.explorer.model.Playlist;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpotifyResponseParserImplTest {

    @Test
    void parseNewAlbums() {
        SpotifyResponseParser<String> jsonParser = new SpotifyResponseParserImpl();
        String newAlbumsJson = "{\n" +
                               "  \"albums\" : {\n" +
                               "    \"href\" : \"https://api.spotify.com/v1/browse/new-releases?country=SE&offset=0&limit=20\",\n" +
                               "    \"items\" : [ {\n" +
                               "      \"album_type\" : \"single\",\n" +
                               "      \"artists\" : [ {\n" +
                               "        \"external_urls\" : {\n" +
                               "          \"spotify\" : \"https://open.spotify.com/artist/2RdwBSPQiwcmiDo9kixcl8\"\n" +
                               "        },\n" +
                               "        \"href\" : \"https://api.spotify.com/v1/artists/2RdwBSPQiwcmiDo9kixcl8\",\n" +
                               "        \"id\" : \"2RdwBSPQiwcmiDo9kixcl8\",\n" +
                               "        \"name\" : \"Pharrell Williams\",\n" +
                               "        \"type\" : \"artist\",\n" +
                               "        \"uri\" : \"spotify:artist:2RdwBSPQiwcmiDo9kixcl8\"\n" +
                               "      } ],\n" +
                               "      \"available_markets\" : [ \"AD\", \"AR\", \"AT\", \"AU\", \"BE\", \"BG\", \"BO\", \"BR\", \"CA\", \"CH\", \"CL\", \"CO\", \"CR\", \"CY\", \"CZ\", \"DE\", \"DK\", \"DO\", \"EC\", \"EE\", \"ES\", \"FI\", \"FR\", \"GB\", \"GR\", \"GT\", \"HK\", \"HN\", \"HU\", \"ID\", \"IE\", \"IS\", \"IT\", \"JP\", \"LI\", \"LT\", \"LU\", \"LV\", \"MC\", \"MT\", \"MX\", \"MY\", \"NI\", \"NL\", \"NO\", \"NZ\", \"PA\", \"PE\", \"PH\", \"PL\", \"PT\", \"PY\", \"SE\", \"SG\", \"SK\", \"SV\", \"TR\", \"TW\", \"US\", \"UY\" ],\n" +
                               "      \"external_urls\" : {\n" +
                               "        \"spotify\" : \"https://open.spotify.com/album/5ZX4m5aVSmWQ5iHAPQpT71\"\n" +
                               "      },\n" +
                               "      \"href\" : \"https://api.spotify.com/v1/albums/5ZX4m5aVSmWQ5iHAPQpT71\",\n" +
                               "      \"id\" : \"5ZX4m5aVSmWQ5iHAPQpT71\",\n" +
                               "      \"images\" : [ {\n" +
                               "        \"height\" : 640,\n" +
                               "        \"url\" : \"https://i.scdn.co/image/e6b635ebe3ef4ba22492f5698a7b5d417f78b88a\",\n" +
                               "        \"width\" : 640\n" +
                               "      }, {\n" +
                               "        \"height\" : 300,\n" +
                               "        \"url\" : \"https://i.scdn.co/image/92ae5b0fe64870c09004dd2e745a4fb1bf7de39d\",\n" +
                               "        \"width\" : 300\n" +
                               "      }, {\n" +
                               "        \"height\" : 64,\n" +
                               "        \"url\" : \"https://i.scdn.co/image/8a7ab6fc2c9f678308ba0f694ecd5718dc6bc930\",\n" +
                               "        \"width\" : 64\n" +
                               "      } ],\n" +
                               "      \"name\" : \"Runnin'\",\n" +
                               "      \"type\" : \"album\",\n" +
                               "      \"uri\" : \"spotify:album:5ZX4m5aVSmWQ5iHAPQpT71\"\n" +
                               "    }, {\n" +
                               "      \"album_type\" : \"single\",\n" +
                               "      \"artists\" : [ {\n" +
                               "        \"external_urls\" : {\n" +
                               "          \"spotify\" : \"https://open.spotify.com/artist/3TVXtAsR1Inumwj472S9r4\"\n" +
                               "        },\n" +
                               "        \"href\" : \"https://api.spotify.com/v1/artists/3TVXtAsR1Inumwj472S9r4\",\n" +
                               "        \"id\" : \"3TVXtAsR1Inumwj472S9r4\",\n" +
                               "        \"name\" : \"Drake\",\n" +
                               "        \"type\" : \"artist\",\n" +
                               "        \"uri\" : \"spotify:artist:3TVXtAsR1Inumwj472S9r4\"\n" +
                               "      } ],\n" +
                               "      \"available_markets\" : [ \"AD\", \"AR\", \"AT\", \"AU\", \"BE\", \"BG\", \"BO\", \"BR\", \"CH\", \"CL\", \"CO\", \"CR\", \"CY\", \"CZ\", \"DE\", \"DK\", \"DO\", \"EC\", \"EE\", \"ES\", \"FI\", \"FR\", \"GB\", \"GR\", \"GT\", \"HK\", \"HN\", \"HU\", \"ID\", \"IE\", \"IS\", \"IT\", \"JP\", \"LI\", \"LT\", \"LU\", \"LV\", \"MC\", \"MT\", \"MY\", \"NI\", \"NL\", \"NO\", \"NZ\", \"PA\", \"PE\", \"PH\", \"PL\", \"PT\", \"PY\", \"SE\", \"SG\", \"SK\", \"SV\", \"TR\", \"TW\", \"UY\" ],\n" +
                               "      \"external_urls\" : {\n" +
                               "        \"spotify\" : \"https://open.spotify.com/album/0geTzdk2InlqIoB16fW9Nd\"\n" +
                               "      },\n" +
                               "      \"href\" : \"https://api.spotify.com/v1/albums/0geTzdk2InlqIoB16fW9Nd\",\n" +
                               "      \"id\" : \"0geTzdk2InlqIoB16fW9Nd\",\n" +
                               "      \"images\" : [ {\n" +
                               "        \"height\" : 640,\n" +
                               "        \"url\" : \"https://i.scdn.co/image/d40e9c3d22bde2fbdb2ecc03cccd7a0e77f42e4c\",\n" +
                               "        \"width\" : 640\n" +
                               "      }, {\n" +
                               "        \"height\" : 300,\n" +
                               "        \"url\" : \"https://i.scdn.co/image/dff06a3375f6d9b32ecb081eb9a60bbafecb5731\",\n" +
                               "        \"width\" : 300\n" +
                               "      }, {\n" +
                               "        \"height\" : 64,\n" +
                               "        \"url\" : \"https://i.scdn.co/image/808a02bd7fc59b0652c9df9f68675edbffe07a79\",\n" +
                               "        \"width\" : 64\n" +
                               "      } ],\n" +
                               "      \"name\" : \"Sneakin’\",\n" +
                               "      \"type\" : \"album\",\n" +
                               "      \"uri\" : \"spotify:album:0geTzdk2InlqIoB16fW9Nd\"\n" +
                               "    } ],\n" +
                               "    \"limit\" : 20,\n" +
                               "    \"next\" : \"https://api.spotify.com/v1/browse/new-releases?country=SE&offset=20&limit=20\",\n" +
                               "    \"offset\" : 0,\n" +
                               "    \"previous\" : null,\n" +
                               "    \"total\" : 500\n" +
                               "  }\n" +
                               "}";

        List<Album> expectedAlbumList = List.of(
                new Album("Runnin'", "[Pharrell Williams]", "https://api.spotify.com/v1/albums/5ZX4m5aVSmWQ5iHAPQpT71"),
                new Album("Sneakin’", "[Drake]", "https://api.spotify.com/v1/albums/0geTzdk2InlqIoB16fW9Nd")
        );

        assertEquals(expectedAlbumList, jsonParser.parseNewAlbums(newAlbumsJson));
    }

    @Test
    void parseCategories() {
        SpotifyResponseParser<String> jsonParser = new SpotifyResponseParserImpl();
        String categoriesJson = "{\n" +
                                "  \"categories\" : {\n" +
                                "    \"href\" : \"https://api.spotify.com/v1/browse/categories?offset=0&limit=20\",\n" +
                                "    \"items\" : [ {\n" +
                                "      \"href\" : \"https://api.spotify.com/v1/browse/categories/toplists\",\n" +
                                "      \"icons\" : [ {\n" +
                                "        \"height\" : 275,\n" +
                                "        \"url\" : \"https://datsnxq1rwndn.cloudfront.net/media/derived/toplists_11160599e6a04ac5d6f2757f5511778f_0_0_275_275.jpg\",\n" +
                                "        \"width\" : 275\n" +
                                "      } ],\n" +
                                "      \"id\" : \"toplists\",\n" +
                                "      \"name\" : \"Top Lists\"\n" +
                                "    }, {\n" +
                                "      \"href\" : \"https://api.spotify.com/v1/browse/categories/mood\",\n" +
                                "      \"icons\" : [ {\n" +
                                "        \"height\" : 274,\n" +
                                "        \"url\" : \"https://datsnxq1rwndn.cloudfront.net/media/original/mood-274x274_976986a31ac8c49794cbdc7246fd5ad7_274x274.jpg\",\n" +
                                "        \"width\" : 274\n" +
                                "      } ],\n" +
                                "      \"id\" : \"mood\",\n" +
                                "      \"name\" : \"Mood\"\n" +
                                "    }, {\n" +
                                "      \"href\" : \"https://api.spotify.com/v1/browse/categories/party\",\n" +
                                "      \"icons\" : [ {\n" +
                                "        \"height\" : 274,\n" +
                                "        \"url\" : \"https://datsnxq1rwndn.cloudfront.net/media/derived/party-274x274_73d1907a7371c3bb96a288390a96ee27_0_0_274_274.jpg\",\n" +
                                "        \"width\" : 274\n" +
                                "      } ],\n" +
                                "      \"id\" : \"party\",\n" +
                                "      \"name\" : \"Party\"\n" +
                                "    }, {\n" +
                                "      \"href\" : \"https://api.spotify.com/v1/browse/categories/pop\",\n" +
                                "      \"icons\" : [ {\n" +
                                "        \"height\" : 274,\n" +
                                "        \"url\" : \"https://datsnxq1rwndn.cloudfront.net/media/derived/pop-274x274_447148649685019f5e2a03a39e78ba52_0_0_274_274.jpg\",\n" +
                                "        \"width\" : 274\n" +
                                "      } ],\n" +
                                "      \"id\" : \"pop\",\n" +
                                "      \"name\" : \"Pop\"\n" +
                                "    }, {\n" +
                                "      \"href\" : \"https://api.spotify.com/v1/browse/categories/workout\",\n" +
                                "      \"icons\" : [ {\n" +
                                "        \"height\" : 275,\n" +
                                "        \"url\" : \"https://datsnxq1rwndn.cloudfront.net/media/derived/workout_856581c1c545a5305e49a3cd8be804a0_0_0_275_275.jpg\",\n" +
                                "        \"width\" : 275\n" +
                                "      } ],\n" +
                                "      \"id\" : \"workout\",\n" +
                                "      \"name\" : \"Workout\"\n" +
                                "    } ],\n" +
                                "    \"limit\" : 20,\n" +
                                "    \"next\" : \"https://api.spotify.com/v1/browse/categories?offset=20&limit=20\",\n" +
                                "    \"offset\" : 0,\n" +
                                "    \"previous\" : null,\n" +
                                "    \"total\" : 31\n" +
                                "  }\n" +
                                "}";

        List<Category> expectedCategoryList = List.of(
                new Category("Top Lists", "toplists"),
                new Category("Mood", "mood"),
                new Category("Party", "party"),
                new Category("Pop", "pop"),
                new Category("Workout", "workout")
        );

        assertEquals(expectedCategoryList, jsonParser.parseCategories(categoriesJson));
    }

    @Test
    void parsePlaylists() {
        SpotifyResponseParser<String> jsonParser = new SpotifyResponseParserImpl();
        String playlistsJson = "{\n" +
                               "  \"message\" : \"Monday morning music, coming right up!\",\n" +
                               "  \"playlists\" : {\n" +
                               "    \"href\" : \"https://api.spotify.com/v1/browse/featured-playlists?country=SE&timestamp=2015-05-18T06:44:32&offset=0&limit=2\",\n" +
                               "    \"items\" : [ {\n" +
                               "      \"collaborative\" : false,\n" +
                               "      \"description\" : \"Relaxed deep house to slowly help you get back on your feet and ready yourself for a productive week.\",\n" +
                               "      \"external_urls\" : {\n" +
                               "        \"spotify\" : \"http://open.spotify.com/user/spotify/playlist/6ftJBzU2LLQcaKefMi7ee7\"\n" +
                               "      },\n" +
                               "      \"href\" : \"https://api.spotify.com/v1/users/spotify/playlists/6ftJBzU2LLQcaKefMi7ee7\",\n" +
                               "      \"id\" : \"6ftJBzU2LLQcaKefMi7ee7\",\n" +
                               "      \"images\" : [ {\n" +
                               "        \"height\" : 300,\n" +
                               "        \"url\" : \"https://i.scdn.co/image/7bd33c65ebd1e45975bbcbbf513bafe272f033c7\",\n" +
                               "        \"width\" : 300\n" +
                               "      } ],\n" +
                               "      \"name\" : \"Monday Morning Mood\",\n" +
                               "      \"owner\" : {\n" +
                               "        \"external_urls\" : {\n" +
                               "          \"spotify\" : \"http://open.spotify.com/user/spotify\"\n" +
                               "        },\n" +
                               "        \"href\" : \"https://api.spotify.com/v1/users/spotify\",\n" +
                               "        \"id\" : \"spotify\",\n" +
                               "        \"type\" : \"user\",\n" +
                               "        \"uri\" : \"spotify:user:spotify\"\n" +
                               "      },\n" +
                               "      \"public\" : null,\n" +
                               "      \"snapshot_id\" : \"WwGvSIVUkUvGvqjgj/bQHlRycYmJ2TkoIxYfoalWlmIZT6TvsgvGMgtQ2dGbkrAW\",\n" +
                               "      \"tracks\" : {\n" +
                               "        \"href\" : \"https://api.spotify.com/v1/users/spotify/playlists/6ftJBzU2LLQcaKefMi7ee7/tracks\",\n" +
                               "        \"total\" : 245\n" +
                               "      },\n" +
                               "      \"type\" : \"playlist\",\n" +
                               "      \"uri\" : \"spotify:user:spotify:playlist:6ftJBzU2LLQcaKefMi7ee7\"\n" +
                               "    }, {\n" +
                               "      \"collaborative\" : false,\n" +
                               "      \"description\" : \"Du kommer studsa ur sängen med den här spellistan.\",\n" +
                               "      \"external_urls\" : {\n" +
                               "        \"spotify\" : \"http://open.spotify.com/user/spotify__sverige/playlist/4uOEx4OUrkoGNZoIlWMUbO\"\n" +
                               "      },\n" +
                               "      \"href\" : \"https://api.spotify.com/v1/users/spotify__sverige/playlists/4uOEx4OUrkoGNZoIlWMUbO\",\n" +
                               "      \"id\" : \"4uOEx4OUrkoGNZoIlWMUbO\",\n" +
                               "      \"images\" : [ {\n" +
                               "        \"height\" : 300,\n" +
                               "        \"url\" : \"https://i.scdn.co/image/24aa1d1b491dd529b9c03392f350740ed73438d8\",\n" +
                               "        \"width\" : 300\n" +
                               "      } ],\n" +
                               "      \"name\" : \"Upp och hoppa!\",\n" +
                               "      \"owner\" : {\n" +
                               "        \"external_urls\" : {\n" +
                               "          \"spotify\" : \"http://open.spotify.com/user/spotify__sverige\"\n" +
                               "        },\n" +
                               "        \"href\" : \"https://api.spotify.com/v1/users/spotify__sverige\",\n" +
                               "        \"id\" : \"spotify__sverige\",\n" +
                               "        \"type\" : \"user\",\n" +
                               "        \"uri\" : \"spotify:user:spotify__sverige\"\n" +
                               "      },\n" +
                               "      \"public\" : null,\n" +
                               "      \"snapshot_id\" : \"0j9Rcbt2KtCXEXKtKy/tnSL5r4byjDBOIVY1dn4S6GV73EEUgNuK2hU+QyDuNnXz\",\n" +
                               "      \"tracks\" : {\n" +
                               "        \"href\" : \"https://api.spotify.com/v1/users/spotify__sverige/playlists/4uOEx4OUrkoGNZoIlWMUbO/tracks\",\n" +
                               "        \"total\" : 38\n" +
                               "      },\n" +
                               "      \"type\" : \"playlist\",\n" +
                               "      \"uri\" : \"spotify:user:spotify__sverige:playlist:4uOEx4OUrkoGNZoIlWMUbO\"\n" +
                               "    } ],\n" +
                               "    \"limit\" : 2,\n" +
                               "    \"next\" : \"https://api.spotify.com/v1/browse/featured-playlists?country=SE&timestamp=2015-05-18T06:44:32&offset=2&limit=2\",\n" +
                               "    \"offset\" : 0,\n" +
                               "    \"previous\" : null,\n" +
                               "    \"total\" : 12\n" +
                               "  }\n" +
                               "}";

        List<Playlist> expectedPlaylists = List.of(
                new Playlist("Monday Morning Mood", "http://open.spotify.com/user/spotify/playlist/6ftJBzU2LLQcaKefMi7ee7", "featured"),
                new Playlist("Upp och hoppa!", "http://open.spotify.com/user/spotify__sverige/playlist/4uOEx4OUrkoGNZoIlWMUbO", "featured")
        );

        assertEquals(expectedPlaylists, jsonParser.parsePlaylists(playlistsJson, "featured"));
    }

    @Test
    void parseAccessToken() {
        SpotifyResponseParser<String> jsonParser = new SpotifyResponseParserImpl();
        String accessTokenJson = "{\n" +
                                 "   \"access_token\": \"NgCXRK...MzYjw\",\n" +
                                 "   \"token_type\": \"Bearer\",\n" +
                                 "   \"scope\": \"user-read-private user-read-email\",\n" +
                                 "   \"expires_in\": 3600,\n" +
                                 "   \"refresh_token\": \"NgAagA...Um_SHo\"\n" +
                                 "}";

        assertEquals("NgCXRK...MzYjw", jsonParser.parseAccessToken(accessTokenJson));
    }
}