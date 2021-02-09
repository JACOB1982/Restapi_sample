package RestAssuredPackage;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Random;

import org.json.simple.JSONObject;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Sample {
static String url="http://localhost:3000";
String name;
double sal;
int id1,id2;
boolean flag;

	@Test
	public void test() throws InterruptedException {
		//fail("Not yet implemented");
		RestAssured.baseURI = url;
		
		name = "UserA"+Integer.toString(new Random().nextInt(99));
		System.out.println("Name ->"+name);
		sal =  1000.00;

		Thread.sleep(1000);
		id1 = createUser(name,sal);
		
		
		System.out.println("Returned Id value -->"+ id1);
		
		Thread.sleep(1000);
		name = "UserB"+Integer.toString(new Random().nextInt(99));
		System.out.println("Name ->"+name);
		sal = 2000.00;
		id2 = createUser(name,sal);
		
		System.out.println("Returned Id value -->"+ id2);
		Thread.sleep(1000);
				
		deleteUser(id1);
		Thread.sleep(1000);
		
		
		flag = verifyUser(id1);
		
		flag = updateUser(id2);
		flag = verifyUser(id2);
		
		getallUsers(url);
	}

	private void getallUsers(String url)
	{
		System.out.println("###############################################START GET");
		String tmp_url;
		tmp_url = url;
		Response Resp;
		RequestSpecification Req;
        //RestAssured.baseURI = tmp_url;
        Req = RestAssured.given();
        Resp = Req.get("/employees");
        Resp.prettyPrint();
        JsonPath parse = new JsonPath(Resp.body().asString());
        List<Integer> Idlist = parse.get("id");
        System.out.println("Number of employees -->" + Idlist.size());
        System.out.println("###############################################END");
        
	}
	
	
	private boolean verifyUser(int id)
	{
		System.out.println("###############################################START VERIFY");
		int tmp_id;
		Response Resp;
		RequestSpecification Req;
        //RestAssured.baseURI = url;
        Req = RestAssured.given();
        Resp = Req.get("/employees/"+id);
        if(Resp.getStatusCode()==404)
        {
        	System.out.println("ID Not found");
        	return false;
        }
        else
        {
        JsonPath parse = new JsonPath(Resp.body().asString());
        tmp_id = parse.getInt("id");
        
        if (id == tmp_id)
        {
        System.out.println("ID found");
        assertThat(parse.get("id"), equalTo(tmp_id));
        System.out.println("Generated Id-->"+parse.getInt("id"));
        System.out.println("Full Name"+parse.getString("name"));
        }
        
		System.out.println("###############################################END");
		return true;
        }
	}
	
	private int createUser( String fname, double sal)
	{
		System.out.println("###############################################START CREATE");
		int tmp_id;
		Response Resp;
		RequestSpecification Req;
        
		//RestAssured.baseURI = url;
		
        Req = RestAssured.given();
        Req.header("Content-Type", "application/json");
        JSONObject payloadvalue = new JSONObject(); 
        payloadvalue.put("name", fname);
        payloadvalue.put("salary", sal);
//-----------------------------------------------------------------------
        String jsonString = payloadvalue.toJSONString();
		Req.body(jsonString ); 
//-----------------------------------------------------------------------
        Resp = Req.post("/employees");
        
        
        JsonPath parse = new JsonPath(Resp.body().asString());
        parse.prettyPrint();
        
        assertThat(parse.get("id"), greaterThan(0));	
    	System.out.println("###############################################END");
		return (int)parse.get("id");
		
	}
	
	private void deleteUser(int id)
	{
		System.out.println("###############################################START DELETE");
				
		Response Resp;
		RequestSpecification Req;
        
		//RestAssured.baseURI = url;
        Req = RestAssured.given();

        Resp = Req.delete("/employees/"+id);
        System.out.println("Delete method ->" + id);
        
        assertThat(Resp.statusCode(), equalTo(200));
            	
      	System.out.println("###############################################END");
	}

	private boolean updateUser (int id)
	{
		System.out.println("###############################################START DELETE");
				
		Response Resp;
		RequestSpecification Req;
        
		//RestAssured.baseURI = url;
        Req = RestAssured.given();

        JSONObject payloadvalues = new JSONObject();
        payloadvalues.put("name", "Updated Name");
        String jsonString = payloadvalues.toJSONString();
        Req.body(jsonString);
        
        Resp = Req.patch("/employees/"+id);
        
        System.out.println("Update method ->" + id);
        
        assertThat(Resp.statusCode(), equalTo(200)); 	
      	System.out.println("###############################################END");
      	return true;
	}

	
}
