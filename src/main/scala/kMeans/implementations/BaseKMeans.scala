package kMeans.implementations

import kMeans.EuclideanDistance.distance
import eCP.Java.SiftDescriptorContainer
import kMeans.AbstractKMeans
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD

case class BaseKMeans() extends AbstractKMeans {

  private val centroidNumber = 10
  private val iterationNumber = 10

  override def initCentroidSelector(data: RDD[SiftDescriptorContainer]): Array[SiftDescriptorContainer] = data.take(centroidNumber)

  override def endCondition(counter: Int): Boolean = counter == iterationNumber

  override def mapReduce(data: RDD[SiftDescriptorContainer], centroids: Broadcast[Array[SiftDescriptorContainer]]): Array[SiftDescriptorContainer] =
    data
      .map(point => {
        val pointsDistance = centroids.value.map(centroid => (centroid.id, distance(centroid, point)))
        val sortedPointsDistance = pointsDistance.sortBy(_._2)
        (sortedPointsDistance(0)._1, (1, point.vector.map(x => x.asInstanceOf[Double])))
      })
      .reduceByKey((x, y) => (x._1 + y._1, sumVectors(x._2, y._2)))
      .map(v => new SiftDescriptorContainer(v._1, divideVector(v._2._2, v._2._1)))
      .collect()

  private def divideVector(vector: Array[Double], n: Int): Array[Byte] = vector.map(x => (x / n).asInstanceOf[Byte])

  private def sumVectors(x: Array[Double], y: Array[Double]): Array[Double] = {
    var i = 0
    val res = x.map(e => {
      val r = e + y(i)
      i += 1
      r
    })
    res
  }
}