import java.io.*;
import java.util.*;

public class MovieAnalyzer {

    private List<HashMap<String,String>> data = new ArrayList<>();
    private String[] col;
    public MovieAnalyzer(String dataset_path) throws FileNotFoundException {
        //Series_Title,Release_Year,Certificate,Runtime,Genre,IMDB_Rating,Overview,Meta_score,Director,Star1,Star2,Star3,Star4,
        // Noofvotes,Gross
        File file=new File(dataset_path);
        file.setReadable(true);
        InputStreamReader input=null;
        BufferedReader br=null;
        String[] tmp;
        try{
            input = new InputStreamReader(new FileInputStream(file),"UTF-8");
            br = new BufferedReader(input);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        String line ="";
        try{
            String intermediate;
            int count;
            boolean not_yet;
            boolean firstline=true;
            while((line=br.readLine())!=null){
                intermediate="";
                count=0;
                not_yet=false;
                HashMap<String,String> map=new HashMap<>();
                tmp=line.split(",");
                if(firstline){
                    col=tmp;
                    firstline=false;
                    continue;

                }
                for(int i=0;i<tmp.length;i++){
                    if(tmp[i].startsWith("\"")){
                        if(tmp[i].endsWith("\"")){
                            map.put(col[count++],tmp[i]);
                        }
                        else{
                            not_yet=true;
                            intermediate+=tmp[i];
                        }

                    }
                    else if(tmp[i].endsWith("\"")){
                        intermediate+=tmp[i];
                        //count # of "
                        String s=tmp[i],des = "\"";

                        int cnt = 0;
                        int offset = 0;
                        while((offset = s.indexOf(des, offset)) != -1){
                            offset = offset + des.length();
                            cnt++;
                        }
                        if(cnt%2==0){
                            map.put(col[count++],intermediate);
                            intermediate="";
                            not_yet=false;
                        }


                    }
                    else if(not_yet){
                        intermediate+=","+tmp[i];

                    }
                    else{
                        map.put(col[count++],tmp[i]);
                    }
                }
                data.add(map);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String,Integer> getMovieCountByGenre(){
        return new HashMap<>();

    }
    public Map<Integer,Integer> getMovieCountByYear(){
        return new HashMap<>();
    }
    public List<String> getTopMovies(int top_k,String by){

        return new ArrayList<>();
    }
    public Map<List<String>,Integer> getCoStarCount(){
        return new HashMap<>();
    }
    public List<String> getTopStars(int top_k,String by){
        if(by.equals("rating")){

        }
        if(by.equals("gross")){

        }
        return new ArrayList<>();
    }
    public List<String> searchMovies(String genre,float min_rating,int max_runtime){
        return new ArrayList<>();
    }

}
