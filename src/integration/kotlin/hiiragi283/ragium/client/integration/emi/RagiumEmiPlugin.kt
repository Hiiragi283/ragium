package hiiragi283.ragium.client.integration.emi

import dev.emi.emi.EmiPort
import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiCraftingRecipe
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories
import dev.emi.emi.api.stack.Comparison
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.block.attribute.HTEnergyBlockAttribute
import hiiragi283.ragium.api.block.attribute.getAttributeOrThrow
import hiiragi283.ragium.api.data.map.HTFluidFuelData
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.api.function.partially1
import hiiragi283.ragium.api.item.createItemStack
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.createKey
import hiiragi283.ragium.api.registry.holdersSequence
import hiiragi283.ragium.api.registry.idOrThrow
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.client.integration.emi.data.HTEmiFluidFuelData
import hiiragi283.ragium.client.integration.emi.recipe.custom.HTExpExtractingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.generator.HTFuelGeneratorEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTAlloyingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTBrewingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTCrushingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTCuttingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTItemToItemEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTItemWithCatalystEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTMeltingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTMixingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTPlantingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTRefiningEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTWashingEmiRecipe
import hiiragi283.ragium.client.integration.emi.type.HTRecipeViewerType
import hiiragi283.ragium.client.integration.emi.type.HTRegistryRecipeViewerType
import hiiragi283.ragium.client.integration.emi.type.RagiumRecipeViewerTypes
import hiiragi283.ragium.common.fluid.HTFluidType
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.common.tier.HTComponentTier
import hiiragi283.ragium.common.util.HTPotionHelper
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.datamaps.DataMapType
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps

@EmiEntrypoint
class RagiumEmiPlugin : EmiPlugin {
    companion object {
        @JvmStatic
        internal var recipeManager: RecipeManager? = null

        @JvmStatic
        fun recipeManager(): RecipeManager = this.recipeManager ?: error("Recipe Manager not initialized")

        @JvmStatic
        private val categoryCache: MutableMap<HTRecipeViewerType<*>, HTEmiRecipeCategory> = hashMapOf()

        @JvmStatic
        private fun getCategory(viewerType: HTRecipeViewerType<*>): HTEmiRecipeCategory =
            categoryCache[viewerType] ?: error("Unknown recipe category for ${viewerType.getId()}")
    }

    override fun register(registry: EmiRegistry) {
        // Recipe
        recipeManager = registry.recipeManager

        addCustomRecipe(registry)
        addGenerators(registry)
        addProcessors(registry)
        addInteractions(registry)

        for (block: ItemLike in listOf(RagiumBlocks.ELECTRIC_FURNACE, RagiumBlocks.MULTI_SMELTER)) {
            registry.addWorkstation(VanillaEmiRecipeCategories.BLASTING, block.toEmi())
            registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, block.toEmi())
            registry.addWorkstation(VanillaEmiRecipeCategories.SMOKING, block.toEmi())
        }
        registry.addWorkstation(VanillaEmiRecipeCategories.SMITHING, RagiumBlocks.AUTO_SMITHING_TABLE.toEmi())
        registry.addWorkstation(VanillaEmiRecipeCategories.STONECUTTING, RagiumBlocks.AUTO_STONECUTTER.toEmi())
        // Functions
        registry.addGenericStackProvider(RagiumEmiStackProvider)

        val potion: Comparison = Comparison.compareData { stack: EmiStack -> stack.get(DataComponents.POTION_CONTENTS) }
        registry.setDefaultComparison(RagiumItems.ICE_CREAM_SODA.get(), potion)
        registry.setDefaultComparison(RagiumItems.POTION_DROP.get(), potion)

        registry.setDefaultComparison(
            RagiumItems.LOOT_TICKET.get(),
            Comparison.compareData { stack: EmiStack -> stack.get(RagiumDataComponents.LOOT_TICKET) },
        )
    }

    //    Recipes    //

    private fun addCustomRecipe(registry: EmiRegistry) {
        // Crafting
        EmiPort.getPotionRegistry().holdersSequence().forEach { potion: Holder<Potion> ->
            addPotionRecipes(registry, potion, potion.idOrThrow)
        }

        val itemRegistry: Registry<Item> = EmiPort.getItemRegistry()
        itemRegistry
            .asSequence()
            .filter { item: Item -> item.defaultInstance.isDamageableItem }
            .forEach { item: Item ->
                val id: ResourceLocation = item.toHolderLike().getId()
                registry.addCustomRecipe(id, "eternal_upgrade") { id1: ResourceLocation ->
                    EmiCraftingRecipe(
                        listOf(
                            EmiStack.of(item),
                            RagiumItems.getComponent(HTComponentTier.ETERNAL).toEmi(),
                        ),
                        createItemStack(item, DataComponents.UNBREAKABLE, Unbreakable(true)).toEmi(),
                        id1,
                        true,
                    )
                }
            }
        for (holder: Holder<Item> in itemRegistry.getTagOrEmpty(ItemTags.CHEST_ARMOR)) {
            val item: HTItemHolderLike = HTItemHolderLike.fromHolder(holder)
            val id: ResourceLocation = item.getId()
            registry.addCustomRecipe(id, "gravitational_upgrade") { id1: ResourceLocation ->
                EmiCraftingRecipe(
                    listOf(
                        EmiStack.of(item),
                        RagiumItems.GRAVITATIONAL_UNIT.toEmi(),
                    ),
                    createItemStack(item, RagiumDataComponents.ANTI_GRAVITY, true).toEmi(),
                    id1,
                    true,
                )
            }
        }
    }

    private fun addPotionRecipes(registry: EmiRegistry, potion: Holder<Potion>, potionId: ResourceLocation) {
        // Ice Cream Soda
        registry.addCustomRecipe(potionId, "ice_cream_soda") { id1: ResourceLocation ->
            EmiCraftingRecipe(
                listOf(
                    RagiumItems.ICE_CREAM.toEmi(),
                    CommonMaterialPrefixes.FOOD.toItemEmi(FoodMaterialKeys.RAGI_CHERRY),
                    HTPotionHelper.createPotion(Items.POTION, potion).toEmi(),
                    Tags.Items.DYES_GREEN.toEmi(),
                ),
                HTPotionHelper.createPotion(RagiumItems.ICE_CREAM_SODA, potion).toEmi(),
                id1,
                true,
            )
        }
        // Potion Drops
        registry.addCustomRecipe(potionId, "potion") { id1: ResourceLocation ->
            EmiCraftingRecipe(
                listOf(
                    HTPotionHelper.createPotion(RagiumItems.POTION_DROP, potion).toEmi(),
                    Items.GLASS_BOTTLE.toEmi(),
                    Items.GLASS_BOTTLE.toEmi(),
                    Items.GLASS_BOTTLE.toEmi(),
                    Items.GLASS_BOTTLE.toEmi(),
                ),
                HTPotionHelper.createPotion(Items.POTION, potion, 4).toEmi(),
                id1,
                true,
            )
        }
    }

    private fun addGenerators(registry: EmiRegistry) {
        val thermalUsage: Int = RagiumBlocks.THERMAL_GENERATOR.getAttributeOrThrow<HTEnergyBlockAttribute>().getUsage()
        val combustionUsage: Int = RagiumBlocks.COMBUSTION_GENERATOR.getAttributeOrThrow<HTEnergyBlockAttribute>().getUsage()

        addCategoryAndFuelRecipes(registry, RagiumRecipeViewerTypes.THERMAL, RagiumDataMaps.THERMAL_FUEL, thermalUsage)
        addCategoryAndFuelRecipes(registry, RagiumRecipeViewerTypes.COMBUSTION, RagiumDataMaps.COMBUSTION_FUEL, combustionUsage)

        val fluidRegistry: Registry<Fluid> = EmiPort.getFluidRegistry()
        val itemRegistry: Registry<Item> = EmiPort.getItemRegistry()

        // Thermal Generator
        val lavaFuelData: HTFluidFuelData = fluidRegistry.getData(
            RagiumDataMaps.THERMAL_FUEL,
            Registries.FLUID.createKey(HTFluidContent.LAVA.getId()),
        ) ?: return
        addRecipes(
            registry,
            RagiumRecipeViewerTypes.THERMAL,
            itemRegistry
                .getDataMap(NeoForgeDataMaps.FURNACE_FUELS)
                .map { (key: ResourceKey<Item>, fuel: FurnaceFuel) ->
                    val lavaInput: EmiStack = HTFluidContent.LAVA.toFluidEmi(fuel.burnTime / 10)
                    key.location().withPrefix("/${RagiumDataMaps.THERMAL_FUEL.id().path}/") to
                        HTEmiFluidFuelData(
                            thermalUsage,
                            lavaFuelData,
                            itemRegistry.getOrThrow(key).toEmi(),
                            lavaInput,
                        )
                }.asSequence(),
            ::HTFuelGeneratorEmiRecipe,
        )
        // Combustion Generator
        val crudeOilFuelData: HTFluidFuelData = fluidRegistry.getData(
            RagiumDataMaps.THERMAL_FUEL,
            Registries.FLUID.createKey(RagiumFluidContents.CRUDE_OIL.getId()),
        ) ?: return
        registry.addRecipeSafe(RagiumDataMaps.THERMAL_FUEL.id().withPrefix("/")) { id: ResourceLocation ->
            HTFuelGeneratorEmiRecipe(
                getCategory(RagiumRecipeViewerTypes.COMBUSTION),
                id,
                HTEmiFluidFuelData(
                    combustionUsage,
                    crudeOilFuelData,
                    ItemTags.COALS.toEmi(),
                    RagiumFluidContents.CRUDE_OIL.toFluidEmi(crudeOilFuelData.amount),
                ),
            )
        }
    }

    private fun addProcessors(registry: EmiRegistry) {
        // Basic
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.ALLOYING, ::HTAlloyingEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.COMPRESSING, ::HTItemToItemEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.CRUSHING, ::HTCrushingEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.CUTTING, ::HTCuttingEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.EXTRACTING, ::HTItemWithCatalystEmiRecipe)

        registry.addRecipeSafe(RagiumAPI.id("/${RagiumConst.EXTRACTING}", "experience_from_items")) {
            HTExpExtractingEmiRecipe(getCategory(RagiumRecipeViewerTypes.EXTRACTING), it)
        }
        // Advanced
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.FLUID_TRANSFORM, ::HTRefiningEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.MELTING, ::HTMeltingEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.MIXING, ::HTMixingEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.WASHING, ::HTWashingEmiRecipe)
        // Elite
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.BREWING, ::HTBrewingEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.PLANTING, ::HTPlantingEmiRecipe)
        addCategoryAndRecipes(registry, RagiumRecipeViewerTypes.SIMULATING, ::HTItemWithCatalystEmiRecipe)
    }

    private fun addInteractions(registry: EmiRegistry) {
        // Water Collector
        registry.addInteraction(HTFluidContent.WATER.toFluidEmi(), prefix = "fluid_generator") {
            leftInput(RagiumBlocks.WATER_COLLECTOR.toEmi())
            rightInput(EmiStack.EMPTY, false)
        }
        // Exp Collector
        registry.addInteraction(RagiumFluidContents.EXPERIENCE.toFluidEmi(), prefix = "fluid_generator") {
            leftInput(RagiumBlocks.EXP_COLLECTOR.toEmi())
            rightInput(EmiStack.EMPTY, false)
        }

        // World Vaporization
        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            val fluidType: FluidType = content.getType()
            if (fluidType is HTFluidType) {
                val result: EmiStack = fluidType.dropItem?.toEmi() ?: continue
                registry.addInteraction(result) {
                    leftInput(content.toTagEmi())
                    rightInput(EmiStack.EMPTY, false)
                }
            }
        }
        // Crude Oil + Lava -> Soul Sand
        registry.addFluidInteraction(Items.SOUL_SAND, RagiumFluidContents.CRUDE_OIL, HTFluidContent.LAVA)
        // Water + Eldritch Flux -> Eldritch Stone
        registry.addFluidInteraction(RagiumBlocks.ELDRITCH_STONE, HTFluidContent.WATER, RagiumFluidContents.ELDRITCH_FLUX)

        // Budding Azure
        registry.addInteraction(RagiumBlocks.BUDDING_AZURE.toEmi()) {
            leftInput(Blocks.BUDDING_AMETHYST.toEmi())
            rightInput(RagiumModTags.Items.BUDDING_AZURE_ACTIVATOR.toEmi(), false)
        }
    }

    //    Extension    //

    private inline fun EmiRegistry.addRecipeSafe(id: ResourceLocation, factory: (ResourceLocation) -> EmiRecipe) {
        runCatching {
            addRecipe(factory(id))
        }.onFailure { throwable: Throwable ->
            RagiumAPI.LOGGER.warn("Exception thrown when parsing vanilla recipe: $id", throwable)
        }
    }

    private inline fun EmiRegistry.addCustomRecipe(id: ResourceLocation, prefix: String, factory: (ResourceLocation) -> EmiRecipe) {
        addRecipeSafe(id.withPrefix("/shapeless/${RagiumAPI.MOD_ID}/$prefix"), factory)
    }

    /**
     * @see mekanism.client.recipe_viewer.emi.MekanismEmi.addCategoryAndRecipes
     */
    private inline fun <INPUT : RecipeInput, BASE : Recipe<INPUT>, reified RECIPE : BASE, EMI_RECIPE : EmiRecipe> addCategoryAndRecipes(
        registry: EmiRegistry,
        viewerType: HTRegistryRecipeViewerType<INPUT, BASE>,
        noinline factory: (HTEmiRecipeCategory, RecipeHolder<RECIPE>) -> EMI_RECIPE?,
    ): HTEmiRecipeCategory {
        val category: HTEmiRecipeCategory = registerCategory(registry, viewerType)
        viewerType
            .getAllHolders(recipeManager())
            .mapNotNull { holder: RecipeHolder<out BASE> ->
                val id: ResourceLocation = holder.id
                val recipe: BASE = holder.value
                if (recipe is RECIPE) {
                    RecipeHolder(id, recipe)
                } else {
                    RagiumAPI.LOGGER.warn("Skipped recipe for EMI registration: $id")
                    null
                }
            }.mapNotNull(factory.partially1(category))
            .forEach(registry::addRecipe)
        return category
    }

    /**
     * 指定された引数からレシピを生成し，登録します。
     * @param RECIPE [recipes]で渡す一覧のクラス
     * @param EMI_RECIPE [factory]で返すレシピのクラス
     * @return 渡された[category]
     * @see mekanism.client.recipe_viewer.emi.MekanismEmi.addCategoryAndRecipes
     */
    private fun <RECIPE : Any, EMI_RECIPE : EmiRecipe> addCategoryAndRecipes(
        registry: EmiRegistry,
        viewerType: HTRecipeViewerType<RECIPE>,
        recipes: Sequence<Pair<ResourceLocation, RECIPE>>,
        factory: (HTEmiRecipeCategory, ResourceLocation, RECIPE) -> EMI_RECIPE?,
    ): HTEmiRecipeCategory = addRecipes(registry, registerCategory(registry, viewerType), recipes, factory)

    /**
     * 指定された引数からレシピを生成し，登録します。
     * @param CATEGORY [category]のクラス
     * @param RECIPE [recipes]で渡す一覧のクラス
     * @param EMI_RECIPE [factory]で返すレシピのクラス
     * @return 渡された[category]
     */
    private fun <CATEGORY : EmiRecipeCategory, RECIPE : Any, EMI_RECIPE : EmiRecipe> addRecipes(
        registry: EmiRegistry,
        category: CATEGORY,
        recipes: Sequence<Pair<ResourceLocation, RECIPE>>,
        factory: (CATEGORY, ResourceLocation, RECIPE) -> EMI_RECIPE?,
    ): CATEGORY {
        recipes.mapNotNull { (id: ResourceLocation, recipe: RECIPE) -> factory(category, id, recipe) }.forEach(registry::addRecipe)
        return category
    }

    /**
     * 指定された引数からレシピを生成し，登録します。
     * @param RECIPE [recipes]で渡す一覧のクラス
     * @param EMI_RECIPE [factory]で返すレシピのクラス
     * @return 渡された[category]
     */
    private fun <RECIPE : Any, EMI_RECIPE : EmiRecipe> addRecipes(
        registry: EmiRegistry,
        viewerType: HTRecipeViewerType<*>,
        recipes: Sequence<Pair<ResourceLocation, RECIPE>>,
        factory: (HTEmiRecipeCategory, ResourceLocation, RECIPE) -> EMI_RECIPE?,
    ): HTEmiRecipeCategory {
        val category: HTEmiRecipeCategory = getCategory(viewerType)
        recipes.mapNotNull { (id: ResourceLocation, recipe: RECIPE) -> factory(category, id, recipe) }.forEach(registry::addRecipe)
        return category
    }

    /**
     * 指定された[viewerType]から[HTEmiRecipeCategory]を返します。
     */
    private fun registerCategory(registry: EmiRegistry, viewerType: HTRecipeViewerType<*>): HTEmiRecipeCategory {
        val category: HTEmiRecipeCategory = HTEmiRecipeCategory.create(viewerType)
        check(categoryCache.put(viewerType, category) == null) { "Duplicated recipe category for ${viewerType.getId()}" }
        registry.addCategory(category)
        viewerType.workStations.map(ItemLike::toEmi).forEach(registry::addWorkstation.partially1(category))
        return category
    }

    private fun addCategoryAndFuelRecipes(
        registry: EmiRegistry,
        viewerType: HTRecipeViewerType<HTEmiFluidFuelData>,
        dataMapType: DataMapType<Fluid, HTFluidFuelData>,
        energyRate: Int,
    ): HTEmiRecipeCategory {
        val fluidRegistry: Registry<Fluid> = EmiPort.getFluidRegistry()
        return addCategoryAndRecipes(
            registry,
            viewerType,
            fluidRegistry
                .getDataMap(dataMapType)
                .map { (key: ResourceKey<Fluid>, fuelData: HTFluidFuelData) ->
                    key.location().withPrefix("/${dataMapType.id().path}/") to
                        HTEmiFluidFuelData(
                            energyRate,
                            fuelData,
                            EmiStack.EMPTY,
                            fluidRegistry.getOrThrow(key).let(EmiStack::of).setAmount(fuelData.amount.toLong()),
                        )
                }.asSequence(),
            ::HTFuelGeneratorEmiRecipe,
        )
    }

    private fun EmiRegistry.addInteraction(
        output: EmiStack,
        id: ResourceLocation = output.id,
        prefix: String = "interaction",
        builderAction: EmiWorldInteractionRecipe.Builder.() -> Unit,
    ) {
        addRecipeSafe(RagiumAPI.id("/world/$prefix/${id.toString().replace(':', '/')}")) { id1: ResourceLocation ->
            EmiWorldInteractionRecipe
                .builder()
                .apply(builderAction)
                .id(id1)
                .output(output)
                .build()
        }
    }

    /**
     * @see dev.emi.emi.VanillaPlugin.addWorldInteraction
     */
    private fun EmiRegistry.addFluidInteraction(output: ItemLike, source: HTFluidContent<*, *, *>, flowing: HTFluidContent<*, *, *>) {
        addInteraction(output.toEmi(), prefix = "fluid_interaction") {
            leftInput(source.toFluidEmi(1000).copyAsCatalyst())
            rightInput(flowing.toFluidEmi(1000).copyAsCatalyst(), false)
        }
    }
}
