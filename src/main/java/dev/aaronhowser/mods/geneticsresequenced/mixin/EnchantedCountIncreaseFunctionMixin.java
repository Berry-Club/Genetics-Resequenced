package dev.aaronhowser.mods.geneticsresequenced.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EnchantedCountIncreaseFunction.class)
public abstract class EnchantedCountIncreaseFunctionMixin {

	@ModifyVariable(
			method = "run",
			at = @At(
					value = "STORE",
					target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantmentLevel(Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/LivingEntity;)I"
			)
	)
	private int modifyEnchantmentLevel(int originalLevel, ItemStack stack, LootContext context) {
		Entity target = context.getParamOrNull(LootContextParams.THIS_ENTITY);
		int amountToAdd = (target instanceof Chicken) ? 100 : 0;
		return originalLevel + amountToAdd;
	}

}
