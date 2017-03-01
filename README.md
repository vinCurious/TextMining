# TextMining
Text Mining using Bag of words technique <br />
--------------------------------------------------------------------------------------<br />
Files in this directory:<br />
ModelBuild.java: This program builds the model with 70% files in each newsgroup directory<br />
TestModel.java: This program tests the model with 30% files which were not used for training<br />
stopWordsList.txt:	List of stopWords<br />
model folder:   This model folder has model files which are already created after running ModelBuild.java on 70% files of each newsgroup<br />
                If user executes ModelBuild.java, the files will be cleared off and then updated with latest values for new selection.<br />
			
How to run the Newsgroup text Analysis:<br />
Part-I: Run already created model on selected test files:<br />
    1. Run TestModel.java program
	   File browser window will pop up
     <br />
	2. Select newsgroup directory e.g. C:\Users\admin\Desktop\Big Data Analytics\project2\20_newsgroups\<br />

Part-II: Build model again and then run that on selected text files<br />
	1. Run ModelBuild.java program<br />
	   File browser window will pop up<br />
	2. Select newsgroup directory e.g. C:\Users\admin\Desktop\Big Data Analytics\project2\20_newsgroups\
	   (new model files will be created in model folder after 2nd step) <br />
    3. Run TestModel.java program (Optional: Change score function - refer 174th line in TestModel.java program)
	   File browser window will pop up	<br />
	4. Select newsgroup directory e.g. C:\Users\admin\Desktop\Big Data Analytics\project2\20_newsgroups\ <br />

Results:<br />
	1. After running ModelBuild.java program, in model folder 20 model files are created with sorted word mapping with their count for each newsgroup<br />
	2. After running TestModel.java program, correctly classifed, iccorrectly instances and accuracy is printed on console.  
