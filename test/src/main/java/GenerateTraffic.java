import java.io.FileOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Iterator;



public class GenerateTraffic {

	
	public static void main(String[] args) {
	
	String url ="http://shop:8010";
	if (null != args && args.length >0 ) {
		url = args[0];
	}

		System.out.println("Utah Location");
		for (int j=0; j<50; j++) {
			
		
			try {
		
				HttpClient client = HttpClient.newHttpClient();
				String theURL = url + "?name=Guest&location=Utah";
			
				HttpRequest request = HttpRequest.newBuilder().uri(URI.create(theURL)).build();
				
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
				String sResult = response.body().toString();
			 
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		System.out.println("California Location");
		long startTime = System.nanoTime();
	
		
		for (int k=0; k<100; k++) {
			
			
			try {
			
				HttpClient client = HttpClient.newHttpClient();
				String theURL =  url + "?name=Guest&location=California";
			
				HttpRequest request = HttpRequest.newBuilder().uri(URI.create(theURL)).build();
				
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
				String sResult = response.body().toString();
			 
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		long endTime = System.nanoTime();

		long duration = (endTime - startTime);
		
		System.out.println("CALIFORNIA DURATION IS: " + duration /  1000000000);
		
		startTime = System.nanoTime();
		
		System.out.println("Colorado Location");
		
		for (int l=0; l<100; l++) {
			
			
			try {
		
				HttpClient client = HttpClient.newHttpClient();
				String theURL =  url + "?name=Guest&location=Colorado";
			
				HttpRequest request = HttpRequest.newBuilder().uri(URI.create(theURL)).build();
				
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
				String sResult = response.body().toString();
			 
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
		 endTime = System.nanoTime();

		 duration = (endTime - startTime);
		
		System.out.println("COLORADO DURATION IS: " + duration/ 1000000000);
	}
}
