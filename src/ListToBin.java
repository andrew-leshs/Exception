import java.io.Serializable;
import java.util.List;

public class ListToBin implements Serializable {

    private static final long serialVersionUID = 40L;

    private List<String> strings;

    public ListToBin(List<String> strings) {
        this.strings = strings;
    }

    public List<String> getStrings() {
        return strings;
    }
}
