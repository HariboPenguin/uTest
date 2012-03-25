package me.HariboPenguin.uTest;

import java.util.logging.Logger;
import lib.PatPeter.SQLibrary.MySQL;
import lib.PatPeter.SQLibrary.SQLite;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class uTest extends JavaPlugin {

    public static uTest plugin;
    public final Logger log = Logger.getLogger("Minecraft");
    public SQLite dbManageSQLite;
    public MySQL dbManageMySQL;
    private uTestCommandHandler uTestExecutor;
    private uTestQuestioner uTestQuestioner;
    private uTestListener uTestListener = new uTestListener(this);
    public uTestSQLDataHandler sqldh = new uTestSQLDataHandler(this);
    public uTestSQLiteDataHandler sqlidh = new uTestSQLiteDataHandler(this);
    public String prefix = ChatColor.DARK_PURPLE + "[" + ChatColor.GOLD + "uTest" + ChatColor.DARK_PURPLE + "] ";
    public static Permission permission = null;

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
        
        if (!setupPermissions()) {
            this.log.warning(prefix + ChatColor.RED + "Permission Failure! - Make sure that you have a permissions plugin and vault in your plugins folder!");
            this.setEnabled(false);
            return;
        }

        if (this.getConfig().getBoolean("use-mysql")) {
            this.log.info("[" + pdfFile.getName() + "] Using MySQL for database!");
            initMySQL();
        } else if (!this.getConfig().getBoolean("use-mysql")) {
            this.log.info("[" + pdfFile.getName() + "] Using SQLite for database!");
            initSQLite();
        }
        
        getServer().getPluginManager().registerEvents(uTestListener, this);

        uTestExecutor = new uTestCommandHandler(this);
        uTestQuestioner = new uTestQuestioner(this);
        getCommand("ut").setExecutor(uTestExecutor);
        getCommand("utest").setExecutor(uTestExecutor);
        getCommand("answer").setExecutor(uTestQuestioner);

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

        int numberOfQuestions = this.getConfig().getList("quiz-questions").size();
        this.log.info("Number of questions: " + numberOfQuestions);
        String appsTable = "CREATE TABLE IF NOT EXISTS `" + database + "`.`applications` (`id` INT NOT NULL AUTO_INCREMENT,`applicationdate` DATETIME ,`applicationtime` TIME ,`applicant` VARCHAR(30) NOT NULL ,`TimePlayed` VARCHAR(30) NOT NULL ,`bannedcheck` INT ,`singleplayercheck` INT ,`multiplayercheck` INT ,`referrer` VARCHAR(50) ,`message` TEXT NOT NULL ,PRIMARY KEY (`id`) )";
        String quizTable = "CREATE  TABLE IF NOT EXISTS `" + database + "`.`quizzes` (`id` INT NOT NULL AUTO_INCREMENT ,`testdate` DATE NOT NULL ,`testtime` TIME NOT NULL ,`applicant` VARCHAR(30) NOT NULL ,PRIMARY KEY (`id`) );";
        String trackerTable = "CREATE  TABLE IF NOT EXISTS `" + database + "`.`tracker` (`id` INT NOT NULL AUTO_INCREMENT ,`applicant` VARCHAR(45) NOT NULL ,`status` VARCHAR(45) NOT NULL ,`quizattempt` INT NULL DEFAULT 0 ,PRIMARY KEY (`id`) )";
        String appArchiveTable = "CREATE TABLE IF NOT EXISTS `" + database + "`.`applications` (`id` INT NOT NULL AUTO_INCREMENT,`applicationdate` DATETIME ,`applicationtime` TIME ,`applicant` VARCHAR(30) NOT NULL ,`TimePlayed` VARCHAR(30) NOT NULL ,`bannedcheck` INT ,`singleplayercheck` INT ,`multiplayercheck` INT ,`referrer` VARCHAR(50) ,`message` TEXT NOT NULL ,PRIMARY KEY (`id`) )";
        dbManageMySQL.createTable(appsTable);
        dbManageMySQL.createTable(quizTable);
        dbManageMySQL.createTable(trackerTable);
        dbManageMySQL.createTable(appArchiveTable);

        int questionCount = 1;

        while (questionCount <= numberOfQuestions) {

            if (questionCount == 1) {
                String addQuestionColumn = "ALTER TABLE `" + database + "`.`quizzes` ADD COLUMN `question1answer` TEXT NULL  AFTER `attempt`";
                dbManageMySQL.query(addQuestionColumn);
                this.log.info("Question Column 1 Added!");
                questionCount++;
            } else {
                String addQuestionColumn = "ALTER TABLE `" + database + "`.`quizzes` ADD COLUMN `question" + questionCount + "answer` TEXT NULL  AFTER `question" + (questionCount - 1) + "answer`";
                dbManageMySQL.query(addQuestionColumn);
                this.log.info("Question Column " + questionCount + " Added!");
                questionCount++;
            }
        }

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
    
    private boolean setupPermissions() {
        
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        
        if (permissionProvider !=null) {
            permission = permissionProvider.getProvider();
        }
        
        return (permission !=null);
    }
    
    public Permission getPerms() {
        return this.permission;
    }
}
