package hiiragi283.ragium.integration.delight

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.HTAddon
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.registry.HTDeferredBlockHolder
import hiiragi283.ragium.api.registry.HTDeferredBlockRegister
import hiiragi283.ragium.api.registry.HTDeferredItemRegister
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumToolTiers
import hiiragi283.ragium.util.material.RagiumMaterialType
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tier
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import net.neoforged.neoforge.registries.DeferredItem
import vectorwing.farmersdelight.common.block.PieBlock
import vectorwing.farmersdelight.common.item.KnifeItem
import vectorwing.farmersdelight.common.registry.ModItems

@HTAddon(RagiumConst.FARMERS_DELIGHT)
object RagiumDelightAddon : RagiumAddon {
    //    Block    //

    @JvmField
    val BLOCK_REGISTER = HTDeferredBlockRegister(RagiumAPI.MOD_ID)

    @JvmField
    val RAGI_CHERRY_PIE: HTDeferredBlockHolder<PieBlock, BlockItem> = BLOCK_REGISTER.registerSimple(
        "ragi_cherry_pie",
        BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE),
        { prop: BlockBehaviour.Properties -> PieBlock(prop, RAGI_CHERRY_PIE_SLICE) },
    )

    //    Item    //

    @JvmField
    val ITEM_REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    // Knives
    @JvmStatic
    private fun registerKnife(name: String, tier: Tier): DeferredItem<KnifeItem> =
        ITEM_REGISTER.register("${name}_knife") { _: ResourceLocation -> KnifeItem(tier, ModItems.knifeItem(tier)) }

    @JvmField
    val RAGI_ALLOY_KNIFE: DeferredItem<KnifeItem> = registerKnife(RagiumConst.RAGI_ALLOY, RagiumToolTiers.RAGI_ALLOY)

    @JvmField
    val RAGI_CRYSTAL_KNIFE: DeferredItem<KnifeItem> = registerKnife(RagiumConst.RAGI_CRYSTAL, RagiumToolTiers.RAGI_ALLOY)

    // Food
    @JvmStatic
    private fun registerFood(name: String, food: FoodProperties): DeferredItem<Item> =
        ITEM_REGISTER.registerSimpleItem(name, Item.Properties().food(food))

    @JvmField
    val RAGI_CHERRY_PULP: DeferredItem<Item> = registerFood("ragi_cherry_pulp", RagiumDelightFoods.RAGI_CHERRY_PULP)

    @JvmField
    val RAGI_CHERRY_JAM: DeferredItem<Item> = registerFood("ragi_cherry_jam", RagiumDelightFoods.RAGI_CHERRY_JAM)

    @JvmField
    val RAGI_CHERRY_PIE_SLICE: DeferredItem<Item> =
        registerFood("ragi_cherry_pie_slice", RagiumDelightFoods.RAGI_CHERRY_PIE_SLICE)

    //    RagiumAddon    //

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        eventBus.addListener(::modifyComponents)
        eventBus.addListener(::buildCreativeTabs)

        BLOCK_REGISTER.register(eventBus)
        ITEM_REGISTER.register(eventBus)
    }

    private fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        event.modify(RAGI_CHERRY_JAM) { builder: DataComponentPatch.Builder ->
            builder.set(RagiumDataComponents.DRINK_SOUND.get(), SoundEvents.HONEY_DRINK)
            builder.set(RagiumDataComponents.EAT_SOUND.get(), SoundEvents.HONEY_DRINK)
        }
    }

    private fun buildCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
        fun addItems(first: DeferredItem<*>, vararg items: DeferredItem<*>) {
            for (i: Int in listOf(first, *items).indices) {
                val item: DeferredItem<*> = items[i]
                val nextItem: DeferredItem<*> = items.getOrNull(i + 1) ?: continue
                event.insertAfter(
                    item.toStack(),
                    nextItem.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
                )
            }
        }
        
        if (RagiumCreativeTabs.ITEMS.`is`(event.tabKey)) {
            addItems(
                // Knife
                RagiumItems.getForgeHammer(RagiumMaterialType.RAGI_ALLOY),
                RAGI_ALLOY_KNIFE,
            )

            addItems(
                // Knife
                RagiumItems.getForgeHammer(RagiumMaterialType.RAGI_CRYSTAL),
                RAGI_CRYSTAL_KNIFE,
            )
            
            addItems(
                // Cherry
                RagiumItems.RAGI_CHERRY,
                RAGI_CHERRY_PULP,
                // Jam
                RAGI_CHERRY_JAM,
                // Pie
                RAGI_CHERRY_PIE.itemHolder,
                RAGI_CHERRY_PIE_SLICE,
            )
        }
    }
}
