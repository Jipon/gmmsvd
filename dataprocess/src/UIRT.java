import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 
 * @author Jipon
 * 
 * user item rating timestrap
 * 对每个user，去掉签到少于20次的,对于地点，少于20次的
 */
public class UIRT {
	
	//定义list来保存总的items
	static HashSet<String> locs=new HashSet<>();
	
	//定义list来保存总的users
	static HashSet<String> users=new HashSet<>();

	//保存每个用户签到的次数
	static HashMap<String, Integer> usertocheck=new HashMap<>();
		
	//保存在每个地点的签到次数
	static HashMap<String, Integer> loctochecks=new HashMap<>();
	
	public static void saveuseranditem(String path) throws IOException{
		
		FileInputStream inputStream=new FileInputStream(path);
		BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
		String line;
		while((line=reader.readLine())!=null){
			String[] str=line.split("	");
			if (str.length==5) {
			     //用户id
	            String userid=str[0];
	            //item
	            String item=str[4];
	            //时间戳
	            String time=str[1];
	            //维度
	            String lat=str[2];
	            //经度
	            String lng=str[3];
	            locs.add(item);
	            users.add(userid);
	            
	            if (!usertocheck.containsKey(userid)) {
	                usertocheck.put(userid, 1);
	            }else {
	                int count=usertocheck.get(userid)+1;
	                usertocheck.put(userid, count);
	            }
	            if (!loctochecks.containsKey(item)) {
	                loctochecks.put(item, 1);
	            }else {
	                int count=loctochecks.get(item)+1;
	                loctochecks.put(item, count);
	            }
                
            }
	
		
	
		}
		reader.close();
		inputStream.close();		
	}
	//item少于20次
	public static void deletelocs(String path) throws IOException{
		FileInputStream inputStream=new FileInputStream(path);
		BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
		String line;
		FileOutputStream outputStream =new FileOutputStream("G:\\08.01\\sdata\\data.txt");;
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		
		while((line=reader.readLine())!=null){
		    
			String[] str=line.split("	");
			if (str.length==5) {
			  //用户id
	            String userid=str[0];
	            //item
	            String item=str[4];

	            if (loctochecks.get(item)>20) {
	                try {
	                    //System.out.println(line);
	                    writer.write(line);
	                    writer.newLine();
	                } catch (Exception e) {
	                    // TODO: handle exception
	                    e.printStackTrace();
	                }
	            }
                
            }
			
		}
		reader.close();
		inputStream.close();
		writer.close();
		outputStream.close();

	}
	
	public static void main(String[] args) {
		String path="G:\\08.01\\新建文件夹\\loc-brightkite_totalCheckins.txt\\Brightkite_totalCheckins.txt";		
		try {
			System.out.println("=====开始处理数据1======");
			saveuseranditem(path);
			System.out.println("用户总数为："+users.size());
			System.out.println("地点总数为："+locs.size());
			System.out.println("=====开始写入数据=====");
			deletelocs(path);
			System.out.println("=====写入完成1======");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
