package hiiragi283.ragium.internal.item

import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.property.HTMaterialPropertyKeys
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

class HTMaterialBlockItem(private val material: HTMaterial, block: Block, properties: Properties) : BlockItem(block, properties) {
    override fun getCreatorModId(registries: HolderLookup.Provider, itemStack: ItemStack): String? = material.get(HTMaterialPropertyKeys.ORIGIN_MOD_ID)
}
