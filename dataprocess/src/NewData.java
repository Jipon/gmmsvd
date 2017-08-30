import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
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
public class NewData {
	
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
		//去掉user签到次数少于20次,path为处理过后data的地址
		public static void deletelines(String path) throws IOException{
			FileInputStream inputStream=new FileInputStream(path);
			BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
			String line;
			FileOutputStream outputStream =new FileOutputStream("G:\\08.01\\sdata\\data1.txt");;
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
			
			while((line=reader.readLine())!=null){
				String[] str=line.split("	");
				if (str.length==5) {
				  //用户id
	                String userid=str[0];
	                //item
	                String item=str[4];

	                if (usertocheck.get(userid)>10) {
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
		
		String path2="G:\\08.01\\sdata\\data.txt";
		
		try {
			System.out.println("=====开始处理数据2=====");
			saveuseranditem(path2);
			System.out.println("处理过后的用户数："+users.size());
			System.out.println("处理过后的地点总数："+locs.size());
			deletelines(path2);
			System.out.println("======写入完成=======");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
