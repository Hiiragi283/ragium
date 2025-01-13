package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.content.HTItemContent
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.world.item.Item
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumItems {
    //    Materials    //

    enum class Dusts(override val material: HTMaterialKey) : HTItemContent.Material {
        IRON(RagiumMaterialKeys.IRON),
        ;

        override val holder: DeferredHolder<Item, Item> = HTContent.itemHolder("${name.lowercase()}_dust")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.DUST
    }

    //    Register    //

    @JvmField
    val REGISTER: DeferredRegister.Items = DeferredRegister.createItems(RagiumAPI.MOD_ID)

    @JvmStatic
    internal fun register(bus: IEventBus) {
        fun DeferredRegister.Items.registerSimple(content: HTItemContent) {
            content.registerSimpleItem(this)
        }

        // materials
        buildList {
            addAll(Dusts.entries)
        }.forEach(REGISTER::registerSimple)

        REGISTER.register(bus)
    }
}
