package net.yan.minecraft.mythrilbans.services;

import net.yan.minecraft.mythrilbans.data.Ban;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface BanService extends SQLService {

    CompletableFuture<List<Ban>> load();

    void save(Ban... bans);

    void delete(Ban ban);

}
