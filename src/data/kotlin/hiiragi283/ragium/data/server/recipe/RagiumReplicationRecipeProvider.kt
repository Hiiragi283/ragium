package hiiragi283.ragium.data.server.recipe

import com.buuz135.replication.ReplicationRegistry
import com.buuz135.replication.api.IMatterType
import com.buuz135.replication.calculation.MatterValue
import com.buuz135.replication.recipe.MatterValueRecipe
import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.tag.RagiumItemTags
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.integration.replication.RagiumReplicationAddon
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem

object RagiumReplicationRecipeProvider : HTRecipeProvider() {
    override fun buildRecipeInternal() {
        // Ore -> Raw
        val rawTagKey: TagKey<Item> = itemTagKey(commonId("raw_materials/replica"))
        createCrushing()
            .itemOutput(rawTagKey, 4)
            .itemOutput(rawTagKey, chance = 1 / 4f)
            .itemInput(itemTagKey(commonId("ores/replica")))
            .saveSuffixed(output, "_from_ore")
        // Raw -> Ingot
        val ingotTagKey: TagKey<Item> = itemTagKey(commonId("ingots/replica"))
        createAlloying()
            .itemOutput(ingotTagKey, 3)
            .itemInput(rawTagKey, 2)
            .itemInput(RagiumItemTags.ALLOY_SMELTER_FLUXES_BASIC)
            .saveSuffixed(output, "_with_basic_flux")

        createAlloying()
            .itemOutput(ingotTagKey, 2)
            .itemInput(rawTagKey)
            .itemInput(RagiumItemTags.ALLOY_SMELTER_FLUXES_ADVANCED)
            .saveSuffixed(output, "_with_advanced_flux")

        matter()
    }

    private fun matter() {
        // Ragium
        register(
            RagiumItemTags.DUSTS_RAGINITE,
            RagiumReplicationAddon.MATTER_RAGIUM.toStack(9),
        )
        // Azure
        register(
            RagiumItems.AZURE_SHARD,
            ReplicationRegistry.Matter.ORGANIC.toStack(2),
            ReplicationRegistry.Matter.PRECIOUS.toStack(5),
            ReplicationRegistry.Matter.EARTH.toStack(3),
        )
        // Crimson
        register(
            RagiumItemTags.GEMS_CRIMSON_CRYSTAL,
            ReplicationRegistry.Matter.PRECIOUS.toStack(4),
            ReplicationRegistry.Matter.NETHER.toStack(4),
        )
        // Warped
        register(
            RagiumItemTags.GEMS_WARPED_CRYSTAL,
            ReplicationRegistry.Matter.PRECIOUS.toStack(4),
            ReplicationRegistry.Matter.ENDER.toStack(4),
        )
        // Eldritch
        register(
            RagiumItemTags.GEMS_ELDRITCH_PEARL,
            ReplicationRegistry.Matter.PRECIOUS.toStack(4),
            ReplicationRegistry.Matter.QUANTUM.toStack(9),
        )

        // Foods
        register(
            RagiumItems.MINCED_MEAT,
            ReplicationRegistry.Matter.LIVING.toStack(4),
            ReplicationRegistry.Matter.ORGANIC.toStack(4),
        )
        register(
            RagiumItemTags.FOODS_RAGI_CHERRY,
            ReplicationRegistry.Matter.LIVING.toStack(4),
            ReplicationRegistry.Matter.ORGANIC.toStack(4),
            RagiumReplicationAddon.MATTER_RAGIUM.toStack(4),
        )
    }

    @JvmStatic
    private fun register(holder: DeferredItem<*>, vararg instances: MatterValue) {
        val id: ResourceLocation = holder.id
        register(
            ResourceLocation.fromNamespaceAndPath(
                RagiumConstantValues.REPLICATION,
                "matter_values/${id.namespace}/items/${id.path}",
            ),
            Ingredient.of(holder),
            *instances,
        )
    }

    @JvmStatic
    private fun register(tagKey: TagKey<Item>, vararg instances: MatterValue) {
        val id: ResourceLocation = tagKey.location
        register(
            ResourceLocation.fromNamespaceAndPath(
                RagiumConstantValues.REPLICATION,
                "matter_values/${id.namespace}/tags/${id.path}",
            ),
            Ingredient.of(tagKey),
            *instances,
        )
    }

    @JvmStatic
    private fun register(id: ResourceLocation, ingredient: Ingredient, vararg instances: MatterValue) {
        val recipe = MatterValueRecipe(ingredient, *instances)
        save(id, recipe)
    }

    private fun DeferredHolder<IMatterType, IMatterType>.toStack(amount: Int): MatterValue = toStack(amount.toDouble())

    private fun DeferredHolder<IMatterType, IMatterType>.toStack(amount: Double): MatterValue = MatterValue(this.get(), amount)
}
