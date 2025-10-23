package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.api.item.HTBlockItem
import hiiragi283.ragium.api.variant.HTVariantKey
import net.minecraft.world.level.block.Block

open class HTVariantBlockItem<VARIANT : HTVariantKey.WithBlock<BLOCK>, BLOCK : Block>(val variant: VARIANT, properties: Properties) :
    HTBlockItem<BLOCK>(variant.getBlock(), properties)
