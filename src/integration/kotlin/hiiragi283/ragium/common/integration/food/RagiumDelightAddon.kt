package hiiragi283.ragium.common.integration.food

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.impl.HTBasicDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockRegister
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.variant.HTHammerToolVariant
import hiiragi283.ragium.common.variant.HTKnifeToolVariant
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumDelightFoods
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumToolTiers
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.common.SimpleTier
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import vectorwing.farmersdelight.common.block.FeastBlock
import vectorwing.farmersdelight.common.block.PieBlock
import vectorwing.farmersdelight.common.registry.ModItems
import kotlin.collections.iterator

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

    // Knives
    @JvmField
    val KNIFE_MAP: Map<HTMaterialType, HTDeferredItem<*>> = mapOf(
        RagiumMaterialType.RAGI_ALLOY to RagiumToolTiers.RAGI_ALLOY,
        RagiumMaterialType.RAGI_CRYSTAL to RagiumToolTiers.RAGI_CRYSTAL,
    ).mapValues { (material: HTMaterialType, tier: SimpleTier) ->
        HTKnifeToolVariant.registerItem(RagiumFoodAddon.ITEM_REGISTER, material, tier)
    }

    @JvmStatic
    fun getKnife(material: HTMaterialType): HTItemHolderLike = when (material) {
        HTVanillaMaterialType.IRON -> HTItemHolderLike.fromItem(ModItems.IRON_KNIFE)
        HTVanillaMaterialType.GOLD -> HTItemHolderLike.fromItem(ModItems.GOLDEN_KNIFE)
        HTVanillaMaterialType.DIAMOND -> HTItemHolderLike.fromItem(ModItems.DIAMOND_KNIFE)
        HTVanillaMaterialType.NETHERITE -> HTItemHolderLike.fromItem(ModItems.NETHERITE_KNIFE)
        else -> KNIFE_MAP[material] ?: error("Unknown knife item for ${material.materialName()}")
    }

    // Food
    @JvmField
    val RAGI_CHERRY_PIE_SLICE: HTDeferredItem<Item> =
        RagiumFoodAddon.registerFood("${RagiumConst.RAGI_CHERRY}_pie_slice", RagiumDelightFoods.RAGI_CHERRY_PIE_SLICE)

    @JvmField
    val RAGI_CHERRY_TOAST: HTDeferredItem<Item> =
        RagiumFoodAddon.registerFood("${RagiumConst.RAGI_CHERRY}_toast", RagiumDelightFoods.RAGI_CHERRY_JAM)

    //    RagiumAddon    //

    override fun onModConstruct(eventBus: IEventBus, dist: Dist) {
        BLOCK_REGISTER.register(eventBus)
    }

    override fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        event.modify(getKnife(RagiumMaterialType.RAGI_CRYSTAL)) { builder: DataComponentPatch.Builder ->
            builder.set(
                RagiumDataComponents.INTRINSIC_ENCHANTMENT,
                HTIntrinsicEnchantment(Enchantments.MENDING, 1),
            )
        }
    }

    override fun buildCreativeTabs(helper: RagiumAddon.CreativeTabHelper) {
        helper.ifMatchTab(RagiumCreativeTabs.ITEMS) { event: BuildCreativeModeTabContentsEvent ->
            for ((material: HTMaterialType, knife: HTDeferredItem<*>) in KNIFE_MAP) {
                event.insertAfter(
                    RagiumItems.getTool(HTHammerToolVariant, material).toStack(),
                    knife.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
                )
            }

            helper.insertAfter(
                event,
                // Cherry
                RagiumItems.RAGI_CHERRY,
                RagiumFoodAddon.RAGI_CHERRY_PULP,
                // Pie
                RAGI_CHERRY_PIE,
                RAGI_CHERRY_PIE_SLICE,
                // Jam
                RagiumFoodAddon.RAGI_CHERRY_JAM,
                RAGI_CHERRY_TOAST_BLOCK,
                RAGI_CHERRY_TOAST,
            )
        }
    }
}
