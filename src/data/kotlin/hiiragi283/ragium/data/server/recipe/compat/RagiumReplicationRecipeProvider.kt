package hiiragi283.ragium.data.server.recipe.compat

import com.buuz135.replication.calculation.MatterValue
import com.buuz135.replication.recipe.MatterValueRecipe
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.integration.replication.HTDeferredMatterType
import hiiragi283.ragium.integration.replication.RagiumReplicationAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient

object RagiumReplicationRecipeProvider : HTRecipeProvider.Integration(RagiumConst.REPLICATION) {
    override fun buildRecipeInternal() {
        matter()
    }

    @JvmStatic
    private fun matter() {
        // Ragium
        register(
            HTItemMaterialVariant.DUST,
            RagiumMaterialType.RAGINITE,
            RagiumReplicationAddon.MATTER_RAGIUM.toValue(9.0),
        )

        register(
            HTItemMaterialVariant.INGOT,
            RagiumMaterialType.ADVANCED_RAGI_ALLOY,
            HTDeferredMatterType.PRECIOUS.toValue(9.0),
            HTDeferredMatterType.METALLIC.toValue(9.0),
            RagiumReplicationAddon.MATTER_RAGIUM.toValue(36.0),
        )
        // Azure
        register(
            HTItemMaterialVariant.GEM,
            RagiumMaterialType.AZURE,
            RagiumReplicationAddon.MATTER_AZURE.toValue(2.0),
            HTDeferredMatterType.PRECIOUS.toValue(5.0),
        )

        register(
            HTItemMaterialVariant.INGOT,
            RagiumMaterialType.AZURE_STEEL,
            RagiumReplicationAddon.MATTER_AZURE.toValue(4.0),
            HTDeferredMatterType.METALLIC.toValue(9.0),
            HTDeferredMatterType.PRECIOUS.toValue(10.0),
        )
        // Deep
        register(
            RagiumBlocks.RESONANT_DEBRIS,
            HTDeferredMatterType.PRECIOUS.toValue(18.0),
            RagiumReplicationAddon.MATTER_DEEP.toValue(18.0),
        )

        register(
            RagiumItems.DEEP_SCRAP,
            HTDeferredMatterType.PRECIOUS.toValue(18.0),
            RagiumReplicationAddon.MATTER_DEEP.toValue(18.0),
        )
        // Crimson
        register(
            HTItemMaterialVariant.GEM,
            RagiumMaterialType.CRIMSON_CRYSTAL,
            HTDeferredMatterType.PRECIOUS.toValue(4.0),
            HTDeferredMatterType.NETHER.toValue(4.0),
        )
        // Warped
        register(
            HTItemMaterialVariant.GEM,
            RagiumMaterialType.WARPED_CRYSTAL,
            HTDeferredMatterType.PRECIOUS.toValue(4.0),
            HTDeferredMatterType.ENDER.toValue(4.0),
        )
        // Eldritch
        register(
            HTItemMaterialVariant.GEM,
            RagiumMaterialType.ELDRITCH_PEARL,
            HTDeferredMatterType.PRECIOUS.toValue(4.0),
            HTDeferredMatterType.QUANTUM.toValue(9.0),
        )

        // Foods
        register(
            HTItemMaterialVariant.DUST,
            RagiumMaterialType.MEAT,
            HTDeferredMatterType.LIVING.toValue(4.0),
            HTDeferredMatterType.ORGANIC.toValue(4.0),
        )
        register(
            RagiumCommonTags.Items.FOODS_RAGI_CHERRY,
            HTDeferredMatterType.LIVING.toValue(4.0),
            HTDeferredMatterType.ORGANIC.toValue(4.0),
            RagiumReplicationAddon.MATTER_RAGIUM.toValue(4.0),
        )
    }

    @JvmStatic
    private fun register(item: HTItemHolderLike, vararg instances: MatterValue) {
        register(item, listOf(*instances))
    }

    @JvmStatic
    private fun register(item: HTItemHolderLike, instances: List<MatterValue>) {
        register(item.getId(), Ingredient.of(item), instances)
    }

    @JvmStatic
    private fun register(variant: HTMaterialVariant.ItemTag, material: HTMaterialType, vararg instances: MatterValue) {
        register(variant.itemTagKey(material), *instances)
    }

    @JvmStatic
    private fun register(tagKey: TagKey<Item>, vararg instances: MatterValue) {
        register(tagKey, listOf(*instances))
    }

    @JvmStatic
    private fun register(tagKey: TagKey<Item>, instances: List<MatterValue>) {
        register(tagKey.location, Ingredient.of(tagKey), instances)
    }

    @JvmStatic
    private fun register(id: ResourceLocation, ingredient: Ingredient, instances: List<MatterValue>) {
        save(id.withPrefix("matter_values/"), MatterValueRecipe(ingredient, instances))
    }
}
