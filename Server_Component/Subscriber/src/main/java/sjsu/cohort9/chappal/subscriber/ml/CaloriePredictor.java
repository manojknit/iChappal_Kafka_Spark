package sjsu.cohort9.chappal.subscriber.ml;

import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.ml.regression.LinearRegressionModel;
import org.apache.spark.ml.regression.LinearRegressionTrainingSummary;

import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;
import org.springframework.stereotype.Service;


@Service
public class CaloriePredictor {
	
	SparkSession spark;
	LinearRegression glr;
	LinearRegressionModel lrModel;
	
	public CaloriePredictor() {
		trainModel();
		
	}
	
	public void trainModel() {
		spark = SparkSession
			      .builder()
			      .appName("CaloriePredictionApp")
			      .master("local")
			      .getOrCreate();

		//User_ID,Gender,Age,Height,Weight,Duration,Heart_Rate,Body_Temp,calories
		StructType schema = new StructType()
			.add("label", "float")
		    .add("Gender", "int")
		    .add("Age", "int")
		    .add("Height", "int")
		    .add("Weight", "int")
			.add("Duration", "int")
			.add("Heart_Rate", "int")
			.add("Body_Temp", "float");
			
		
		Dataset<Row> training = spark.read()
			    .option("mode", "DROPMALFORMED")
			    .schema(schema)
			    .csv("exercise.csv");
		training.show();
		
		VectorAssembler assembler = new VectorAssembler()
				.setInputCols(new String[]{"Gender", "Age","Height","Weight","Duration","Heart_Rate","Body_Temp"})
				.setOutputCol("features");

		
		Dataset<Row> vectorized_df = assembler.transform(training);
		vectorized_df.show(false);
		
		
		glr = new LinearRegression()
			     .setElasticNetParam(0.8)
			      .setMaxIter(10)
			      .setRegParam(0.3);
		
		// Fit the model.
		lrModel = glr.fit(vectorized_df);

		// Print the coefficients and intercept for linear regression.
		System.out.println("Coefficients: "
				+ lrModel.coefficients() + " Intercept: " + lrModel.intercept());

		// Summarize the model over the training set and print out some metrics.
		LinearRegressionTrainingSummary trainingSummary = lrModel.summary();
		System.out.println("numIterations: " + trainingSummary.totalIterations());
		System.out.println("objectiveHistory: " + Vectors.dense(trainingSummary.objectiveHistory()));
		trainingSummary.residuals().show();
		System.out.println("RMSE: " + trainingSummary.rootMeanSquaredError());
		System.out.println("r2: " + trainingSummary.r2());
		// $example off$
		trainingSummary.predictions().show();
//	    spark.stop();
		
	}
	
	public double predictCalorie(double gender, double age, double height,double weight, double duration,double heartRate, double temp ) {
		double [] features = {gender, age, height, weight, duration, heartRate, temp};
		org.apache.spark.ml.linalg.Vector feature= Vectors.dense(features);
		double prediction = lrModel.predict(feature);
		System.out.println("prediction " + prediction);
		return prediction;

	}
	public static void main(String args[]) {
		CaloriePredictor cp = new CaloriePredictor();
		cp.predictCalorie(1,68,190,94,29,105,40);
		cp.predictCalorie(1,68,190,94,60,105,40);
		cp.predictCalorie(1,68,190,94,180,105,40);
	}
}
