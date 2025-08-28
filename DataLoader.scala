package com.outfitmatcher.data

import org.apache.spark.sql.{DataFrame, SparkSession}
import com.outfitmatcher.config.DBConfig

object DataLoader {
  def loadItems(spark: SparkSession): DataFrame = {
    spark.read
      .format("jdbc")
      .option("url", DBConfig.url)
      .option("dbtable", "items")
      .option("user", DBConfig.user)
      .option("password", DBConfig.password)
      .option("driver", DBConfig.driver)
      .load()
  }
}
// it is used to read data from your PostgreSQL database into your Spark program.