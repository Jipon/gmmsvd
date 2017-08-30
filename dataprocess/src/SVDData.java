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
 * 
 * @author Jipon
 * 数据格式： user loc rating 
 *
 */
public class SVDData {
	
	//定义保存每位用户对每个位置的签到次数
	static Map<String, HashMap<String, Integer>> map=new HashMap<>();
	
    //定义一个map保存位置经纬度 loc lat log
    static Map<String, String[]> locloglat=new HashMap<>();

	public static void processtxt(String path) throws IOException{
		FileInputStream inputStream=new FileInputStream(path);
		BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
		String line;
		while((line=reader.readLine())!=null){
			String[] str=line.split("	");
			//用户id
			String userid=str[0];
			//item
			String loc=str[4];
			//lat
			String lat=str[2];
			//log
			String log=str[3];
			
			 if (!locloglat.containsKey(loc)) {
	                String [] loglat=new String[2];
	                loglat[0]=lat;
	                loglat[1]=log;
	                locloglat.put(loc, loglat);
	            }       
			
			if (!map.containsKey(userid)) {
				HashMap<String, Integer> map1=new HashMap<>();
				map1.put(loc, 1);
				map.put(userid, map1);
				
			}else {
				if (!map.get(userid).containsKey(loc)) {
		
					map.get(userid).put(loc, 1);
				}else {
					int count=map.get(userid).get(loc)+1;
					map.get(userid).put(loc, count);
				}
			}
		}

		reader.close();
		inputStream.close();
		
	}
	
	public static void saveSVDdata() throws IOException{
		FileOutputStream outputStream =new FileOutputStream("G:\\08.01\\sdata\\svddata.txt");;
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		for (Map.Entry<String, HashMap<String, Integer>> iter : map.entrySet()) {
			String str=iter.getKey();
			for (Map.Entry<String, Integer> iter1 : iter.getValue().entrySet()) {
				String str1=str+" "+iter1.getKey()+" "+iter1.getValue();
				try {
					writer.write(str1);
					writer.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}	
		}
		writer.close();
		outputStream.close();
		
	}
	
	 /**
     * 写入位置数据
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
	public static void main(String[] args) throws IOException {
		String path="G:\\08.01\\sdata\\data1.txt";
		 
		String savepath="G:\\08.01\\sdata\\loclatlog.txt";
		 
		System.out.println("===开始处理文件====");
		processtxt(path);
		System.out.println("=====处理完成=====");
		//saveSVDdata();
		savedata(savepath);
		System.out.println("====保存完成====");

		
	}
}
