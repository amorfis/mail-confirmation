import com.bleedingwolf.ratpack.RatpackRunner;

import java.io.File;

public class Start {
    public static void main(String[] args) {
        new RatpackRunner().run(new File("src/sample.groovy"));
    }
}
