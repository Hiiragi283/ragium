package hiiragi283.ragium.client.jei

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.integration.jei.HTJeiPlugin
import hiiragi283.core.api.integration.jei.HTSubtypeInterpreter
import hiiragi283.core.api.integration.jei.JeiRecipeType
import hiiragi283.core.api.registry.HTFluidHolderLike
import hiiragi283.core.api.registry.HTSimpleHolderLike
import hiiragi283.core.api.registry.toFluidLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.IdToValue
import hiiragi283.core.api.util.emptyOptional
import hiiragi283.core.client.jei.HCJeiRecipeTypes
import hiiragi283.core.client.jei.category.HTItemToChancedRecipeCategory
import hiiragi283.core.client.jei.category.HTItemToItemRecipeCategory
import hiiragi283.core.client.jei.extension.HTBasicItemToChancedRecipeCategoryExtension
import hiiragi283.core.client.jei.extension.HTBasicItemToItemRecipeCategoryExtension
import hiiragi283.core.setup.HCDataComponents
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.tank.HTTankInteraction
import hiiragi283.ragium.client.jei.category.HTCombiningRecipeCategory
import hiiragi283.ragium.client.jei.category.HTElectrolyzingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTEnchantingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTFreezingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTItemOrFluidRecipeCategory
import hiiragi283.ragium.client.jei.category.HTMeltingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTMixingRecipeCategory
import hiiragi283.ragium.client.jei.category.HTTankInteractionRecipeCategory
import hiiragi283.ragium.client.jei.category.HTWashingRecipeCategory
import hiiragi283.ragium.client.jei.category.RagiumDuplicatingRecipeCategory
import hiiragi283.ragium.client.jei.extension.HTBasicItemOrFluidRecipeCategoryExtension
import hiiragi283.ragium.client.jei.extension.HTHolderEnchantingRecipeCategoryExtension
import hiiragi283.ragium.client.jei.extension.HTPotionTankInteractionCategoryExtension
import hiiragi283.ragium.client.jei.extension.HTSimpleTankInteractionCategoryExtension
import hiiragi283.ragium.common.data.tank.HTSimpleTankInteraction
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import hiiragi283.ragium.setup.RagiumItems
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.RecipeTypes
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.registration.ISubtypeRegistration
import mezz.jei.api.runtime.IIngredientManager
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluid

@JeiPlugin
class RagiumJeiPlugin : HTJeiPlugin(RagiumAPI.MOD_ID) {
    companion object {
        @JvmStatic
        lateinit var tankInteraction: HTTankInteractionRecipeCategory
            private set

        // ItemToItem
        @JvmStatic
        lateinit var compressing: HTItemToItemRecipeCategory
            private set

        // ItemToChanced
        @JvmStatic
        lateinit var cutting: HTItemToChancedRecipeCategory
            private set

        @JvmStatic
        lateinit var planting: HTItemToChancedRecipeCategory
            private set

        // ItemOrFluid
        @JvmStatic
        lateinit var pyrolyzing: HTItemOrFluidRecipeCategory
            private set

        @JvmStatic
        lateinit var refining: HTItemOrFluidRecipeCategory
            private set

        // Other
        @JvmStatic
        lateinit var enchanting: HTEnchantingRecipeCategory
            private set
    }

    override fun registerItemSubtypes(registration: ISubtypeRegistration) {
        registration.registerSubtypeInterpreter(
            RagiumBlocks.UNIVERSAL_CHEST.asItem(),
            HTSubtypeInterpreter { stack: ItemStack, _ -> stack.get(HCDataComponents.COLOR) },
        )
        registration.registerSubtypeInterpreter(
            RagiumBlocks.IMITATION_SPAWNER.asItem(),
            HTSubtypeInterpreter { stack: ItemStack, _ -> stack.get(RagiumDataComponents.SPAWNER_MOB) },
        )

        registration.registerSubtypeInterpreter(
            RagiumItems.BLUEPRINT.get(),
            HTSubtypeInterpreter { stack: ItemStack, _ -> stack.get(RagiumDataComponents.BLUEPRINT_NUMBER) },
        )
        registration.registerSubtypeInterpreter(
            RagiumItems.LOOT_TICKET.get(),
            HTSubtypeInterpreter { stack: ItemStack, _ -> stack.get(RagiumDataComponents.LOOT_TICKET) },
        )
    }

    override fun registerCategories(registration: IRecipeCategoryRegistration) {
        val guiHelper: IGuiHelper = registration.jeiHelpers.guiHelper
        val manager: IIngredientManager = registration.jeiHelpers.ingredientManager

        tankInteraction = HTTankInteractionRecipeCategory(guiHelper)
        tankInteraction.addExtension(HTSimpleTankInteractionCategoryExtension)
        // tankInteraction.addExtension(HTBucketInteractionRecipeCategoryExtension(manager))
        tankInteraction.addExtension(HTPotionTankInteractionCategoryExtension)

        initItemToItem(guiHelper, manager)
        initItemToChanced(guiHelper, manager)
        initItemOrFluid(guiHelper, manager)

        enchanting = HTEnchantingRecipeCategory(guiHelper)
        enchanting.addExtension(HTHolderEnchantingRecipeCategoryExtension)

        registration.addRecipeCategories(
            tankInteraction,
            // Machine - Basic
            HTCombiningRecipeCategory(3, guiHelper, RagiumJeiRecipeTypes.ALLOYING),
            HTCombiningRecipeCategory(2, guiHelper, RagiumJeiRecipeTypes.ASSEMBLING),
            compressing,
            cutting,
            planting,
            // Machine - Advanced
            HTFreezingRecipeCategory(guiHelper),
            HTMeltingRecipeCategory(guiHelper),
            pyrolyzing,
            refining,
            // Machine - Elite
            HTElectrolyzingRecipeCategory(guiHelper),
            HTMixingRecipeCategory(guiHelper),
            HTWashingRecipeCategory(guiHelper),
            // Machine - Ultimate
            enchanting,
            RagiumDuplicatingRecipeCategory(guiHelper),
            // Device
        )
    }

    private fun initItemToItem(guiHelper: IGuiHelper, manager: IIngredientManager) {
        compressing = HTItemToItemRecipeCategory(guiHelper, RagiumJeiRecipeTypes.COMPRESSING)

        compressing.addExtension(HTBasicItemToItemRecipeCategoryExtension())
    }

    private fun initItemToChanced(guiHelper: IGuiHelper, manager: IIngredientManager) {
        cutting = HTItemToChancedRecipeCategory(guiHelper, RagiumJeiRecipeTypes.CUTTING)
        planting = HTItemToChancedRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PLANTING)

        cutting.addExtension(HTBasicItemToChancedRecipeCategoryExtension())
        planting.addExtension(HTBasicItemToChancedRecipeCategoryExtension())
    }

    private fun initItemOrFluid(guiHelper: IGuiHelper, manager: IIngredientManager) {
        pyrolyzing = HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.PYROLYZING)
        refining = HTItemOrFluidRecipeCategory(guiHelper, RagiumJeiRecipeTypes.REFINING)

        pyrolyzing.addExtension(HTBasicItemOrFluidRecipeCategoryExtension())
        refining.addExtension(HTBasicItemOrFluidRecipeCategoryExtension())
    }

    override fun registerRecipes(registration: IRecipeRegistration) {
        registerTankInteractions(registration)
        // Machine - Basic
        registration.addRecipes(RagiumJeiRecipeTypes.ALLOYING)
        registration.addRecipes(RagiumJeiRecipeTypes.ASSEMBLING)
        registration.addRecipes(RagiumJeiRecipeTypes.COMPRESSING)
        registration.addRecipes(RagiumJeiRecipeTypes.CUTTING)
        registration.addRecipes(RagiumJeiRecipeTypes.PLANTING)
        // Machine - Advanced
        registration.addRecipes(RagiumJeiRecipeTypes.FREEZING)
        registration.addRecipes(RagiumJeiRecipeTypes.MELTING)
        registration.addRecipes(RagiumJeiRecipeTypes.PYROLYZING)
        registration.addRecipes(RagiumJeiRecipeTypes.REFINING)
        // Machine - Elite
        registration.addRecipes(RagiumJeiRecipeTypes.ELECTROLYZING)
        registration.addRecipes(RagiumJeiRecipeTypes.MIXING)
        registration.addRecipes(RagiumJeiRecipeTypes.WASHING)
        // Machine - Ultimate
        registration.addRecipes(RagiumJeiRecipeTypes.DUPLICATING)
        registration.addRecipes(RagiumJeiRecipeTypes.ENCHANTING)
        // Device
    }

    private fun registerTankInteractions(registration: IRecipeRegistration) {
        val recipeType: JeiRecipeType<IdToValue<HTTankInteraction>> = getRecipeType(RagiumJeiRecipeTypes.TANK_INTERACTION)
        // Custom
        registration.addRecipes(RagiumJeiRecipeTypes.TANK_INTERACTION)
        // Bucket
        BuiltInRegistries.FLUID
            .holders()
            .filter { holder: Holder<Fluid> ->
                val fluid: Fluid = holder.value()
                fluid.isSource(fluid.defaultFluidState()) && !fluid.bucket.let(::ItemStack).isEmpty
            }.map(Holder<Fluid>::toLike)
            .map(HTSimpleHolderLike<Fluid>::toFluidLike)
            .map { holder: HTFluidHolderLike<Fluid> ->
                HTSimpleTankInteraction(
                    Items.BUCKET.toLike(),
                    holder.get().bucket.toLike(),
                    holder,
                    HTConst.DEFAULT_FLUID_AMOUNT,
                    emptyOptional(),
                ).let { holder.getId().withPrefix("bucket/") to it as HTTankInteraction }
            }.toList()
            .let { registration.addRecipes(recipeType, it) }
    }

    override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
        registration.addRecipeCatalyst(RagiumBlocks.AUTO_CHISEL, RecipeTypes.STONECUTTING)
        registration.addRecipeCatalyst(RagiumBlocks.BREWERY, getRecipeType(HCJeiRecipeTypes.BREWING))
        registration.addRecipeCatalyst(RagiumBlocks.CRUSHER, getRecipeType(HCJeiRecipeTypes.CRUSHING))
        registration.addRecipeCatalyst(RagiumBlocks.ELECTRIC_FURNACE, RecipeTypes.SMELTING)

        registration.addRecipeCatalysts(
            RagiumJeiRecipeTypes.TANK_INTERACTION,
            // Machine - Basic
            RagiumJeiRecipeTypes.ALLOYING,
            RagiumJeiRecipeTypes.ASSEMBLING,
            RagiumJeiRecipeTypes.COMPRESSING,
            RagiumJeiRecipeTypes.CUTTING,
            RagiumJeiRecipeTypes.PLANTING,
            // Machine - Advanced
            RagiumJeiRecipeTypes.FREEZING,
            RagiumJeiRecipeTypes.MELTING,
            RagiumJeiRecipeTypes.PYROLYZING,
            RagiumJeiRecipeTypes.REFINING,
            // Machine - Elite
            RagiumJeiRecipeTypes.ELECTROLYZING,
            RagiumJeiRecipeTypes.MIXING,
            RagiumJeiRecipeTypes.WASHING,
            // Machine - Ultimate
            RagiumJeiRecipeTypes.ENCHANTING,
            RagiumJeiRecipeTypes.DUPLICATING,
            // Device
        )
    }
}
