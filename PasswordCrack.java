import java.util.*;
import java.io.*;

class PasswordCrack {

	public static ArrayList<String> foundHashes = new ArrayList<String>();

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: java PasswordCrack <dictionary> <password file>");	
			return;
		}

		File dictFile = new File("./" + args[0]);
		File passFile = new File("./" + args[1]);

		boolean dictExists = dictFile.exists();
		boolean passwdExists = passFile.exists();

		if (!dictExists || !passwdExists) {
			System.out.println("The dictionary file or password file does not exist or it is not in this directory.");
			System.out.println("Usage: java PasswordCrack <dictionary> <password file>");
			return;
		}
		
		ArrayList<String> dictionary = fileToArrayList(args[0]);
		ArrayList<String> passwd = fileToArrayList(args[1]);

		crackPasswords(dictionary, passwd);
		System.out.println("proceeding with analysis of passwd file");
		passwdAnalysis(dictionary, passwd);
		System.out.println("proceeding with permutations of dictionary");
		mutateDictionary(dictionary, passwd);
		System.out.println("done");
	}
	

	public static ArrayList<String> fileToArrayList(String fileName) {
		ArrayList<String> contentBuilder = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader("./"+fileName))) {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				contentBuilder.add(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return contentBuilder;
	}
	
	public static void mutateDictionary(ArrayList<String> dictionary, ArrayList<String> p) {
		
		System.out.println("appending numbers");
		for (int i = 0; i < 10; i++) {
			ArrayList<String> appended = new ArrayList<String>();
			for (int j = 0; j < dictionary.size(); j++) {
				appended.add(dictionary.get(j) + Integer.toString(i));
			}

			crackPasswords(appended, p);
		}

		System.out.println("appending letters");
		for (int i = 65; i < 123; i++) {
                        ArrayList<String> appendedLetters = new ArrayList<String>();
                        for (int j = 0; j < dictionary.size(); j++) {
                                appendedLetters.add(dictionary.get(j) + (char)i);
                        }

                        crackPasswords(appendedLetters, p);
                }

		System.out.println("prepending letters");
                for (int i = 65; i < 123; i++) {
                        ArrayList<String> prependedLetters = new ArrayList<String>();
                        for (int j = 0; j < dictionary.size(); j++) {
                                prependedLetters.add((char)i + dictionary.get(j));
                        }

                        crackPasswords(prependedLetters, p);
                }
		
		System.out.println("Prepending");
		for (int i = 0; i < 10; i++) {
			ArrayList<String> prepended = new ArrayList<String>();
                        for (int j = 0; j < dictionary.size(); j++) {
                                prepended.add(Integer.toString(i) + dictionary.get(j));
                        }

                        crackPasswords(prepended, p);
		}

		System.out.println("upper case dictionary");
		ArrayList<String> upper = new ArrayList<String>();
                for (int i = 0; i < dictionary.size(); i++) {
                        upper.add(dictionary.get(i).toUpperCase());
                }

		crackPasswords(upper, p);
	}

	public static void crackPasswords(ArrayList<String> dictionary, ArrayList<String> p) {
		int i = 0;
		int inc = 0;
		int oldInc = 0;
		ArrayList <String> passwd = extractInfo(p, 1);
		while (i < dictionary.size()) {
			
			String salt = passwd.get(inc).substring(0,2);
			String password = passwd.get(inc).substring(2);
			String a = jcrypt.crypt(salt, dictionary.get(i));
			
			if (foundHashes.contains(a)) {
				continue;
			}

			if (a.substring(2).equals(password)) {
				System.out.println("PASSWORD FOUND!!!!: " + dictionary.get(i));
				foundHashes.add(a);
				inc++;
				oldInc = inc;
				i = 0;
				continue;
			}
			
			if (inc == (passwd.size() - 1)) {
                                break;
                        }


			if ((i == (dictionary.size() - 1)) && inc == oldInc) {
				i = 0;
				inc++;
				oldInc = inc;
				continue;
			}


			i++;
		}

	}

	public static void passwdAnalysis(ArrayList<String> dictionary, ArrayList<String> p) {
		ArrayList<String> usernames = extractInfo(p, 0);
		ArrayList<String> names = extractInfo(p, 4);
		
		for (String pass:p) {
			String username = extractInfo(pass, 0);
			String infoField = extractInfo(pass, 4);
			String passwordField = extractInfo(pass, 1);
			
			String password = passwordField.substring(2);
			String salt = passwordField.substring(0, 2);
			
			//tryUsername(username, password, salt);
			tryInfoField(infoField, password, salt);
		}

		System.out.println("Done");
	}
	
	public static void tryUsername(String username, String password, String salt) {
		String encUsername = jcrypt.crypt(salt, username);
		String reversed = new StringBuilder(username).reverse().toString();
		String revUsername = jcrypt.crypt(salt, reversed);
		if (encUsername.substring(2).equals(password)) {
			System.out.println("PASSWORD FOUND!!! " + username);
		}
		
		if (revUsername.substring(2).equals(password)) {
			System.out.println("PASSWORD FOUND!!! " + reversed);
		}
	}
	
	public static void tryInfoField(String infoField, String password, String salt) {
		String info[] = infoField.split(" ");
		String firstName = info[0];
		String lastName = info[info.length - 1];
		String fullName = infoField.join(" ");

		if (jcrypt.crypt(salt, fullName).substring(2).equals(password)) {
                        System.out.println("PASSWORD FOUND!!! " + fullName);
                }

		if (jcrypt.crypt(salt, fullName.toUpperCase()).substring(2).equals(password)) {
                        System.out.println("PASSWORD FOUND!!! " + fullName.toUpperCase());
                }

		if (jcrypt.crypt(salt, fullName.toLowerCase()).substring(2).equals(password)) {
                        System.out.println("PASSWORD FOUND!!! " + firstName.toLowerCase());
                }

		if (jcrypt.crypt(salt, firstName).substring(2).equals(password)) {
			System.out.println("PASSWORD FOUND!!! " + firstName);
		}

		if (jcrypt.crypt(salt, firstName.toUpperCase()).substring(2).equals(password)) {
                        System.out.println("PASSWORD FOUND!!! " + firstName.toUpperCase());
                }

		if (jcrypt.crypt(salt, firstName.toLowerCase()).substring(2).equals(password)) {
                        System.out.println("PASSWORD FOUND!!! " + firstName.toLowerCase());
                }

		if (jcrypt.crypt(salt, lastName.toUpperCase()).substring(2).equals(password)) {
                        System.out.println("PASSWORD FOUND!!! " + lastName.toUpperCase());
                }
		
		if (jcrypt.crypt(salt, lastName.toLowerCase()).substring(2).equals(password)) {
                        System.out.println("PASSWORD FOUND!!! " + lastName.toLowerCase());
                }

		if (jcrypt.crypt(salt, lastName).substring(2).equals(password)) {
			System.out.println("PASSWORD FOUND!!! " + lastName);
                }

	}

	public static ArrayList<String> extractInfo (ArrayList<String> unparsed, int index) {
		ArrayList<String> parsed = new ArrayList<String>();
		for (int i = 0; i < unparsed.size(); i++) {
			parsed.add(unparsed.get(i).split(":")[index]);
		}
	
		//System.out.println(parsed);
		return parsed;
	}

	public static String extractInfo (String unparsed, int index) {
                return unparsed.split(":")[index];
        }
}
