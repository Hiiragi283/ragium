package hiiragi283.ragium.common.integration.food

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.variant.HTHammerToolVariant
import hiiragi283.ragium.common.variant.HTKitchenKnifeToolVariant
import hiiragi283.ragium.setup.RagiumCreativeTabs
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumToolTiers
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Tier
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent

object RagiumKaleidoCookeryAddon : RagiumAddon {
    //    Item    //

    // Knives
    @JvmField
    val KNIFE_MAP: Map<HTMaterialType, HTDeferredItem<*>> = mapOf(
        RagiumMaterialType.RAGI_ALLOY to RagiumToolTiers.RAGI_ALLOY,
        RagiumMaterialType.RAGI_CRYSTAL to RagiumToolTiers.RAGI_CRYSTAL,
    ).mapValues { (material: HTMaterialType, tier: Tier) ->
        HTKitchenKnifeToolVariant.registerItem(RagiumFoodAddon.ITEM_REGISTER, material, tier)
    }

    @JvmStatic
    fun getKnife(material: HTMaterialType): HTItemHolderLike = when (material) {
        HTVanillaMaterialType.IRON -> HTItemHolderLike.fromHolder(ModItems.IRON_KITCHEN_KNIFE)
        HTVanillaMaterialType.GOLD -> HTItemHolderLike.fromHolder(ModItems.GOLD_KITCHEN_KNIFE)
        HTVanillaMaterialType.DIAMOND -> HTItemHolderLike.fromHolder(ModItems.DIAMOND_KITCHEN_KNIFE)
        HTVanillaMaterialType.NETHERITE -> HTItemHolderLike.fromHolder(ModItems.NETHERITE_KITCHEN_KNIFE)
        else -> KNIFE_MAP[material] ?: error("Unknown knife item for ${material.materialName()}")
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
        }
    }
}
