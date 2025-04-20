package hiiragi283.ragium.integration.delight

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.extension.itemProperty
import hiiragi283.ragium.api.extension.toStack
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.common.init.RagiumCreativeTabs
import hiiragi283.ragium.common.init.RagiumFoods
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.registries.DeferredItem
import vectorwing.farmersdelight.common.item.ConsumableItem

@HTAddon("farmersdelight")
object RagiumDelightAddon : RagiumAddon {
    //    Item    //

    @JvmField
    val ITEM_REGISTER = HTItemRegister(RagiumAPI.MOD_ID)

    @JvmField
    val RAGI_CHERRY_PULP: DeferredItem<ConsumableItem> =
        registerConsumable("ragi_cherry_pulp", RagiumFoods.RAGI_CHERRY_PULP)

    @JvmField
    val RAGI_CHERRY_JAM: DeferredItem<ConsumableItem> =
        registerConsumable("ragi_cherry_jam", RagiumFoods.RAGI_CHERRY_JAM)

    @JvmStatic
    private fun registerConsumable(
        name: String,
        food: FoodProperties,
        effectTooltip: Boolean = true,
        customTooltip: Boolean = false,
    ): DeferredItem<ConsumableItem> = ITEM_REGISTER.registerItem(
        name,
        { prop: Item.Properties -> ConsumableItem(prop, effectTooltip, customTooltip) },
        itemProperty().food(food),
    )

    //    RagiumAddon    //

    override val priority: Int = 0

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        eventBus.addListener(::buildCreativeTabs)

        ITEM_REGISTER.register(eventBus)
    }

    private fun buildCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
        fun acceptCherry(item: ItemLike) {
            event.insertAfter(
                RagiumItems.RAGI_CHERRY.toStack(),
                item.toStack(),
                CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            )
        }

        if (RagiumCreativeTabs.COMMON.`is`(event.tabKey)) {
            acceptCherry(RAGI_CHERRY_PULP)
            acceptCherry(RAGI_CHERRY_JAM)
        }
    }
}
