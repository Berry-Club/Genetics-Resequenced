package dev.aaronhowser.mods.geneticsresequenced.util

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import net.minecraft.ChatFormatting
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import java.util.*

@Suppress("MemberVisibilityCanBePrivate")
object OtherUtil {

    fun modResource(path: String): ResourceLocation = ResourceLocation(GeneticsResequenced.ID, path)

    fun CompoundTag.getUuidOrNull(key: String): UUID? {
        if (!this.hasUUID(key)) return null
        return this.getUUID(key)
    }

    fun MutableComponent.withColor(color: ChatFormatting): MutableComponent = this.withStyle { it.withColor(color) }

}