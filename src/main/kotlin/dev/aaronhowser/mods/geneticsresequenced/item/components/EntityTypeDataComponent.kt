package dev.aaronhowser.mods.geneticsresequenced.item.components

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.world.entity.EntityType
import net.minecraftforge.registries.ForgeRegistries

class EntityTypeDataComponent(
	val entityType: EntityType<*>
) : PseudoDataComponent<EntityTypeDataComponent, EntityTypeDataComponent.Type>() {

	object Type : PseudoDataComponent.Type<EntityTypeDataComponent>(GeneticsResequenced.modResource("entity_type")) {
		val CODEC: Codec<EntityTypeDataComponent> =
			ForgeRegistries.ENTITY_TYPES.codec
				.xmap(
					{ holder -> EntityTypeDataComponent(holder) },
					{ component -> component.entityType }
				)

		override fun getCodec(): Codec<EntityTypeDataComponent> = CODEC
	}

	override val type: Type = Type
}