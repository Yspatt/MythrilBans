package net.yan.minecraft.mythrilbans.services;

import net.yan.minecraft.mythrilbans.data.Ban;
import net.yan.minecraft.mythrilbans.data.History;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface HistoryService extends SQLService {

    CompletableFuture<List<History>> load();

    void save(History... histories);

    void delete(History history);

}
