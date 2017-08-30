from surprise import SVDpp
from surprise import Dataset
from surprise import dataset
import os

"""
gmm fused svd

"""
path1="H:\\sdata\\train7.txt"
path2="H:\\sdata\\test3.txt"
gmmpath="H:\\sdata\\new\\useritemloc7.txt"

#testpath="H:\\sdata\\sdata\\test.txt"
file_path = os.path.expanduser(path1)
reader=dataset.Reader(line_format='user item rating', sep=' ')
#data =Dataset.load_from_file('G:\\08.01\\data\\train8\\train.txt',reader)
data =Dataset.load_from_file(file_path ,reader)
trainset = data.build_full_trainset()
algo = SVDpp()
algo.train(trainset)

#running gmm 
gmmuir=trainset.get_gmm_rating(gmmpath)
#print(gmmuir)

#get recommend correctcount ---@k
#top-k
k=5
correctcount = trainset.build_hit_testdata(algo,path2,k,gmmuir)
#print(correctcount)
#get testcount

#get recommend totalcount
#totalcount=trainset.n_users*k
totalcount=10083*k

print("---------------------")
print("hitcount",correctcount)

print("Precision:")
if totalcount!=0:
    print(correctcount/totalcount)
    
testcount=len(trainset.read_file(path2))
print("Recall:")
if testcount!=0:
    print(correctcount/testcount)
print("--------------------")
