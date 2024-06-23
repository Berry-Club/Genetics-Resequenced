package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.genes
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.item.components.BooleanItemComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.GenesItemComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.SpecificEntityItemComponent
import dev.aaronhowser.mods.geneticsresequenced.item.components.SpecificEntityItemComponent.Companion.hasEntity
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.util.FakePlayer

class SyringeItem : Item(
    Properties()
        .stacksTo(1)
) {

    companion object {

        fun Item.isSyringe(): Boolean {
            return this == ModItems.SYRINGE.get() || this == ModItems.METAL_SYRINGE.get()
        }

        fun ItemStack.isSyringe(): Boolean {
            return this.item.isSyringe()
        }

        fun isBeingUsed(syringeStack: ItemStack, entity: LivingEntity?): Boolean {
            if (entity == null) return false

            return entity.useItem == syringeStack
        }

        fun setEntity(pStack: ItemStack, entity: LivingEntity?) {
            if (entity == null) {
                pStack.remove(SpecificEntityItemComponent.component)
                return
            }

            val newComponent = SpecificEntityItemComponent(entity.uuid, entity.name.string)
            pStack.set(SpecificEntityItemComponent.component, newComponent)
        }

        fun injectEntity(syringeStack: ItemStack, entity: LivingEntity) {
            val syringeEntityUuid = syringeStack.get(SpecificEntityItemComponent.component)?.entityUuid ?: return
            if (entity.uuid != syringeEntityUuid) return

            addGenes(entity, getGenes(syringeStack))
            removeGenes(entity, getAntigenes(syringeStack))

            clearGenes(syringeStack)
            setEntity(syringeStack, null)
        }

        private fun removeGenes(entity: LivingEntity, syringeAntigenes: Set<Gene>) {

            val entityGenesBefore = entity.genes

            for (antigene in syringeAntigenes) {
                entity.removeGene(antigene)
            }

            val entityGenesAfter = entity.genes
            val genesRemoved = entityGenesBefore - entityGenesAfter
            val genesNotRemoved = syringeAntigenes - genesRemoved

            if (entity.level().isClientSide) {

                for (removedGene in genesRemoved) {
                    entity.sendSystemMessage(
                        Component.translatable(
                            ModLanguageProvider.Messages.SYRINGE_REMOVE_GENES_SUCCESS,
                            removedGene.nameComponent
                        )
                    )
                }

                for (notRemovedGene in genesNotRemoved) {
                    entity.sendSystemMessage(
                        Component.translatable(
                            ModLanguageProvider.Messages.SYRINGE_REMOVE_GENES_FAIL,
                            notRemovedGene.nameComponent
                        )
                    )
                }
            }

        }

        private fun addGenes(entity: LivingEntity, syringeGenes: Set<Gene>) {

            val entityGenesBefore = entity.genes

            val genesToAdd = if (entity is Player) {
                syringeGenes
            } else {
                syringeGenes
            }

            for (gene in genesToAdd) {
                entity.addGene(gene)
            }

            val entityGenesAfter = entity.genes
            val genesAdded = entityGenesAfter - entityGenesBefore
            val genesNotAdded = genesToAdd - genesAdded

            if (entity.level().isClientSide) {

                for (addedGene in genesAdded) {
                    entity.sendSystemMessage(
                        Component.translatable(
                            ModLanguageProvider.Messages.SYRINGE_INJECTED,
                            addedGene.nameComponent
                        )
                    )
                }

                for (notAddedGene in genesNotAdded) {
                    entity.sendSystemMessage(
                        Component.translatable(
                            ModLanguageProvider.Messages.SYRINGE_FAILED,
                            notAddedGene.nameComponent
                        )
                    )
                }
            }

        }

        fun hasBlood(syringeStack: ItemStack): Boolean {
            return syringeStack.hasEntity()
        }

        fun getGenes(syringeStack: ItemStack): Set<Gene> {
            return syringeStack.get(GenesItemComponent.genesComponent)?.genes ?: emptySet()
        }

        fun canAddGene(syringeStack: ItemStack, gene: Gene): Boolean {
            return hasBlood(syringeStack) && !getGenes(syringeStack).contains(gene)
        }

        fun addGene(syringeStack: ItemStack, gene: Gene): Boolean {
            if (!canAddGene(syringeStack, gene)) return false

            val currentGenes = getGenes(syringeStack)
            val newGenes = currentGenes + gene
            val newComponent = GenesItemComponent(newGenes)

            syringeStack.set(GenesItemComponent.genesComponent, newComponent)

            return true
        }

        private fun clearGenes(syringeStack: ItemStack) {
            syringeStack.remove(GenesItemComponent.genesComponent)
        }

        fun isContaminated(syringeStack: ItemStack): Boolean {
            return syringeStack.get(BooleanItemComponent.isContaminatedComponent)?.value ?: false
        }

        fun setContaminated(syringeStack: ItemStack, value: Boolean) {
            syringeStack.set(BooleanItemComponent.isContaminatedComponent, BooleanItemComponent(value))
        }

        fun getAntigenes(syringeStack: ItemStack): Set<Gene> {
            return syringeStack.get(GenesItemComponent.antigenesComponent)?.genes ?: emptySet()
        }

        fun canAddAntigene(syringeStack: ItemStack, gene: Gene): Boolean {
            return hasBlood(syringeStack)
                    && !getAntigenes(syringeStack).contains(gene)
                    && !getGenes(syringeStack).contains(gene)
        }

        fun addAntigene(syringeStack: ItemStack, gene: Gene): Boolean {
            if (!canAddAntigene(syringeStack, gene)) return false

            val currentAntigenes = getAntigenes(syringeStack)
            val newGenes = currentAntigenes + gene
            val newComponent = GenesItemComponent(newGenes)

            syringeStack.set(GenesItemComponent.antigenesComponent, newComponent)

            return true
        }

        //TODO: Add damage sources for on use and pickup

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

        if (pLivingEntity !is Player || pTimeCharged < 1) return
        if (pLivingEntity is FakePlayer) return

        if (isContaminated(pStack)) {
            if (!pLevel.isClientSide) {
                pLivingEntity.sendSystemMessage(
                    Component.translatable(ModLanguageProvider.Messages.SYRINGE_CONTAMINATED)
                )
            }
            return
        }

        if (hasBlood(pStack)) {
            injectEntity(pStack, pLivingEntity)
        } else {
            setEntity(pStack, pLivingEntity)
        }

    }

    override fun getName(pStack: ItemStack): Component {
        return if (hasBlood(pStack)) {
            Component.translatable(ModLanguageProvider.Items.SYRINGE_FULL)
        } else {
            Component.translatable(ModLanguageProvider.Items.SYRINGE_EMPTY)
        }
    }

}