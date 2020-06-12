
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Index {
    public HashMap<String, HashMap<String, Integer>> indexDirect;
    public HashMap<String, HashMap<String, Integer>> indexInvers;

    public Index(Queue<File> files, List<String> exceptions, List<String> stopWords){
        indexDirect = new HashMap<>();
        for ( File file : files) {
            indexDirect.put(file.getPath(), getHashMapFile(file, exceptions, stopWords));
        }
        indexInvers = determineIndexInvers();
    }

    private void countWords(String text, HashMap<String,Integer> dictionary, List<String> exceptions, List<String> stopWords) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); ++i) {
            if (Character.isDigit(text.charAt(i)) || Character.isLetter(text.charAt(i))) {
                sb.append(text.charAt(i));
            } else {
                String word = sb.toString().toLowerCase();
                sb = new StringBuilder();
                if (!word.equals("")) {
                    if (exceptions.contains(word)) {
                        if (dictionary.containsKey(word)) {
                            int count = dictionary.get(word);
                            dictionary.put(word, count + 1);
                        } else {
                            dictionary.put(word, 1);
                        }
                    } else {
                        if (!stopWords.contains(word)) {
                            word = Stemmer.getFormaCanonica(word);
                            if (dictionary.containsKey(word)) {
                                int count = dictionary.get(word);
                                dictionary.put(word, count + 1);
                            } else {
                                dictionary.put(word, 1);
                            }
                        }
                    }
                }
            }
        }
    }

    private HashMap<String, Integer> getHashMapFile(File file, List<String> exceptions, List<String> stopWords) {
        HashMap<String, Integer> auxHashMap = new HashMap<>();
        try {
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String text = myReader.nextLine();
                countWords(text, auxHashMap, exceptions, stopWords);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return auxHashMap;
    }

    private HashMap<String, HashMap<String, Integer>> determineIndexInvers(){
        HashMap<String, HashMap<String, Integer>> indirectDictionary= new HashMap<>();
        for(String file : indexDirect.keySet()){
            for(String word : indexDirect.get(file).keySet()){
                if(indirectDictionary.containsKey(word)){
                    HashMap<String, Integer> auxiliarHashMap = indirectDictionary.get(word);
                    if(!auxiliarHashMap.containsKey(file)){
                        indirectDictionary.get(word).put(file, indexDirect.get(file).get(word));
                    }
                }
                else{
                    indirectDictionary.put(word, new HashMap<>());
                    indirectDictionary.get(word).put(file, indexDirect.get(file).get(word));
                }
            }
        }
        return indirectDictionary;
    }
}
