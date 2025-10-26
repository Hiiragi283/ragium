package hiiragi283.ragium.setup

import hiiragi283.ragium.api.block.attribute.HTTierBlockAttribute
import hiiragi283.ragium.api.block.type.HTEntityBlockType
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.common.tier.HTDrumTier

/**
 * @see mekanism.common.registries.MekanismBlockTypes
 */
object RagiumBlockTypes {
    //    Storage    //

    @JvmField
    val CRATES: Map<HTCrateTier, HTEntityBlockType> = HTCrateTier.entries.associateWith { tier: HTCrateTier ->
        HTEntityBlockType
            .builder { tier.getBlockEntityType() }
            .add(HTTierBlockAttribute(tier))
            .build()
    }

    @JvmField
    val DRUMS: Map<HTDrumTier, HTEntityBlockType> = HTDrumTier.entries.associateWith { tier: HTDrumTier ->
        HTEntityBlockType
            .builder {
                RagiumBlockEntityTypes.DRUMS[tier] ?: error("Unknown drum tier: $tier")
            }.add(HTTierBlockAttribute(tier))
            .build()
    }
}
