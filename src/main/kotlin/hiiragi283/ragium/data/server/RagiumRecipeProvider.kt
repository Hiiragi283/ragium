package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTCookingRecipeBuilder
import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.data.HTRefineryRecipeBuilder
import hiiragi283.ragium.api.data.HTSingleItemRecipeBuilder
import hiiragi283.ragium.api.extension.catalyst
import hiiragi283.ragium.api.extension.sources
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.tag.RagiumBlockTags
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumRecipes
import hiiragi283.ragium.common.recipe.condition.HTRockGeneratorCondition
import hiiragi283.ragium.data.server.integration.HTAARecipeProvider
import hiiragi283.ragium.data.server.integration.HTDelightRecipeProvider
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
import net.minecraft.world.item.crafting.Ingredient
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
        HTBlockRecipeProvider.buildRecipes(output, holderLookup)
        HTChemicalRecipeProvider.buildRecipes(output, holderLookup)
        HTFoodRecipeProvider.buildRecipes(output, holderLookup)
        HTIngredientRecipeProvider.buildRecipes(output, holderLookup)
        HTMachineRecipeProvider.buildRecipes(output, holderLookup)
        HTMaterialRecipeProvider.buildRecipes(output, holderLookup)

        HTAARecipeProvider.buildRecipes(output, holderLookup)
        HTDelightRecipeProvider.buildRecipes(output, holderLookup)
        HTMekanismRecipeProvider.buildRecipes(output, holderLookup)

        registerVanilla(output)
        registerAlt(output)
    }

    //    Vanilla    //

    private fun registerVanilla(output: RecipeOutput) {
        // Skulls
        HTMachineRecipeBuilder
            .create(RagiumRecipes.LASER_TRANSFORMER)
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
            .create(RagiumRecipes.LASER_TRANSFORMER)
            .itemInput(Items.SKELETON_SKULL)
            .itemInput(input)
            .catalyst(tier)
            .itemOutput(skull)
            .save(output)
    }

    //    Alternative    //

    private fun registerAlt(output: RecipeOutput) {
        // Blaze Powder
        HTCookingRecipeBuilder
            .create(
                Ingredient.of(RagiumItems.CRIMSON_CRYSTAL),
                Items.BLAZE_POWDER,
                time = 500,
                types = HTCookingRecipeBuilder.BLASTING_TYPES,
            ).unlockedBy("has_crystal", has(RagiumItems.CRIMSON_CRYSTAL))
            .save(output)
        // Ender Pearl
        HTCookingRecipeBuilder
            .create(
                Ingredient.of(RagiumItems.WARPED_CRYSTAL),
                Items.ENDER_PEARL,
                time = 500,
                types = HTCookingRecipeBuilder.BLASTING_TYPES,
            ).unlockedBy("has_crystal", has(RagiumItems.WARPED_CRYSTAL))
            .save(output)

        // Mushroom Stew
        HTMachineRecipeBuilder
            .create(RagiumRecipes.MIXER)
            .itemInput(Tags.Items.MUSHROOMS, 2)
            .milkInput()
            .itemInput(Items.BOWL, 2)
            .itemOutput(Items.MUSHROOM_STEW, 2)
            .save(output)
        // Pumpkin Pie
        HTMachineRecipeBuilder
            .create(RagiumRecipes.ASSEMBLER)
            .itemInput(Tags.Items.CROPS_PUMPKIN)
            .itemInput(RagiumBlocks.SPONGE_CAKE)
            .itemOutput(Items.PUMPKIN_PIE, 2)
            .save(output)

        // Mud
        HTMachineRecipeBuilder
            .create(RagiumRecipes.MIXER)
            .itemInput(Items.DIRT)
            .waterInput()
            .itemOutput(Items.MUD)
            .save(output)
        // Packed Mud
        HTSingleItemRecipeBuilder
            .compressor()
            .itemInput(Items.MUD)
            .itemOutput(Items.PACKED_MUD)
            .save(output)

        registerSnow(output)
        registerStone(output)
    }

    private fun registerSnow(output: RecipeOutput) {
        // Water -> Ice
        HTRefineryRecipeBuilder()
            .waterInput()
            .itemOutput(Items.SNOW_BLOCK)
            .save(output)

        // Snow Block -> 4x Snow Ball
        HTMachineRecipeBuilder
            .create(RagiumRecipes.GRINDER)
            .itemInput(Items.SNOW_BLOCK)
            .itemOutput(Items.SNOWBALL, 4)
            .saveSuffixed(output, "_from_block")
        // Ice -> 4x Snow Ball
        HTMachineRecipeBuilder
            .create(RagiumRecipes.GRINDER)
            .itemInput(Items.ICE)
            .itemOutput(Items.SNOWBALL, 4)
            .saveSuffixed(output, "_from_ice")

        // Powder Snow
        HTMachineRecipeBuilder
            .create(RagiumRecipes.MIXER)
            .waterInput()
            .sources(RagiumBlockTags.COOLING_SOURCES)
            .fluidOutput(RagiumFluids.SNOW)
            .saveSuffixed(output, "_from_water")

        HTMachineRecipeBuilder
            .create(RagiumRecipes.ASSEMBLER)
            .itemInput(Tags.Items.BUCKETS_POWDER_SNOW)
            .itemOutput(Items.BUCKET)
            .fluidOutput(RagiumFluids.SNOW)
            .save(output, RagiumAPI.id("powder_snow_from_bucket"))

        HTMachineRecipeBuilder
            .create(RagiumRecipes.ASSEMBLER)
            .itemInput(Items.BUCKET)
            .fluidInput(RagiumFluids.SNOW)
            .itemOutput(Items.POWDER_SNOW_BUCKET)
            .save(output)
    }

    private fun registerStone(output: RecipeOutput) {
        fun registerRock(rock: ItemLike) {
            HTMachineRecipeBuilder
                .create(RagiumRecipes.MIXER)
                .condition(HTRockGeneratorCondition(Ingredient.of(rock)))
                .itemOutput(rock, 8)
                .save(output)
        }

        registerRock(Items.STONE)
        registerRock(Items.COBBLESTONE)
        registerRock(Items.GRANITE)
        registerRock(Items.DIORITE)
        registerRock(Items.ANDESITE)

        registerRock(Items.DEEPSLATE)
        registerRock(Items.COBBLED_DEEPSLATE)
        registerRock(Items.CALCITE)
        registerRock(Items.TUFF)
        registerRock(Items.DRIPSTONE_BLOCK)
        registerRock(Items.NETHERRACK)
        registerRock(Items.BASALT)
        registerRock(Items.BLACKSTONE)

        registerRock(Items.END_STONE)

        HTMachineRecipeBuilder
            .create(RagiumRecipes.MIXER)
            .fluidInput(Tags.Fluids.LAVA)
            .condition(HTRockGeneratorCondition(Ingredient.of(Items.OBSIDIAN)))
            .itemOutput(Items.OBSIDIAN)
            .save(output)
    }
}
