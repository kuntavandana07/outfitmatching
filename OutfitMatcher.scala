package com.outfitmatcher

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._


object OutfitMatcher {

  def generateOutfitCombinations(items: DataFrame)(implicit spark: SparkSession): DataFrame = {
    import spark.implicits._ // This function takes all clothing items (as a DataFrame).

    val shirts = items.filter($"type" === "shirt")
      .withColumnRenamed("name", "shirt_name")
      .withColumnRenamed("style", "shirt_style")
      .withColumnRenamed("season", "shirt_season")
    //It filters only the rows where the item type is "shirt".

    val pants = items.filter($"type" === "pants")
      .withColumnRenamed("name", "pants_name")
      .withColumnRenamed("style", "pants_style")
      .withColumnRenamed("season", "pants_season")
      //It filters only the rows where the item type is "Pants".


    val outfits = shirts.crossJoin(pants)
      .filter($"shirt_style" === $"pants_style" && $"shirt_season" === $"pants_season")
      .select(
        $"shirt_name".as("Shirt"),
        $"pants_name".as("Pants"),
        $"shirt_style".as("Style"),
        $"shirt_season".as("Season")
      )
    outfits
  }
    def filterOutfits(outfitsDF: DataFrame, style: String, season: String)(implicit spark: SparkSession): DataFrame = {
      import spark.implicits._ // ✅ Needed here too
      outfitsDF.filter($"style" === style && $"season" === season)
    }}
//It tries every shirt with every pant (cross join).
//Then it filters only those pairs where:Shirt and pant have the same style and season


















//    val outfitCombos = shirtsDF.alias("shirt")
//      .join(pantsDF.alias("pants"),
//        $"shirt.style" === $"pants.style" && $"shirt.season" === $"pants.season"
//      )
//      .select(
//        $"shirt.name".as("Shirt"),
//        $"pants.name".as("Pants"),
//        $"shirt.style".as("Style"),
//        $"shirt.season".as("Season")
//      )
//
//    outfitCombos
//  }
//
//  // ✅ Add this method with Spark implicits

//  }
//}