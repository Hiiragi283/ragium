package hiiragi283.ragium.common.integration.delight

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.item.component.HTItemSoundEvent
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.impl.HTBasicDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockRegister
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTDeferredItemRegister
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.variant.HTHammerToolVariant
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumToolTiers
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import vectorwing.farmersdelight.common.block.FeastBlock
import vectorwing.farmersdelight.common.block.PieBlock
import vectorwing.farmersdelight.common.item.KnifeItem
import vectorwing.farmersdelight.common.registry.ModItems
import java.util.function.Supplier

object RagiumDelightAddon : RagiumAddon {
    //    Block    //

    @JvmField
    val BLOCK_REGISTER = HTDeferredBlockRegister(RagiumAPI.MOD_ID)

    @JvmField
    val RAGI_CHERRY_PIE: HTBasicDeferredBlock<PieBlock> = BLOCK_REGISTER.registerSimple(
        "${RagiumConst.RAGI_CHERRY}_pie",
        BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE),
        { prop: BlockBehaviour.Properties -> PieBlock(prop, RAGI_CHERRY_PIE_SLICE) },
    )

    @JvmField
    val RAGI_CHERRY_TOAST_BLOCK: HTBasicDeferredBlock<FeastBlock> = BLOCK_REGISTER.registerSimple(
        "${RagiumConst.RAGI_CHERRY}_toast_block",
        BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE),
        { prop: BlockBehaviour.Properties -> FeastBlock(prop, RAGI_CHERRY_TOAST, true) },
    )

    //    Item    //

    @JvmField
    val ITEM_REGISTER = HTDeferredItemRegister(RagiumAPI.MOD_ID)

    // Knives
    @JvmField
    val RAGI_ALLOY_KNIFE: HTDeferredItem<KnifeItem> =
        HTKnifeToolVariant.registerItem(ITEM_REGISTER, RagiumMaterialType.RAGI_ALLOY, RagiumToolTiers.RAGI_ALLOY)

    @JvmField
    val RAGI_CRYSTAL_KNIFE: HTDeferredItem<KnifeItem> =
        HTKnifeToolVariant.registerItem(ITEM_REGISTER, RagiumMaterialType.RAGI_CRYSTAL, RagiumToolTiers.RAGI_CRYSTAL)

    @JvmField
    val KNIFE_MAP: Map<RagiumMaterialType, HTDeferredItem<KnifeItem>> = buildMap {
        put(RagiumMaterialType.RAGI_ALLOY, RAGI_ALLOY_KNIFE)
        put(RagiumMaterialType.RAGI_CRYSTAL, RAGI_CRYSTAL_KNIFE)
    }

    @JvmField
    val ALL_KNIFE_MAP: Map<HTMaterialType, Supplier<out Item>> = buildMap {
        // Delight
        put(HTVanillaMaterialType.IRON, ModItems.IRON_KNIFE)
        put(HTVanillaMaterialType.GOLD, ModItems.GOLDEN_KNIFE)
        put(HTVanillaMaterialType.DIAMOND, ModItems.DIAMOND_KNIFE)
        put(HTVanillaMaterialType.NETHERITE, ModItems.NETHERITE_KNIFE)
        // Ragium
        put(RagiumMaterialType.RAGI_ALLOY, RAGI_ALLOY_KNIFE)
        put(RagiumMaterialType.RAGI_CRYSTAL, RAGI_CRYSTAL_KNIFE)
    }

    // Food
    @JvmStatic
    private fun registerFood(name: String, food: FoodProperties): HTDeferredItem<Item> =
        ITEM_REGISTER.registerSimpleItem(name, Item.Properties().food(food))

    @JvmField
    val RAGI_CHERRY_PULP: HTDeferredItem<Item> =
        registerFood("${RagiumConst.RAGI_CHERRY}_pulp", RagiumDelightFoods.RAGI_CHERRY_PULP)

    @JvmField
    val RAGI_CHERRY_PIE_SLICE: HTDeferredItem<Item> =
        registerFood("${RagiumConst.RAGI_CHERRY}_pie_slice", RagiumDelightFoods.RAGI_CHERRY_PIE_SLICE)

    @JvmField
    val RAGI_CHERRY_JAM: HTDeferredItem<Item> =
        registerFood("${RagiumConst.RAGI_CHERRY}_jam", RagiumDelightFoods.RAGI_CHERRY_JAM)

    @JvmField
    val RAGI_CHERRY_TOAST: HTDeferredItem<Item> =
        registerFood("${RagiumConst.RAGI_CHERRY}_toast", RagiumDelightFoods.RAGI_CHERRY_JAM)

    //    RagiumAddon    //

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        eventBus.addListener(::modifyComponents)
        eventBus.addListener(::buildCreativeTabs)

        BLOCK_REGISTER.register(eventBus)
        ITEM_REGISTER.register(eventBus)
    }

    private fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        event.modify(RAGI_CRYSTAL_KNIFE) { builder: DataComponentPatch.Builder ->
            builder.set(
                RagiumDataComponents.INTRINSIC_ENCHANTMENT,
                HTIntrinsicEnchantment(Enchantments.MENDING, 1),
            )
        }

        event.modify(RAGI_CHERRY_JAM) { builder: DataComponentPatch.Builder ->
            builder.set(RagiumDataComponents.DRINK_SOUND, HTItemSoundEvent.create(SoundEvents.HONEY_DRINK))
            builder.set(RagiumDataComponents.EAT_SOUND, HTItemSoundEvent.create(SoundEvents.HONEY_DRINK))
        }
    }

    private fun buildCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
        if (RagiumCreativeTabs.ITEMS.`is`(event.tabKey)) {
            for ((material: RagiumMaterialType, knife: HTDeferredItem<KnifeItem>) in KNIFE_MAP) {
                event.insertAfter(
                    RagiumItems.getTool(HTHammerToolVariant, material).toStack(),
                    knife.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
                )
            }

            val items: List<HTDeferredItem<*>> = listOf(
                // Cherry
                RagiumItems.RAGI_CHERRY,
                RAGI_CHERRY_PULP,
                // Pie
                RAGI_CHERRY_PIE.itemHolder,
                RAGI_CHERRY_PIE_SLICE,
                // Jam
                RAGI_CHERRY_JAM,
                RAGI_CHERRY_TOAST_BLOCK.itemHolder,
                RAGI_CHERRY_TOAST,
            )

            for (i: Int in items.indices) {
                val item: HTDeferredItem<*> = items[i]
                val nextItem: HTDeferredItem<*> = items.getOrNull(i + 1) ?: continue
                event.insertAfter(
                    item.toStack(),
                    nextItem.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
                )
            }
        }
    }
}
