package dev.aaronhowser.mods.geneticsresequenced.advancements

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.advancements.critereon.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import java.util.function.Predicate

class DecryptDnaTrigger : SimpleCriterionTrigger<DecryptDnaTrigger.Instance>() {

    companion object {
        private val id = OtherUtil.modResource("decrypt_dna")
    }

    override fun getId(): ResourceLocation = Companion.id

    override fun createInstance(
        json: JsonObject,
        entityPredicate: EntityPredicate.Composite,
        context: DeserializationContext
    ): Instance {
        return Instance(entityPredicate)
    }

    override fun trigger(pPlayer: ServerPlayer, pTestTrigger: Predicate<Instance>) {
        super.trigger(pPlayer) { instance -> pPlayer.inventory.items.any { instance.test(it) } }
    }

    class Instance(predicate: EntityPredicate.Composite) : AbstractCriterionTriggerInstance(
        Companion.id,
        predicate
    ) {
        override fun getCriterion(): ResourceLocation = Companion.id

        override fun serializeToJson(context: SerializationContext): JsonObject {
            val json = super.serializeToJson(context)
            return json
        }

        fun test(itemStack: ItemStack): Boolean = itemStack.getGene() != null

    }
}
