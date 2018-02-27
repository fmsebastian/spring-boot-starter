package testClasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DerivedObjectChecks {

    private DerivedObjectChecks(){
    }

    public static boolean checkCompareDerivedObjects() {

        try {
            List<String> listNull = Arrays.asList(null);
            System.out.println("list null is: not null");
        } catch (Exception e) {
            System.out.println("list null is: Null");
        }


        ExternalObjectModification object = new ExternalObjectModification();
        ExternalObjectModification.DerivedExternalObject derived = (new ExternalObjectModification()).new DerivedExternalObject();

        System.out.println("derived class : " + derived.getClass());
        System.out.println("base class : " + object.getClass());
        System.out.println("are the same? " + (derived.getClass() == object.getClass()));
        System.out.println("base instance of derived? " + (object instanceof ExternalObjectModification.DerivedExternalObject));
        System.out.println("derived instance of base? " + (derived instanceof ExternalObjectModification));

        List<ExternalObjectModification.DerivedExternalObject> listOfBs = new ArrayList<ExternalObjectModification.DerivedExternalObject>();
        listOfBs.add((new ExternalObjectModification()).new DerivedExternalObject());

        System.out.println("show list of bs: " + listOfBs.toString());
        ExternalObjectModification.C c = (new ExternalObjectModification()).new C();
        System.out.println("bs contains any c? " + (listOfBs.contains(c)));

        List<Integer> list = object.getList();
        list.forEach(integer -> {
            System.out.println("Integer: " + integer);
        });

        System.out.println("Change of list");

        list.clear();
        List<Integer> list2 = object.getList();

        list2.forEach(integer -> {
            System.out.println("2nd list Integer: " + integer);
        });
        list2.add(Integer.valueOf(0));
        System.out.println("1st list: ");
        list.forEach(integer -> {
            System.out.println("Integer: " + integer);
        });
        list = null;
        System.out.println("list 3: ");
        List<Integer> list3 = object.getList();

        list3.forEach(integer -> {
            System.out.println("Integer: " + integer);
        });
        return true;
    }

}
