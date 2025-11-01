package hiiragi283.ragium.common.integration.food

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
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
    val KNIFE_MAP: Map<HTMaterialKey, HTDeferredItem<*>> = mapOf(
        RagiumMaterialKeys.RAGI_ALLOY to RagiumToolTiers.RAGI_ALLOY,
        RagiumMaterialKeys.RAGI_CRYSTAL to RagiumToolTiers.RAGI_CRYSTAL,
    ).mapValues { (key: HTMaterialKey, tier: Tier) ->
        HTKitchenKnifeToolVariant.registerItem(RagiumFoodAddon.ITEM_REGISTER, key, tier)
    }

    @JvmStatic
    fun getKnife(material: HTMaterialLike): HTItemHolderLike = when (val key: HTMaterialKey = material.asMaterialKey()) {
        VanillaMaterialKeys.IRON -> HTItemHolderLike.fromHolder(ModItems.IRON_KITCHEN_KNIFE)
        VanillaMaterialKeys.GOLD -> HTItemHolderLike.fromHolder(ModItems.GOLD_KITCHEN_KNIFE)
        VanillaMaterialKeys.DIAMOND -> HTItemHolderLike.fromHolder(ModItems.DIAMOND_KITCHEN_KNIFE)
        VanillaMaterialKeys.NETHERITE -> HTItemHolderLike.fromHolder(ModItems.NETHERITE_KITCHEN_KNIFE)
        else -> KNIFE_MAP[key] ?: error("Unknown knife item for ${key.name}")
    }

    override fun buildCreativeTabs(helper: RagiumAddon.CreativeTabHelper) {
        helper.ifMatchTab(RagiumCreativeTabs.ITEMS) { event: BuildCreativeModeTabContentsEvent ->
            for ((key: HTMaterialKey, knife: HTDeferredItem<*>) in KNIFE_MAP) {
                event.insertAfter(
                    RagiumItems.getTool(HTHammerToolVariant, key).toStack(),
                    knife.toStack(),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
                )
            }
        }
    }
}
