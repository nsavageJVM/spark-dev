# spark-dev

## spark now runs with java 8 so we can use java lambda syntax
  
`export GRADLE_HOME=/{path to your gradle}/gradle`      
`export JAVA_HOME=/{path to your java}/java-8-oracle`  
`export PATH=$PATH:$GRADLE_HOME/bin:$JAVA_HOME/bin`  
  

## Binary classification with the Logistic model
  
Good things in Math are always simple, 
  
### Training
  
get a bunch of data and label it, ie add a binary classification {0,1}
make the usuall statistical assumptions D(x,y) -> N(mu, theta) -> N(0,1)  
create a model (train)  

### Estimation
  
if your data is not numerical convert it to a numerical representation (hash) 
fit some other data and based on your model, and using  some associated statistical test 
based on your assumptions make a decision about the data point ( classify it)  

Spark does this very well for you with their Data Pipelines see this in 
com.logistic.BasicRunner  

#### Comment  
  
In my opinion your are better off assuming a Cauchy distribution  
Next up Clustering using some thing they call a k-mean wtf  
  
  
