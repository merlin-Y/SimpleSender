package cn.merlin.database
import org.jetbrains.exposed.sql.Database

class DeviceDB {
    val url = "jdbc:sqlite:resources/database/sender.db"
    val database = Database.connect(url, driver = "org.sqlite.JDBC")
}