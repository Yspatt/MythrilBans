package net.yan.minecraft.mythrilbans.services;

public interface SQLService {

    String BANS_TABLE_NAME = "mythrilbans_bans";
    String HISTORY_TABLE_NAME = "mythrilbans_histories";

    interface BanQuery {
        String INSERT_OR_UPDATE =  "INSERT INTO\n" +
                "  `" + BANS_TABLE_NAME + "` (id, data)\n" +
                "VALUES\n" +
                "  (?, ?) ON DUPLICATE KEY\n" +
                "UPDATE\n" +
                "  data = ?";

        String SELECT = "SELECT * FROM `" + BANS_TABLE_NAME + "`;";

        String DELETE = "DELETE FROM `" + BANS_TABLE_NAME + "` WHERE `id` = ?";
    }

    interface HistoryQuery {
        String INSERT_OR_UPDATE =  "INSERT INTO\n" +
                "  `" + HISTORY_TABLE_NAME + "` (id, data)\n" +
                "VALUES\n" +
                "  (?, ?) ON DUPLICATE KEY\n" +
                "UPDATE\n" +
                "  data = ?";

        String SELECT = "SELECT * FROM `" + HISTORY_TABLE_NAME + "`;";

        String DELETE = "DELETE FROM `" + HISTORY_TABLE_NAME + "` WHERE `id` = ?";
    }

    String BAN_TABLE = "CREATE TABLE IF NOT EXISTS `" + BANS_TABLE_NAME + "` (\n" +
            "  `id` VARCHAR(36) NOT NULL,\n" +
            "  `data` TEXT NOT NULL,\n" +
            "  PRIMARY KEY(`id`)\n" +
            ")\n";

    String HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS `" + HISTORY_TABLE_NAME + "` (\n" +
            "  `id` VARCHAR(36) NOT NULL,\n" +
            "  `data` TEXT NOT NULL,\n" +
            "  PRIMARY KEY(`id`)\n" +
            ")\n";
}
