package com.syszee.workshopcore.common.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public record EntityPopup(Entity entity, Component title, Component description) {}
