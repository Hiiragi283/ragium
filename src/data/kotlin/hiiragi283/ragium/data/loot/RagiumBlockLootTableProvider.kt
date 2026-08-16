package hiiragi283.ragium.data.loot

import hiiragi283.lib.collection.forEach
import hiiragi283.lib.data.loot.HTBlockLootTableProvider
import hiiragi283.lib.resource.SupplierWithId
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTBlockPart
import hiiragi283.ragium.api.tag.HTMaterial
import hiiragi283.ragium.block.RagiumBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.world.level.block.Block

class RagiumBlockLootTableProvider(registries: HolderLookup.Provider) : HTBlockLootTableProvider(registries, RagiumAPI.MOD_ID, RagiumBlocks.REGISTER.asBlockSequence()) {
    override fun generate() {
        RagiumBlocks.MATERIAL_BLOCKS.forEach { (part: HTBlockPart, material: HTMaterial, block: SupplierWithId<Block>) ->
            when (part) {
                HTBlockPart.ORE -> TODO()
                HTBlockPart.DEEPSLATE_ORE -> TODO()
                HTBlockPart.NETHER_ORE -> TODO()
                HTBlockPart.END_ORE -> TODO()
                HTBlockPart.STORAGE_BLOCK -> dropSelf(block)
                HTBlockPart.RAW_STORAGE_BLOCK -> dropSelf(block)
            }
        }
    }
}
