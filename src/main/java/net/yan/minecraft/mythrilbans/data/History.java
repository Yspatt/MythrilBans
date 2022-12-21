package net.yan.minecraft.mythrilbans.data;


import net.yan.minecraft.mythrilbans.data.enums.HistoryType;

import java.util.UUID;

public record History(

        UUID uuid,
        Ban ban,
        HistoryType type,
        long registeredAt

) { }
