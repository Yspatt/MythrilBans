package net.yan.minecraft.mythrilbans;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zaxxer.hikari.HikariDataSource;
import net.yan.minecraft.mythrilbans.commands.BanCommand;
import net.yan.minecraft.mythrilbans.commands.HistoryCommand;
import net.yan.minecraft.mythrilbans.commands.UnbanCommand;
import net.yan.minecraft.mythrilbans.controllers.BanController;
import net.yan.minecraft.mythrilbans.controllers.HistoryController;
import net.yan.minecraft.mythrilbans.controllers.MessageController;
import net.yan.minecraft.mythrilbans.controllers.impl.BanControllerImpl;
import net.yan.minecraft.mythrilbans.controllers.impl.HistoryControllerImpl;
import net.yan.minecraft.mythrilbans.core.Plugin;
import net.yan.minecraft.mythrilbans.listeners.PlayerBanEvents;
import net.yan.minecraft.mythrilbans.services.BanService;
import net.yan.minecraft.mythrilbans.services.HistoryService;
import net.yan.minecraft.mythrilbans.services.impl.BanServiceImpl;
import net.yan.minecraft.mythrilbans.services.impl.HistoryServiceImpl;
import net.yan.minecraft.mythrilbans.util.ConfigurationHelper;

import java.util.concurrent.ForkJoinPool;

public final class MythrilBans extends Plugin {

    private final ForkJoinPool executor = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
    private HikariDataSource hikariDataSource;
    private MessageController messageController;
    private BanController banController;
    private HistoryController historyController;
    private final Gson gson = new GsonBuilder().create();
    private ConfigurationHelper configuration;

    @Override
    public void load() {
        this.configuration = new ConfigurationHelper(this, "config.yml");

        this.messageController = new MessageController();
        this.hikariDataSource = getDataSourceFromConfig();

        this.provideService(BanService.class, new BanServiceImpl(this));
        this.provideService(HistoryService.class, new HistoryServiceImpl(this));
    }

    @Override
    public void enable() {

        this.banController = new BanControllerImpl();
        this.historyController = new HistoryControllerImpl();

        this.messageController.build(getInstance());

        this.banController.constructor(this).thenAcceptAsync(unused -> {
            this.getLog().info("Successfully loaded all bans.");
            this.historyController.constructor(this).thenAcceptAsync(unused1 -> {
                this.getLog().info("Successfully loaded all histories.");

                // Plugin methods
                register(this, new PlayerBanEvents(this));
                this.getCommand("ban").setExecutor(new BanCommand(this));
                this.getCommand("unban").setExecutor(new UnbanCommand(this));
                this.getCommand("history").setExecutor(new HistoryCommand(this));

            }, this.getExecutor());
        }, this.getExecutor());

    }

    @Override
    public void disable() {
        this.banController.destructor(this);
        this.historyController.destructor(this);
    }

    /* Services */
    public BanService getBanService() {
        return this.getService(BanService.class);
    }

    /* Controllers */
    public BanController getBanController() {
        return this.banController;
    }
    public HistoryController getHistoryController() { return historyController; }

    public HistoryService getHistoryService() {
        return this.getService(HistoryService.class);
    }
    public HikariDataSource getHikariDataSource() {
        return hikariDataSource;
    }
    public ConfigurationHelper getConfiguration() {
        return configuration;
    }
    public MessageController getMessageController() {
        return messageController;
    }
    public static MythrilBans getInstance() {
        return getPlugin(MythrilBans.class);
    }
    public ForkJoinPool getExecutor() {
        return executor;
    }
    public Gson getGson() { return gson; }
}
