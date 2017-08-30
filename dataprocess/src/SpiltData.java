import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class SpiltData {
	
	/**@author Jipon
	 * @throws IOException 
	 * @throws NumberFormatException 
	 * @随机按比例分割数据集
	 */
	
	//保存用户的id
	static Set<Integer> userids=new TreeSet<>();
	
	//每个用户所有的行数
	static TreeMap<Integer, Integer> idrows=new TreeMap<>();
	
	//对每个id：<行号，行>
	static HashMap<Integer, TreeMap<Integer, String>> idrowidrows=new HashMap<>();
	
	public static void getdata(String path) throws NumberFormatException, IOException{
		

		FileInputStream inputStream=new FileInputStream(path);
		BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
		String line;
		while((line=reader.readLine())!=null){
			String[] str=line.split(" ");
			int userid=Integer.parseInt(str[0]);
			userids.add(userid);
			if (!idrows.containsKey(userid)) {
				
				idrows.put(userid,1);
				TreeMap<Integer, String> map=new TreeMap<>();
				map.put(1, line);
				idrowidrows.put(userid, map);
				
			}else {
				int count=idrows.get(userid)+1;
				idrows.put(userid, count);
				
				TreeMap<Integer, String> map=idrowidrows.get(userid);
				map.put(count, line);
				idrowidrows.put(userid, map);
			}
			
		}		
		reader.close();
	}
	
	/**
	 * 随机分割数据集
	 * @param 分割比例ratio为training所占的比例
	 * @throws IOException 
	 */
	
	public static void splitData(double ratio,String path,String path1) throws IOException {
		
		//for测试集
		OutputStream outputStream=new FileOutputStream(path);
		BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(outputStream));
		
		//for训练集
		OutputStream outputStream1=new FileOutputStream(path1);
		BufferedWriter writer1=new BufferedWriter(new OutputStreamWriter(outputStream1));
		
		//对每个用户
		for (Integer userid : userids) {
							
				//取得每位用户的行数
				int rows=idrows.get(userid);
				//每位用户需要training:test的比例
				int testrows=rows-(int) (rows*ratio);
				//测试集行数集
				Set<Integer> ir=randomSet(1, rows, testrows, new HashSet<Integer>());
				//获得行
				for (Integer rowid : ir) {
					//把测试集数据写入文件
					String row=idrowidrows.get(userid).get(rowid);
					writer.write(row);
					writer.newLine();
					//删除测试集行数，剩余为训练集
					idrowidrows.get(userid).remove(rowid);
					
				}
		}
		//关闭连接
		writer.close();
		outputStream.close();
		
		//写训练集数据
		for (Integer userid : userids) {
			//每个用户所有的行数据
			for (Map.Entry<Integer, String> useridrows : idrowidrows.get(userid).entrySet()) {
					writer1.write(useridrows.getValue());
					writer1.newLine();
				
				}
			}
		writer1.close();
		outputStream1.close();
	}
	

	
	/**
	 *@Method 生成随机数方法
	 *
	 *
     * 随机指定范围内N个不重复的数
     * 利用HashSet的特征，只能存放不同的值
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param n 随机数个数
     * @param HashSet<Integer> set 随机数结果集
     */
    public static Set<Integer> randomSet(int min, int max, int n, HashSet<Integer> set) {
        if (n > (max - min + 1) || max < min) {
            return set;
        }
        for (int i = 0; i < n; i++) {
            // 调用Math.random()方法
            int num = (int) (Math.random() * (max - min)) + min;
            set.add(num);// 将不同的数存入HashSet中
        }
        int setSize = set.size();
        // 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
        if (setSize < n) {
            randomSet(min, max, n - setSize, set);// 递归
        }
		return set;
    }
    public static void main(String[] args) {
    	//划分比例
    	double ratio=0.7;
		String path="G:\\08.01\\sdata\\svddata.txt";
		String testpath="G:\\08.01\\sdata\\test3.txt";
		String trainpath="G:\\08.01\\sdata\\train7.txt";
		try {
			System.out.println("===开始获取===");
			getdata(path);
			System.out.println("===获取完毕===");
			System.out.println("===开始尝试划分数据集并保存===");
			splitData(ratio, testpath, trainpath);
			System.out.println("===划分完毕=====");
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
}
