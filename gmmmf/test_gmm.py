# -*- coding: utf-8 -*-
"""
Created on Fri Aug 18 14:17:03 2017

@author: Jipon
"""
#!/usr/bin/python
from gmm_method import getItem_pdf as itempdf
"""
#读取用户的中心点个数
cnumspath="G:\\08.01\\sdata\\sdata\\centernums7.txt"

data = itempdf.read_file(cnumspath)  

#用户和对应的comps
centernums={} 
 
#用户和lines
useriditems={}

#user item rating
uir={}

for i in range(len(data)):
    data_ = data[i].split()
    centernums[data_[0]]= data_[1]  
"""
#用户和对应的comps
centernums={} 
 
#用户和lines
useriditems={}

#user item rating
uir={}
#
fp=open("H:\\sdata\\new\\useritemloc71.txt")    
for line in fp.readlines():
#x,y=line.split()
    
    str = line.split()
    uid=str[0]
        
    if uid not in useriditems: 
        lines=[]
        lines.append(line)
        useriditems[uid]=lines
    else:
        useriditems.get(uid).append(line)
        
count=0    
for uid in useriditems:
    count=count+1
    lines=useriditems.get(uid)
    print(uid,":",count)
    #ncomps=int(centernums.get(uid))
    #位置和所在的分数
    itemrating=[]
    data=[]
    item=[]
    for line in lines:
        str = line.split(" ",3)
        uid=str[0]
        itemid=str[1]
        x=str[2]
        y=str[3]            
        #每位用户的需要gmm的位置
        #data.append([float(x),float(y)])
        if x is not None and y is not None:
            data.append([float(x),float(y)])
            item.append(itemid)
        #item=[float(x),float(y)]
   # print(data)
    if data is not None:
    #if int(ncomps)!=0 and data is not None and int(ncomps)<=len(data):
        #print("ncopms",int(ncomps))
        gmm=itempdf.get_item_pdf(data,1)
        mean=gmm.mean()
        #训练后的pdf
        for i in range(len(data)):
            locrating=gmm.pdf(data[i])
            distance=itempdf.calcDistance(x,y,mean[0],mean[1])
            locrating=locrating*(1/distance)
            itemrating.append((item[i],locrating))
            
        
        if itemrating is not None and len(itemrating)!=0:
            uir[uid] =itemrating 
        #print("uid:===",uid,itemrating)

#推荐的数目
k=5
topn=itempdf.gmm_top_k(uir,k)

testpath="H:\\sdata\\test3.txt"
#推荐正确的数目
hitcount=itempdf.hit_in_testset(topn,testpath)

#get recommend totalcount
totalcount=len(topn)*k


print("---------------------")
print("hitcount:",hitcount)
print()
print("Precision:")
if totalcount!=0:
    print(hitcount/totalcount)
print("Recall:")

testcount=len(itempdf.read_file(testpath))
if testcount!=0:
    print(hitcount/testcount)
    
print("--------------------")
  