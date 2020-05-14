import java.util.*;
import java.io.*;
import java.util.stream.*;

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
			
		for (String p:passwd) {
			if (p.split(":").length != 7) {
				System.out.println("invalid passwd file format.");
				return;
			}
		}

		crackPasswords(dictionary, passwd);
		passwdAnalysis(dictionary, passwd);
		mutateDictionary(dictionary, passwd);
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
			if (dictionary.get(i).length() > 1) {
				varannan.add(tur.substring(1));
				varannan.add(tur.substring(0, tur.length() - 1));
				varannan.add(tur.substring(1, tur.length() - 1));
				varannan.add(tur2.substring(1));
				varannan.add(tur2.substring(0, tur2.length() - 1));
				varannan.add(tur2.substring(1, tur2.length() - 1));
			}

			varannan.add((new StringBuilder(tur).reverse().toString()));
			varannan.add((new StringBuilder(tur2).reverse().toString()));
			varannan.add(tur + tur);
			varannan.add(tur2 + tur2);
                }

                crackPasswords(varannan, p);

                ArrayList<String> upper = new ArrayList<String>();
                for (int i = 0; i < dictionary.size(); i++) {
                        upper.add(dictionary.get(i).toUpperCase());
			if (dictionary.get(i).length() > 1) {
				upper.add(dictionary.get(i).toUpperCase().substring(1));
				upper.add(dictionary.get(i).toUpperCase().substring(0, dictionary.get(i).length() - 1));
				upper.add(dictionary.get(i).toUpperCase().substring(1, dictionary.get(i).length() - 1));
			}
                }

                crackPasswords(upper, p);
		
                ArrayList<String> upperRev = new ArrayList<String>();
		String r = "";
                for (int i = 0; i < dictionary.size(); i++) {
			r = new StringBuilder(dictionary.get(i)).reverse().toString().toUpperCase();
                        upperRev.add(r);
                }

		crackPasswords(upperRev, p);

		ArrayList<String> reversed = new ArrayList<String>();
		String rev = "";
                for (int i = 0; i < dictionary.size(); i++) {
			rev = new StringBuilder(dictionary.get(i)).reverse().toString();
                        reversed.add(rev);
			if (dictionary.get(i).length() > 1) {
				reversed.add(rev.substring(1));
				reversed.add(rev.substring(0, rev.length() - 1));
				reversed.add(rev.substring(1, rev.length() - 1));
                	}
		}

		crackPasswords(reversed, p);
		
                ArrayList<String> reflected = new ArrayList<String>();
                String ref = "";
		String revref = "";
		
                for (int i = 0; i < dictionary.size(); i++) {
                        ref = (new StringBuilder(dictionary.get(i)).reverse().toString()) + dictionary.get(i);
			revref = dictionary.get(i) + (new StringBuilder(dictionary.get(i)).reverse().toString());
                        reflected.add(rev);
			reflected.add(revref);
			if (dictionary.get(i).length() > 1) {
				reflected.add(ref.substring(1));
				reflected.add(ref.substring(0, ref.length() - 1));
				reflected.add(ref.substring(1, ref.length() - 1));
				reflected.add(revref.substring(1));
                        	reflected.add(revref.substring(0, revref.length() - 1));
                        	reflected.add(revref.substring(1, revref.length() - 1));
				reflected.add(rev.toUpperCase());
				reflected.add(revref.toUpperCase());
			}
                }
		
		crackPasswords(reflected, p);
			
		String doubleWord = "";
		ArrayList<String> doubleWords = new ArrayList<String>();
		for (int i = 0; i < dictionary.size(); i++) {
			doubleWord = dictionary.get(i) + dictionary.get(i);
			doubleWords.add(doubleWord);
			if (dictionary.get(i).length() > 1) {
				doubleWords.add(doubleWord.substring(1));
				doubleWords.add(doubleWord.substring(0, doubleWord.length() - 1));
				doubleWords.add(doubleWord.substring(1, doubleWord.length() - 1));
				doubleWords.add((new StringBuilder(doubleWord).reverse().toString()));
			}
		}

		String toMutate = "";
		String mutateTo = "";
		String endAndBeg = "";
		String lastTwo = "";
		String firstTwo = "";
		ArrayList<String> toMutateString0 = new ArrayList<String>();
                for (int i = 0; i < dictionary.size(); i++) {
			if (dictionary.get(i).length() > 1) {
				toMutate = dictionary.get(i).substring(0, dictionary.get(i).length() - 1);
                        	mutateTo = dictionary.get(i).substring(1);
                        	endAndBeg = dictionary.get(i).substring(1, dictionary.get(i).length() - 1);
				lastTwo = dictionary.get(i).substring(0, dictionary.get(i).length() - 2);
				firstTwo = dictionary.get(i).substring(2);
				toMutateString0.add(lastTwo);
				toMutateString0.add(firstTwo);
				toMutateString0.add(toMutate);
                        	toMutateString0.add(mutateTo);
                        	toMutateString0.add(endAndBeg);
			}
                }
		
		crackPasswords(toMutateString0, p);

                String toMutate2 = "";
                String mutateTo2 = "";
		String endAndBeg2 = "";
		String lastTwo2 = "";
		String firstTwo2 = "";
                ArrayList<String> toMutateString2 = new ArrayList<String>();
                for (int i = 0; i < dictionary.size(); i++) {
			if (dictionary.get(i).length() > 1) {
                                toMutate2 = dictionary.get(i).toUpperCase().substring(0, dictionary.get(i).length() - 1);
                        	mutateTo2 = dictionary.get(i).toUpperCase().substring(1); 
                        	endAndBeg2 = dictionary.get(i).toUpperCase().substring(1, dictionary.get(i).length() - 1);
				lastTwo2 = dictionary.get(i).substring(0, dictionary.get(i).length() - 2);
                                firstTwo2 = dictionary.get(i).substring(2);
                                toMutateString2.add(lastTwo2);
                                toMutateString2.add(firstTwo2);
				toMutateString2.add(toMutate2);
                        	toMutateString2.add(mutateTo2);
                        	toMutateString2.add(endAndBeg2);
                        }
                }
	
		crackPasswords(toMutateString2, p);
		
		ArrayList<String> firstLetterUpperCase = new ArrayList<String>();
                String firstLetter = "";
		String lastLetter = "";
		String firstLower = "";
		String lastLower = "";
                
                for (int i = 0; i < dictionary.size(); i++) {
			if (dictionary.get(i).length() > 1) {
				firstLetter = dictionary.get(i).substring(0, 1).toUpperCase() + dictionary.get(i).substring(1);
                        	firstLower = dictionary.get(i).substring(0, 1) + dictionary.get(i).substring(1).toUpperCase();
				lastLetter = dictionary.get(i).substring(0, dictionary.get(i).length() - 1) + dictionary.get(i).substring(dictionary.get(i).length()-2).toUpperCase();
				lastLower = dictionary.get(i).substring(0, dictionary.get(i).length() - 1).toUpperCase() + dictionary.get(i).substring(dictionary.get(i).length()-2);
				firstLetterUpperCase.add(lastLetter);
				firstLetterUpperCase.add(lastLower);
				firstLetterUpperCase.add(firstLetter);
                        	firstLetterUpperCase.add(firstLower);
			}
		}
		
                crackPasswords(firstLetterUpperCase, p);
			
		ArrayList<ArrayList<String>> appendedNumbers = new ArrayList<ArrayList<String>>();
                ArrayList<String> appended;
		for (int i = 0; i < 10; i++) {
                        appended = new ArrayList<String>();
                        for (int j = 0; j < dictionary.size(); j++) {
                                appended.add(dictionary.get(j) + Integer.toString(i));
                        }
			
			appendedNumbers.add(appended);
                }
		
		appendedNumbers.parallelStream()
			.forEach(appendedNum -> {
				crackPasswords(appendedNum, p);
			});

		ArrayList<ArrayList<String>> prependedNumbers = new ArrayList<ArrayList<String>>();
		ArrayList<String> prepended;
                for (int i = 0; i < 10; i++) {
                        prepended = new ArrayList<String>();
                        for (int j = 0; j < dictionary.size(); j++) {
                                prepended.add(Integer.toString(i) + dictionary.get(j));
                        }
			prependedNumbers.add(prepended);
                }

		prependedNumbers.parallelStream()
			.forEach(prependedNum -> {
				crackPasswords(prependedNum, p);
			});

		ArrayList<ArrayList<String>> appendedLetters = new ArrayList<ArrayList<String>>();
		ArrayList<String> appendedChars;

		for (int i = 65; i < 123; i++) {
			if (i > 90 && i < 97) {
				continue;
			}
			
			appendedChars = new ArrayList<String>();	
                        for (int j = 0; j < dictionary.size(); j++) {
                                appendedChars.add(dictionary.get(j) + (char)i);
				appendedChars.add((char)i + dictionary.get(j));
                        }

			appendedLetters.add(appendedChars);
                }
		
		appendedLetters.parallelStream()
			.forEach(appendedList -> { 
				crackPasswords(appendedList, p);
			});
	}
	
	public static void crackPasswords(ArrayList<String> dictionary, ArrayList<String> p) {
		ArrayList <String> passwd = extractInfo(p, 1);
		passwd.parallelStream()
			.forEach(passEntry -> {
				for (int i = 0; i < dictionary.size(); i++) {
					String salt = passEntry.substring(0,2);
					String password = passEntry.substring(2);
					String a = jcrypt.crypt(salt, dictionary.get(i));

					if (foundHashes.contains(a)) {
						continue;
					}

					if (a.substring(2).equals(password)) {
						System.out.println(dictionary.get(i));
						foundHashes.add(a);
					}
				}
			});
	}

	public static void passwdAnalysis(ArrayList<String> dictionary, ArrayList<String> p) {
		ArrayList<String> usernames = extractInfo(p, 0);
		ArrayList<String> names = extractInfo(p, 4);
		
		for (String pass:p) {
			String username = extractInfo(pass, 0);
			String infoField = extractInfo(pass, 4);
			String passwordField = extractInfo(pass, 1);
			
			if (foundHashes.contains(passwordField)) {
				continue;
			}

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

		if (encUsername.substring(2).equals(password)) {
			System.out.println(username);
			foundHashes.add(encUsername);
			return;
		}
		
		if (revUsername.substring(2).equals(password)) {
			System.out.println(reversed);
			foundHashes.add(revUsername);
			return;
		}
		
		String capsUsername = jcrypt.crypt(salt, username.toUpperCase());

		if (capsUsername.substring(2).equals(password)) {
                        System.out.println(username.toUpperCase());
			foundHashes.add(capsUsername);
                 	return;	
		 }
		 
		 String pp = username+username;
                 if (jcrypt.crypt(salt, pp).substring(2).equals(password)) {
                        System.out.println(pp);
			foundHashes.add(jcrypt.crypt(salt, pp));
               	 	return;
		 }
		
		 String ok = pp.toUpperCase();
               	 if (jcrypt.crypt(salt, ok).substring(2).equals(password)) {
                        System.out.println(ok);
			foundHashes.add(jcrypt.crypt(salt, ok));
                 	return;
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
                                System.out.println(perm);
				foundHashes.add(jcrypt.crypt(salt, perm));
				return;
                        }

                        if (jcrypt.crypt(salt, mrep).substring(2).equals(password)) {
                                System.out.println(mrep);
				foundHashes.add(jcrypt.crypt(salt, mrep));
                        	return;
			}

                        if (jcrypt.crypt(salt, mrepl).substring(2).equals(password)) {
                                System.out.println(mrepl);
				foundHashes.add(jcrypt.crypt(salt, mrepl));
                        	return;
			}

                        if (jcrypt.crypt(salt, lperm).substring(2).equals(password)) {
                                System.out.println(lperm);
				foundHashes.add(jcrypt.crypt(salt, lperm));
                        	return;
			}
                }
		
		String ll = "";
		String xx = "";
		for (int i = 65; i < 122; i++) {
			ll = username + (char) i;
			xx = (char) i + username;
			
			if (jcrypt.crypt(salt, ll).substring(2).equals(password)) {
                                System.out.println(perm);
				foundHashes.add(jcrypt.crypt(salt, ll));
                        	return;
			}

			if (jcrypt.crypt(salt, xx).substring(2).equals(password)) {
                                System.out.println(xx);
				foundHashes.add(jcrypt.crypt(salt, xx));
                        	return;
			}
		}

			
	}
	
	public static void tryInfoField(String infoField, String password, String salt) {
		String info[] = infoField.split(" ");
		String firstName = info[0];
		String lastName = info[info.length - 1];
		String fullName = firstName + " " + lastName;
		
		if (foundHashes.contains(salt+password)) {
			return;
		}
		
		String fn = "";
		String fn2 = "";
		String ln = "";
		String ln2 = "";

		for (int i = 0; i < firstName.length(); i++) {
			char a = ' ';
			char b = ' ';
			if (i % 2 == 0) {
				a = Character.toUpperCase(firstName.charAt(i));
				b = firstName.charAt(i);
			} else {
				a = firstName.charAt(i);
				b = Character.toUpperCase(firstName.charAt(i));
			}

			fn += a;
                        fn2 += b;
		}

		for (int i = 0; i < lastName.length(); i++) {
                        char a = ' ';
                        char b = ' ';
                        if (i % 2 == 0) {
                                a = Character.toUpperCase(lastName.charAt(i));
                                b = lastName.charAt(i);
                        } else {
                                a = lastName.charAt(i);
                                b = Character.toUpperCase(lastName.charAt(i));
                        }

                        ln += a;
                        ln2 += b;
                }

		
		if (jcrypt.crypt(salt, (firstName + lastName)).substring(2).equals(password)) {
                        System.out.println(firstName + lastName);
			foundHashes.add(jcrypt.crypt(salt, (firstName+lastName)));
			return;
                }

		if (jcrypt.crypt(salt, (firstName + lastName).toUpperCase()).substring(2).equals(password)) {
                        System.out.println((firstName + lastName).toUpperCase());
			foundHashes.add(jcrypt.crypt(salt, (firstName + lastName).toUpperCase()));
			return;
                }

		if (jcrypt.crypt(salt, fn).substring(2).equals(password)) {
                        System.out.println(fn);
			foundHashes.add(jcrypt.crypt(salt, fn));
			return;
                }

		if (jcrypt.crypt(salt, ln).substring(2).equals(password)) {
                        System.out.println(ln);
			foundHashes.add(jcrypt.crypt(salt, ln));
                	return;
		}

		if (jcrypt.crypt(salt, ln2).substring(2).equals(password)) {
                        System.out.println(ln2);
			foundHashes.add(jcrypt.crypt(salt, ln2));
                	return;
		}

		if (jcrypt.crypt(salt, fn2).substring(2).equals(password)) {
                        System.out.println(fn2);
			foundHashes.add(jcrypt.crypt(salt, fn2));
                	return;
		}

		if (jcrypt.crypt(salt, fullName).substring(2).equals(password)) {
                        System.out.println(fullName);
			foundHashes.add(jcrypt.crypt(salt, fullName));
                	return;
		}

		if (jcrypt.crypt(salt, firstName + "123").substring(2).equals(password)) {
                        System.out.println(fullName + "123");
			foundHashes.add(jcrypt.crypt(salt, (fullName + "123")));
                	return;
		}

		if (jcrypt.crypt(salt, lastName + "123").substring(2).equals(password)) {
                        System.out.println(lastName + "123");
			foundHashes.add(jcrypt.crypt(salt, (lastName + "123")));
                	return;
		}

		if (jcrypt.crypt(salt, fullName.toUpperCase()).substring(2).equals(password)) {
                        System.out.println(fullName.toUpperCase());
			foundHashes.add(jcrypt.crypt(salt, fullName.toUpperCase()));
                	return;
		}

		if (jcrypt.crypt(salt, fullName.toLowerCase()).substring(2).equals(password)) {
                        System.out.println(fullName.toLowerCase());
			foundHashes.add(jcrypt.crypt(salt, fullName.toLowerCase()));
                	return;
		}

		if (jcrypt.crypt(salt, firstName).substring(2).equals(password)) {
			System.out.println(firstName);
			foundHashes.add(jcrypt.crypt(salt, firstName));
			return;
		}

		if (jcrypt.crypt(salt, firstName.toUpperCase()).substring(2).equals(password)) {
                        System.out.println(firstName.toUpperCase());
			foundHashes.add(jcrypt.crypt(salt, firstName.toUpperCase()));
                	return;
		}

		if (jcrypt.crypt(salt, lastName.toUpperCase()).substring(2).equals(password)) {
                        System.out.println(lastName.toUpperCase());
			foundHashes.add(jcrypt.crypt(salt, lastName.toUpperCase()));
                	return;
		}
		
		if (jcrypt.crypt(salt, lastName.toLowerCase()).substring(2).equals(password)) {
                        System.out.println(lastName.toLowerCase());
			foundHashes.add(jcrypt.crypt(salt, lastName.toLowerCase()));
                	return;
		}

		if (jcrypt.crypt(salt, lastName).substring(2).equals(password)) {
			System.out.println(lastName);
			foundHashes.add(jcrypt.crypt(salt, lastName));
                	return;
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
				System.out.println(perm);
				foundHashes.add(jcrypt.crypt(salt, perm));
				return;
			}

			if (jcrypt.crypt(salt, mrep).substring(2).equals(password)) {
                                System.out.println(mrep);
				foundHashes.add(jcrypt.crypt(salt, mrep));
				return;
                        }

			if (jcrypt.crypt(salt, mrepl).substring(2).equals(password)) {
                                System.out.println(mrepl);
				foundHashes.add(jcrypt.crypt(salt, mrepl));
                        	return;
			}

			if (jcrypt.crypt(salt, lperm).substring(2).equals(password)) {
                                System.out.println(lperm);
				foundHashes.add(jcrypt.crypt(salt, lperm));
                        	return;
			}
		}
		
		String appended = "";
		String prepended = "";
		String mirror = firstName + (new StringBuilder(firstName).reverse().toString());
		String firstFirst = firstName.substring(1);
		String firstLast = firstName.substring(0, firstName.length() - 1);
		String removeBoth = firstName.substring(1, firstName.length() - 1);
		if (jcrypt.crypt(salt, firstFirst).substring(2).equals(password)) {
                                System.out.println(firstFirst);
                                foundHashes.add(jcrypt.crypt(salt, firstFirst));
                                return;
                }

		if (jcrypt.crypt(salt, firstLast).substring(2).equals(password)) {
                                System.out.println(firstLast);
                                foundHashes.add(jcrypt.crypt(salt, firstLast));
                                return;
                }

		if (jcrypt.crypt(salt, removeBoth).substring(2).equals(password)) {
                                System.out.println(removeBoth);
                                foundHashes.add(jcrypt.crypt(salt, removeBoth));
                                return;
                }

		if (jcrypt.crypt(salt, mirror).substring(2).equals(password)) {
                                System.out.println(mirror);
                                foundHashes.add(jcrypt.crypt(salt, mirror));
                                return;
                }
			
		for (int i = 65; i < 122; i++) {
			appended = firstName + (char) i;
			prepended = (char) i + firstName;
			if (jcrypt.crypt(salt, appended).substring(2).equals(password)) {
                                System.out.println(appended);
				foundHashes.add(jcrypt.crypt(salt, appended));
				break;
                        }

			if (jcrypt.crypt(salt, ((char) i + mirror)).substring(2).equals(password)) {
                                System.out.println(((char) i + mirror));
                                foundHashes.add(jcrypt.crypt(salt, ((char) i + mirror)));
                                break;
                        }

			if (jcrypt.crypt(salt, (mirror + (char) i)).substring(2).equals(password)) {
                                System.out.println((mirror + (char) i));
                                foundHashes.add(jcrypt.crypt(salt, (mirror + (char) i)));
                                break;
                        }

			if (jcrypt.crypt(salt, prepended).substring(2).equals(password)) {
                                System.out.println(prepended);
				foundHashes.add(jcrypt.crypt(salt, prepended));
				break;
                        }
			
			findCommonPasswords(password, salt);
		}
	}

	public static void findCommonPasswords(String password, String salt) {
		ArrayList<String> commonPasswords = new ArrayList<String>();
		commonPasswords.add("123456");
		commonPasswords.add("12345");
		commonPasswords.add("123456789");
		commonPasswords.add("qwerty");
		commonPasswords.add("password");
		commonPasswords.add("Password1");
		commonPasswords.add("password1");
		commonPasswords.add("abc123");
		commonPasswords.add("111111");
		commonPasswords.add("iloveu");
		commonPasswords.add("12345678");
		commonPasswords.add("1234567890");
		commonPasswords.add("987654321");
		commonPasswords.add("red");
		commonPasswords.add("blue");
		commonPasswords.add("green");
		commonPasswords.add("password123");

		for (String pass : commonPasswords) {
			if (foundHashes.contains(salt+password)) {
                        	return;
                	}

			if (jcrypt.crypt(salt, pass).substring(2).equals(password)) {
                                System.out.println(pass);
                                foundHashes.add(jcrypt.crypt(salt, pass));
                                break;
                        }

			if (jcrypt.crypt(salt, pass.toUpperCase()).substring(2).equals(password)) {
				System.out.println(pass.toUpperCase());
				foundHashes.add(jcrypt.crypt(salt, pass.toUpperCase()));
				break;
			}
		}
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

}
