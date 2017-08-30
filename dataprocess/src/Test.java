import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;


/**
 * Ticket: 
 * @author  chenzhipeng
 *
 */
public class Test {
  //定义保存每位用户对每个位置的签到次数
    static HashMap<String, String> centersnums=new HashMap<>();
    //return total_n_users
    static HashSet<String> n_users=new HashSet<>();
    
    //return total_n_locs
    static HashSet<String> n_locs=new HashSet<>();

    public static void processtxt(String path) throws IOException{
        FileInputStream inputStream=new FileInputStream(path);
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
           
        String line;
        while((line=reader.readLine())!=null){
        	
            String[] str=line.split(" ");
            
            String userid=str[0];
            n_users.add(userid);

            
        }

        reader.close();
        inputStream.close();
    }
    

    public static void main(String[] args) throws IOException {
        String path1="H:\\sdata\\train7.txt";
        processtxt(path1);
        System.out.println(n_users.size());
        //System.out.println("完毕，没有发现");
    }

}
