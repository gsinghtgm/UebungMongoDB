package singh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;
import com.mongodb.Block;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;

public class Connector {
	public static void addSchueler() throws IOException {

		// Standard URI format: mongodb://[dbuser:dbpassword@]host:port/dbname
		System.out.print("Enter Password for MongoDB:");
		// Enter data using BufferReader
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		// Reading data using readLine
		String password = reader.readLine();
		MongoClientURI uri = new MongoClientURI("mongodb://admin:" + password + "@ds111124.mlab.com:11124/uebung");
		MongoClient client = new MongoClient(uri);
		MongoDatabase db = client.getDatabase(uri.getDatabase());
		MongoCollection<Document> students = db.getCollection("students");
		List<Document> documents = new ArrayList<Document>();
		Document schueler1 = new Document("name", "Schueler 1").append("jahr", 2000).append("klasse", "4DHIT")
				.append("ampeln", Arrays.asList(new Document("fach", "AM").append("farbe", "Gruen"),
						new Document("fach", "INSY").append("farbe", "Gelb")));
		Document schueler2 = new Document("name", "Schueler 2").append("jahr", 2001).append("klasse", "4BHIT")
				.append("ampeln", Arrays.asList(new Document("fach", "AM").append("farbe", "Gelb"),
						new Document("fach", "INSY").append("farbe", "Rot")));
		Document schueler3 = new Document("name", "Schueler 3").append("jahr", 1997).append("klasse", "4DHIT")
				.append("ampeln", Arrays.asList(new Document("fach", "AM").append("farbe", "Gelb"),
						new Document("fach", "INSY").append("farbe", "Gelb")));
		Document schueler4 = new Document("name", "Schueler 4").append("jahr", 1998).append("klasse", "4DHIT")
				.append("ampeln", Arrays.asList(new Document("fach", "AM").append("farbe", "Rot"),
						new Document("fach", "INSY").append("farbe", "Rot")));
		Document schueler5 = new Document("name", "Schueler 5").append("jahr", 1996).append("klasse", "4CHIT")
				.append("ampeln", Arrays.asList(new Document("fach", "AM").append("farbe", "Rot"),
						new Document("fach", "INSY").append("farbe", "Gruen")));
		documents.add(schueler1);
		documents.add(schueler2);
		documents.add(schueler3);
		documents.add(schueler4);
		documents.add(schueler5);
		students.insertMany(documents);
		client.close();
	}

	public static void queries() throws IOException {
		System.out.print("Enter Password for MongoDB:");
		// Enter data using BufferReader
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		// Reading data using readLine
		String password = reader.readLine();
		MongoClientURI uri = new MongoClientURI("mongodb://admin:" + password + "@ds111124.mlab.com:11124/uebung");
		MongoClient client = new MongoClient(uri);
		MongoDatabase db = client.getDatabase(uri.getDatabase());
		MongoCollection<Document> students = db.getCollection("students");
		// - Alle Schueler
		MongoCursor<Document> cursor = students.find().iterator();
		try {
			System.out.println("Alle Schueler:");
			while (cursor.hasNext()) {
				System.out.println(cursor.next().toJson());
			}
		} finally {
			cursor.close();
		}

		// - Alle Schueler, die 2000 gebohren sind
		cursor = students.find(eq("jahr", 2000)).iterator();
		try {
			System.out.println("Alle Schueler, die 2000 gebohren sind:");
			while (cursor.hasNext()) {
				System.out.println(cursor.next().toJson());
			}
		} finally {
			cursor.close();
		}
		// - Alle Schueler, die 2000 gebohren sind, oder aelter
		cursor = students.find(lte("jahr", 2000)).iterator();
		try {
			System.out.println("Alle Schueler, die 2000 gebohren sind, oder aelter:");
			while (cursor.hasNext()) {
				System.out.println(cursor.next().toJson());
			}
		} finally {
			cursor.close();
		}
		// - Alle Schueler, die 2000 geboren sind und in die Klasse 4dhit gehen
		cursor = students.find(and(eq("jahr", 2000), eq("klasse", "4DHIT"))).iterator();
		try {
			System.out.println(" Alle Schueler, die 2000 geboren sind und in die Klasse 4dhit gehen:");
			while (cursor.hasNext()) {
				System.out.println(cursor.next().toJson());
			}
		} finally {
			cursor.close();
		}
		// - Alle Schueler, die in die Klasse 4dhit gehen und in "AM" eine rote Ampel
		// haben
		cursor = students
				.find(and(eq("klasse", "4DHIT"), elemMatch("ampeln", and(eq("fach", "AM"), eq("farbe", "Rot")))))
				.iterator();
		try {
			System.out.println(" Alle Schueler, die in die Klasse 4dhit gehen und in \"AM\" eine rote Ampel:");
			while (cursor.hasNext()) {
				System.out.println(cursor.next().toJson());
			}
		} finally {
			cursor.close();
		}
		client.close();
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		queries();
	}
}