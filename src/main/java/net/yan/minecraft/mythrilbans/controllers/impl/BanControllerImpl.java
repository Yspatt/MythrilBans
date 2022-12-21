package net.yan.minecraft.mythrilbans.controllers.impl;

import net.yan.minecraft.mythrilbans.MythrilBans;
import net.yan.minecraft.mythrilbans.controllers.BanController;
import net.yan.minecraft.mythrilbans.data.Ban;
import net.yan.minecraft.mythrilbans.services.BanService;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class BanControllerImpl implements BanController {
    private HashSet<Ban> bans = new HashSet<>();

    private BanService service;

    @Override
    public CompletableFuture<Void> constructor(MythrilBans mythrilBans) {
        this.service = mythrilBans.getBanService();

        mythrilBans.getLog().info("Loading all bans...");
        return this.service.load().thenAcceptAsync(find -> this.bans.addAll(find), mythrilBans.getExecutor());
    }

    @Override
    public void destructor(MythrilBans mythrilBans) {
        Ban[] toArray = new Ban[this.bans.size()];
        toArray = this.bans.toArray(toArray);

        this.service.save(toArray);
    }

    @Override
    public Optional<Ban> find(UUID uniqueId) {
        return this.stream().filter(data -> data.player().equals(uniqueId)).findFirst();
    }

    @Override
    public void create(Ban ban) {
        this.bans.add(ban);

        this.service.save(ban);
    }

    @Override
    public void delete(Ban ban) {
        this.bans.remove(ban);

        this.service.delete(ban);
    }

    @Override
    public void forEach(Consumer<Ban> banConsumer) {
        this.stream().forEach(banConsumer);
    }

    @Override
    public Stream<Ban> stream() {
        return this.bans.stream();
    }
}
