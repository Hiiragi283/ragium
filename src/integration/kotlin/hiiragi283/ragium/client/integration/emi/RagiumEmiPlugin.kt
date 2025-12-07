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
import hiiragi283.ragium.api.data.map.HTFluidCoolantData
import hiiragi283.ragium.api.data.map.HTFluidFuelData
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.function.partially1
import hiiragi283.ragium.api.item.alchemy.HTPotionHelper
import hiiragi283.ragium.api.item.createItemStack
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.math.toFraction
import hiiragi283.ragium.api.registry.HTFluidHolderLike
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.getHolderDataMap
import hiiragi283.ragium.api.registry.idOrThrow
import hiiragi283.ragium.client.integration.emi.category.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.category.HTRegistryEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.category.RagiumEmiRecipeCategories
import hiiragi283.ragium.client.integration.emi.data.HTBiomassRecipeData
import hiiragi283.ragium.client.integration.emi.data.HTEmiFluidFuelData
import hiiragi283.ragium.client.integration.emi.handler.HTEmiRecipeHandler
import hiiragi283.ragium.client.integration.emi.recipe.custom.HTCopyEnchantingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.custom.HTExpExtractingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.custom.HTMachineUpgradeEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.device.HTRockGeneratingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.generator.HTBiomassEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.generator.HTCoolantEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.generator.HTFuelGeneratorEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTAlloyingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTBrewingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTEnchantingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTItemWithCatalystEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTMeltingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTMixingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTPlantingEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTRefiningEmiRecipe
import hiiragi283.ragium.client.integration.emi.recipe.processor.HTSingleExtraItemEmiRecipe
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.generator.HTCulinaryGeneratorBlockEntity
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.FoodMaterialKeys
import hiiragi283.ragium.setup.DeferredBEMenu
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.datamaps.DataMapType
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import org.apache.commons.lang3.math.Fraction
import kotlin.streams.asSequence

@EmiEntrypoint
class RagiumEmiPlugin : EmiPlugin {
    companion object {
        @JvmStatic
        private val ITEM_LOOKUP: HolderLookup.RegistryLookup<Item> by lazy(EmiPort.getItemRegistry()::asLookup)

        @JvmStatic
        private val FLUID_LOOKUP: HolderLookup.RegistryLookup<Fluid> by lazy {
            EmiPort.getFluidRegistry().asLookup().filterElements { fluid: Fluid ->
                fluid != Fluids.EMPTY && fluid.isSource(fluid.defaultFluidState())
            }
        }
    }

    override fun register(registry: EmiRegistry) {
        // Recipe
        addRecipes(
            registry,
            RagiumEmiRecipeCategories.MACHINE_UPGRADE,
            ITEM_LOOKUP
                .filterElements { item: Item -> item.defaultInstance.has(RagiumDataComponents.MACHINE_UPGRADE_FILTER) }
                .listElements()
                .map { holder: Holder<Item> ->
                    holder.idOrThrow.withPrefix("/machine/upgrade/") to EmiStack.of(holder.value())
                }.asSequence(),
            ::HTMachineUpgradeEmiRecipe,
        )

        addCustomRecipe(registry)
        addGenerators(registry)
        addProcessors(registry)
        addInteractions(registry)

        // Workstations
        for (block: ItemLike in listOf(RagiumBlocks.ELECTRIC_FURNACE, RagiumBlocks.MULTI_SMELTER)) {
            registry.addWorkstation(VanillaEmiRecipeCategories.BLASTING, block.toEmi())
            registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, block.toEmi())
            registry.addWorkstation(VanillaEmiRecipeCategories.SMOKING, block.toEmi())
        }
        // Functions
        registry.addGenericStackProvider(RagiumEmiStackProvider)

        val potion: Comparison = Comparison.compareData { stack: EmiStack -> stack.get(DataComponents.POTION_CONTENTS) }
        registry.setDefaultComparison(RagiumItems.ICE_CREAM_SODA.get(), potion)
        registry.setDefaultComparison(RagiumItems.POTION_DROP.get(), potion)

        registry.setDefaultComparison(
            RagiumItems.LOOT_TICKET.get(),
            Comparison.compareData { stack: EmiStack -> stack.get(RagiumDataComponents.LOOT_TICKET) },
        )

        addRecipeHandler(registry)
    }

    private fun addRecipeHandler(registry: EmiRegistry) {
        // Generator
        addRecipeHandler(
            registry,
            RagiumMenuTypes.ITEM_GENERATOR,
            RagiumEmiRecipeCategories.THERMAL,
            RagiumEmiRecipeCategories.CULINARY,
        )
        // Processor
        addRecipeHandler(
            registry,
            RagiumMenuTypes.SMELTER,
            VanillaEmiRecipeCategories.BLASTING,
            VanillaEmiRecipeCategories.SMELTING,
            VanillaEmiRecipeCategories.SMOKING,
        )

        addRecipeHandler(registry, RagiumMenuTypes.ALLOY_SMELTER, RagiumEmiRecipeCategories.ALLOYING)
        addRecipeHandler(registry, RagiumMenuTypes.BREWERY, RagiumEmiRecipeCategories.BREWING)
        addRecipeHandler(registry, RagiumMenuTypes.COMPRESSOR, RagiumEmiRecipeCategories.COMPRESSING)
        addRecipeHandler(registry, RagiumMenuTypes.CUTTING_MACHINE, RagiumEmiRecipeCategories.CUTTING)
        addRecipeHandler(registry, RagiumMenuTypes.ENCHANTER, RagiumEmiRecipeCategories.ENCHANTING)
        addRecipeHandler(registry, RagiumMenuTypes.EXTRACTOR, RagiumEmiRecipeCategories.EXTRACTING)
        addRecipeHandler(registry, RagiumMenuTypes.MELTER, RagiumEmiRecipeCategories.MELTING)
        addRecipeHandler(registry, RagiumMenuTypes.SIMULATOR, RagiumEmiRecipeCategories.SIMULATING)
        addRecipeHandler(registry, RagiumMenuTypes.SINGLE_ITEM_WITH_FLUID, RagiumEmiRecipeCategories.CRUSHING)
    }

    private fun <BE : HTBlockEntity> addRecipeHandler(
        registry: EmiRegistry,
        menuType: DeferredBEMenu<BE>,
        vararg categories: EmiRecipeCategory,
    ) {
        registry.addRecipeHandler(menuType.get(), HTEmiRecipeHandler(*categories))
    }

    //    Recipes    //

    private fun addCustomRecipe(registry: EmiRegistry) {
        // Crafting
        EmiPort
            .getPotionRegistry()
            .holders()
            .forEach { potion: Holder<Potion> -> addPotionRecipes(registry, potion, potion.idOrThrow) }

        ITEM_LOOKUP
            .filterElements { item: Item -> item.defaultInstance.isDamageableItem }
            .listElements()
            .forEach { holder: Holder<Item> ->
                val item: Item = holder.value()
                registry.addCustomRecipe(holder.idOrThrow, "eternal_upgrade") { id1: ResourceLocation ->
                    EmiCraftingRecipe(
                        listOf(
                            EmiStack.of(item),
                            RagiumItems.ETERNAL_COMPONENT.toEmi(),
                        ),
                        createItemStack(item, DataComponents.UNBREAKABLE, Unbreakable(true)).toEmi(),
                        id1,
                        true,
                    )
                }
            }
        for (holder: Holder<Item> in ITEM_LOOKUP.getOrThrow(ItemTags.CHEST_ARMOR)) {
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
        fun addFuelRecipes(category: HTEmiRecipeCategory, dataMapType: DataMapType<Fluid, HTFluidFuelData>) {
            addDataMapRecipes(
                registry,
                category,
                FLUID_LOOKUP,
                dataMapType,
                { holder: Holder<Fluid>, data: HTFluidFuelData ->
                    val stack: EmiStack = holder.value().toEmi(100).takeUnless(EmiStack::isEmpty) ?: return@addDataMapRecipes null
                    HTEmiFluidFuelData(stack, data.time)
                },
                ::HTFuelGeneratorEmiRecipe,
            )
        }

        // Basic
        addItemStackRecipes(
            registry,
            RagiumEmiRecipeCategories.THERMAL,
            { stack: ItemStack ->
                val burnTime: Int = stack.getBurnTime(null)
                if (burnTime <= 0) return@addItemStackRecipes null
                val stack1: EmiStack = stack.toEmi()
                stack1.remainder = stack.craftingRemainingItem.toEmi()
                HTEmiFluidFuelData(stack1, burnTime)
            },
            ::HTFuelGeneratorEmiRecipe,
        )
        // Advanced
        addFuelRecipes(RagiumEmiRecipeCategories.MAGMATIC, RagiumDataMapTypes.MAGMATIC_FUEL)

        addItemStackRecipes(
            registry,
            RagiumEmiRecipeCategories.CULINARY,
            { stack: ItemStack ->
                val food: FoodProperties = stack.getFoodProperties(null) ?: return@addItemStackRecipes null
                val stack1: EmiStack = stack.toEmi()
                food.usingConvertsTo().map(ItemStack::toEmi).ifPresent(stack1::setRemainder)
                HTEmiFluidFuelData(stack1, HTCulinaryGeneratorBlockEntity.getTime(food))
            },
            ::HTFuelGeneratorEmiRecipe,
        )
        // Elite
        addDataMapRecipes(
            registry,
            RagiumEmiRecipeCategories.BIOMASS,
            ITEM_LOOKUP,
            NeoForgeDataMaps.COMPOSTABLES,
            { holder: Holder<Item>, compostable: Compostable ->
                val chance: Fraction = compostable.chance().toFraction()
                HTBiomassRecipeData(
                    holder.value().toEmi(),
                    RagiumFluidContents.CRUDE_BIO.toFluidEmi((1000 * chance).toInt()),
                )
            },
            ::HTBiomassEmiRecipe,
        )

        addDataMapRecipes(
            registry,
            RagiumEmiRecipeCategories.COOLANT,
            FLUID_LOOKUP,
            RagiumDataMapTypes.COOLANT,
            { holder: Holder<Fluid>, data: HTFluidCoolantData ->
                holder.value().toEmi(data.amount).takeUnless(EmiStack::isEmpty)
            },
            ::HTCoolantEmiRecipe,
        )

        addFuelRecipes(RagiumEmiRecipeCategories.COMBUSTION, RagiumDataMapTypes.COMBUSTION_FUEL)
    }

    private fun addProcessors(registry: EmiRegistry) {
        // Basic
        addRegistryRecipes(registry, RagiumEmiRecipeCategories.ALLOYING, ::HTAlloyingEmiRecipe)
        addRegistryRecipes(registry, RagiumEmiRecipeCategories.COMPRESSING, ::HTItemWithCatalystEmiRecipe)
        addRegistryRecipes(registry, RagiumEmiRecipeCategories.CRUSHING, ::HTSingleExtraItemEmiRecipe)
        addRegistryRecipes(registry, RagiumEmiRecipeCategories.CUTTING, ::HTSingleExtraItemEmiRecipe)
        addRegistryRecipes(registry, RagiumEmiRecipeCategories.EXTRACTING, ::HTItemWithCatalystEmiRecipe)

        registry.addRecipeSafe(RagiumAPI.id("/${RagiumConst.EXTRACTING}", "experience_from_items")) {
            HTExpExtractingEmiRecipe(RagiumEmiRecipeCategories.EXTRACTING, it)
        }
        // Advanced
        addRegistryRecipes(registry, RagiumEmiRecipeCategories.MELTING, ::HTMeltingEmiRecipe)
        addRegistryRecipes(registry, RagiumEmiRecipeCategories.MIXING, ::HTMixingEmiRecipe)
        addRegistryRecipes(registry, RagiumEmiRecipeCategories.REFINING, ::HTRefiningEmiRecipe)
        // Elite
        addRegistryRecipes(registry, RagiumEmiRecipeCategories.BREWING, ::HTBrewingEmiRecipe)
        addRegistryRecipes(registry, RagiumEmiRecipeCategories.PLANTING, ::HTPlantingEmiRecipe)
        // Ultimate
        addRegistryRecipes(registry, RagiumEmiRecipeCategories.ENCHANTING, ::HTEnchantingEmiRecipe)
        addRegistryRecipes(registry, RagiumEmiRecipeCategories.SIMULATING, ::HTItemWithCatalystEmiRecipe)

        registry.addRecipeSafe(RagiumAPI.id("/${RagiumConst.ENCHANTING}", "copy_from_book")) {
            HTCopyEnchantingEmiRecipe(RagiumEmiRecipeCategories.ENCHANTING, it)
        }

        // Device
        addRegistryRecipes(registry, RagiumEmiRecipeCategories.ROCK_GENERATING, ::HTRockGeneratingEmiRecipe)
    }

    private fun addInteractions(registry: EmiRegistry) {
        // Water from Collector
        registry.addInteraction(HTFluidHolderLike.WATER.toFluidEmi(), prefix = "fluid_generator") {
            leftInput(RagiumBlocks.FLUID_COLLECTOR.toEmi())
            rightInput(EmiStack.EMPTY, false)
        }
        // Experience from Collector
        registry.addInteraction(RagiumFluidContents.EXPERIENCE.toFluidEmi(), prefix = "fluid_generator") {
            leftInput(RagiumBlocks.FLUID_COLLECTOR.toEmi())
            rightInput(RagiumItems.EXP_COLLECTOR_UPGRADE.toEmi(), false)
        }

        // World Vaporization
        /*for (content: HTFluidContent<*, *, *, *, *> in RagiumFluidContents.REGISTER.contents) {
            val fluidType: FluidType = content.getType()
            if (fluidType is HTFluidType) {
                val result: EmiStack = fluidType.dropItem?.toEmi() ?: continue
                registry.addInteraction(result) {
                    leftInput(content.toTagEmi())
                    rightInput(EmiStack.EMPTY, false)
                }
            }
        }*/
        // Crude Oil + Lava -> Soul Sand
        registry.addFluidInteraction(Items.SOUL_SAND, RagiumFluidContents.CRUDE_OIL, HTFluidHolderLike.LAVA)
        // Water + Eldritch Flux -> Eldritch Stone
        registry.addFluidInteraction(RagiumBlocks.ELDRITCH_STONE, HTFluidHolderLike.WATER, RagiumFluidContents.ELDRITCH_FLUX)
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
    private inline fun <INPUT : RecipeInput, BASE : Recipe<INPUT>, reified RECIPE : BASE, EMI_RECIPE : EmiRecipe> addRegistryRecipes(
        registry: EmiRegistry,
        category: HTRegistryEmiRecipeCategory<INPUT, BASE>,
        noinline factory: (HTEmiRecipeCategory, RecipeHolder<RECIPE>) -> EMI_RECIPE?,
    ) {
        registerCategory(registry, category)
        category
            .getAllHolders(registry.recipeManager)
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
    }

    private fun <RECIPE : Any, EMI_RECIPE : EmiRecipe> addItemStackRecipes(
        registry: EmiRegistry,
        category: HTEmiRecipeCategory,
        recipeFactory: (ItemStack) -> RECIPE?,
        factory: (HTEmiRecipeCategory, ResourceLocation, RECIPE) -> EMI_RECIPE?,
    ) {
        addRecipes(
            registry,
            category,
            ITEM_LOOKUP
                .listElements()
                .asSequence()
                .mapNotNull { holder: Holder<Item> ->
                    val item: Item = holder.value()
                    val stack: ItemStack = item.defaultInstance
                    val recipe: RECIPE = recipeFactory(stack) ?: return@mapNotNull null
                    val typeId: ResourceLocation = category.id
                    val id: ResourceLocation = holder.idOrThrow.withPrefix("/${typeId.namespace}/${typeId.path}/")
                    id to recipe
                },
            factory,
        )
    }

    /**
     * 指定された引数からレシピを生成し，登録します。
     * @param R レジストリのクラス
     * @param T [DataMapType]のクラス
     * @param RECIPE [recipeFactory]で渡す一覧のクラス
     * @param EMI_RECIPE [factory]で返すレシピのクラス
     * @return 渡された[category]
     * @see mekanism.client.recipe_viewer.emi.MekanismEmi.addCategoryAndRecipes
     */
    private fun <R : Any, T : Any, RECIPE : Any, EMI_RECIPE : EmiRecipe> addDataMapRecipes(
        registry: EmiRegistry,
        category: HTEmiRecipeCategory,
        lookup: HolderLookup.RegistryLookup<R>,
        dataMapType: DataMapType<R, T>,
        recipeFactory: (Holder<R>, T) -> RECIPE?,
        factory: (HTEmiRecipeCategory, ResourceLocation, RECIPE) -> EMI_RECIPE?,
    ) {
        addRecipes(
            registry,
            category,
            lookup
                .getHolderDataMap(dataMapType)
                .mapNotNull { (holder: Holder.Reference<R>, value: T) ->
                    val recipe: RECIPE = recipeFactory(holder, value) ?: return@mapNotNull null
                    val typeId: ResourceLocation = dataMapType.id()
                    val id: ResourceLocation = holder.idOrThrow.withPrefix("/${typeId.namespace}/${typeId.path}/")
                    id to recipe
                }.asSequence(),
            factory,
        )
    }

    /**
     * 指定された引数からレシピを生成し，登録します。
     * @param RECIPE [recipes]で渡す一覧のクラス
     * @param EMI_RECIPE [factory]で返すレシピのクラス
     * @return 渡された[category]
     */
    private fun <RECIPE : Any, EMI_RECIPE : EmiRecipe> addRecipes(
        registry: EmiRegistry,
        category: HTEmiRecipeCategory,
        recipes: Sequence<Pair<ResourceLocation, RECIPE>>,
        factory: (HTEmiRecipeCategory, ResourceLocation, RECIPE) -> EMI_RECIPE?,
    ) {
        registerCategory(registry, category)
        recipes.mapNotNull { (id: ResourceLocation, recipe: RECIPE) -> factory(category, id, recipe) }.forEach(registry::addRecipe)
    }

    private fun registerCategory(registry: EmiRegistry, category: HTEmiRecipeCategory) {
        registry.addCategory(category)
        category.workStations.forEach(registry::addWorkstation.partially1(category))
    }

    private fun EmiRegistry.addInteraction(
        output: EmiStack,
        id: ResourceLocation = output.id,
        prefix: String = "interaction",
        builderAction: EmiWorldInteractionRecipe.Builder.() -> Unit,
    ) {
        addRecipeSafe(RagiumAPI.id("/world", prefix, id.toString().replace(':', '/'))) { id1: ResourceLocation ->
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
    private fun EmiRegistry.addFluidInteraction(output: ItemLike, source: HTFluidHolderLike, flowing: HTFluidHolderLike) {
        addInteraction(output.toEmi(), prefix = "fluid_interaction") {
            leftInput(source.toFluidEmi(1000).copyAsCatalyst())
            rightInput(flowing.toFluidEmi(1000).copyAsCatalyst(), false)
        }
    }
}
