import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Ticket: Algorithm  Multi-center Discovering Algorithm
 * @author  chenzhipeng
 *
 */
public class MDA {
    
    /**
     * @return each user's centerlist
     * @throws IOException 
     */
    
    
    //定义一个map保存位置经纬度 loc lat log,总
    static Map<String, String[]> locloglat=new HashMap<>();

    //定义一个map保存数据每一行
    static Map<String, Map<String,Integer>> usercenter=new HashMap<>();
    
    //定义一个map保存user centers
    static Map<String, CopyOnWriteArrayList<String>> usercenters=new HashMap<>();
    
    //user item lat log
    static Map<String, HashMap<String, String[]>> useritemloc=new HashMap<>();
    
    //每位用户center的个数
    static Map<String, Integer> usercenternum=new HashMap<>();
    
    //
    
    public static void getcenterlist(String path) throws IOException{
      
            FileInputStream inputStream=new FileInputStream(path);
            BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line=reader.readLine())!=null){
                String[] str=line.split(" ");
                //用户id
                String userid=str[0];
                //item
                String loc=str[1];
                
                //rating
                int rating=Integer.parseInt(str[2]);
                
                //清洗locloglat，去除不在Training里的loc
                if (!locloglat.containsKey(loc)) {
                    locloglat.remove(loc);
                }
                
               //保存每位用户的所有位置
                if (!usercenter.containsKey(userid)) {
                    Map<String, Integer> map=new HashMap<>();
                    map.put(loc, rating);
                    usercenter.put(userid,map)  ;
                    
                }else {
                    usercenter.get(userid).put(loc,rating);
                }
                

            }
        
            //选出k个中心,设置阈值，最大不超过10
            for (Map.Entry<String, Map<String, Integer>> map : usercenter.entrySet()) {
                
                List<Map.Entry<String, Integer>> list=new ArrayList<>(map.getValue().entrySet());
                Collections.sort(list,new Comparator<Map.Entry<String, Integer>>() {

                    @Override
                    public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                        // 比较器，从大到小实现
                        return (o2.getValue()-o1.getValue());
                    }
                });
                
                //usercenters.put(map.getKey(), map.get)
                int i=0;
                
                //根据rating排序loc，并放入usercenters
                CopyOnWriteArrayList<String> list2=new CopyOnWriteArrayList<>();
                
                for (Entry<String, Integer> entry : list) {
                    //中心点个数阈值，为了方便,设置固定值
                    if (i<2) {
                        list2.add(entry.getKey());
                    }
                   i=i+1;
                }
                usercenters.put(map.getKey(), list2);
            }
    }
    
    /**
     * 根据位置筛选中心点
     * 😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂😂
     * 如果中心点聚集的数目/totalloc<0.02则删除中心点
     * 
     */
    public static void getCenters(){
        Distance distance=new Distance();
        for (Map.Entry<String, CopyOnWriteArrayList<String>> m: usercenters.entrySet()) {
            //
            //System.out.println(m.getKey());
            for (String item : m.getValue()) {
                int count=0;
                //中心点的位置
                String[] loglat=locloglat.get(item);
                double centerlog=Double.parseDouble(loglat[1]);
                double centerlat=Double.parseDouble(loglat[0]);
                
                for (Map.Entry<String, String[]> locs : locloglat.entrySet()) {
                    //位置点
                    double log=Double.parseDouble(locs.getValue()[1]);
                    double lat=Double.parseDouble(locs.getValue()[0]);
                    //两者之间的距离
                    double d=distance.GetDistance(centerlog, centerlat, log, lat);
                    //如果d小于一个阈值，则为中心点里的一个点
                    if (d<15) {
                        
                        //判断count是否大于总共位置*α,α为0.02,这里默认10
                        count=count+1;
                    }
                    
                }
                //否则，从中心点删除
                if (count<=20) {
                    usercenters.get(m.getKey()).remove(item);
                }
                
            }
        }  
        
        
    }
    /**
     * 继续筛选中心点，最多中心点个数不大于5
     * 
     */
    public  static void processcenters(){
        
        Distance distance=new Distance();
        for (Map.Entry<String, CopyOnWriteArrayList<String>> m: usercenters.entrySet()) {
            for (int i=0;i<m.getValue().size()-1;i++) {
                //定义临时的中心点个数
                int temp=0;
                //中心点的位置i
                String[] loglati=locloglat.get(m.getValue().get(i));
                double centerlog=Double.parseDouble(loglati[1]);
                double centerlat=Double.parseDouble(loglati[0]);
                
                for(int j=i+1;j<m.getValue().size();j++){

                    //中心点的位置j
                    String[] loglatj=locloglat.get(m.getValue().get(j));
                    double centerlog1=Double.parseDouble(loglatj[1]);
                    double centerlat1=Double.parseDouble(loglatj[0]);
                    
                    //两者之间的距离
                    double d=distance.GetDistance(centerlog, centerlat, centerlog1, centerlat1);
                    if (d<30) {
                        usercenters.get(m.getKey()).remove(m.getValue().get(j));
                    }
                    
                }
            }
            
        }
        

    }
    
    
    /**
     * 保存数据，格式为：
     * user item rating lat log
     * 
     * 
     */
    public static void reData(){
        Distance distance=new Distance();
        
        //读取每位用户的centers
        for (Map.Entry<String, CopyOnWriteArrayList<String>> useritems : usercenters.entrySet()) {
            
            
          if (useritems.getValue().size()!=0) {
            
        
            for (String loc : useritems.getValue()) {

                //中心点的位置
                String[] loglat=locloglat.get(loc);
                double centerlog=Double.parseDouble(loglat[1]);
                double centerlat=Double.parseDouble(loglat[0]);
                
                for (Map.Entry<String, String[]> locs : locloglat.entrySet()) {
                    //位置点
                    double log=Double.parseDouble(locs.getValue()[1]);
                    double lat=Double.parseDouble(locs.getValue()[0]);
                    //两者之间的距离
                    double d=distance.GetDistance(centerlog, centerlat, log, lat);

                    if (d<15) {
                        
                        if (!useritemloc.containsKey(useritems.getKey())) {
                            HashMap<String, String[]> map=new HashMap<>();
                            map.put(locs.getKey(), locs.getValue());
                            useritemloc.put(useritems.getKey(), map);
                        }else {
                            useritemloc.get(useritems.getKey()).put(locs.getKey(), locs.getValue());
                        }
                    }
                    
                }
            }
          }
        }
    
    }
    
/*    //去除training中访问过的数据
    public static void anti_training(){
        
        for (Map.Entry<String, Map<String,Integer>> map : usercenter.entrySet()) {
            HashMap<String, String[]> itemloc=new HashMap<>();
            itemloc=useritemloc.get(map.getKey());
            for (Map.Entry<String, Integer> map1 : map.getValue().entrySet()) {
                
                //判断训练集中的数据是否存在，存在，则去除
                if (itemloc.containsKey(map1.getKey())) {
                    useritemloc.get(map.getKey()).remove(map1.getKey());
                }
            }
            
        }
        

        
    }*/
    
    //输出到文本
    
    public static void savedata(String path) throws IOException{
        FileOutputStream outputStream =new FileOutputStream(path);;
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        for (Map.Entry<String, HashMap<String, String[]>> iter : useritemloc.entrySet()) {
            
            for (Map.Entry<String, String[]> map : iter.getValue().entrySet()) {
                String str=iter.getKey()+" ";
                str=str+map.getKey()+" "+map.getValue()[0]+" "+map.getValue()[1];
                writer.write(str);
                writer.newLine();
            }

        }
        writer.close();
        outputStream.close();  
        
    }
    
    //输出user-centernum到文本
    public static void  savaecenternum(String path) throws IOException{
        
        FileOutputStream outputStream =new FileOutputStream(path);;
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
        for (Map.Entry<String, CopyOnWriteArrayList<String>> iter : usercenters.entrySet()) {
         
                String str=iter.getKey()+" ";
                
                if (iter.getValue().size()>2) {
                    str=str+"2";
                }else {
                    str=str+iter.getValue().size();
                }
                
                writer.write(str);
                writer.newLine();
            

        }
        writer.close();
        outputStream.close();
        
    }
    
    public static void main(String[] args) throws IOException {
        //加载数据集获得loc
        String locpath="H:\\sdata\\data1.txt";
        //user item lat log文件
        String savepath="H:\\sdata\\new\\useritemloc7.txt";
        //加载svddata
        String openpath="H:\\sdata\\train7.txt";
        //保存user-
        String usercenternums="G:\\08.01\\sdata\\sdata\\newcenternums.txt";
        
        //开始执行
        System.out.println("开始执行");
        //get loclatlog
        SVDData data=new SVDData();
        data.processtxt(locpath);
        locloglat=data.locloglat;
        
        getcenterlist(openpath);
        System.out.println("centers存储完成");
        //getCenters();
        System.out.println("筛选过程1完成");
        //processcenters();
        System.out.println("筛选过程2完成");
        System.out.println("centers筛选完成");
        reData();
        //anti_training();
        //System.out.println("去除训练集数据完毕");
        System.out.println("数据整合完成");
        savedata(savepath);
        System.out.println("数据 保存完成");
        //savaecenternum(usercenternums);
        System.out.println("执行完毕");
        
    
    }
    
}
