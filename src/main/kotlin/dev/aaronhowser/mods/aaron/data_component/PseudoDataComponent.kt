package dev.aaronhowser.mods.aaron.data_component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isTrue
import net.minecraft.nbt.NbtOps
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraftforge.registries.RegistryObject

abstract class PseudoDataComponent<
		C : PseudoDataComponent<C, T>,
		T : PseudoDataComponent.Type<C>
		> {

	abstract class Type<C : PseudoDataComponent<C, *>>(
		val id: ResourceLocation
	) {
		abstract fun getCodec(): Codec<C>
	}

	abstract val type: T

	companion object {

		fun ItemStack.hasComponent(type: Type<*>): Boolean {
			return this.tag?.contains(type.id.toString()).isTrue()
		}

		fun <C : PseudoDataComponent<C, *>> ItemStack.setComponent(component: C) {
			val encoded = component.type
				.getCodec()
				.encodeStart(NbtOps.INSTANCE, component)
				.getOrThrow(false) {}

			this.orCreateTag.put(component.type.id.toString(), encoded)
		}

		fun <C : PseudoDataComponent<C, T>, T : Type<C>> ItemStack.getComponent(type: T): C? {
			val tag = this.tag ?: return null
			val encoded = tag.get(type.id.toString()) ?: return null

			return type
				.getCodec()
				.decode(NbtOps.INSTANCE, encoded)
				.getOrThrow(false) {}
				.first
		}

		fun ItemStack.removeComponent(type: Type<*>) {
			this.tag?.remove(type.id.toString())
		}

		fun <C : PseudoDataComponent<C, *>> RegistryObject<out Item>.withComponent(component: C): ItemStack {
			return this.get().withComponent(component)
		}

		fun <C : PseudoDataComponent<C, *>> ItemLike.withComponent(component: C): ItemStack {
			return this.asItem().defaultInstance.withComponent(component)
		}

		fun <C : PseudoDataComponent<C, *>> ItemStack.withComponent(component: C): ItemStack {
			this.setComponent(component)
			return this
		}
	}
}