package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.geneHolders
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModDamageTypeTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.components.SpecificEntityItemComponent.Companion.getEntityName
import dev.aaronhowser.mods.geneticsresequenced.item.components.SpecificEntityItemComponent.Companion.getEntityUuid
import dev.aaronhowser.mods.geneticsresequenced.item.components.SpecificEntityItemComponent.Companion.hasEntity
import dev.aaronhowser.mods.geneticsresequenced.item.components.SpecificEntityItemComponent.Companion.setEntity
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.util.FakePlayer
import java.util.*

open class SyringeItem : Item(
    Properties()
        .stacksTo(1)
) {

    companion object {

        fun ItemStack.isSyringe(): Boolean {
            return this.`is`(ModItemTagsProvider.SYRINGES)
        }

        fun isBeingUsed(syringeStack: ItemStack, entity: LivingEntity?): Boolean {
            if (entity == null) return false

            return entity.useItem == syringeStack
        }

        fun setEntity(pStack: ItemStack, entity: LivingEntity?, setContaminated: Boolean = true) {
            if (entity == null) {
                pStack.remove(ModDataComponents.SPECIFIC_ENTITY_COMPONENT)
                return
            }

            pStack.setEntity(entity)

            if (setContaminated) {
                setContaminated(pStack, true)
            }
        }

        private fun getEntityUuid(syringeStack: ItemStack): UUID? {
            return syringeStack.getEntityUuid()
        }

        fun getEntityName(syringeStack: ItemStack): Component? {
            return syringeStack.getEntityName()
        }

        fun injectEntity(syringeStack: ItemStack, entity: LivingEntity) {
            val syringeEntityUuid = getEntityUuid(syringeStack) ?: return
            if (entity.uuid != syringeEntityUuid) return

            val genesToAdd = if (entity is Player) {
                getGenes(syringeStack)
            } else {
                getGenes(syringeStack).filter { it.value().allowsMobs }.toSet()
            }

            val genesToRemove = getAntigenes(syringeStack)

            addGenes(entity, genesToAdd)
            removeGenes(entity, genesToRemove)

            clearGenes(syringeStack)
            setEntity(syringeStack, null)
        }

        private fun removeGenes(entity: LivingEntity, syringeAntigenes: Set<Holder<Gene>>) {

            val entityGenesBefore = entity.geneHolders

            for (antigene in syringeAntigenes) {
                entity.removeGene(antigene)
            }

            val entityGenesAfter = entity.geneHolders
            val genesRemoved = entityGenesBefore - entityGenesAfter
            val genesNotRemoved = syringeAntigenes - genesRemoved

            if (entity.level().isClientSide) {

                for (removedGeneHolder in genesRemoved) {
                    entity.sendSystemMessage(
                        ModLanguageProvider.Messages.SYRINGE_REMOVE_GENES_SUCCESS.toComponent(
                            Gene.getNameComponent(removedGeneHolder)
                        )
                    )
                }

                for (notRemovedGeneHolder in genesNotRemoved) {
                    entity.sendSystemMessage(
                        ModLanguageProvider.Messages.SYRINGE_REMOVE_GENES_FAIL.toComponent(
                            Gene.getNameComponent(notRemovedGeneHolder)
                        )
                    )
                }
            }

        }

        private fun addGenes(entity: LivingEntity, syringeGenes: Set<Holder<Gene>>) {

            val entityGenesBefore = entity.geneHolders

            val genesToAdd = if (entity is Player) {
                syringeGenes
            } else {
                syringeGenes
            }

            for (gene in genesToAdd) {
                entity.addGene(gene)
            }

            val entityGenesAfter = entity.geneHolders
            val genesAdded = entityGenesAfter - entityGenesBefore
            val genesNotAdded = genesToAdd - genesAdded

            if (entity.level().isClientSide) {

                for (addedGeneHolder in genesAdded) {
                    entity.sendSystemMessage(
                        ModLanguageProvider.Messages.SYRINGE_INJECTED.toComponent(
                            Gene.getNameComponent(addedGeneHolder)
                        )
                    )
                }

                for (notAddedGeneHolder in genesNotAdded) {
                    entity.sendSystemMessage(
                        ModLanguageProvider.Messages.SYRINGE_FAILED.toComponent(
                            Gene.getNameComponent(notAddedGeneHolder)
                        )
                    )
                }
            }

        }

        fun hasBlood(syringeStack: ItemStack): Boolean {
            return syringeStack.hasEntity()
        }

        fun getGenes(syringeStack: ItemStack): Set<Holder<Gene>> {
            return syringeStack.get(ModDataComponents.GENES_COMPONENT)?.toSet() ?: emptySet()
        }

        fun getGeneRks(syringeStack: ItemStack): Set<ResourceKey<Gene>> {
            return getGenes(syringeStack).mapNotNull { it.key }.toSet()
        }

        fun canAddGene(syringeStack: ItemStack, gene: Holder<Gene>): Boolean {
            return hasBlood(syringeStack) && gene !in getGenes(syringeStack)
        }

        fun addGene(syringeStack: ItemStack, gene: Holder<Gene>): Boolean {
            if (!canAddGene(syringeStack, gene)) return false

            val currentGenes = getGenes(syringeStack)
            val newGenes = currentGenes + gene
            val newHolderSet = HolderSet.direct(newGenes.toList())

            syringeStack.set(ModDataComponents.GENES_COMPONENT, newHolderSet)

            return true
        }

        private fun clearGenes(syringeStack: ItemStack) {
            syringeStack.remove(ModDataComponents.GENES_COMPONENT)
        }

        fun isContaminated(syringeStack: ItemStack): Boolean {
            return syringeStack.get(ModDataComponents.IS_CONTAMINATED_COMPONENT) ?: false
        }

        fun setContaminated(syringeStack: ItemStack, value: Boolean) {
            syringeStack.set(ModDataComponents.IS_CONTAMINATED_COMPONENT, value)
        }

        fun getAntigenes(syringeStack: ItemStack): Set<Holder<Gene>> {
            return syringeStack.get(ModDataComponents.ANTIGENES_COMPONENT)?.toSet() ?: emptySet()
        }

        fun canAddAntigene(syringeStack: ItemStack, gene: Holder<Gene>): Boolean {
            return hasBlood(syringeStack)
                    && gene !in getAntigenes(syringeStack)
                    && gene !in getGenes(syringeStack)
        }

        fun addAntigene(syringeStack: ItemStack, gene: Holder<Gene>): Boolean {
            if (!canAddAntigene(syringeStack, gene)) return false

            val currentAntigenes = getAntigenes(syringeStack)
            val newGenes = currentAntigenes + gene
            val holderSet = HolderSet.direct(newGenes.toList())

            syringeStack.set(ModDataComponents.ANTIGENES_COMPONENT, holderSet)

            return true
        }

        fun damageSourceStepOnSyringe(level: Level, thrower: LivingEntity?): DamageSource {
            return level.damageSources().source(ModDamageTypeTagsProvider.STEP_ON_SYRINGE, thrower)
        }

        fun damageSourceUseSyringe(level: Level, thrower: LivingEntity?): DamageSource {
            return level.damageSources().source(ModDamageTypeTagsProvider.USE_SYRINGE, thrower)
        }

    }

    override fun getUseDuration(pStack: ItemStack, pHolder: LivingEntity): Int = 40
    override fun getUseAnimation(pStack: ItemStack): UseAnim = UseAnim.BOW

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val realStack = pPlayer.getItemInHand(pUsedHand)
        pPlayer.startUsingItem(pUsedHand)
        return InteractionResultHolder.consume(realStack)
    }

    override fun onUseTick(pLevel: Level, pLivingEntity: LivingEntity, pStack: ItemStack, pRemainingUseDuration: Int) {

        if (pRemainingUseDuration <= 1) {
            pLivingEntity.stopUsingItem()
            releaseUsing(pStack, pLevel, pLivingEntity, pRemainingUseDuration)
        }

    }

    override fun releaseUsing(pStack: ItemStack, pLevel: Level, pLivingEntity: LivingEntity, pTimeCharged: Int) {

        if (pLivingEntity !is Player || pTimeCharged > 1) return
        if (pLivingEntity is FakePlayer) return

        if (isContaminated(pStack)) {
            if (!pLevel.isClientSide) {
                pLivingEntity.sendSystemMessage(
                    ModLanguageProvider.Messages.SYRINGE_CONTAMINATED.toComponent()
                )
            }
            return
        }

        if (hasBlood(pStack)) {
            injectEntity(pStack, pLivingEntity)
        } else {
            setEntity(pStack, pLivingEntity)
        }

        pLivingEntity.apply {
            hurt(damageSourceUseSyringe(pLevel, pLivingEntity), 1f)
            addEffect(MobEffectInstance(MobEffects.BLINDNESS, 20 * 3))

            cooldowns.addCooldown(ModItems.SYRINGE.get(), 10)
        }
    }

    override fun getName(pStack: ItemStack): Component {
        return if (hasBlood(pStack)) {
            ModLanguageProvider.Items.SYRINGE_FULL.toComponent()
        } else {
            ModLanguageProvider.Items.SYRINGE_EMPTY.toComponent()
        }
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pContext: TooltipContext,
        pTooltipComponents: MutableList<Component>,
        pTooltipFlag: TooltipFlag
    ) {

        val bloodOwner = getEntityName(pStack)
        if (hasBlood(pStack) && bloodOwner != null) {
            pTooltipComponents.add(
                ModLanguageProvider.Tooltips.SYRINGE_OWNER
                    .toComponent(bloodOwner)
                    .withStyle(ChatFormatting.GRAY)
            )
        }

        if (isContaminated(pStack)) {
            pTooltipComponents.add(
                ModLanguageProvider.Tooltips.SYRINGE_CONTAMINATED
                    .toComponent()
                    .withStyle(ChatFormatting.DARK_GREEN)
            )
        }

        val addingGenes = getGenes(pStack)
        if (addingGenes.isNotEmpty()) {
            pTooltipComponents.add(
                ModLanguageProvider.Tooltips.SYRINGE_ADDING_GENES
                    .toComponent()
                    .withStyle(ChatFormatting.GRAY)
            )

            for (geneHolder in addingGenes) {
                val nameComponent = Gene.getNameComponent(geneHolder)

                val component = Component
                    .literal("• ")
                    .withStyle {
                        it.withColor(nameComponent.style.color)
                    }.append(nameComponent)

                pTooltipComponents.add(component)
            }
        }

        val removingGenes = getAntigenes(pStack)
        if (removingGenes.isNotEmpty()) {
            pTooltipComponents.add(
                ModLanguageProvider.Tooltips.SYRINGE_REMOVING_GENES
                    .toComponent()
                    .withStyle(ChatFormatting.GRAY)
            )

            for (geneHolder in removingGenes) {
                val nameComponent = Gene.getNameComponent(geneHolder)

                val component = Component
                    .literal("• ")
                    .withStyle {
                        it.withColor(nameComponent.style.color)
                    }.append(nameComponent)

                pTooltipComponents.add(component)
            }
        }
    }

}