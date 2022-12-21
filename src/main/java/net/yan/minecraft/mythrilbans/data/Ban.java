package net.yan.minecraft.mythrilbans.data;

import java.util.UUID;

public record Ban(

        UUID uuid,
        UUID player,
        UUID staff,
        String reason,
        long registeredAt,
        long duration

) { }
