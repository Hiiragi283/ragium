package hiiragi283.ragium.common.material

import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.references.BlockItemIds
import net.minecraft.references.ItemIds

data object RagiumMaterialHelper {
    @JvmStatic
    fun getBase(fuel: RagiumMaterial.Fuel): HTSimpleDeferredItem = when (fuel) {
        RagiumMaterial.Fuel.COAL -> HTSimpleDeferredItem(ItemIds.COAL)
        RagiumMaterial.Fuel.CHARCOAL -> HTSimpleDeferredItem(ItemIds.CHARCOAL)
        RagiumMaterial.Fuel.COAL_COKE -> RagiumItems.COAL_COKE
        RagiumMaterial.Fuel.PITCH_COKE -> RagiumItems.PITCH_COKE
    }

    @JvmStatic
    fun getDust(dustLike: RagiumMaterial.DustLike): HTSimpleDeferredItem = when (dustLike) {
        RagiumMaterial.Mineral.REDSTONE -> HTSimpleDeferredItem(BlockItemIds.REDSTONE_DUST)
        RagiumMaterial.Mineral.GLOWSTONE -> HTSimpleDeferredItem(ItemIds.GLOWSTONE_DUST)
        else -> RagiumItems.getOrThrow(HTItemPart.DUST, dustLike)
    }

    @JvmStatic
    fun getGem(gem: RagiumMaterial.Gem): HTSimpleDeferredItem = when (gem) {
        RagiumMaterial.Gem.LAPIS -> HTSimpleDeferredItem(ItemIds.LAPIS_LAZULI)
        RagiumMaterial.Gem.QUARTZ -> HTSimpleDeferredItem(ItemIds.QUARTZ)
        RagiumMaterial.Gem.AMETHYST -> HTSimpleDeferredItem(ItemIds.AMETHYST_SHARD)
        RagiumMaterial.Gem.DIAMOND -> HTSimpleDeferredItem(ItemIds.DIAMOND)
        RagiumMaterial.Gem.EMERALD -> HTSimpleDeferredItem(ItemIds.EMERALD)
        RagiumMaterial.Gem.ECHO -> HTSimpleDeferredItem(ItemIds.ECHO_SHARD)
        RagiumMaterial.Gem.PRISMARINE -> HTSimpleDeferredItem(ItemIds.PRISMARINE_CRYSTALS)
        else -> RagiumItems.getOrThrow(HTItemPart.GEM, gem)
    }

    @JvmStatic
    fun getIngot(metalLike: RagiumMaterial.MetalLike): HTSimpleDeferredItem? = when (metalLike) {
        RagiumMaterial.Metal.COPPER -> HTSimpleDeferredItem(ItemIds.COPPER_INGOT)
        RagiumMaterial.Metal.IRON -> HTSimpleDeferredItem(ItemIds.IRON_INGOT)
        RagiumMaterial.Metal.GOLD -> HTSimpleDeferredItem(ItemIds.GOLD_INGOT)
        RagiumMaterial.Alloy.NETHERITE -> HTSimpleDeferredItem(ItemIds.NETHERITE_INGOT)
        else -> RagiumItems.MATERIAL_ITEMS[HTItemPart.INGOT, metalLike]
    }

    @JvmStatic
    fun getNugget(metalLike: RagiumMaterial.MetalLike): HTSimpleDeferredItem = when (metalLike) {
        RagiumMaterial.Metal.COPPER -> HTSimpleDeferredItem(ItemIds.COPPER_NUGGET)
        RagiumMaterial.Metal.IRON -> HTSimpleDeferredItem(ItemIds.IRON_NUGGET)
        RagiumMaterial.Metal.GOLD -> HTSimpleDeferredItem(ItemIds.GOLD_NUGGET)
        else -> RagiumItems.getOrThrow(HTItemPart.NUGGET, metalLike)
    }
}
