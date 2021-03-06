/*
 * Basic program.
 *--Verify the Hash:
 *--Loads organization Hash from ./src/TXIDListOne.txt
 *--Displays Unique Secure Hash using the 2 USER Inputs and the organization Hash
 *--Saves a Screenshot to ./out/ss/<HASH>.png
 *--Handles Exception errors.
 */
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

/**
 * @author per_z
 *
 */
public class HashVerifier {
	
	int salt;
	
	public HashVerifier() {
		salt = 0;
	}
	
	final int Salt() throws NoSuchAlgorithmException {
	SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
	salt = random.nextInt(9000000) + 1000000;
	return salt;
	}
/*	
	public int GetSalt() {
		return salt;
	}
*/
	
	private static Scanner scanner = new Scanner( System.in );

	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

		try {
			System.out.println("~~~~This Program Allows You to Verify the HASH.");
			System.out.println("~~~~Enter The Date and The Time below.");
			System.out.println("~~~~Example Date: mmddyyyy like 02062018 for 02.06.2018");
			System.out.println("~~~~Example Time: hhmmss like 120101 for 12:01:01 PM");
			TimeUnit.SECONDS.sleep(1); //wait 1 second
			// Instantiate a Date object
			Date date = new Date();
			// display time and date using toString()
			System.out.println("\nCurrent Date and Time: "+date.toString());
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
		}
		catch (Exception e) {
			throw new RuntimeException("Error! Something went wrong!");
		}

		// Asks USER for first input
		System.out.println("\nEnter the Date: ");
		String input1 = String.valueOf(scanner.nextLine());

		try {
			TimeUnit.SECONDS.sleep(1); //wait 1 second
		}
		catch (Exception e) {
			throw new RuntimeException("Error! Something went wrong!");
		}

		// Asks USER for second input
		System.out.println("\nEnter the Time: ");
		String input2 = String.valueOf(scanner.nextLine());

		try {
			TimeUnit.SECONDS.sleep(1); //wait 1 second
		}
		catch (Exception e) {
			throw new RuntimeException("Error! Something went wrong!");
		}

		// Asks USER for third input
		System.out.println("\nEnter the Hash to verify: ");
		String input3 = String.valueOf(scanner.nextLine());

		HashMap<Integer, String> TXIDList = new HashMap<Integer, String>();

		BufferedReader TXID = new BufferedReader( new FileReader ("./src/TXIDListOne.txt"));
		String line = ":";

		int i = 1; //start from i = 1 instead of i = 0 otherwise output starts at 0 is:... instead of 1 is:...

		while ((line = TXID.readLine()) != null) {

			TXIDList.put(i, line);
			i++;
		}
		TXID.close();

		if (TXIDList.size() != 1) {
			System.out.println("\n" + "There can't be: " + TXIDList.size() + " TXID!" + "\n");
		}

		for (int k=0; k<TXIDList.size(); k++)
		{
			//1. String _idtoString = _id;
			String _id = input1;
			//2. String _totaltoString = _total;
			String _total = input2;
			//3. append id before total
			String _id_total = (_id+_total);

			//3abc: for randomization implementations
			boolean dicer;
			// Asks USER for fourth input
			//Set dicer to true or false
			//if dicer is true, use salt method for Random Hash Generation
			//if dicer is false, use append method for Fixed Hash Generation
			//dicer = false;
			System.out.println("\nDicer true or false? Type 'true' or 'false'.");
			String input4 = String.valueOf(scanner.nextLine());
			dicer = Boolean.valueOf(input4);
			String unQ = null;
			if (dicer!= false) {
				//3a. use SecureRandom to create salt
				HashVerifier mySalt = new HashVerifier();
//				mySalt.GetSalt();
				int dSalt = mySalt.Salt();
				
				//3b. append id before salt before total
				unQ = (_id+String.valueOf(dSalt)+_total);
				System.out.println("\nSalt: "+(dSalt)+".");
				System.out.println("Unique String: "+unQ+".\n");
			}
			else {
				//3c. append id before _id_total before total
				unQ = (_id+_id_total+_total);
			}
			//4. 3b or 3c
			String unQString = (unQ);

			//5. idtoSHA256: sha256Hex of id
			String _idtoSHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(_id);
			//6. totaltoSha256: sha256Hex of total
			String _totaltoSHA512 = org.apache.commons.codec.digest.DigestUtils.sha512Hex(_total);
			//7. stoHash1: appended _id_total's Sha256Hex
			String stoHash1 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(_id_total);

			//8. append id before stoHash1(appended _id_total's Sha256Hex) before total
			String hString1 = (_id+stoHash1+_total);
			//9. append idtoSHA256 before unQString before totaltoSHA256
			String hString2 = (_idtoSHA256+unQString+_totaltoSHA512);
			//10. sha256hex of hstring1: appended id before stoHash1(appended _id_total's Sha256Hex) before total
			String hS1toSHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hString1);
			//11. sha256 of hstring2: appended idtoSHA256 before unQString before totaltoSHA256
			String hS2toSHA512 = org.apache.commons.codec.digest.DigestUtils.sha512Hex(hString2);
			//12. sha256hex of hString1 appended before hString2
			String hStoSHA256 = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hString1+hString2); 

			//13: 
			String hString3 = (hS1toSHA256+hS2toSHA512+hStoSHA256);
			//Final0: 
			String hashFinal = org.apache.commons.codec.digest.DigestUtils.sha512Hex(hString3);

			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			System.out.println("\n~~~~Commencing HASH Verification Display\n");
			System.out.println("\nThe Date: "+_id+"." + "\nThe Time: " + _total+".\n");
			System.out.println("Organization: PZ.");

			try (BufferedReader show = new BufferedReader(new FileReader("./src/TXIDListOne.txt"))) {
				String _line = null;
				if ((_line = show.readLine()) != null) {
					String hashZ1 = (hashFinal+_line);
					String hashZ2 = org.apache.commons.codec.digest.DigestUtils.sha512Hex(hashZ1);
					String hashZ3 = org.apache.commons.codec.digest.DigestUtils.sha512Hex(hashZ2);
					String hashZf = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hashZ3);
					String hashZFinal = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hashZf);

					if (input3.equals(hashZFinal)) {
						System.out.println("\nThe HASH is correct.");
						System.out.println("\nThe Hash for that Date and Time: "+hashZFinal+".");
					}
					else {
						System.out.println("\nThe HASH is incorrect! Make sure you entered correctly!");
						//remove next line for production
						System.out.println("\nThe Hash for that Date and Time: "+hashZFinal+".");
					}

					// determine current screen size
					Toolkit toolkit = Toolkit.getDefaultToolkit();
					Dimension screenSize = toolkit.getScreenSize();
					Rectangle screenRect = new Rectangle(screenSize);

					// Screen Capture section
					System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
					System.out.println("~~~~Screen Capture\n");
					Date date = new Date();
					String dMonth = String.format("%tb", date);
					String dDay = String.format("%td", date);
					String dYear = String.format("%tY", date);
					String dHour = String.format("%tH", date);
					String dMinute = String.format("%tM", date);
					String dSecond = String.format("%tS", date);
					String outImage = ("./out/ss/["+_id+"_"+_total+"] "+hashZFinal+".gif");
					String outImageNew = ("./out/ss/checked/["+dMonth+" "+dDay+" "+dYear+" "+dHour+"_"+dMinute+"_"+dSecond+"] "+hashZFinal+"_checked.gif");
					File f = new File(outImage);
					File fNew = new File(outImageNew);					
					try {
						Robot robot;
						robot = new Robot();
						BufferedImage image = robot.createScreenCapture(screenRect);
						if(f.exists() && !f.isDirectory() && fNew.exists() && !fNew.isDirectory()) {
							System.out.println("\nThe image exists in both folders! No screenshots taken!");
						}
						else if (f.exists() && !f.isDirectory()) {
							System.out.println("\nThe image exists, now taking a screenshot!");
							// save captured image to GIF file
							ImageIO.write(image, "gif", fNew);
						}
						else {
							// save captured image to GIF file
							ImageIO.write(image, "gif", f);
							// give feedback
							System.out.println("Saved screen shot (" + image.getWidth() +
									" x " + image.getHeight() + ") to file: "+outImage+".");
						}
					}
					catch (AWTException e1) {
						throw new RuntimeException("Error! File not found! Make sure the folder exists!");
					}
					System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				}
			} catch (FileNotFoundException e) {
				throw new RuntimeException("Error! File not found! Make sure the filepath is correct!");
			} catch (IOException e) {
				throw new RuntimeException("Error! Something went wrong! Please try again!");
			}
		}
	}
}