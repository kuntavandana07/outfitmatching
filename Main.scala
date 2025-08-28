package com.outfitmatcher

import org.apache.spark.sql.SparkSession
import com.outfitmatcher.data.DataLoader
import com.outfitmatcher.config.DBConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.spark.sql._

object Main {
  val logger: Logger = LogManager.getLogger(Main.getClass)

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("Outfit Matching System")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    try {
      // This uses your DataLoader.scala to read data from the items table
      val itemsDF = DataLoader.loadItems(spark)
      logger.info("📦 Loaded Items from PostgreSQL:")
      itemsDF.show(truncate = false)

      val outfitCombos = OutfitMatcher.generateOutfitCombinations(itemsDF)(spark)
      logger.info(" Suggested Outfit Combinations (Shirt + Pants)")
      outfitCombos.show(truncate = false)

      outfitCombos.printSchema()

//   This calls matching logic from OutfitMatcher.scala.creates shirt + pant combinations basedon style,season.


      //Group items by style and season
      val groupedItems = itemsDF.groupBy("style", "season").count()
      logger.info("👕 Outfit Group Suggestions by Style and Season:")
      groupedItems.show(truncate = false)
//
//

       //Save combinations to PostgreSQL
      try{
      outfitCombos.write
        .format("jdbc")
        .option("url", DBConfig.url)
        .option("dbtable", "outfits")
        .option("user", DBConfig.user)
        .option("password", DBConfig.password)
        .option("driver", DBConfig.driver)
        .mode("overwrite")
        .save()
        //This saves the matched outfits into a new table called outfits in PostgreSQL.

        logger.info("✅ Outfit combinations saved to PostgreSQL table 'outfits'")

       //Read saved combinations

        // Get style and season from command-line arguments
        val style = if (args.length > 0) args(0) else "casual"
        val season = if (args.length > 1) args(1) else "summer"


      val savedOutfitsDF = spark.read
        .format("jdbc")
        .option("url", DBConfig.url)
        .option("dbtable", "outfits")
        .option("user", DBConfig.user)
        .option("password", DBConfig.password)
        .option("driver", DBConfig.driver)
        .load()//It reads the outfits table.The result is stored in a DataFrame called savedOutfitsDF.

//      // Apply filtering
      val filteredOutfits = OutfitMatcher.filterOutfits(savedOutfitsDF, style, season)(spark)
      logger.info(s"🎯 Filtered Outfit Suggestions ($style, $season):")
      filteredOutfits.show(truncate = false)

    } catch {
      case e: Exception =>
        logger.error("❌ An error occurred during outfit matching process", e)
    } finally {
      spark.stop()
      logger.info("🛑 Spark session stopped.")
    }

  }}
}