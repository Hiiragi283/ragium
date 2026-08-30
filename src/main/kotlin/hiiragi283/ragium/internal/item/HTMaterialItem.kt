package hiiragi283.ragium.internal.item

import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.property.HTMaterialPropertyKeys
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class HTMaterialItem(private val material: HTMaterial, properties: Properties) : Item(properties) {
    override fun getCreatorModId(registries: HolderLookup.Provider, itemStack: ItemStack): String? = material.get(HTMaterialPropertyKeys.ORIGIN_MOD_ID)
}
