package hiiragi283.ragium.data.server.recipe.compat

import com.buuz135.replication.calculation.MatterValue
import com.buuz135.replication.recipe.MatterValueRecipe
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.HTMaterialPrefix
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.integration.RagiumReplicationAddon
import hiiragi283.ragium.common.material.RagiumEssenceType
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.CommonMaterialPrefixes
import hiiragi283.ragium.setup.DefaultMatterTypes
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
            CommonMaterialPrefixes.DUST,
            RagiumMaterialKeys.RAGINITE,
            RagiumReplicationAddon.getMatterType(RagiumEssenceType.RAGIUM).toValue(9.0),
        )

        register(
            CommonMaterialPrefixes.INGOT,
            RagiumMaterialKeys.ADVANCED_RAGI_ALLOY,
            DefaultMatterTypes.PRECIOUS.toValue(9.0),
            DefaultMatterTypes.METALLIC.toValue(9.0),
            RagiumReplicationAddon.getMatterType(RagiumEssenceType.RAGIUM).toValue(36.0),
        )
        // Azure
        register(
            CommonMaterialPrefixes.GEM,
            RagiumEssenceType.AZURE,
            RagiumReplicationAddon.getMatterType(RagiumEssenceType.AZURE).toValue(2.0),
            DefaultMatterTypes.PRECIOUS.toValue(5.0),
        )

        register(
            CommonMaterialPrefixes.INGOT,
            RagiumMaterialKeys.AZURE_STEEL,
            RagiumReplicationAddon.getMatterType(RagiumEssenceType.AZURE).toValue(4.0),
            DefaultMatterTypes.METALLIC.toValue(9.0),
            DefaultMatterTypes.PRECIOUS.toValue(10.0),
        )
        // Deep
        register(
            RagiumCommonTags.Items.ORES_DEEP_SCRAP,
            DefaultMatterTypes.PRECIOUS.toValue(18.0),
            RagiumReplicationAddon.getMatterType(RagiumEssenceType.DEEP).toValue(18.0),
        )

        register(
            CommonMaterialPrefixes.SCRAP,
            RagiumEssenceType.DEEP,
            DefaultMatterTypes.PRECIOUS.toValue(18.0),
            RagiumReplicationAddon.getMatterType(RagiumEssenceType.DEEP).toValue(18.0),
        )
        // Crimson
        register(
            CommonMaterialPrefixes.GEM,
            RagiumMaterialKeys.CRIMSON_CRYSTAL,
            DefaultMatterTypes.PRECIOUS.toValue(4.0),
            DefaultMatterTypes.NETHER.toValue(4.0),
        )
        // Warped
        register(
            CommonMaterialPrefixes.GEM,
            RagiumMaterialKeys.WARPED_CRYSTAL,
            DefaultMatterTypes.PRECIOUS.toValue(4.0),
            DefaultMatterTypes.ENDER.toValue(4.0),
        )
        // Eldritch
        register(
            CommonMaterialPrefixes.GEM,
            RagiumMaterialKeys.ELDRITCH_PEARL,
            DefaultMatterTypes.PRECIOUS.toValue(4.0),
            DefaultMatterTypes.QUANTUM.toValue(9.0),
        )

        // Foods
        register(
            CommonMaterialPrefixes.DUST,
            RagiumMaterialKeys.MEAT,
            DefaultMatterTypes.LIVING.toValue(4.0),
            DefaultMatterTypes.ORGANIC.toValue(4.0),
        )
        register(
            RagiumCommonTags.Items.FOODS_RAGI_CHERRY,
            DefaultMatterTypes.LIVING.toValue(4.0),
            DefaultMatterTypes.ORGANIC.toValue(4.0),
            RagiumReplicationAddon.getMatterType(RagiumEssenceType.RAGIUM).toValue(4.0),
        )
    }

    @JvmStatic
    private fun register(prefix: HTMaterialPrefix, material: HTMaterialLike, vararg instances: MatterValue) {
        register(prefix.itemTagKey(material), *instances)
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
