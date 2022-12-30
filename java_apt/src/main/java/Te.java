import annotation.Autowired;
import annotation.Route;

@Route(path = "/g1/nihao")
public class Te {
    @Autowired(name = "name")
    String  name = "";

    @Autowired(name = "haha")
    String  haha = "";
}
