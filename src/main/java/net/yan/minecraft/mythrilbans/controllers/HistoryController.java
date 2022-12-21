package net.yan.minecraft.mythrilbans.controllers;

import net.yan.minecraft.mythrilbans.MythrilBans;
import net.yan.minecraft.mythrilbans.data.Ban;
import net.yan.minecraft.mythrilbans.data.History;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface HistoryController {

    CompletableFuture<Void> constructor(MythrilBans mythrilBans);

    void destructor(MythrilBans mythrilBans);

    Optional<History> find(UUID uniqueId);

    List<History> getBans(UUID uuid);

    void create(History history);

    void delete(History history);

    void forEach(Consumer<History> historyConsumer);

    Stream<History> stream();

}