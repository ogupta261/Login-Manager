import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

public class abc {
    public static void main(String[] args) {
        List<Integer> scratchCodes = new ArrayList<Integer>();
        scratchCodes.add(154884);
        scratchCodes.add(254884);
        scratchCodes.add(354884);
        scratchCodes.add(454884);
        scratchCodes.add(554884);
        scratchCodes.add(654884);
        System.out.println("adwda");
        List<Integer> scratchCodes1 = Arrays.asList(scratchCodes.stream().map(a->a+"").reduce((a,b) -> a+","+b).get().split(",")).stream().map(Integer :: parseInt).collect(Collectors.toList());
        System.out.println(scratchCodes1.get(0)+1);
    }
}
