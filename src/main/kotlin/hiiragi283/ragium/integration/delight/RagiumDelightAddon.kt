package hiiragi283.ragium.integration.delight

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.registry.HTBlockRegister
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumFoods
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.registries.DeferredItem

@HTAddon(RagiumConstantValues.FARMERS_DELIGHT)
object RagiumDelightAddon : RagiumAddon {
    //    Block    //

    @JvmField
    val BLOCK_REGISTER = HTBlockRegister(RagiumAPI.MOD_ID)

    //    Item    //

    @JvmField
    val ITEM_REGISTER = HTItemRegister(RagiumAPI.MOD_ID)

    @JvmField
    val RAGI_CHERRY_PULP: DeferredItem<Item> =
        ITEM_REGISTER.registerSimpleItem("ragi_cherry_pulp", Item.Properties().food(RagiumFoods.RAGI_CHERRY_PULP))

    //    RagiumAddon    //

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        eventBus.addListener(::buildCreativeTabs)

        BLOCK_REGISTER.register(eventBus)
        ITEM_REGISTER.register(eventBus)
    }

    private fun buildCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
        if (RagiumCreativeTabs.ITEMS.`is`(event.tabKey)) {
            event.insertAfter(
                RagiumItems.RAGI_CHERRY.toStack(),
                RAGI_CHERRY_PULP.toStack(),
                CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
            )
        }
    }
}
