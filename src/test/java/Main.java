import java.io.File;

import org.apache.commons.codec.digest.DigestUtils;


public class Main {

	public static void main(String[] args) {
		long t1 = System.currentTimeMillis();
		for(int i=0;i<10;i++){
			String hex = DigestUtils.md5Hex("abcabc");
		}
		System.out.println(System.currentTimeMillis()-t1);
		System.out.println(new File("d:/tmp/text.txt").getParent());
	}
}
