package ru.buseso.dreamtime.dttickets;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import ru.buseso.dreamtime.dttickets.utils.MySQLConnect;

import java.io.File;
import java.io.IOException;

public final class DTTickets extends Plugin {

    public static String Prefix = "§eТикеты §8| §7";

    public static MySQLConnect mysql;

    @Override
    public void onEnable() {
        Config();
        MySQL();
        Commands();
    }


    private void Commands() {
        getProxy().getPluginManager().registerCommand(this, new TicketCMD("ticket"));
    }


    private void Config() {
        File ordner = new File(getDataFolder().getPath());
        if (!ordner.exists()) {
            ordner.mkdir();
        }
        File mysql = new File(getDataFolder().getPath(), "mysql.yml");
        if (!mysql.exists()) {
            try {
                mysql.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Configuration cfg = YamlConfiguration.getProvider(YamlConfiguration.class).load(mysql);
                cfg.set("HOST", "localhost");
                cfg.set("DATABASE", "dreamtime_global");
                cfg.set("USER", "dreamtime");
                cfg.set("PASSWORD", "dreamtimepass");
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, mysql);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void MySQL() {
        try {
            File file = new File(getDataFolder().getPath(), "mysql.yml");
            Configuration cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            MySQLConnect.HOST = cfg.getString("HOST");
            MySQLConnect.DATABASE = cfg.getString("DATABASE");
            MySQLConnect.USER = cfg.getString("USER");
            MySQLConnect.PASSWORD = cfg.getString("PASSWORD");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mysql = new MySQLConnect(MySQLConnect.HOST, MySQLConnect.DATABASE, MySQLConnect.USER, MySQLConnect.PASSWORD);
        mysql.update("CREATE TABLE IF NOT EXISTS accounts(ID int(11) UNIQUE AUTO_INCREMENT, UUID varchar(64), USERNAME varchar(255), EMAIL varchar(255), PASSWORD varchar(255), LASTLOGIN varchar(255), FIRSTLOGIN varchar(255), ACCOUNTRANK int(11));");
    }
}
