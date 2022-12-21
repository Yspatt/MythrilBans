package net.yan.minecraft.mythrilbans.services.impl;

import com.google.common.collect.Lists;
import net.yan.minecraft.mythrilbans.MythrilBans;
import net.yan.minecraft.mythrilbans.data.Ban;
import net.yan.minecraft.mythrilbans.services.BanService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BanServiceImpl implements BanService {
    private MythrilBans mythrilBans;

    public BanServiceImpl(MythrilBans mythrilBans) {
        this.mythrilBans = mythrilBans;

        try(Connection connection = this.mythrilBans.getHikariDataSource().getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(BAN_TABLE)) {
                preparedStatement.executeUpdate();

                connection.close();
                this.mythrilBans.getLog().info("Table {} successfully loaded", BANS_TABLE_NAME);
            }
        } catch(SQLException exception) {
            this.mythrilBans.getLog().info("Could not create table {}", BANS_TABLE_NAME);
        }
    }

    @Override
    public CompletableFuture<List<Ban>> load() {
        return CompletableFuture.supplyAsync(() -> {
            List<Ban> bans = Lists.newArrayList();

            try(Connection connection = this.mythrilBans.getHikariDataSource().getConnection()) {
                try(PreparedStatement preparedStatement = connection.prepareStatement(BanQuery.SELECT)) {
                    ResultSet resultSet = preparedStatement.executeQuery();

                    while(resultSet.next()) {
                        String data = resultSet.getString("data");

                        if(data != null) {
                            Ban ban = this.mythrilBans.getGson().fromJson(data, Ban.class);

                            if(ban != null) {
                                bans.add(ban);
                            }
                        }
                    }
                }
            }catch(SQLException exception) {
                this.mythrilBans.getLog().info("Could not load bans.");
            }

            return bans;
        });
    }

    @Override
    public void save(Ban... bans) {
        try(Connection connection = this.mythrilBans.getHikariDataSource().getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(BanQuery.INSERT_OR_UPDATE)) {
                for(Ban ban : bans) {

                    preparedStatement.setString(1, ban.uuid().toString());

                    String data = this.mythrilBans.getGson().toJson(ban, Ban.class);

                    preparedStatement.setString(2, data);
                    preparedStatement.setString(3, data);

                    preparedStatement.addBatch();
                    preparedStatement.clearParameters();
                }

                preparedStatement.executeBatch();
                connection.close();
            }
        }catch(SQLException exception) {
            this.mythrilBans.getLog().info("Could not save {} bans", bans.length);
        }
    }

    @Override
    public void delete(Ban ban) {
        try(Connection connection = this.mythrilBans.getHikariDataSource().getConnection()) {
            try(PreparedStatement preparedStatement = connection.prepareStatement(BanQuery.DELETE)) {
                preparedStatement.setString(1, ban.uuid().toString());

                preparedStatement.executeUpdate();
                connection.close();
            }
        }catch(SQLException exception) {
            this.mythrilBans.getLog().info("Could not delete ban {}", ban.uuid().toString());
        }
    }
}
