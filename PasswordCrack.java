import java.util.*;
import java.io.*;

class PasswordCrack {
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

	public static void crackPasswords(ArrayList<String> dictionary, ArrayList<String> p) {
		int i = 0;
		int inc = 0;
		int oldInc = 0;
		ArrayList <String> passwd = extractHash(p);
		while (i < dictionary.size()) {
			
			String salt = passwd.get(inc).substring(0,2);
			String password = passwd.get(inc).substring(2);
			String a = jcrypt.crypt(salt, password);
			System.out.println(salt + " " + password + ":" + passwd.get(inc) + ":" + a);

			if (a == passwd.get(inc)) {
				System.out.println("PASSWORD FOUND!!!!: " + dictionary.get(i));
				inc++;
				oldInc = inc;
				i = 0;
				continue;
			}
			
			if (inc == (passwd.size() - 1)) {
                                System.out.println("could not find the password in this dictionary! proceeding with analysis of passwd file");
                                break;
                        }


			if ((i == (dictionary.size() - 1)) && inc == oldInc) {
				i = 0;
				inc++;
				oldInc = inc;
				System.out.println(inc);
				System.out.println(i);
				continue;
			}


			i++;
		}
	}

	public static ArrayList<String> extractHash (ArrayList<String> unparsed) {
		ArrayList<String> parsed = new ArrayList<String>();
		for (int i = 0; i < unparsed.size(); i++) {
			parsed.add(unparsed.get(i).split(":")[1]);
		}
		
		System.out.println(parsed);
		return parsed;
	}
}
