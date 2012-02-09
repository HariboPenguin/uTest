package me.HariboPenguin.uTest;

import java.util.logging.Logger;
import lib.PatPeter.SQLibrary.MySQL;
import lib.PatPeter.SQLibrary.SQLite;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class uTest extends JavaPlugin {
    
    public static uTest plugin;
    public final Logger log = Logger.getLogger("Minecraft");
    public SQLite dbManageSQLite;
    public MySQL dbManageMySQL;
    private uTestCommandHandler uTestExecutor;
    private uTestQuestioner uTestQuestioner;
    public uTestSQLDataHandler sqldh = new uTestSQLDataHandler(this);
    public uTestSQLiteDataHandler sqlidh = new uTestSQLiteDataHandler(this);
    public String prefix = ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "uTest" + ChatColor.DARK_PURPLE + "] ";
    
    @Override
    public void onDisable() {
        
        PluginDescriptionFile pdfFile = this.getDescription();
        
        if (this.getConfig().getBoolean("use-mysql")) {
            if (dbManageMySQL != null) {
                dbManageMySQL.close();
                this.log.info("[" + pdfFile.getName() + "] MySQL database connection closed!");
            }
        } else if (!this.getConfig().getBoolean("use-mysql")) {
            if (dbManageSQLite != null) {
                dbManageSQLite.close();
                this.log.info("[" + pdfFile.getName() + "] SQLite database connection closed!");
            }
        }
    }
    
    @Override
    public void onEnable() {
        
        PluginDescriptionFile pdfFile = this.getDescription();
        
        getConfig().options().copyDefaults(true);
        saveConfig();
        
        if (this.getConfig().getBoolean("use-mysql")) {
            this.log.info("[" + pdfFile.getName() + "] Using MySQL for database!");
            initMySQL();
        } else if (!this.getConfig().getBoolean("use-mysql")) {
            this.log.info("[" + pdfFile.getName() + "] Using SQLite for database!");
            initSQLite();
        }
        
        uTestExecutor = new uTestCommandHandler(this);
        getCommand("ut").setExecutor(uTestExecutor);
        getCommand("utest").setExecutor(uTestExecutor);
//        getCommand("answer").setExecutor(uTestQuestioner);
        
    }
    
    public void initMySQL() {
        
        PluginDescriptionFile pdfFile = this.getDescription();
        
        // Get database config
        String hostname = this.getConfig().getString("mysql.hostname");
        String portnumbr = this.getConfig().getString("mysql.port");
        String database = this.getConfig().getString("mysql.database");
        String username = this.getConfig().getString("mysql.username");
        String password = this.getConfig().getString("mysql.password");
        // End get database config
        
        // Initialise db
        dbManageMySQL = new MySQL(this.log, pdfFile.getName(), hostname, portnumbr, database, username, password);
        dbManageMySQL.open();
        this.log.info("[" + pdfFile.getName() + "] MySQL Database connection esablished");
        String appsTable = "CREATE TABLE IF NOT EXISTS `" + database + "`.`applications` (`id` INT NOT NULL ,`applicationdate` DATETIME ,`applicationtime` TIME ,`applicant` VARCHAR(30) NOT NULL ,`TimePlayed` VARCHAR(30) NOT NULL ,`bannedcheck` INT ,`singleplayercheck` INT ,`multiplayercheck` INT ,`referrer` VARCHAR(50) ,`message` TEXT NOT NULL )";
        String quizTable = "CREATE TABLE IF NOT EXISTS `" + database + "`.`quizzes` (`id` INT NOT NULL ,`name` VARCHAR(30) NOT NULL ,PRIMARY KEY ( `id` ))";
        dbManageMySQL.createTable(appsTable);
        dbManageMySQL.createTable(quizTable);
        this.log.info("[" + pdfFile.getName() + "] MySQL now initialized");
        // End Iitialise db
    }
    
    public void initSQLite() {
        
        PluginDescriptionFile pdfFile = this.getDescription();

        // Initialise db
        dbManageSQLite = new SQLite(this.log, pdfFile.getName(), "uTest", this.getDataFolder().getPath());
        dbManageSQLite.open();
        this.log.info("[" + pdfFile.getName() + "] SQLite Database connection esablished");
        String appsTable = "CREATE TABLE IF NOT EXISTS `applications` (`id` INT NOT NULL ,`applicant` VARCHAR(30) NOT NULL ,PRIMARY KEY ( `id` ))";
        String quizTable = "CREATE TABLE IF NOT EXISTS `quizzes` (`id` INT NOT NULL ,`name` VARCHAR(30) NOT NULL ,PRIMARY KEY ( `id` ))";
        dbManageSQLite.createTable(appsTable);
        dbManageSQLite.createTable(quizTable);
        this.log.info("[" + pdfFile.getName() + "] SQLite now initialized");
        // End Initialise db
    }
}
