import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		List<Integer> l = new ArrayList<>();
		l.add(1);
		l.add(2);

		for (int i = 0; i < l.size(); i++) {
			if (l.size() < 10)
				l.add(1000 + l.get(i));

		}
		
		System.out.println(l);
	}
}
