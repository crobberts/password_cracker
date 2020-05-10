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
		
		System.out.println("Varannan dictionary");
                System.out.println("---------------------");
                ArrayList<String> varannan = new ArrayList<String>();
                for (int i = 0; i < dictionary.size(); i++) {
			String tur = "";
			String tur2 = "";
			for (int j = 0; j < dictionary.get(i).length(); j++) {
				char c = ' ';
				char d = ' ';
				if (j % 2 == 0) {
					 c = Character.toUpperCase(dictionary.get(i).charAt(j));
					 d = dictionary.get(i).charAt(j);
					 tur += c;
					 tur2 += d;
				} else {
					c = dictionary.get(i).charAt(j);
					d = Character.toUpperCase(dictionary.get(i).charAt(j));
					tur += c;
					tur2 += d;
				}
			}
                        varannan.add(tur);
			varannan.add(tur2);
                }

                crackPasswords(varannan, p);

		System.out.println("Upper case dictionary");
		System.out.println("---------------------");
                ArrayList<String> upper = new ArrayList<String>();
                for (int i = 0; i < dictionary.size(); i++) {
                        upper.add(dictionary.get(i).toUpperCase());
                }

                crackPasswords(upper, p);
		
		System.out.println("Upper case dictionary reversed");
                System.out.println("---------------------");
                ArrayList<String> upperRev = new ArrayList<String>();
		String r = "";
                for (int i = 0; i < dictionary.size(); i++) {
			r = new StringBuilder(dictionary.get(i)).reverse().toString().toUpperCase();
                        upperRev.add(r);
                }

		crackPasswords(upperRev, p);

		System.out.println("Reversing dictionary words");
		System.out.println("---------------------");
		ArrayList<String> reversed = new ArrayList<String>();
		String rev = "";
                for (int i = 0; i < dictionary.size(); i++) {
			rev = new StringBuilder(dictionary.get(i)).reverse().toString();
                        reversed.add(rev);
                }

		crackPasswords(reversed, p);
		
		System.out.println("Reflecting dictionary words");
                System.out.println("---------------------");
                ArrayList<String> reflected = new ArrayList<String>();
                String ref = "";
		String revref = "";
                for (int i = 0; i < dictionary.size(); i++) {
                        ref = (new StringBuilder(dictionary.get(i)).reverse().toString()) + dictionary.get(i);
			revref = dictionary.get(i) + (new StringBuilder(dictionary.get(i)).reverse().toString());
                        reflected.add(rev);
			reflected.add(revref);
                }
		
		crackPasswords(reflected, p);
			
		System.out.println("Repeating/appending word");
		String doubleWord = "";
		ArrayList<String> doubleWords = new ArrayList<String>();
		for (int i = 0; i < dictionary.size(); i++) {
			doubleWord = dictionary.get(i) + dictionary.get(i);
			doubleWords.add(doubleWord);
		}

		System.out.println("Appending numbers");
		System.out.println("---------------------");
		for (int i = 0; i < 10; i++) {
			ArrayList<String> appended = new ArrayList<String>();
			for (int j = 0; j < dictionary.size(); j++) {
				appended.add(dictionary.get(j) + Integer.toString(i));
			}

			crackPasswords(appended, p);
		}

		System.out.println("Prepending numbers");
		System.out.println("---------------------");
                for (int i = 0; i < 10; i++) {
                        ArrayList<String> prepended = new ArrayList<String>();
                        for (int j = 0; j < dictionary.size(); j++) {
                                prepended.add(Integer.toString(i) + dictionary.get(j));
                        }

                        crackPasswords(prepended, p);
                }

		System.out.println("Removing letters");
                System.out.println("---------------------");
		String toMutate = "";
		String mutateTo = "";
		ArrayList<String> toMutateString0 = new ArrayList<String>();
                for (int i = 0; i < dictionary.size(); i++) {
			toMutate = dictionary.get(i).substring(0, dictionary.get(i).length() - 2);
			mutateTo = dictionary.get(i).substring(1, dictionary.get(i).length() - 1);
			toMutateString0.add(toMutate);
			toMutateString0.add(mutateTo);
                }
		
		crackPasswords(toMutateString0, p);

		System.out.println("Removing letters uppercase");
                System.out.println("---------------------");
                String toMutate2 = "";
                String mutateTo2 = "";
                ArrayList<String> toMutateString2 = new ArrayList<String>();
                for (int i = 0; i < dictionary.size(); i++) {
                        toMutate2 = dictionary.get(i).toUpperCase().substring(0, dictionary.get(i).length() - 2);
                        mutateTo2 = dictionary.get(i).toLowerCase().substring(1, dictionary.get(i).length() - 1);
                        toMutateString2.add(toMutate2);
                        toMutateString2.add(mutateTo2);
                }
	
		crackPasswords(toMutateString2, p);
		
		System.out.println("First letter uppercase");
                System.out.println("---------------------");
		ArrayList<String> firstLetterUpperCase = new ArrayList<String>();
                String firstLetter = "";
		String lastLetter = "";
                ArrayList<String> toMutateString = new ArrayList<String>();
                for (int i = 0; i < dictionary.size(); i++) {
                        firstLetter = dictionary.get(i).substring(0, 1).toUpperCase() + dictionary.get(i).substring(1, dictionary.get(i).length() - 1);
			System.out.println(firstLetter);
                        lastLetter = dictionary.get(i).substring(0, dictionary.get(i).length() - 2) + dictionary.get(i).substring(dictionary.get(i).length()-2, dictionary.get(i).length()-1).toUpperCase();
                        firstLetterUpperCase.add(firstLetter);
                        firstLetterUpperCase.add(lastLetter);
                }

                crackPasswords(firstLetterUpperCase, p);

		System.out.println("Appending letters");
		System.out.println("---------------------");
		for (int i = 65; i < 123; i++) {
                        ArrayList<String> appendedLetters = new ArrayList<String>();
                        for (int j = 0; j < dictionary.size(); j++) {
                                appendedLetters.add(dictionary.get(j) + (char)i);
                        }

                        crackPasswords(appendedLetters, p);
                }

		System.out.println("Prepending letters");
		System.out.println("---------------------");
                for (int i = 65; i < 123; i++) {
                        ArrayList<String> prependedLetters = new ArrayList<String>();
                        for (int j = 0; j < dictionary.size(); j++) {
                                prependedLetters.add((char)i + dictionary.get(j));
                        }

                        crackPasswords(prependedLetters, p);
                }
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
				i = 0;
				inc++;
				oldInc = inc;
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
		
		System.out.println("Passwd file analysis");
		for (String pass:p) {
			String username = extractInfo(pass, 0);
			String infoField = extractInfo(pass, 4);
			String passwordField = extractInfo(pass, 1);
			
			String password = passwordField.substring(2);
			String salt = passwordField.substring(0, 2);
			
			tryUsername(username, password, salt);
			tryInfoField(infoField, password, salt);
		}
	}
	
	public static void tryUsername(String username, String password, String salt) {
		String encUsername = jcrypt.crypt(salt, username);
		String reversed = new StringBuilder(username).reverse().toString();
		String revUsername = jcrypt.crypt(salt, reversed);

		if (foundHashes.contains(salt+password)) {
                	return;
                }

		if (encUsername.substring(2).equals(password)) {
			System.out.println("PASSWORD FOUND!!! " + username);
		}
		
		if (revUsername.substring(2).equals(password)) {
			System.out.println("PASSWORD FOUND!!! " + reversed);
		}

		 if (jcrypt.crypt(salt, username.toUpperCase()).substring(2).equals(password)) {
                                System.out.println("PASSWORD FOUND!!! " + username.toUpperCase());
                 }
		 
		 String pp = username+username;
                 if (jcrypt.crypt(salt, pp).substring(2).equals(password)) {
                                System.out.println("PASSWORD FOUND!!! " + pp);
               	 }
		
		 String ok = pp.toUpperCase();
               	 if (jcrypt.crypt(salt, ok).substring(2).equals(password)) {
                                System.out.println("PASSWORD FOUND!!! " + ok);
                 }

		
		String perm = "";
                String mrep = "";
                String lperm = "";
                String mrepl = "";
                for (int i = 0; i < 10; i++) {
                        perm = username + Integer.toString(i);
                        mrep = Integer.toString(i) + username;

                        perm = revUsername + Integer.toString(i);
                        mrep = Integer.toString(i) + revUsername;

                        if (jcrypt.crypt(salt, perm).substring(2).equals(password)) {
                                System.out.println("PASSWORD FOUND!!! " + perm);
                        }

                        if (jcrypt.crypt(salt, mrep).substring(2).equals(password)) {
                                System.out.println("PASSWORD FOUND!!! " + mrep);
                        }

                        if (jcrypt.crypt(salt, mrepl).substring(2).equals(password)) {
                                System.out.println("PASSWORD FOUND!!! " + mrepl);
                        }

                        if (jcrypt.crypt(salt, lperm).substring(2).equals(password)) {
                                System.out.println("PASSWORD FOUND!!! " + lperm);
                        }
                }
		
		String ll = "";
		String xx = "";
		for (int i = 65; i < 122; i++) {
			ll = username + (char) i;
			xx = (char) i + username;
			
			if (jcrypt.crypt(salt, ll).substring(2).equals(password)) {
                                System.out.println("PASSWORD FOUND!!! " + perm);
                        }

			if (jcrypt.crypt(salt, xx).substring(2).equals(password)) {
                                System.out.println("PASSWORD FOUND!!! " + xx);
                        }
		}

			
	}
	
	public static void tryInfoField(String infoField, String password, String salt) {
		String info[] = infoField.split(" ");
		String firstName = info[0];
		String lastName = info[info.length - 1];
		String fullName = infoField.join(" ");
		if (foundHashes.contains(salt+password)) {
			return;
		}

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
		
		String perm = "";
		String mrep = "";
		String lperm = "";
		String mrepl = "";
		for (int i = 0; i < 10; i++) {
			perm = firstName + Integer.toString(i);
			mrep = Integer.toString(i) + firstName;

			perm = lastName + Integer.toString(i);
                        mrep = Integer.toString(i) + lastName;

			if (jcrypt.crypt(salt, perm).substring(2).equals(password)) {
				System.out.println("PASSWORD FOUND!!! " + perm);
			}

			if (jcrypt.crypt(salt, mrep).substring(2).equals(password)) {
                                System.out.println("PASSWORD FOUND!!! " + mrep);
                        }

			if (jcrypt.crypt(salt, mrepl).substring(2).equals(password)) {
                                System.out.println("PASSWORD FOUND!!! " + mrepl);
                        }

			if (jcrypt.crypt(salt, lperm).substring(2).equals(password)) {
                                System.out.println("PASSWORD FOUND!!! " + lperm);
                        }
		}

		checkStandardPasswords(salt, password);

	}

	public static ArrayList<String> extractInfo (ArrayList<String> unparsed, int index) {
		ArrayList<String> parsed = new ArrayList<String>();
		for (int i = 0; i < unparsed.size(); i++) {
			parsed.add(unparsed.get(i).split(":")[index]);
		}
	
		return parsed;
	}

	public static String extractInfo (String unparsed, int index) {
                return unparsed.split(":")[index];
        }

	public static void checkStandardPasswords(String salt, String password) {
		ArrayList<String> standard = new ArrayList<String>();
		standard.add("123456");
		standard.add("12345");
		standard.add("password");
		standard.add("iloveyou");
		standard.add("princess");
		standard.add("1234567");
		standard.add("12345678");
		standard.add("abc123");
		standard.add("nicole");
		standard.add("daniel");
		standard.add("babygirl");
		standard.add("chocolate");
		standard.add("Password123");
		standard.add("password123");
		standard.add("password1");
		standard.add("qwerty");
		standard.add("QWERTY");
		standard.add("secret");
		for (int i = 0; i < standard.size(); i++) {
			if (jcrypt.crypt(salt, standard.get(i)).substring(2).equals(password)) {
                                System.out.println("PASSWORD FOUND!!! " + standard.get(i));
                        }
		}
	}
}
