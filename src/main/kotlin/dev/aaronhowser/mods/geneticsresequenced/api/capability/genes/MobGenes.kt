package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.tags.ITagManager
import java.util.*

class MobGenes(
    val entity: TagKey<LivingEntity>,
    val genes: Set<Gene>
) : IForgeRegistry<MobGenes> {

    init {
        GeneticsResequenced.LOGGER.info("Registered MobGenes for $entity with genes: $genes")
    }

    override fun iterator(): MutableIterator<MobGenes> {
        TODO("Not yet implemented")
    }

    override fun getRegistryKey(): ResourceKey<Registry<MobGenes>> {
        TODO("Not yet implemented")
    }

    override fun getRegistryName(): ResourceLocation {
        TODO("Not yet implemented")
    }

    override fun containsKey(key: ResourceLocation?): Boolean {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getValue(key: ResourceLocation?): MobGenes? {
        TODO("Not yet implemented")
    }

    override fun getDefaultKey(): ResourceLocation? {
        TODO("Not yet implemented")
    }

    override fun getKeys(): MutableSet<ResourceLocation> {
        TODO("Not yet implemented")
    }

    override fun getValues(): MutableCollection<MobGenes> {
        TODO("Not yet implemented")
    }

    override fun getEntries(): MutableSet<MutableMap.MutableEntry<ResourceKey<MobGenes>, MobGenes>> {
        TODO("Not yet implemented")
    }

    override fun getCodec(): Codec<MobGenes> {
        TODO("Not yet implemented")
    }

    override fun getHolder(location: ResourceLocation?): Optional<Holder<MobGenes>> {
        TODO("Not yet implemented")
    }

    override fun tags(): ITagManager<MobGenes>? {
        TODO("Not yet implemented")
    }

    override fun getDelegate(key: ResourceLocation?): Optional<Holder.Reference<MobGenes>> {
        TODO("Not yet implemented")
    }

    override fun getDelegateOrThrow(key: ResourceLocation?): Holder.Reference<MobGenes> {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> getSlaveMap(slaveMapName: ResourceLocation?, type: Class<T>?): T {
        TODO("Not yet implemented")
    }

    override fun getDelegateOrThrow(value: MobGenes?): Holder.Reference<MobGenes> {
        TODO("Not yet implemented")
    }

    override fun getDelegateOrThrow(rkey: ResourceKey<MobGenes>?): Holder.Reference<MobGenes> {
        TODO("Not yet implemented")
    }

    override fun getDelegate(value: MobGenes?): Optional<Holder.Reference<MobGenes>> {
        TODO("Not yet implemented")
    }

    override fun getDelegate(rkey: ResourceKey<MobGenes>?): Optional<Holder.Reference<MobGenes>> {
        TODO("Not yet implemented")
    }

    override fun getHolder(value: MobGenes?): Optional<Holder<MobGenes>> {
        TODO("Not yet implemented")
    }

    override fun getHolder(key: ResourceKey<MobGenes>?): Optional<Holder<MobGenes>> {
        TODO("Not yet implemented")
    }

    override fun getResourceKey(value: MobGenes?): Optional<ResourceKey<MobGenes>> {
        TODO("Not yet implemented")
    }

    override fun getKey(value: MobGenes?): ResourceLocation? {
        TODO("Not yet implemented")
    }

    override fun containsValue(value: MobGenes?): Boolean {
        TODO("Not yet implemented")
    }

    override fun register(key: ResourceLocation?, value: MobGenes?) {
        TODO("Not yet implemented")
    }

    override fun register(key: String?, value: MobGenes?) {
        TODO("Not yet implemented")
    }
}