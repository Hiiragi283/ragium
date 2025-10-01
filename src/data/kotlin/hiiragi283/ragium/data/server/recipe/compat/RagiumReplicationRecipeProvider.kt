package hiiragi283.ragium.data.server.recipe.compat

import com.buuz135.replication.ReplicationRegistry
import com.buuz135.replication.api.IMatterType
import com.buuz135.replication.calculation.MatterValue
import com.buuz135.replication.recipe.MatterValueRecipe
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.extension.toId
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.tag.RagiumCommonTags
import hiiragi283.ragium.common.material.HTItemMaterialVariant
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.integration.replication.RagiumReplicationAddon
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.registries.DeferredHolder

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
            RagiumReplicationAddon.MATTER_RAGIUM.toStack(9),
        )
        // Azure
        register(
            HTItemMaterialVariant.GEM,
            RagiumMaterialType.AZURE,
            ReplicationRegistry.Matter.ORGANIC.toStack(2),
            ReplicationRegistry.Matter.PRECIOUS.toStack(5),
            ReplicationRegistry.Matter.EARTH.toStack(3),
        )
        // Crimson
        register(
            HTItemMaterialVariant.GEM,
            RagiumMaterialType.CRIMSON_CRYSTAL,
            ReplicationRegistry.Matter.PRECIOUS.toStack(4),
            ReplicationRegistry.Matter.NETHER.toStack(4),
        )
        // Warped
        register(
            HTItemMaterialVariant.GEM,
            RagiumMaterialType.WARPED_CRYSTAL,
            ReplicationRegistry.Matter.PRECIOUS.toStack(4),
            ReplicationRegistry.Matter.ENDER.toStack(4),
        )
        // Eldritch
        register(
            HTItemMaterialVariant.GEM,
            RagiumMaterialType.ELDRITCH_PEARL,
            ReplicationRegistry.Matter.PRECIOUS.toStack(4),
            ReplicationRegistry.Matter.QUANTUM.toStack(9),
        )

        // Foods
        register(
            HTItemMaterialVariant.DUST,
            RagiumMaterialType.MEAT,
            ReplicationRegistry.Matter.LIVING.toStack(4),
            ReplicationRegistry.Matter.ORGANIC.toStack(4),
        )
        register(
            RagiumCommonTags.Items.FOODS_RAGI_CHERRY,
            ReplicationRegistry.Matter.LIVING.toStack(4),
            ReplicationRegistry.Matter.ORGANIC.toStack(4),
            RagiumReplicationAddon.MATTER_RAGIUM.toStack(4),
        )
    }

    /*private fun register(holder: DeferredItem<*>, vararg instances: MatterValue) {
        val id: ResourceLocation = holder.id
        register(
            ResourceLocation.fromNamespaceAndPath(
                RagiumConst.REPLICATION,
                "matter_values/${id.namespace}/items/${id.path}",
            ),
            Ingredient.of(holder),
     *instances,
        )
    }*/

    @JvmStatic
    private fun register(variant: HTMaterialVariant.ItemTag, material: HTMaterialType, vararg instances: MatterValue) {
        register(variant.itemTagKey(material), *instances)
    }

    @JvmStatic
    private fun register(tagKey: TagKey<Item>, vararg instances: MatterValue) {
        val id: ResourceLocation = tagKey.location
        register(
            RagiumConst.REPLICATION.toId("matter_values/${id.namespace}/tags/${id.path}"),
            Ingredient.of(tagKey),
            *instances,
        )
    }

    @JvmStatic
    private fun register(id: ResourceLocation, ingredient: Ingredient, vararg instances: MatterValue) {
        val recipe = MatterValueRecipe(ingredient, *instances)
        save(id, recipe)
    }

    @JvmStatic
    private fun DeferredHolder<IMatterType, IMatterType>.toStack(amount: Int): MatterValue = toStack(amount.toDouble())

    @JvmStatic
    private fun DeferredHolder<IMatterType, IMatterType>.toStack(amount: Double): MatterValue = MatterValue(this.get(), amount)
}
