import org.json.JSONObject;

public class Main {

	public static void main(String[] args) {
		System.out.println("****Freshworks ****");
		try {			
			FileEngine fileEngine =  new FileEngine(); // Need to pass Path otherwise default constructor will take default path
			JSONObject jobj = new JSONObject();
			jobj.put("Backend", "java");
			jobj.put("Forntend", "angular");
			fileEngine.CreateEntry("Technologies", jobj );
			
			jobj.put("Author", "Balagurusami");
			jobj.put("Edision", "5thEdision");
			fileEngine.CreateEntry("Book", jobj );
			
			System.out.println(fileEngine.ReadEntry("Technologies"));
			fileEngine.DeleteEntry("Technologies");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
