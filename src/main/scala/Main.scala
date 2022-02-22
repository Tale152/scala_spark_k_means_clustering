import kMeans.implementations.BaseKMeans
import org.apache.log4j.{Level, Logger}
import utils.ContextFactory

object Main {
    
    val appName = "test"
    //val masterAddress = "spark://spark-VirtualBox:7077"
    val masterAddress = "spark://192.168.1.82:7077"
    //val jarPath = "target/scala-2.12/app_2.12-1.0.jar"
    val jarPath = "target\\scala-2.12\\app_2.12-1.0.jar"
    //val dataPath = "/home/spark/Documents/big_data_assignment_2/bigann_query.seq"
    val dataPath = "C:\\Users\\teemo\\Desktop\\big_data_assignment_2\\bigann_query.seq"

    def main(args: Array[String]){
      Logger.getLogger("org").setLevel(Level.OFF)
      Logger.getLogger("akka").setLevel(Level.OFF)

      println("STARTING COMPUTATION")

      val sc = ContextFactory.create(appName, masterAddress, jarPath)

      val result = BaseKMeans().compute(sc, dataPath)

      println(result.mkString("Array(", ", ", ")"))

      sc.stop()
      println("COMPUTATION ENDED")
    }
}