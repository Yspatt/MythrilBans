package net.yan.minecraft.mythrilbans.controllers;

import net.yan.minecraft.mythrilbans.MythrilBans;
import net.yan.minecraft.mythrilbans.data.Ban;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface BanController {

    CompletableFuture<Void> constructor(MythrilBans mythrilBans);

    void destructor(MythrilBans mythrilBans);

    Optional<Ban> find(UUID uniqueId);

    void create(Ban ban);

    void delete(Ban ban);

    void forEach(Consumer<Ban> banConsumer);

    Stream<Ban> stream();

}