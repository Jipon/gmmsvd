

public class Distance {
private static final  double EARTH_RADIUS = 6378.137;//赤道半径(单位km)  
    
    /** 
     * 转化为弧�?rad) 
     * */  
    private static double rad(double d)  
    {  
       return d * Math.PI / 180.0;  
    }  
    /** 
     * 基于googleMap中的算法得到两经纬度之间的距�?计算精度与谷歌地图的距离精度差不多，相差范围�?.2米以�?
     * @param lon1 第一点的精度 
     * @param lat1 第一点的纬度 
     * @param lon2 第二点的精度 
     * @param lat3 第二点的纬度 
     * @return 返回的距离，单位km 
     * */  
    public double GetDistance(double lon1,double lat1,double lon2, double lat2)  
    {  
       double radLat1 = rad(lat1);  
       double radLat2 = rad(lat2);  
       double a = radLat1 - radLat2;  
       double b = rad(lon1) - rad(lon2);  
       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));  
       s = s * EARTH_RADIUS;  
       s = Math.round(s * 10000d) / 10000d;  
       return s;  
    }
    public static void main(String[] args) {
		//System.out.println(GetDistance(118.796877, 32.060255, 116.407395, 39.904211));
	}
  
}
