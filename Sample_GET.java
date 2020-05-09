import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
public class Sample_GET {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RestAssured.baseURI= "http://dummy.restapiexample.com/";
		//try
		{
		String api_contentType=when().
		get("/public-api/users").
		then().
		assertThat().statusCode(404).extract().response().getContentType();
		
		System.out.println("Assertion Passed\n" + api_contentType);
		}
		/*catch (Exception e)
		{
			e.printStackTrace();
		}*/
	}

}
