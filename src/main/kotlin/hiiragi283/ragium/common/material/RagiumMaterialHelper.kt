package hiiragi283.ragium.common.material

import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.vanillaId
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.item.RagiumItems

data object RagiumMaterialHelper {
    @JvmStatic
    fun getBase(fuel: RagiumMaterial.Fuel): HTSimpleDeferredItem = when (fuel) {
        RagiumMaterial.Fuel.COAL -> HTSimpleDeferredItem(vanillaId("coal"))
        RagiumMaterial.Fuel.CHARCOAL -> HTSimpleDeferredItem(vanillaId("charcoal"))
        RagiumMaterial.Fuel.COAL_COKE -> RagiumItems.COAL_COKE
        RagiumMaterial.Fuel.PITCH_COKE -> RagiumItems.PITCH_COKE
    }

    @JvmStatic
    fun getDust(dustLike: RagiumMaterial.DustLike): HTSimpleDeferredItem = when (dustLike) {
        RagiumMaterial.Mineral.REDSTONE -> HTSimpleDeferredItem(vanillaId("redstone"))
        RagiumMaterial.Mineral.GLOWSTONE -> HTSimpleDeferredItem(vanillaId("glowstone_dust"))
        else -> RagiumItems.getOrThrow(HTItemPart.DUST, dustLike)
    }

    @JvmStatic
    fun getGem(gem: RagiumMaterial.Gem): HTSimpleDeferredItem = when (gem) {
        RagiumMaterial.Gem.LAPIS -> HTSimpleDeferredItem(vanillaId("lapis_lazuri"))
        RagiumMaterial.Gem.QUARTZ -> HTSimpleDeferredItem(vanillaId("quartz"))
        RagiumMaterial.Gem.AMETHYST -> HTSimpleDeferredItem(vanillaId("amethyst_shard"))
        RagiumMaterial.Gem.DIAMOND -> HTSimpleDeferredItem(vanillaId("diamond"))
        RagiumMaterial.Gem.EMERALD -> HTSimpleDeferredItem(vanillaId("emerald"))
        RagiumMaterial.Gem.ECHO -> HTSimpleDeferredItem(vanillaId("echo_shard"))
        RagiumMaterial.Gem.PRISMARINE -> HTSimpleDeferredItem(vanillaId("prismarine_crystals"))
        else -> RagiumItems.getOrThrow(HTItemPart.GEM, gem)
    }

    @JvmStatic
    fun getIngot(metalLike: RagiumMaterial.MetalLike): HTSimpleDeferredItem = when (metalLike) {
        RagiumMaterial.Metal.COPPER -> HTSimpleDeferredItem(vanillaId("copper_ingot"))
        RagiumMaterial.Metal.IRON -> HTSimpleDeferredItem(vanillaId("iron_ingot"))
        RagiumMaterial.Metal.GOLD -> HTSimpleDeferredItem(vanillaId("gold_ingot"))
        RagiumMaterial.Alloy.NETHERITE -> HTSimpleDeferredItem(vanillaId("netherite_ingot"))
        else -> RagiumItems.getOrThrow(HTItemPart.INGOT, metalLike)
    }

    @JvmStatic
    fun getNugget(metalLike: RagiumMaterial.MetalLike): HTSimpleDeferredItem = when (metalLike) {
        RagiumMaterial.Metal.COPPER -> HTSimpleDeferredItem(vanillaId("copper_nugget"))
        RagiumMaterial.Metal.IRON -> HTSimpleDeferredItem(vanillaId("iron_nugget"))
        RagiumMaterial.Metal.GOLD -> HTSimpleDeferredItem(vanillaId("gold_nugget"))
        else -> RagiumItems.getOrThrow(HTItemPart.NUGGET, metalLike)
    }
}
