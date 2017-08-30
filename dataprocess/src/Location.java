
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * Ticket: 地点的经纬度 
 * @author  JiponChan
 *
 */
public class Location {
    
    
    //定义一个map保存位置经纬度 loc lat log
    static Map<String, String[]> locloglat=new HashMap<>();
    
    public static void getlatlog(String path) throws IOException{
        FileInputStream inputStream=new FileInputStream(path);
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line=reader.readLine())!=null){
            System.out.println(line);
            String[] str=line.split("   ");
            System.out.println(str[0]);
            //item
            String loc=str[4];
            //维度
            String lat=str[2];
            //经度
            String log=str[3];
            
            if (!locloglat.containsKey(loc)) {
                String [] loglat=new String[2];
                loglat[0]=lat;
                loglat[1]=log;
                locloglat.put(loc, loglat);
            }        
        }
        reader.close();
    }
    
    /**
     * 写入数据
     * @param path
     * @throws IOException
     */
    public static void savedata(String path) throws IOException{
        FileOutputStream outputStream =new FileOutputStream(path);;
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        for (Map.Entry<String, String[]> iter : locloglat.entrySet()) {
            String str=iter.getKey()+" ";
            str=str+iter.getValue()[0]+" "+iter.getValue()[1];
            writer.write(str);
            writer.newLine();
        }
        writer.close();
        outputStream.close();        
        
    }
    public static void main(String[] args) {
        String locpath="G:\\08.01\\sdata\\data1.txt";
        String savepath="G:\\08.01\\sdata\\loclatlog.txt";
        try {
            getlatlog(locpath);
            savedata(savepath);
        } catch (IOException e) {
            
            e.printStackTrace();
        }
    }

}
