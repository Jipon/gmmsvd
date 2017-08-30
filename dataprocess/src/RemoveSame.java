import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.plaf.synth.SynthInternalFrameUI;

/**
 * Ticket:移除相同的行 ，去除不在训练集里的数据 anti_trainset
 * @author  chenzhipeng
 *
 */
public class RemoveSame {
    


    static HashMap<String, HashSet<String>> trainmap=new HashMap<>();
    
    //保存
    static HashMap<String, HashSet<String>> useritemloc=new HashMap<>();
    
    
    
    public static void getcenterlist1(String path,Map<String, HashSet<String>> map) throws IOException{
		        
		        FileInputStream inputStream=new FileInputStream(path);
		        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
		        String line;
		        while((line=reader.readLine())!=null){
		            String[] str=line.split(" ");
		            //用户id
		            String userid=str[0];
		            String loc=str[1];
		            if (!map.containsKey(userid)) {
						HashSet<String> set=new HashSet<>();
						set.add(loc);
						map.put(userid, set);
					}else {
						map.get(userid).add(loc);
					}
		        }
		        reader.close();
		 }
    
    
    
    public static void getcenterlist(String path) throws IOException{
        
        FileInputStream inputStream=new FileInputStream(path);
        BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line=reader.readLine())!=null){
            String[] str=line.split(" ");
            //用户id
            String userid=str[0];
            String loc=str[1];

            if (!useritemloc.containsKey(userid)) {
            	if (!trainmap.get(userid).contains(loc)) {
                	HashSet<String>set=new HashSet<>();
                	set.add(line);
    				useritemloc.put(userid, set);
				}

			}else 
			{
				useritemloc.get(userid).add(line);
			}
            

            }
        reader.close();
        inputStream.close();
       }

    

    //输出到文本
    
    public static void savedata(String path) throws IOException{
        FileOutputStream outputStream =new FileOutputStream(path);;
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        for (Map.Entry<String, HashSet<String>> iter : useritemloc.entrySet()) {
            if (iter.getValue().size()>=20) {
            	for (String entry : iter.getValue()) {
            		writer.write(entry);
            		writer.newLine();
    			}

            }

        }
        writer.close();
        outputStream.close();  
        
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println("开始处理");
        String path="H:\\sdata\\new\\useritemloc7.txt";
        String path1="H:\\sdata\\train7.txt";
        String savepath="H:\\sdata\\new\\useritemloc71.txt";
        getcenterlist1(path1, trainmap);
        getcenterlist(path);
        savedata(savepath);
        System.out.println("处理完成");
    }

}
