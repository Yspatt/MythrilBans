package net.yan.minecraft.mythrilbans.services.impl;

import com.google.common.collect.Lists;
import net.yan.minecraft.mythrilbans.MythrilBans;
import net.yan.minecraft.mythrilbans.data.Ban;
import net.yan.minecraft.mythrilbans.data.History;
import net.yan.minecraft.mythrilbans.services.HistoryService;
import net.yan.minecraft.mythrilbans.services.SQLService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HistoryServiceImpl implements HistoryService {
    private MythrilBans mythrilBans;

    public HistoryServiceImpl(MythrilBans mythrilBans) {
        this.mythrilBans = mythrilBans;

        try(Connection connection = this.mythrilBans.getHikariDataSource().getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(HISTORY_TABLE)) {
                preparedStatement.executeUpdate();

                connection.close();
                this.mythrilBans.getLog().info("Table {} successfully loaded", HISTORY_TABLE_NAME);
            }
        } catch(SQLException exception) {
            this.mythrilBans.getLog().info("Could not create table {}", HISTORY_TABLE_NAME);
        }
    }

    @Override
    public CompletableFuture<List<History>> load() {
        return CompletableFuture.supplyAsync(() -> {
            List<History> histories = Lists.newArrayList();

            try(Connection connection = this.mythrilBans.getHikariDataSource().getConnection()) {
                try(PreparedStatement preparedStatement = connection.prepareStatement(SQLService.HistoryQuery.SELECT)) {
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while(resultSet.next()) {
                        String data = resultSet.getString("data");

                        if(data != null) {
                            History history = this.mythrilBans.getGson().fromJson(data, History.class);

                            if(history != null) {
                                histories.add(history);
                            }
                        }
                    }
                }
            }catch(SQLException exception) {
                this.mythrilBans.getLog().info("Could not load histories.");
            }

            return histories;
        });
    }

    @Override
    public void save(History... histories) {
        try(Connection connection = this.mythrilBans.getHikariDataSource().getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(SQLService.HistoryQuery.INSERT_OR_UPDATE)) {
                for(History history : histories) {

                    preparedStatement.setString(1, history.uuid().toString());

                    String data = this.mythrilBans.getGson().toJson(history, History.class);

                    preparedStatement.setString(2, data);
                    preparedStatement.setString(3, data);

                    preparedStatement.addBatch();
                    preparedStatement.clearParameters();
                }

                preparedStatement.executeBatch();
                connection.close();
            }
        }catch(SQLException exception) {
            this.mythrilBans.getLog().info("Could not save {} histories", histories.length);
        }
    }

    @Override
    public void delete(History history) {
        try(Connection connection = this.mythrilBans.getHikariDataSource().getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(SQLService.HistoryQuery.DELETE)) {
                preparedStatement.setString(1, history.uuid().toString());

                preparedStatement.executeUpdate();
                connection.close();
            }
        }catch(SQLException exception) {
            this.mythrilBans.getLog().info("Could not delete history {}", history.uuid().toString());
        }
    }
}
