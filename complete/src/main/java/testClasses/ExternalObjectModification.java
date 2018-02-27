package testClasses;

import java.util.ArrayList;
import java.util.List;

public class ExternalObjectModification {

    private final List<Integer> integerList = new ArrayList<Integer>();

    public ExternalObjectModification(){
        integerList.add(Integer.valueOf(5));
        integerList.add(Integer.valueOf(8));
        integerList.add(Integer.valueOf(18));
    }

    public List<Integer> getList(){
        return integerList;
    }


    public class DerivedExternalObject extends ExternalObjectModification {
        private final List<Boolean> booleans = new ArrayList<Boolean>();
        public DerivedExternalObject (){
            super();
            booleans.add(true);
            booleans.add(false);
        }
    }

    public class C extends ExternalObjectModification implements Comparable {
        private final List<String> strings = new ArrayList<String>();
        public C (){
            super();
            strings.add("true");
            strings.add("false");
        }

        @Override
        public int compareTo(Object o) {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            return (compareTo(o) == 0);
        }
    }
}
