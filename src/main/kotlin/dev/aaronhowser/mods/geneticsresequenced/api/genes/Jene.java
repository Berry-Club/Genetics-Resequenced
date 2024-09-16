package dev.aaronhowser.mods.geneticsresequenced.api.genes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.neoforged.neoforge.registries.holdersets.AnyHolderSet;

import java.util.ArrayList;

public class Jene {

    static Codec<Gene> DIRECT_CODEC =
            RecordCodecBuilder.create(
                    instance -> instance.group(
                            Codec.BOOL.optionalFieldOf("negative", false).forGetter(Gene::isNegative),
                            Codec.BOOL.optionalFieldOf("hidden", false).forGetter(Gene::isHidden),
                            Codec.INT.fieldOf("dna_points_required").forGetter(Gene::getDnaPointsRequired),
                            RegistryCodecs
                                    .homogeneousList(Registries.ENTITY_TYPE)
                                    .optionalFieldOf(
                                            "allowed_entities",
                                            new AnyHolderSet<>(BuiltInRegistries.ENTITY_TYPE.asLookup())
                                    )
                                    .forGetter(Gene::getAllowedEntities),
                            RegistryFixedCodec.create(GeneRegistry.INSTANCE.getGENE_REGISTRY_KEY()).optionalFieldOf("mutates_into").forGetter(Gene::getMutatesInto),
                            Gene.PotionDetails.Companion.getDIRECT_CODEC().optionalFieldOf("potion_details").forGetter(Gene::getPotionDetails),
                            Gene.AttributeEntry.Companion.getDIRECT_CODEC().listOf().optionalFieldOf("attribute_modifiers", new ArrayList<>()).forGetter(Gene::getAttributeModifiers)
                    ).apply(instance, Gene::new)
            );

    static StreamCodec<RegistryFriendlyByteBuf, Gene> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.optional(ByteBufCodecs.BOOL), Gene::isNegative,
            ByteBufCodecs.optional(ByteBufCodecs.BOOL), Gene::isHidden,
            ByteBufCodecs.INT, Gene::getDnaPointsRequired,
            ByteBufCodecs.optional(ByteBufCodecs.holderSet(Registries.ENTITY_TYPE)), Gene::getAllowedEntities,
            ByteBufCodecs.optional(ByteBufCodecs.holder(GeneRegistry.INSTANCE.getGENE_REGISTRY_KEY(), Gene.Companion.getSTREAM_CODEC())), Gene::getMutatesInto,
            ByteBufCodecs.optional(Gene.PotionDetails.Companion.getDIRECT_STREAM_CODEC()), Gene::getPotionDetails,
            ByteBufCodecs.optional(Gene.AttributeEntry.Companion.getDIRECT_STREAM_CODEC().apply(ByteBufCodecs.list())), Gene::getAttributeModifiers,
            Gene::new
    );

}
