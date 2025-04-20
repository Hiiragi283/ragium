package hiiragi283.ragium.integration.delight

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.extension.itemProperty
import hiiragi283.ragium.api.extension.toStack
import hiiragi283.ragium.api.item.HTConsumableItem
import hiiragi283.ragium.api.registry.HTBlockRegister
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.common.init.RagiumCreativeTabs
import hiiragi283.ragium.common.init.RagiumFoods
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.registries.Registries
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.RegisterEvent
import vectorwing.farmersdelight.common.block.FeastBlock
import vectorwing.farmersdelight.common.item.PopsicleItem

@HTAddon("farmersdelight")
object RagiumDelightAddon : RagiumAddon {
    //    Block    //

    @JvmField
    val BLOCK_REGISTER = HTBlockRegister(RagiumAPI.MOD_ID)

    @JvmField
    val COOKED_MEAT_ON_THE_BONE: DeferredBlock<FeastBlock> =
        BLOCK_REGISTER.registerBlock("cooked_meat_on_the_bone", ::HTMeatBlock, blockProperty(Blocks.MUD))

    //    Item    //

    @JvmField
    val ITEM_REGISTER = HTItemRegister(RagiumAPI.MOD_ID)

    @JvmField
    val RAGI_CHERRY_PULP: DeferredItem<HTConsumableItem> =
        registerConsumable("ragi_cherry_pulp", RagiumFoods.RAGI_CHERRY_PULP)

    @JvmField
    val RAGI_CHERRY_POPSICLE: DeferredItem<PopsicleItem> =
        ITEM_REGISTER.registerItem(
            "ragi_cherry_popsicle",
            ::PopsicleItem,
            itemProperty().food(RagiumFoods.RAGI_CHERRY_POPSICLE),
        )

    @JvmField
    val RAGI_CHERRY_JAM: DeferredItem<HTConsumableItem> =
        registerConsumable("ragi_cherry_jam", RagiumFoods.RAGI_CHERRY_JAM)

    @JvmStatic
    private fun registerConsumable(name: String, food: FoodProperties): DeferredItem<HTConsumableItem> =
        ITEM_REGISTER.registerItem(name, ::HTConsumableItem, itemProperty().food(food))

    //    RagiumAddon    //

    override val priority: Int = 0

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        eventBus.addListener(::registerBlockItem)
        eventBus.addListener(::buildCreativeTabs)

        BLOCK_REGISTER.register(eventBus)
        ITEM_REGISTER.register(eventBus)
    }

    private fun registerBlockItem(event: RegisterEvent) {
        event.register(Registries.ITEM) { helper: RegisterEvent.RegisterHelper<Item> ->
            helper.register(COOKED_MEAT_ON_THE_BONE.id, BlockItem(COOKED_MEAT_ON_THE_BONE.get(), itemProperty()))
        }
    }

    private lateinit var lastStack: ItemStack

    private fun buildCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
        fun acceptCherry(item: ItemLike) {
            val stack: ItemStack = item.toStack()
            event.insertAfter(
                lastStack,
                stack,
                CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
            )
            lastStack = stack
        }

        if (RagiumCreativeTabs.COMMON.`is`(event.tabKey)) {
            lastStack = RagiumItems.RAGI_CHERRY.toStack()

            event.insertAfter(
                RagiumItems.CANNED_COOKED_MEAT.toStack(),
                COOKED_MEAT_ON_THE_BONE.toStack(),
                CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
            )

            acceptCherry(RAGI_CHERRY_PULP)

            acceptCherry(RAGI_CHERRY_POPSICLE)
            acceptCherry(RAGI_CHERRY_JAM)
        }
    }
}
