package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.block.type.HTEntityBlockType
import hiiragi283.ragium.common.block.HTTypedEntityBlock
import hiiragi283.ragium.setup.RagiumBlockTypes

class HTUniversalChestBlock(properties: Properties) :
    HTTypedEntityBlock<HTEntityBlockType>(RagiumBlockTypes.UNIVERSAL_CHEST, properties)
