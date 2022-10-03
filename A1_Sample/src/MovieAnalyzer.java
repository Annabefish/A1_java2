import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Analyze movies.
 */
public class MovieAnalyzer {
    private List<Movie> movies;

    /**
    * Store information for every movie.
    * */
    public static class Movie {
        private String Poster_Link;
        private String Series_Title;
        private int Release_Year;
        private String Certificate;
        private String Runtime;
        private String Genre;
        private float IMDB_Rating;
        private String Overview;
        private int Meta_score;
        private String Director;
        private String Star1;
        private String Star2;
        private String Star3;
        private String Star4;
        private String Noofvotes;
        private String Gross;

        public Movie( String[] input) {

            Poster_Link = input[0].equals("") ? null : input[0];
            Series_Title = input[1].equals("") ? null : input[1];
            Release_Year = isNumeric(input[2]) ? Integer.parseInt(input[2]) : -1;
            Certificate = input[3].equals("") ? null : input[3];
            Runtime = input[4].equals("") ? null : input[4];
            Genre = input[5].equals("") ? null : input[5];
            IMDB_Rating = input[6].equals("") ? -1f : Float.parseFloat(input[6]);
            Overview = input[7].equals("") ? null : input[7];
            Meta_score = isNumeric(input[8]) ? Integer.parseInt(input[8]) : -1;
            Director = input[9].equals("") ? null : input[9];
            Star1 = input[10].equals("") ? null : input[10];
            Star2 = input[11].equals("") ? null : input[11];
            Star3 = input[12].equals("") ? null : input[12];
            Star4 = input[13].equals("") ? null : input[13];
            Noofvotes = input[14].equals("") ? null : input[14];
            Gross = input[15].equals("") ? null : input[15];
        }

        /**
         * Whether the string can be converted into integer.
         *
         * @param str string to be judged
         * @return true for all integer char, false for null,"" or contains other char
         */
        public static boolean isNumeric(String str) {
            if (str.equals("") || str == null) {
                return false;
            }
            for (int i = str.length()-1; i >= 0; i--) {
                if (!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        }

        public String getPoster_Link() {
            return Poster_Link;
        }

        public String getSeries_Title() {
            if (Series_Title.startsWith("\"") && Series_Title.endsWith("\"")) {
                return Series_Title.substring(1,Series_Title.length()-1);
            }
            return Series_Title;
        }

        public int getRelease_Year() {
            return Release_Year;
        }

        public String getCertificate() {
            return Certificate;
        }

        public int getRuntime() {
            return Integer.parseInt(Runtime.split(" ")[0]);
        }

        public String getGenre() {

            if (Genre.startsWith("\"") && Genre.endsWith("\"")) {
                return Genre.substring(1,Genre.length()-1);
            }
            return Genre;
        }

        public float getIMDB_Rating() {
            return IMDB_Rating;
        }

        public String getOverview() {
            return Overview;
        }

        public int getOverviewLength() {
            if (Overview.startsWith("\"") && Overview.endsWith("\"")) {
                return Overview.length()-2;
            }
            return Overview.length();
        }

        public int getMeta_score() {
            return Meta_score;
        }

        public String getDirector() {
            return Director;
        }

        public String getStar1() {
            return Star1;
        }

        public String getStar2() {
            return Star2;
        }

        public String getStar3() {
            return Star3;
        }

        public String getStar4() {
            return Star4;
        }

        /**
         * get Noofvotes.
         * @return Noofvotes
         */
        public String getNoofvotes() {
            return Noofvotes;
        }

        /**
         * get gross.
         * @return gross in long
         */
        public long getGross() {
            if (Gross == null) {
                return -1;
            }
            String[] array = Gross.split(",");
            String s = "";
            for (int i = 0; i < array.length; i++) {
                s += array[i];
            }
            if (Gross.startsWith("\"") && Gross.endsWith("\"")) {
                return Long.parseLong(s.substring(1,s.length()-1));
            }
            return Long.parseLong(s);
        }
    }

    /**
     * read movies from csv file and store it to a list of movies.
     * @param dataset_path path to the csv file storing information of movies
     * */
    public MovieAnalyzer(String dataset_path) throws IOException {

        this.movies = Files.lines(Paths.get(dataset_path)).filter(s -> !s.equals("Poster_Link,Series_Title,Released_Year,Certificate,Runtime,Genre,IMDB_Rating,Overview,Meta_score,Director,Star1,Star2,Star3,Star4,No_of_Votes,Gross"))
                .map(l -> l.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1))
                .map(a -> new Movie(a)).collect(Collectors.toList());
    }

    /**
     * count the number of movies which are in the same genre
     * @return  genre correspond to its number of movies
     * */
    public Map<String,Integer> getMovieCountByGenre()  {
        String[][] array = movies.stream()
                .filter(a -> a.getGenre()!=null)
                .map(a -> a.getGenre())
                .map(a -> a.split(", "))
                .toArray(String[][]::new);
        ArrayList<String> new_array = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                new_array.add(array[i][j]);
            }
        }
        Map<String,Long> map = new_array.stream().collect(Collectors.groupingBy(a -> a,Collectors.counting()));
        Map<String,Integer> map1 = new HashMap<String, Integer>();
        map.forEach((key,value) -> map1.put(key, new Integer(Math.toIntExact(value))));

        return sortByValue(map1, map1.size());

    }

    /**
     * Sort map by its value which is Integer type
     * @param map map need to be sorted
     * @param topN needed number of <key,value>
     * @return sorted map
     */
    public static Map sortByValue(Map map, int topN) {

        List list = new LinkedList(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry>() {

            public int compare(Map.Entry o1, Map.Entry o2) {
                if (o1.getKey() instanceof String) {
                    if (((Integer) o1.getValue()) - (Integer) o2.getValue() == 0) {
                        return ((String) o1.getKey()).compareTo((String) o2.getKey());
                    }
                }
                return ((Integer)o2.getValue()) - (Integer)o1.getValue();

            }

        });
        Map result = new LinkedHashMap();
        int i = 0;
        for (Object it : list) {
            Map.Entry entry = (Map.Entry) it;
            if (i >= topN) {
                break;
            }
            result.put(entry.getKey(), entry.getValue());
            i++;
        }
        return result;
    }

    /**
     * Sort map by its value which is Double type
     * @param map map need to be sorted
     * @param topN needed number of <key,value>
     * @return sorted map
     */
    public static Map sortByValue_double(Map map, int topN) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry>() {
            public int compare(Map.Entry o1, Map.Entry o2) {
                if(o1.getKey() instanceof String) {
                    if (((Double) o1.getValue()) - (Double) o2.getValue() == 0) {
                        return ((String) o1.getKey()).compareTo((String) o2.getKey());
                    }
                }
                return ((Double)o2.getValue())-(Double)o1.getValue()<0?-1:1;
            }
        });
        Map result = new LinkedHashMap();
        int i = 0;
        for (Object it : list) {
            Map.Entry entry = (Map.Entry) it;
            if (i >= topN) {
                break;
            }
            result.put(entry.getKey(), entry.getValue());
            i++;
        }
        return result;
    }

    /**
     * Sort map by keys
     * @param map map need to be sorted
     * @param topN needed number of elements
     * @return sorted map
     */
    public static Map sortByKey(Map map, int topN) {

        List list = new LinkedList(map.entrySet());

        Collections.sort(list, new Comparator<Map.Entry>() {

            public int compare(Map.Entry o1, Map.Entry o2) {
                return ((Integer)o2.getKey())-(Integer)o1.getKey();

            }

        });

        Map result = new LinkedHashMap();

        int i = 0;

        for (Object it : list) {

            Map.Entry entry = (Map.Entry) it;

            if (i >= topN) {

                break;

            }

            result.put(entry.getKey(), entry.getValue());

            i++;

        }

        return result;

    }

    /**
     * Count the number of movies published in every year
     * @return year corresponding to its number of movies published
     */
    public Map<Integer,Integer> getMovieCountByYear()  {
        Map<Integer,Long> map = movies.stream().filter(a->a.getGenre() != null).collect(Collectors.groupingBy(Movie::getRelease_Year,Collectors.counting()));
        Map<Integer,Integer> map1 = new HashMap<Integer, Integer>();
        map.forEach((key,value) -> map1.put(key,new Integer(Math.toIntExact(value))));
        return sortByKey(map1 , map1.size());
    }

    /**
     * Get the top few movies by runtime or length of overview
     * @param top_k number of movies needed
     * @param by determine by runtime or length of overview
     * @return
     */
    public List<String> getTopMovies(int top_k,String by) {
        if (by.equals("runtime")) {
           return  movies.stream().sorted(Comparator.comparing(Movie::getSeries_Title)).sorted(Comparator.comparing(Movie::getRuntime).reversed()).map(a -> a.getSeries_Title()).limit(top_k).collect(Collectors.toList());
        }
        if (by.equals("overview")) {
            return movies.stream().sorted(Comparator.comparing(Movie::getSeries_Title)).sorted(Comparator.comparing(Movie::getOverviewLength).reversed()).map(a -> a.getSeries_Title()).limit(top_k).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * Count the times of two stars' co-working
     * @return two stars' name corresponding to co-work times
     */
    public Map<List<String>,Integer> getCoStarCount()  {
        String[][] array=movies.stream()
                .map(a -> new String[] {a.getStar1(),a.getStar2(),a.getStar3(),a.getStar4()})
                .toArray(String[][]::new);
        ArrayList<List<String>> list = new ArrayList<>();
        for (int i = 0;i < array.length; i++) {
            for (int j = 0; j < array[i].length-1; j++) {
                for (int k = j+1;k < array[i].length; k++) {
                    List<String> tmp = new ArrayList<>();
                    if ( array[i][j].compareTo(array[i][k]) < 0) {
                        tmp.add(array[i][j]);
                        tmp.add(array[i][k]);
                    }
                    else {
                        tmp.add(array[i][k]);
                        tmp.add(array[i][j]);
                    }
                    list.add(tmp);
                }
            }
        }
        Map<List<String>,Long> map = list.stream().collect(Collectors.groupingBy(a->a,Collectors.counting()));
        Map<List<String>,Integer> map1 = new HashMap<List<String>, Integer>();
        map.forEach((key,value) -> map1.put(key,new Integer(Math.toIntExact(value))));
        return map1;
    }

    /**
     * Get few top stars by average rating of movies acted in or average gross of movies acted in.
     * @param top_k number of stars needed
     * @param by determine sort by rating or gross
     * @return
     */
    public List<String> getTopStars(int top_k,String by)  {
        Stream<String> stream1 = movies.stream().filter(a -> a.getStar1() != null).map(Movie::getStar1).distinct();
        Stream<String> stream2 = movies.stream().filter(a -> a.getStar2() != null).map(Movie::getStar2).distinct();
        Stream<String> stream3 = movies.stream().filter(a -> a.getStar3() != null).map(Movie::getStar3).distinct();
        Stream<String> stream4 = movies.stream().filter(a -> a.getStar4() != null).map(Movie::getStar4).distinct();
        List<String> stars = Stream.concat(Stream.concat(Stream.concat(stream1,stream2),stream3),stream4).distinct().sorted(Comparator.comparing(a -> a)).collect(Collectors.toList());

        Map<String,Double> map=new HashMap<>();
        double average;
        if (by.equals("rating")) {

            for (int i = 0; i < stars.size(); i++) {
                String name = stars.get(i);
                average = movies.stream().filter(a -> a.getStar1().equals(name) || a.getStar2().equals(name) || a.getStar3().equals(name) || a.getStar4().equals(name)).filter(a -> a.getIMDB_Rating() != -1).collect(Collectors.averagingDouble(Movie::getIMDB_Rating));
                map.put(name, average);
            }


            List<String> names = (List<String>) sortByValue_double(map,top_k).keySet().stream().collect(Collectors.toList());
            return names;


        }
        if(by.equals("gross")) {
            for (int i = 0; i < stars.size(); i++) {
                String name = stars.get(i);
                average = movies.stream().filter(a -> a.getStar1().equals(name) || a.getStar2().equals(name) || a.getStar3().equals(name) || a.getStar4().equals(name)).filter(a -> a.getGross() != -1).collect(Collectors.averagingDouble(Movie::getGross));
                map.put(name,average);
            }
            List<String> names = (List<String>) sortByValue_double(map,top_k).keySet().stream().collect(Collectors.toList());
            return names;
        }
        return new ArrayList<>();
    }

    /**
     * Fine movies satisfy the restritions
     * @param genre determine the genre wanted
     * @param min_rating determine the minimal rating wanted
     * @param max_runtime determine the max runtime of the movie wanted
     * @return
     */
    public List<String> searchMovies(String genre,float min_rating,int max_runtime) {
         List<String> list = movies.stream().filter(a -> {
            String[] array = a.getGenre().split(", ");
            boolean flag = false;
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals(genre)) {
                    flag = true;
                    break;
                }
            }
            return flag && a.getIMDB_Rating() >= min_rating && a.getRuntime() <= max_runtime;
        }).map(Movie::getSeries_Title).sorted(Comparator.comparing(a -> a)).collect(Collectors.toList());
         System.out.println(list);
        return list;
    }

}
