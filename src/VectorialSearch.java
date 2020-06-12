import java.util.*;

public class VectorialSearch {

    public static HashMap<String, Double> sortByValue(HashMap<String, Double> hm)
    {
        List<Map.Entry<String, Double> > list =
                new LinkedList<Map.Entry<String, Double> >(hm.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() {
            public int compare(Map.Entry<String, Double> o1,
                               Map.Entry<String, Double> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static List<String> vectorialSearch(List<String> documents, List<String> terms, HashMap<String, HashMap<String, Integer>> indexDirect){
        HashMap<String, Double> hashMapCosinusValues = new HashMap<>();
        for(String doc : documents){
            int countTotalWords = 0;
            int countGoodWords = 0;

            for(String word : indexDirect.get(doc).keySet()){
                if(terms.contains(word)){
                    countGoodWords += indexDirect.get(doc).get(word);
                }
                countTotalWords += indexDirect.get(doc).get(word);
            }

            double cos = (double)countGoodWords / Math.sqrt(countTotalWords * terms.size());
            hashMapCosinusValues.put(doc, cos);
        }

        hashMapCosinusValues = sortByValue(hashMapCosinusValues);

        List<String> result = new ArrayList<>();

        for(String word: hashMapCosinusValues.keySet()) {
            result.add(word);
        }


        for(String word: hashMapCosinusValues.keySet()) {
            System.out.println(" <" + word + ", " + hashMapCosinusValues.get(word).toString() + ">");
        }

        return result;
    }
}
