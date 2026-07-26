package hiiragi283.ragium.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.registry.createKey
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab

data object RagiumCreativeTabs {
    @JvmField
    val COMMON: ResourceKey<CreativeModeTab> = create(HTConst.COMMON)

    @JvmStatic
    private fun create(name: String) = Registries.CREATIVE_MODE_TAB.createKey(RagiumAPI.id(name))
}
