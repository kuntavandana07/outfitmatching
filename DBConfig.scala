package com.outfitmatcher.config

import com.typesafe.config.ConfigFactory

object DBConfig {
  private val config = ConfigFactory.load()

  val url: String = config.getString("db.url")
  val user: String = config.getString("db.user")
  val password: String = config.getString("db.password")
  val driver: String = "org.postgresql.Driver"
}

//load database connection settings from the application.conf and stored in variables