package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.data.server.integration.HTAARecipeProvider
import hiiragi283.ragium.data.server.integration.HTDelightRecipeProvider
import hiiragi283.ragium.data.server.integration.HTEvilRecipeProvider
import hiiragi283.ragium.data.server.integration.HTMekanismRecipeProvider
import hiiragi283.ragium.data.server.recipe.*
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import net.neoforged.neoforge.common.crafting.SizedIngredient
import java.util.*
import java.util.concurrent.CompletableFuture

class RagiumRecipeProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
    RecipeProvider(output, registries) {
    fun interface Child {
        fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider)

        fun has(item: ItemLike): Criterion<InventoryChangeTrigger.TriggerInstance> = inventoryTrigger(ItemPredicate.Builder.item().of(item))

        fun has(prefix: HTTagPrefix, material: HTMaterialKey): Criterion<InventoryChangeTrigger.TriggerInstance> =
            has(prefix.createTag(material))

        fun has(tagKey: TagKey<Item>): Criterion<InventoryChangeTrigger.TriggerInstance> =
            inventoryTrigger(ItemPredicate.Builder.item().of(tagKey))

        private fun inventoryTrigger(builder: ItemPredicate.Builder): Criterion<InventoryChangeTrigger.TriggerInstance> =
            CriteriaTriggers.INVENTORY_CHANGED.createCriterion(
                InventoryChangeTrigger.TriggerInstance(
                    Optional.empty(),
                    InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                    listOf(builder.build()),
                ),
            )
    }

    abstract class ModChild(val modId: String) : Child {
        fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(modId, path)

        final override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
            buildModRecipes(output.withConditions(ModLoadedCondition(modId)), holderLookup)
        }

        abstract fun buildModRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider)
    }

    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        HTBuildingRecipeProvider.buildRecipes(output, holderLookup)
        HTChemicalRecipeProvider.buildRecipes(output, holderLookup)
        HTDistillationRecipeProvider.buildRecipes(output, holderLookup)
        HTFoodRecipeProvider.buildRecipes(output, holderLookup)
        HTIngredientRecipeProvider.buildRecipes(output, holderLookup)
        HTMachineRecipeProvider.buildRecipes(output, holderLookup)
        HTMaterialRecipeProvider.buildRecipes(output, holderLookup)

        HTAARecipeProvider.buildRecipes(output, holderLookup)
        HTDelightRecipeProvider.buildRecipes(output, holderLookup)
        HTEvilRecipeProvider.buildRecipes(output, holderLookup)
        HTMekanismRecipeProvider.buildRecipes(output, holderLookup)

        registerVanilla(output)
    }

    private fun registerVanilla(output: RecipeOutput) {
        // Skulls
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.LASER_TRANSFORMER)
            .itemInput(Tags.Items.STORAGE_BLOCKS_BONE_MEAL)
            .itemOutput(Items.SKELETON_SKULL)
            .save(output)

        registerSkull(
            output,
            SizedIngredient.of(Tags.Items.STORAGE_BLOCKS_COAL, 1),
            Items.WITHER_SKELETON_SKULL,
            HTMachineTier.ELITE,
        )
        registerSkull(output, SizedIngredient.of(Items.GOLDEN_APPLE, 8), Items.PLAYER_HEAD)
        registerSkull(output, SizedIngredient.of(Items.ROTTEN_FLESH, 16), Items.ZOMBIE_HEAD)
        registerSkull(output, SizedIngredient.of(Tags.Items.GUNPOWDERS, 16), Items.CREEPER_HEAD)
        registerSkull(output, SizedIngredient.of(Tags.Items.INGOTS_GOLD, 8), Items.PIGLIN_HEAD)
    }

    private fun registerSkull(
        output: RecipeOutput,
        input: SizedIngredient,
        skull: Item,
        tier: HTMachineTier = HTMachineTier.ADVANCED,
    ) {
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.LASER_TRANSFORMER, tier)
            .itemInput(Items.SKELETON_SKULL)
            .itemInput(input)
            .itemOutput(skull)
            .save(output)
    }
}
