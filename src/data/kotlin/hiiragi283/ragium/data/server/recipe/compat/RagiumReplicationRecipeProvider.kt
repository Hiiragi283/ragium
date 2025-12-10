package hiiragi283.ragium.data.server.recipe.compat

import com.buuz135.replication.api.IMatterType
import com.buuz135.replication.api.MatterType
import com.buuz135.replication.calculation.MatterValue
import com.buuz135.replication.recipe.MatterValueRecipe
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.material.RagiumEssenceType
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumMatterTypes
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
            RagiumMatterTypes.getType(RagiumEssenceType.RAGIUM) to 9.0,
        )

        register(
            CommonMaterialPrefixes.INGOT,
            RagiumMaterialKeys.ADVANCED_RAGI_ALLOY,
            MatterType.PRECIOUS to 9.0,
            MatterType.METALLIC to 9.0,
            RagiumMatterTypes.getType(RagiumEssenceType.RAGIUM) to 36.0,
        )
        // Azure
        register(
            CommonMaterialPrefixes.GEM,
            RagiumEssenceType.AZURE,
            RagiumMatterTypes.getType(RagiumEssenceType.AZURE) to 2.0,
            MatterType.PRECIOUS to 5.0,
        )

        register(
            CommonMaterialPrefixes.INGOT,
            RagiumMaterialKeys.AZURE_STEEL,
            RagiumMatterTypes.getType(RagiumEssenceType.AZURE) to 4.0,
            MatterType.METALLIC to 9.0,
            MatterType.PRECIOUS to 10.0,
        )
        // Deep
        register(
            RagiumCommonTags.Items.ORES_DEEP_SCRAP,
            MatterType.PRECIOUS to 18.0,
            RagiumMatterTypes.getType(RagiumEssenceType.DEEP) to 18.0,
        )

        register(
            CommonMaterialPrefixes.SCRAP,
            RagiumEssenceType.DEEP,
            MatterType.PRECIOUS to 18.0,
            RagiumMatterTypes.getType(RagiumEssenceType.DEEP) to 18.0,
        )
        // Crimson
        register(
            CommonMaterialPrefixes.GEM,
            RagiumMaterialKeys.CRIMSON_CRYSTAL,
            MatterType.PRECIOUS to 4.0,
            MatterType.NETHER to 4.0,
        )
        // Warped
        register(
            CommonMaterialPrefixes.GEM,
            RagiumMaterialKeys.WARPED_CRYSTAL,
            MatterType.PRECIOUS to 4.0,
            MatterType.ENDER to 4.0,
        )
        // Eldritch
        register(
            CommonMaterialPrefixes.GEM,
            RagiumMaterialKeys.ELDRITCH_PEARL,
            MatterType.PRECIOUS to 4.0,
            MatterType.QUANTUM to 9.0,
        )

        // Foods
        register(
            CommonMaterialPrefixes.DUST,
            FoodMaterialKeys.RAW_MEAT,
            MatterType.LIVING to 4.0,
            MatterType.ORGANIC to 4.0,
        )
        register(
            CommonMaterialPrefixes.FOOD,
            FoodMaterialKeys.RAGI_CHERRY,
            MatterType.LIVING to 4.0,
            MatterType.ORGANIC to 4.0,
            RagiumMatterTypes.getType(RagiumEssenceType.RAGIUM) to 4.0,
        )
    }

    @JvmStatic
    private fun register(prefix: HTPrefixLike, material: HTMaterialLike, vararg instances: Pair<IMatterType, Double>) {
        register(prefix.itemTagKey(material), *instances)
    }

    @JvmStatic
    private fun register(tagKey: TagKey<Item>, vararg instances: Pair<IMatterType, Double>) {
        register(tagKey, instances.map { (type: IMatterType, amount: Double) -> MatterValue(type, amount) })
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
