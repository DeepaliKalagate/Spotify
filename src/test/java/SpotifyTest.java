import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.apache.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
public class SpotifyTest
{
        String tokenValue;
        String userid="";
        String playlistId="";
        @BeforeMethod
        public void setUp()
        {
            tokenValue = "Bearer BQDGIZLw9rInj1_4z01Wmd2Il9dq2wU07MoEzv0hbsnXr08eyqe5qQJib5mBFQHKo9fn4r02RtKlAuQHZBxS2jrwoDtQtTWvbAo8vwoL6Tvx7OH3jfxL2Ygtf-vSw5RIdFW8pbJUS2rMsDDYlGHeNYOYKAg4kcXBdToroNAzAp6rrGIRml4XnynRT2CVjO5Z2qsFzwmCK9RlKii0O1Cu88MSINYQrs0d7yGdrhtbmvTHMHjyZWRc2Id_HPDFOuo-B1BQaVFHHD_r_Jy71e7THZD_udxkoQ";
        }

    @Test
    public void givenApi_WhenPassedToken_ShouldGiveSuccessCode() throws ParseException
    {
        //Fecthing User Id
        Response response = RestAssured.given()
                .accept("application/json")
                .contentType(ContentType.JSON)
                .header("Authorization", tokenValue)
                .when()
                .get("https://api.spotify.com/v1/me");
        ResponseBody body = response.getBody();
        int statusCode = response.getStatusCode();
        JSONObject Object = (JSONObject) new JSONParser().parse(body.prettyPrint());
        userid = (String) Object.get("id");
        System.out.println("\n\nId is : "+userid);
        Assert.assertEquals(200, statusCode);

        //Matching User Id
        Response response1 = RestAssured.given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", tokenValue)
                .when()
                .get("https://api.spotify.com/v1/me");
        int statusCode1=response1.getStatusCode();
        ResponseBody body1=response1.getBody();
        Object object1=new JSONParser().parse(body1.prettyPrint());
        MatcherAssert.assertThat(statusCode1, Matchers.equalTo(HttpStatus.SC_OK));

        //Playlist Id
        Response response7=RestAssured.given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", tokenValue)
                .when()
                .get("https://api.spotify.com/v1/me/playlists");
        int statusCode7=response7.getStatusCode();
        ResponseBody body6=response7.getBody();
        Object object6=new JSONParser().parse(body6.prettyPrint());
        String getAsString=response7.asString();
        JsonPath jsonPath=new JsonPath(getAsString);
        String playlistID=response7.path("items[0].id");
        System.out.println("Playlist id : "+playlistID);

        //Getting List Of User Id
        Response response2 = RestAssured.given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", tokenValue)
                .when()
                .get("https://api.spotify.com/v1/users/"+userid);
        int statusCode2=response2.getStatusCode();
        ResponseBody body2=response2.getBody();
        Object object2=new JSONParser().parse(body2.prettyPrint());
        MatcherAssert.assertThat(statusCode2, Matchers.equalTo(HttpStatus.SC_OK));

        //Getting playlist
        Response response3 = RestAssured.given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", tokenValue)
                .when()
                .get("https://api.spotify.com/v1/users/"+userid+"/playlists");
        int statusCode3=response3.getStatusCode();
        ResponseBody body3=response3.getBody();
        Object object3=new JSONParser().parse(body3.prettyPrint());
        MatcherAssert.assertThat(statusCode3, Matchers.equalTo(HttpStatus.SC_OK));

        //Creating Playlist
        Response response4 = RestAssured.given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", tokenValue)
                .body("{\"name\" :\"New Songs Album\",\"description\" : \"New Songs Album\",\"public\":\"false\"}")
                .when()
                .post("https://api.spotify.com/v1/users/"+userid+"/playlists");
        int statusCode4=response4.getStatusCode();
        ResponseBody body4=response4.getBody();
        Object object4=new JSONParser().parse(body4.prettyPrint());
        MatcherAssert.assertThat(statusCode4, Matchers.equalTo(HttpStatus.SC_CREATED));

        //Update Playlist Name
        Response response8=RestAssured.given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", tokenValue)
                .body("{\"name\" :\"My Favourite Songs Album\",\"description\" : \"New Songs Album\",\"public\":\"false\"}")
                .when()
                .put("https://api.spotify.com/v1/me/playlists/"+playlistId);
        int statusCode8=response8.getStatusCode();
        System.out.println("Playlist name updated Successfully");
    }

    //Count Playlist
    @Test
    public void countPlaylist() throws ParseException
    {
        Response response6 = RestAssured.given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", tokenValue)
                .when()
                .get("https://api.spotify.com/v1/me/playlists");
        ResponseBody body6=response6.getBody();
        Object object6=new JSONParser().parse(body6.prettyPrint());
        String getAsString=response6.asString();
        JsonPath jsonPath=new JsonPath(getAsString);
        int count=jsonPath.getInt("total");
        System.out.println("Total count is : "+count);
        Assert.assertEquals(21,count);
    }
}