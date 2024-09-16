package dev.aaronhowser.mods.geneticsresequenced.api.genes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.neoforge.registries.holdersets.AnyHolderSet;

import java.util.ArrayList;
import java.util.HashMap;

public class Jene {

    static Codec<Gene> DIRECT_CODEC =
            RecordCodecBuilder.create(
                    instance -> instance.group(
                            Codec.BOOL.optionalFieldOf("negative", false).forGetter(Gene::isNegative),
                            Codec.BOOL.optionalFieldOf("hidden", false).forGetter(Gene::isHidden),
                            RegistryCodecs
                                    .homogeneousList(Registries.ENTITY_TYPE)
                                    .optionalFieldOf(
                                            "allowed_entities",
                                            new AnyHolderSet<>(BuiltInRegistries.ENTITY_TYPE.asLookup())
                                    )
                                    .forGetter(Gene::getAllowedEntities),
                            Codec.INT.fieldOf("dna_points_required").forGetter(Gene::getDnaPointsRequired),
                            RegistryFixedCodec.create(GeneRegistry.INSTANCE.getGENE_REGISTRY_KEY()).optionalFieldOf("mutates_into").forGetter(Gene::getMutatesInto),
                            Gene.PotionDetails.Companion.getDIRECT_CODEC().optionalFieldOf("potion_details").forGetter(Gene::getPotionDetails),
                            Gene.AttributeEntry.Companion.getDIRECT_CODEC().listOf().optionalFieldOf("attribute_modifiers", new ArrayList<>()).forGetter(Gene::getAttributeModifiers)
                    ).apply(instance, Gene::new)
            );

}
