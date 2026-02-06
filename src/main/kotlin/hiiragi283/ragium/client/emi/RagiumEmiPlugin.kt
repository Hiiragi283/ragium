package hiiragi283.ragium.client.emi

import dev.emi.emi.EmiPort
import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiCraftingRecipe
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories
import dev.emi.emi.api.stack.Comparison
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.integration.emi.HTEmiPlugin
import hiiragi283.core.api.integration.emi.HTEmiRecipeCategory
import hiiragi283.core.api.integration.emi.toEmi
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.registry.HTHolderLike
import hiiragi283.core.api.registry.getHolderDataMap
import hiiragi283.core.api.registry.toLike
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.client.emi.recipe.HTAlloyingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTAssemblingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTCompressingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTCrushingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTCuttingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTEnchantingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTItemToFluidEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTMixingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTPlantingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTPressingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTPyrolyzingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTReactingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTRefiningEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTSolidifyingEmiRecipe
import hiiragi283.ragium.client.emi.recipe.HTWashingEmiRecipe
import hiiragi283.ragium.common.block.HTImitationSpawnerBlock
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.DataMapType
import kotlin.streams.asSequence

@EmiEntrypoint
class RagiumEmiPlugin : HTEmiPlugin(RagiumAPI.MOD_ID) {
    override fun register(registry: EmiRegistry) {
        // Category
        listOf(
            // Generator
            RagiumEmiRecipeCategories.THERMAL,
            RagiumEmiRecipeCategories.CULINARY,
            RagiumEmiRecipeCategories.MAGMATIC,
            RagiumEmiRecipeCategories.COOLANT,
            RagiumEmiRecipeCategories.COMBUSTION,
            // Machine - Basic
            RagiumEmiRecipeCategories.ALLOYING,
            RagiumEmiRecipeCategories.ASSEMBLING,
            RagiumEmiRecipeCategories.CRUSHING,
            RagiumEmiRecipeCategories.CUTTING,
            RagiumEmiRecipeCategories.PRESSING,
            // Machine - Heat
            RagiumEmiRecipeCategories.MELTING,
            RagiumEmiRecipeCategories.PYROLYZING,
            RagiumEmiRecipeCategories.REFINING,
            RagiumEmiRecipeCategories.SOLIDIFYING,
            // Machine - Chemical
            RagiumEmiRecipeCategories.MIXING,
            RagiumEmiRecipeCategories.REACTING,
            RagiumEmiRecipeCategories.WASHING,
            // Device
            RagiumEmiRecipeCategories.COMPRESSING,
            RagiumEmiRecipeCategories.ENCHANTING,
            RagiumEmiRecipeCategories.FERMENTING,
            RagiumEmiRecipeCategories.PLANTING,
        ).forEach(::addCategory.partially1(registry))

        // Recipes
        addCustomRecipes(registry)
        addGenerators(registry)
        addInteractions(registry)

        addRegistryRecipes(registry, RagiumRecipeTypes.ALLOYING, ::HTAlloyingEmiRecipe)
        addRegistryRecipes(registry, RagiumRecipeTypes.ASSEMBLING, ::HTAssemblingEmiRecipe)
        addRegistryRecipes(registry, RagiumRecipeTypes.CRUSHING, ::HTCrushingEmiRecipe)
        addRegistryRecipes(registry, RagiumRecipeTypes.CUTTING, ::HTCuttingEmiRecipe)
        addRegistryRecipes(registry, RagiumRecipeTypes.PRESSING, ::HTPressingEmiRecipe)

        addRegistryRecipes(registry, RagiumRecipeTypes.MELTING, HTItemToFluidEmiRecipe.Companion::melting)
        addRegistryRecipes(registry, RagiumRecipeTypes.PYROLYZING, ::HTPyrolyzingEmiRecipe)
        addRegistryRecipes(registry, RagiumRecipeTypes.REFINING, ::HTRefiningEmiRecipe)
        addRegistryRecipes(registry, RagiumRecipeTypes.SOLIDIFYING, ::HTSolidifyingEmiRecipe)

        addRegistryRecipes(registry, RagiumRecipeTypes.MIXING, ::HTMixingEmiRecipe)
        addRegistryRecipes(registry, RagiumRecipeTypes.REACTING, ::HTReactingEmiRecipe)
        addRegistryRecipes(registry, RagiumRecipeTypes.WASHING, ::HTWashingEmiRecipe)

        addRegistryRecipes(registry, RagiumRecipeTypes.COMPRESSING, ::HTCompressingEmiRecipe)
        addRegistryRecipes(registry, RagiumRecipeTypes.ENCHANTING, ::HTEnchantingEmiRecipe)
        addRegistryRecipes(registry, RagiumRecipeTypes.PLANTING, ::HTPlantingEmiRecipe)
        // Misc
        registry.addWorkstation(VanillaEmiRecipeCategories.SMELTING, RagiumBlocks.ELECTRIC_FURNACE.toEmi())

        registry.setDefaultComparison(
            RagiumBlocks.UNIVERSAL_CHEST.asItem(),
            Comparison.compareData { stack: EmiStack -> stack.get(RagiumDataComponents.COLOR) },
        )
        registry.setDefaultComparison(
            RagiumBlocks.IMITATION_SPAWNER.asItem(),
            Comparison.compareData { stack: EmiStack -> stack.get(RagiumDataComponents.SPAWNER_MOB) },
        )

        registry.setDefaultComparison(
            RagiumItems.LOOT_TICKET.get(),
            Comparison.compareData { stack: EmiStack -> stack.get(RagiumDataComponents.LOOT_TICKET) },
        )

        val potion: Comparison = Comparison.compareData { stack: EmiStack -> stack.get(DataComponents.POTION_CONTENTS) }
        registry.setDefaultComparison(RagiumItems.POTION_DROP.get(), potion)
    }

    private fun addCustomRecipes(registry: EmiRegistry) {
        // Potion Drop
        EmiPort
            .getPotionRegistry()
            .holders()
            .forEach { potion: Holder<Potion> ->
                addRecipeSafe(
                    registry,
                    potion.toLike().getId().withPrefix("/${HTConst.SHAPELESS}/${RagiumAPI.MOD_ID}/potion"),
                ) { id: ResourceLocation ->
                    EmiCraftingRecipe(
                        listOf(
                            HTPotionHelper.createPotion(RagiumItems.POTION_DROP, potion).toEmi(),
                            Items.GLASS_BOTTLE.toEmi(),
                            Items.GLASS_BOTTLE.toEmi(),
                            Items.GLASS_BOTTLE.toEmi(),
                            Items.GLASS_BOTTLE.toEmi(),
                        ),
                        HTPotionHelper.createPotion(Items.POTION, potion, 4).toEmi(),
                        id,
                        true,
                    )
                }
            }
    }

    private fun addGenerators(registry: EmiRegistry) {
        fun addFuelRecipes(category: HTEmiRecipeCategory, dataMapType: DataMapType<Fluid, Int>) {
            /*addDataMapRecipes(
                registry,
                FLUID_LOOKUP,
                dataMapType,
                { holder: HTHolderLike<Fluid, Fluid>, data: HTFluidFuelData ->
                    val stack: EmiStack = holder.get().toEmi(100).takeUnless(EmiStack::isEmpty) ?: return@addDataMapRecipes null
                    HTEmiFluidFuelData(stack, data.time)
                },
                ::HTFuelGeneratorEmiRecipe.partially1(category),
            )*/
        }

        /*addItemStackRecipes(
            registry,
            "thermal",
            { stack: ItemStack ->
                val burnTime: Int = stack.getBurnTime(null)
                if (burnTime <= 0) return@addItemStackRecipes null
                val stack1: EmiStack = stack.toEmi()
                stack1.remainder = stack.craftingRemainingItem.toEmi()
                HTEmiFluidFuelData(stack1, burnTime)
            },
            ::HTFuelGeneratorEmiRecipe.partially1(RagiumEmiRecipeCategories.THERMAL),
        )*/

        addFuelRecipes(RagiumEmiRecipeCategories.MAGMATIC, RagiumDataMapTypes.MAGMATIC_FUEL)

        addFuelRecipes(RagiumEmiRecipeCategories.COMBUSTION, RagiumDataMapTypes.COMBUSTION_FUEL)
    }

    private fun addInteractions(registry: EmiRegistry) {
        // Imitation Spawner
        BuiltInRegistries.ENTITY_TYPE
            .asLookup()
            .filterElements(HTImitationSpawnerBlock::filterEntityType)
            .listElements()
            .forEach { holder: Holder.Reference<EntityType<*>> ->
                val spawner: EmiStack = HTImitationSpawnerBlock.createStack(holder).toEmi()
                val egg: EmiStack = SpawnEggItem.byId(holder.value())?.toEmi() ?: return@forEach
                addRecipeSafe(
                    registry,
                    RagiumAPI.id("/world", "imitation_spawner", holder.toLike().getId().toDebugFileName()),
                ) { id: ResourceLocation ->
                    EmiWorldInteractionRecipe
                        .builder()
                        .id(id)
                        .leftInput(RagiumBlocks.IMITATION_SPAWNER.toEmi())
                        .rightInput(egg, false)
                        .output(spawner)
                        .build()
                }
            }
    }

    //    Extensions    //

    private fun <RECIPE : Any, EMI_RECIPE : EmiRecipe> addItemStackRecipes(
        registry: EmiRegistry,
        prefix: String,
        recipeFactory: (ItemStack) -> RECIPE?,
        factory: Factory<RECIPE, EMI_RECIPE>,
    ) {
        addRecipes(
            registry,
            ITEM_LOOKUP
                .listElements()
                .asSequence()
                .map(Holder<Item>::toLike)
                .mapNotNull { holder: HTHolderLike<Item, Item> ->
                    val item: Item = holder.get()
                    val stack: ItemStack = item.defaultInstance
                    val recipe: RECIPE = recipeFactory(stack) ?: return@mapNotNull null
                    holder.getId().withPrefix("/$prefix/") to recipe
                },
            factory,
        )
    }

    private fun <R : Any, T : Any, RECIPE : Any, EMI_RECIPE : EmiRecipe> addDataMapRecipes(
        registry: EmiRegistry,
        lookup: HolderLookup.RegistryLookup<R>,
        dataMapType: DataMapType<R, T>,
        recipeFactory: (HTHolderLike<R, R>, T) -> RECIPE?,
        factory: Factory<RECIPE, EMI_RECIPE>,
    ) {
        addRecipes(
            registry,
            lookup
                .getHolderDataMap(dataMapType)
                .mapNotNull { (holder: HTHolderLike<R, R>, value: T) ->
                    val recipe: RECIPE = recipeFactory(holder, value) ?: return@mapNotNull null
                    val typeId: ResourceLocation = dataMapType.id()
                    val id: ResourceLocation = holder.getId().withPrefix("/${typeId.namespace}/${typeId.path}/")
                    id to recipe
                }.asSequence(),
            factory,
        )
    }
}
