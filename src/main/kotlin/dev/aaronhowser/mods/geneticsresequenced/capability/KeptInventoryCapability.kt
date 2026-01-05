package dev.aaronhowser.mods.geneticsresequenced.capability

import com.mojang.serialization.Codec
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import kotlin.jvm.optionals.getOrNull

class KeptInventoryCapability() {

	constructor(stacks: List<ItemStack>) : this() {
		this.stacks = stacks
	}

	private var stacks: List<ItemStack> = emptyList()

	fun toTag(): CompoundTag {
		val tag = CompoundTag()

		val listTag = ListTag()
		for (stack in stacks) {
			val stackTag = stack.save(CompoundTag())
			listTag.add(stackTag)
		}
		tag.put(KEPT_INVENTORY_TAG, listTag)

		return tag
	}

	fun fromTag(tag: CompoundTag) {
		val listTag = tag.getList(KEPT_INVENTORY_TAG, Tag.TAG_COMPOUND.toInt())
		val newStacks = mutableListOf<ItemStack>()

		for (i in listTag.indices) {
			val stackTag = listTag.getCompound(i)
			val stack = ItemStack.of(stackTag)
			newStacks.add(stack)
		}

		stacks = newStacks
	}

	companion object {
		const val KEPT_INVENTORY_TAG = "KeptInventory"

		val CODEC: Codec<KeptInventoryCapability> =
			ItemStack.CODEC
				.listOf()
				.xmap(::KeptInventoryCapability, KeptInventoryCapability::stacks)

		fun setSavedInventory(player: Player, stacks: List<ItemStack>): Boolean {
			val cap = player.getCapability(KeptInventoryCapabilityProvider.CAPABILITY)
				.resolve()
				.getOrNull()
				?: return false

			cap.stacks = stacks
			return true
		}

		fun getSavedInventory(player: Player): List<ItemStack> {
			val cap = player.getCapability(KeptInventoryCapabilityProvider.CAPABILITY)
				.resolve()
				.getOrNull()

			return cap?.stacks ?: emptyList()
		}

		fun clearSavedInventory(player: Player): Boolean = setSavedInventory(player, emptyList())
	}

}