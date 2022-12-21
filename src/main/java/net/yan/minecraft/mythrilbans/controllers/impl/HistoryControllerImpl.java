package net.yan.minecraft.mythrilbans.controllers.impl;

import net.yan.minecraft.mythrilbans.MythrilBans;
import net.yan.minecraft.mythrilbans.controllers.BanController;
import net.yan.minecraft.mythrilbans.controllers.HistoryController;
import net.yan.minecraft.mythrilbans.data.Ban;
import net.yan.minecraft.mythrilbans.data.History;
import net.yan.minecraft.mythrilbans.services.BanService;
import net.yan.minecraft.mythrilbans.services.HistoryService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HistoryControllerImpl implements HistoryController {
    private HashSet<History> histories = new HashSet<>();

    private HistoryService service;

    @Override
    public CompletableFuture<Void> constructor(MythrilBans mythrilBans) {
        this.service = mythrilBans.getHistoryService();

        mythrilBans.getLog().info("Loading all histories...");
        return this.service.load().thenAcceptAsync(find -> this.histories.addAll(find), mythrilBans.getExecutor());
    }

    @Override
    public void destructor(MythrilBans mythrilBans) {
        if (histories.size() == 0) {
            return;
        }
        History[] toArray = new History[this.histories.size()];
        toArray = this.histories.toArray(toArray);

        this.service.save(toArray);
    }

    @Override
    public Optional<History> find(UUID uniqueId) {
        return this.stream().filter(data -> data.uuid().equals(uniqueId)).findFirst();
    }

    @Override
    public List<History> getBans(UUID uuid) {
        return this.stream().filter(data -> data.ban().player().equals(uuid)).collect(Collectors.toList());
    }

    @Override
    public void create(History history) {
        this.histories.add(history);

        this.service.save(history);
    }

    @Override
    public void delete(History history) {
        this.histories.remove(history);

        this.service.delete(history);
    }

    @Override
    public void forEach(Consumer<History> historyConsumer) {
        this.stream().forEach(historyConsumer);
    }

    @Override
    public Stream<History> stream() {
        return this.histories.stream();
    }
}
