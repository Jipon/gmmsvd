#! /usr/bin/env python
"""
@Author Jipon

"""

import numpy as np
npa = np.array
from gmm import GMM
from math import *


class getItem_pdf():
    
    #read file   
    def read_file(filename):
        f = open(filename, 'r')
        d = f.readlines()
        f.close()
        return d
        
     #calcute distance  
    def calcDistance(Lat_A, Lng_A, Lat_B, Lng_B):
        Lat_A=float(Lat_A)
        Lng_A=float(Lng_A)
        Lat_B=float(Lat_B)
        Lng_B=float(Lng_B)
        EARTH_RADIUS = 6378.137;#赤道半径(单位km)  
        radLat1 = radians(Lat_A)
        radLat2 = radians(Lat_B)
        a=radLat1-radLat2
        b=radians(Lng_A)-radians(Lng_B)
    
        s = 2 * asin(sqrt(pow(sin(a/2),2)+cos(radLat1)*cos(radLat2)*pow(sin(b/2),2)));  
        s = s * EARTH_RADIUS;  
        #s = Math.round(s * 10000d) / 10000d;  
        return s;  
        
    #get each user's pdf
    def get_item_pdf(data,ncomps):
        
        data = npa(data)
        #print(data)
        #pl.scatter(data[:,0],data[:,1])
        gmm = GMM(dim = 2, ncomps = ncomps, data = data, method = "kmeans")
        gmm.em(data, nsteps = 100)
        """
        for item in data: 
                #gmm.pdf(j)
             p=gmm.pdf(item)
           
           print("P(y|θ):",gmm.pdf(item))
          """
        return gmm
    #def get_uid_centernum(path):
        
        
    #top-n ,uir is (user item rating) and n is @k
    def gmm_top_k(uir,n):
        
        #top-n items
        topn={}
        
        # Then sort the predictions for each user and retrieve the k highest ones.
        for uid in uir:
            #print(uid)
            uir.get(uid).sort(key=lambda x: x[1], reverse=True)                
                #user_ratings.sort(key=lambda x: x[1], reverse=True)
            topn[uid] = uir.get(uid)[:n]
        return topn
        
    def hit_in_testset(topn,testpath):
        
        testdic={}
        #dictionary
        #testpath="H:\\svdgmm\\mfgmm\\sdata\\test.txt"
        testdic=getItem_pdf.get_testdict(testpath)
        hitcounts=0
          
        for uid in topn:
                
            itemlist=[iid for (iid,_) in topn.get(uid)]
    
            for i in itemlist:
                if testdic.get(uid):
                    hitcounts+=testdic.get(uid).count(i)
        
        return hitcounts 
          # get testdict
    def get_testdict(testpath):
        """
        According to testset get a dictionary
        Testdict type is dictionary
        
        """
        data = getItem_pdf.read_file(testpath)  
        testdic={}       
        #testcount=len(data)
        for i in range(len(data)):
            data_ = data[i].split()        
            
            if data_[0] not in testdic:       
                itemlist=[]
                itemlist.append(data_[1])
                testdic[data_[0]]=itemlist      
            else:                
                testdic.get(data_[0]).append(data_[1])
        
        return testdic


        