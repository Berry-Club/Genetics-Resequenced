package dev.aaronhowser.mods.geneticsresequenced.util

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.world.item.ItemStack

object ItemStackNbt {
	fun contains(itemStack: ItemStack, key: String, type: Int): Boolean {
		return itemStack.tag?.contains(key, type) == true
	}

	fun getBoolean(itemStack: ItemStack, key: String): Boolean? {
		if (!contains(itemStack, key, Tag.TAG_BYTE.toInt())) return null
		return itemStack.tag?.getBoolean(key)
	}

	fun getBoolean(itemStack: ItemStack, key: String, defaultValue: Boolean): Boolean {
		return getBoolean(itemStack, key) ?: defaultValue
	}

	fun getFloat(itemStack: ItemStack, key: String): Float? {
		if (!contains(itemStack, key, Tag.TAG_FLOAT.toInt())) return null
		return itemStack.tag?.getFloat(key)
	}

	fun getFloat(itemStack: ItemStack, key: String, defaultValue: Float): Float {
		return getFloat(itemStack, key) ?: defaultValue
	}

	fun getInt(itemStack: ItemStack, key: String): Int? {
		if (!contains(itemStack, key, Tag.TAG_INT.toInt())) return null
		return itemStack.tag?.getInt(key)
	}

	fun getInt(itemStack: ItemStack, key: String, defaultValue: Int): Int {
		return getInt(itemStack, key) ?: defaultValue
	}

	fun getString(itemStack: ItemStack, key: String): String? {
		if (!contains(itemStack, key, Tag.TAG_STRING.toInt())) return null
		return itemStack.tag?.getString(key)
	}

	fun getString(itemStack: ItemStack, key: String, defaultValue: String): String {
		return getString(itemStack, key) ?: defaultValue
	}

	fun getCompound(itemStack: ItemStack, key: String): CompoundTag? {
		if (!contains(itemStack, key, Tag.TAG_COMPOUND.toInt())) return null
		return itemStack.tag?.getCompound(key)
	}

	fun getList(itemStack: ItemStack, key: String, elementType: Int): ListTag? {
		if (!contains(itemStack, key, Tag.TAG_LIST.toInt())) return null
		return itemStack.tag?.getList(key, elementType)
	}

	fun putBoolean(itemStack: ItemStack, key: String, value: Boolean) {
		itemStack.orCreateTag.putBoolean(key, value)
	}

	fun putFloat(itemStack: ItemStack, key: String, value: Float) {
		itemStack.orCreateTag.putFloat(key, value)
	}

	fun putInt(itemStack: ItemStack, key: String, value: Int) {
		itemStack.orCreateTag.putInt(key, value)
	}

	fun putString(itemStack: ItemStack, key: String, value: String) {
		itemStack.orCreateTag.putString(key, value)
	}

	fun put(itemStack: ItemStack, key: String, value: Tag) {
		itemStack.orCreateTag.put(key, value)
	}

	fun remove(itemStack: ItemStack, key: String) {
		itemStack.tag?.remove(key)
	}
}