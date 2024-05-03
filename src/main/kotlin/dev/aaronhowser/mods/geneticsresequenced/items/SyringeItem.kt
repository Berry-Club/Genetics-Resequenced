package dev.aaronhowser.mods.geneticsresequenced.items

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.getUuidOrNull
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
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
import net.minecraftforge.common.util.FakePlayer
import java.util.*

object SyringeItem : Item(
    Properties()
        .tab(ModItems.MOD_TAB)
        .stacksTo(1)
) {

    // Regular item functions

    override fun getUseDuration(pStack: ItemStack): Int = 40

    override fun getUseAnimation(pStack: ItemStack): UseAnim = UseAnim.BOW

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        val itemStack = pPlayer.getItemInHand(pUsedHand)
        pPlayer.startUsingItem(pUsedHand)
        return InteractionResultHolder.consume(itemStack)
    }

    override fun onUsingTick(stack: ItemStack?, player: LivingEntity?, count: Int) {
        if (stack == null) return
        if (player == null) return

        if (count <= 1) {
            player.stopUsingItem()
            releaseUsing(stack, player.level, player, count)
        }
    }

    override fun releaseUsing(pStack: ItemStack, pLevel: Level, pLivingEntity: LivingEntity, pTimeLeft: Int) {
        if (pLivingEntity !is Player) return
        if (pTimeLeft > 1) return

        if (pLivingEntity is FakePlayer) return

        if (hasBlood(pStack)) {
            injectEntity(pStack, pLivingEntity)
        } else {
            setEntity(pStack, pLivingEntity)
            pLivingEntity.hurt(DamageSource.WITHER, 1f)

            val randomGene = Gene.getRegistry().random()
            if (canAddGene(pStack, randomGene)) {
                addGene(pStack, randomGene)
            }
        }

        pLivingEntity.cooldowns.addCooldown(this, 10)
    }

    override fun getName(pStack: ItemStack): Component {
        return if (hasBlood(pStack)) {
            Component.translatable("item.geneticsresequenced.syringe.full")
        } else {
            super.getName(pStack)
        }
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pLevel: Level?,
        pTooltipComponents: MutableList<Component>,
        pIsAdvanced: TooltipFlag
    ) {

        if (isContaminated(pStack)) {
            pTooltipComponents.add(
                Component.translatable("tooltip.geneticsresequenced.syringe.contaminated")
                    .withColor(ChatFormatting.DARK_GREEN)
            )
        }

        for (gene in getGenes(pStack)) {
            pTooltipComponents.add(gene.nameComponent)
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced)
    }

    // Animation / model functions

    fun isBeingUsed(syringeStack: ItemStack, entity: LivingEntity?): Boolean {
        if (entity == null) return false

        return entity.useItem == syringeStack
    }

    // Entity functions

    private const val ENTITY_UUID_NBT_KEY = "entity"
    private fun setEntity(syringeStack: ItemStack, entity: LivingEntity) {
        if (!syringeStack.`is`(SyringeItem)) return

        val tag = syringeStack.getOrCreateTag()

        tag.putUUID(ENTITY_UUID_NBT_KEY, entity.uuid)
        setContaminated(syringeStack, true)
    }

    private fun injectEntity(syringeStack: ItemStack, entity: LivingEntity) {
        val entityDna = getEntity(syringeStack) ?: return
        if (entity.uuid != entityDna) return

        if (isContaminated(syringeStack)) {
            entity.addEffect(MobEffectInstance(MobEffects.CONFUSION, 200))
            entity.addEffect(MobEffectInstance(MobEffects.WITHER, 200))
            setContaminated(syringeStack, false)
        }

        val genes = getGenes(syringeStack)
        val entityGenes = entity.getGenes() ?: return

        for (gene in genes) {
            val success = entityGenes.addGene(gene)

            if (!entity.level.isClientSide) {
                val translateKey = if (success) {
                    "message.geneticsresequenced.syringe.injected"
                } else {
                    "message.geneticsresequenced.syringe.failed"
                }
                entity.sendSystemMessage(
                    Component.translatable(
                        translateKey,
                        gene.nameComponent
                    )
                )
            }
        }

        syringeStack.getOrCreateTag().remove(ENTITY_UUID_NBT_KEY)
        clearGenes(syringeStack)
    }

    private fun getEntity(syringeStack: ItemStack): UUID? {
        if (!syringeStack.`is`(SyringeItem)) return null

        return syringeStack.getOrCreateTag().getUuidOrNull(ENTITY_UUID_NBT_KEY)
    }

    fun hasBlood(syringeStack: ItemStack): Boolean {
        return getEntity(syringeStack) != null
    }

    private const val CONTAMINATED_NBT_KEY = "contaminated"
    private fun isContaminated(syringeStack: ItemStack): Boolean {
        return syringeStack.getOrCreateTag().getBoolean(CONTAMINATED_NBT_KEY)
    }

    private fun setContaminated(syringeStack: ItemStack, contaminated: Boolean) {
        if (contaminated) {
            syringeStack.getOrCreateTag().putBoolean(CONTAMINATED_NBT_KEY, true)
        } else {
            syringeStack.getOrCreateTag().remove(CONTAMINATED_NBT_KEY)
        }
    }

    // Gene functions

    private const val GENE_LIST_NBT_KEY = "genes"

    private fun getGenes(syringeStack: ItemStack): List<Gene> {
        return syringeStack.getOrCreateTag()
            .getList(GENE_LIST_NBT_KEY, Tag.TAG_STRING.toInt())
            .mapNotNull { Gene.fromId(it.asString) }
    }

    private fun canAddGene(syringeStack: ItemStack, gene: Gene): Boolean {
        return hasBlood(syringeStack) && !getGenes(syringeStack).contains(gene)
    }

    private fun addGene(syringeStack: ItemStack, vararg genes: Gene): Boolean {

        if (!hasBlood(syringeStack)) return false

        val geneList = getGenes(syringeStack).toMutableList()
        geneList.addAll(genes.filter { canAddGene(syringeStack, it) })

        GeneticsResequenced.LOGGER.debug(
            "Could not add these genes to the syringe: ${genes.filterNot { canAddGene(syringeStack, it) }}"
        )

        val stringTags: List<StringTag> = geneList.map { StringTag.valueOf(it.id.toString()) }
        val listTag = syringeStack
            .getOrCreateTag()
            .getList(GENE_LIST_NBT_KEY, Tag.TAG_STRING.toInt())

        listTag.clear()
        listTag.addAll(stringTags)

        syringeStack.getOrCreateTag().put(GENE_LIST_NBT_KEY, listTag)
        return true
    }

    private fun clearGenes(syringeStack: ItemStack) {
        syringeStack.getOrCreateTag().remove(GENE_LIST_NBT_KEY)
    }

}