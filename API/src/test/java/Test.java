public class Test {

	public static void main(String... args) {
		String version = "v1_8_R2";
		String lazyVersion = version.substring(0,version.lastIndexOf("_R"));// Remove _R?
		System.out.println(lazyVersion);
	}

}
