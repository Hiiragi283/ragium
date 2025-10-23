package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.common.variant.HTGeneratorVariant

class HTGeneratorBlockItem<BLOCK : HTEntityBlock>(variant: HTGeneratorVariant<BLOCK, *>, properties: Properties) :
    HTVariantBlockItem<HTGeneratorVariant<BLOCK, *>, BLOCK>(variant, properties)
