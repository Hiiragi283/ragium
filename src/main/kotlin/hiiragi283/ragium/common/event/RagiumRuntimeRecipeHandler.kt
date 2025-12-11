package hiiragi283.ragium.common.event

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.data.HTBrewingRecipeData
import hiiragi283.ragium.api.material.HTMaterialDefinition
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.attribute.HTStorageBlockMaterialAttribute
import hiiragi283.ragium.api.material.get
import hiiragi283.ragium.api.material.getDefaultPrefix
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.recipe.HTRegisterRuntimeRecipeEvent
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.registry.idOrThrow
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.HTMoldType
import hiiragi283.ragium.common.data.recipe.HTItemWithCatalystRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTShapelessInputsRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSingleExtraItemRecipeBuilder
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.recipe.HTBrewingRecipe
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.alchemy.PotionContents
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
object RagiumRuntimeRecipeHandler {
    @SubscribeEvent
    fun registerRuntimeRecipe(event: HTRegisterRuntimeRecipeEvent) {
        for ((key: HTMaterialKey, definition: HTMaterialDefinition) in RagiumPlatform.INSTANCE.getMaterialDefinitions()) {
            // Alloying
            alloyingRawOre(event, key, isAdvanced = false, isBlock = false)
            alloyingRawOre(event, key, isAdvanced = false, isBlock = true)
            alloyingRawOre(event, key, isAdvanced = true, isBlock = false)
            alloyingRawOre(event, key, isAdvanced = true, isBlock = true)
            // Compressing
            compressing(event, key, definition)
            // Crushing
            crushing(event, key, definition)
            // Mixing
            mixingMetalOre(event, key, definition)
        }

        // Brewing
        brewing(event)
    }

    //    Alloying    //

    @JvmStatic
    private fun alloyingRawOre(
        event: HTRegisterRuntimeRecipeEvent,
        key: HTMaterialKey,
        isAdvanced: Boolean,
        isBlock: Boolean,
    ) {
        val prefix: HTPrefixLike = when (isBlock) {
            true -> CommonMaterialPrefixes.RAW_STORAGE_BLOCK
            false -> CommonMaterialPrefixes.RAW_MATERIAL
        }
        val inputCount: Int = when (isAdvanced) {
            true -> 1
            false -> 2
        }
        val outputCount: Int = when (isBlock) {
            true -> 9
            false -> 1
        }
        val outputMultiplier: Int = when (isAdvanced) {
            true -> 2
            false -> 3
        }
        val fluxCount: Int = when (isBlock) {
            true -> 6
            false -> 1
        }

        val flux: TagKey<Item> = when (isAdvanced) {
            true -> RagiumModTags.Items.ALLOY_SMELTER_FLUXES_ADVANCED
            false -> RagiumModTags.Items.ALLOY_SMELTER_FLUXES_BASIC
        }

        if (!event.isPresentTag(CommonMaterialPrefixes.INGOT, key)) return
        if (event.isPresentTag(prefix, key)) {
            HTShapelessInputsRecipeBuilder
                .alloying(
                    event.resultHelper.item(CommonMaterialPrefixes.INGOT, key, outputCount * outputMultiplier),
                    event.itemCreator.fromTagKey(prefix, key, inputCount),
                    event.itemCreator.fromTagKey(flux, fluxCount),
                ).saveSuffixed(event.output, "/${prefix.asPrefixName()}/${flux.location.path}")
        }
    }

    //    Brewing    //

    @JvmStatic
    private fun brewing(event: HTRegisterRuntimeRecipeEvent) {
        event.registryAccess
            .lookupOrThrow(RagiumAPI.BREWING_RECIPE_KEY)
            .listElements()
            .forEach { holder: Holder.Reference<HTBrewingRecipeData> ->
                val id: ResourceLocation = holder.idOrThrow
                val recipeData: HTBrewingRecipeData = holder.value()
                // Base
                event.save(
                    id,
                    HTBrewingRecipe(
                        event.itemCreator.fromTagKey(Tags.Items.CROPS_NETHER_WART),
                        recipeData.getIngredient(),
                        recipeData.getBasePotion(),
                    ),
                )
                // Long
                val long: PotionContents = recipeData.getLongPotion()
                if (long.allEffects.any()) {
                    event.save(
                        id.withSuffix("/long"),
                        HTBrewingRecipe(
                            event.itemCreator.fromTagKey(Tags.Items.DUSTS_REDSTONE),
                            createDropIngredient(recipeData),
                            long,
                        ),
                    )
                }
                // Strong
                val strong: PotionContents = recipeData.getStrongPotion()
                if (strong.allEffects.any()) {
                    event.save(
                        id.withSuffix("/strong"),
                        HTBrewingRecipe(
                            event.itemCreator.fromTagKey(Tags.Items.DUSTS_GLOWSTONE),
                            createDropIngredient(recipeData),
                            strong,
                        ),
                    )
                }
            }
    }

    @JvmStatic
    private fun createDropIngredient(recipeData: HTBrewingRecipeData): HTItemIngredient = HTItemIngredient(
        DataComponentIngredient.of(
            false,
            DataComponents.POTION_CONTENTS,
            recipeData.getBasePotion(),
            RagiumItems.POTION_DROP,
        ),
        1,
    )

    //    Compressing    //

    @JvmStatic
    private fun compressing(event: HTRegisterRuntimeRecipeEvent, key: HTMaterialKey, definition: HTMaterialDefinition) {
        val basePrefix: HTMaterialPrefix = definition.getDefaultPrefix() ?: return
        when {
            basePrefix.isOf(CommonMaterialPrefixes.GEM) -> compressingDust(event, key, basePrefix)
            basePrefix.isOf(CommonMaterialPrefixes.INGOT) -> compressingMetal(event, key)

            basePrefix.isOf(CommonMaterialPrefixes.FUEL) -> compressingDust(event, key, basePrefix)
        }
    }

    @JvmStatic
    private fun compressingMetal(event: HTRegisterRuntimeRecipeEvent, key: HTMaterialKey) {
        // Gear
        if (event.isPresentTag(CommonMaterialPrefixes.GEAR, key)) {
            HTItemWithCatalystRecipeBuilder
                .compressing(
                    event.itemCreator.fromTagKey(CommonMaterialPrefixes.INGOT, key),
                    event.resultHelper.item(CommonMaterialPrefixes.GEAR, key),
                    event.itemCreator.fromItem(HTMoldType.GEAR),
                ).saveSuffixed(event.output, "_from_ingot")
        }
        // Plate
        if (event.isPresentTag(CommonMaterialPrefixes.PLATE, key)) {
            HTItemWithCatalystRecipeBuilder
                .compressing(
                    event.itemCreator.fromTagKey(CommonMaterialPrefixes.INGOT, key),
                    event.resultHelper.item(CommonMaterialPrefixes.PLATE, key),
                    event.itemCreator.fromItem(HTMoldType.PLATE),
                ).saveSuffixed(event.output, "_from_ingot")
        }
    }

    @JvmStatic
    private fun compressingDust(event: HTRegisterRuntimeRecipeEvent, key: HTMaterialKey, outputPrefix: HTPrefixLike) {
        if (!event.isPresentTag(CommonMaterialPrefixes.DUST, key)) return
        if (!event.isPresentTag(outputPrefix, key)) return

        HTItemWithCatalystRecipeBuilder
            .compressing(
                event.itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, key),
                event.resultHelper.item(outputPrefix, key),
                event.itemCreator.fromItem(HTMoldType.GEM),
            ).saveSuffixed(event.output, "_from_dust")
    }

    //    Crushing    //

    @JvmStatic
    private fun crushing(event: HTRegisterRuntimeRecipeEvent, key: HTMaterialKey, definition: HTMaterialDefinition) {
        crushingBlock(event, key, definition)
        crushingTo(event, key, CommonMaterialPrefixes.RAW_STORAGE_BLOCK, 1, 12)

        crushingTo(event, key, CommonMaterialPrefixes.INGOT, 1, 1)
        crushingTo(event, key, CommonMaterialPrefixes.GEM, 1, 1)
        crushingTo(event, key, CommonMaterialPrefixes.GEAR, 1, 4)
        crushingTo(event, key, CommonMaterialPrefixes.PLATE, 1, 1)
        crushingTo(event, key, CommonMaterialPrefixes.RAW_MATERIAL, 3, 4)
        crushingTo(event, key, CommonMaterialPrefixes.ROD, 2, 1)
        crushingTo(event, key, CommonMaterialPrefixes.FUEL, 1, 1)

        crushingTo(event, key, CommonMaterialPrefixes.CROP, 1, 1, CommonMaterialPrefixes.FLOUR)
    }

    @JvmStatic
    private fun crushingBlock(event: HTRegisterRuntimeRecipeEvent, key: HTMaterialKey, definition: HTMaterialDefinition) {
        val basePrefix: HTMaterialPrefix = definition.getDefaultPrefix() ?: return
        val resultPrefix: HTPrefixLike = when {
            basePrefix.isOf(CommonMaterialPrefixes.GEM) -> CommonMaterialPrefixes.GEM
            basePrefix.isOf(CommonMaterialPrefixes.FUEL) -> CommonMaterialPrefixes.FUEL
            else -> CommonMaterialPrefixes.DUST
        }

        val storageBlock: HTStorageBlockMaterialAttribute = if (basePrefix.isOf(CommonMaterialPrefixes.INGOT)) {
            HTStorageBlockMaterialAttribute.THREE_BY_THREE
        } else {
            definition.get<HTStorageBlockMaterialAttribute>() ?: return
        }

        if (!event.isPresentTag(CommonMaterialPrefixes.STORAGE_BLOCK, key)) return
        if (!event.isPresentTag(resultPrefix, key)) return

        HTSingleExtraItemRecipeBuilder
            .crushing(
                event.itemCreator.fromTagKey(CommonMaterialPrefixes.STORAGE_BLOCK, key),
                event.resultHelper.item(resultPrefix, key, storageBlock.baseCount),
            ).saveSuffixed(event.output, "_from_storage_block")
    }

    @JvmStatic
    private fun crushingTo(
        event: HTRegisterRuntimeRecipeEvent,
        key: HTMaterialKey,
        inputPrefix: HTPrefixLike,
        inputCount: Int,
        outputCount: Int,
        outputPrefix: HTPrefixLike = CommonMaterialPrefixes.DUST,
    ) {
        if (!event.isPresentTag(inputPrefix, key)) return
        if (!event.isPresentTag(outputPrefix, key)) return

        HTSingleExtraItemRecipeBuilder
            .crushing(
                event.itemCreator.fromTagKey(inputPrefix, key, inputCount),
                event.resultHelper.item(outputPrefix, key, outputCount),
            ).saveSuffixed(event.output, "_from_${inputPrefix.asPrefixName()}")
    }

    //    Mixing    //

    @JvmStatic
    private fun mixingMetalOre(event: HTRegisterRuntimeRecipeEvent, key: HTMaterialKey, definition: HTMaterialDefinition) {
        val basePrefix: HTMaterialPrefix = definition.getDefaultPrefix() ?: return
        if (!basePrefix.isOf(CommonMaterialPrefixes.INGOT)) return
        if (!event.isPresentTag(CommonMaterialPrefixes.ORE, key)) return
        if (!event.isPresentTag(CommonMaterialPrefixes.INGOT, key)) return

        HTMixingRecipeBuilder
            .create()
            .addIngredient(event.itemCreator.fromTagKey(CommonMaterialPrefixes.ORE, key))
            .addIngredient(event.fluidCreator.fromHolder(RagiumFluidContents.CRIMSON_BLOOD, 250))
            .setResult(event.resultHelper.item(CommonMaterialPrefixes.INGOT, key, 4))
            .saveSuffixed(event.output, "_from_ore")
    }
}
