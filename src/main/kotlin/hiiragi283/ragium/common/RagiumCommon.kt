package hiiragi283.ragium.common

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.component.HTModularToolComponent
import hiiragi283.ragium.api.content.HTContentRegister
import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.api.extension.tier
import hiiragi283.ragium.api.recipe.machine.HTRecipeComponentTypes
import hiiragi283.ragium.common.accessories.RagiumAccessoriesInit
import hiiragi283.ragium.common.data.HTHardModeResourceCondition
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.item.HTMetaMachineBlockItem
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.PillarBlock
import net.minecraft.component.type.FoodComponent
import net.minecraft.item.Item
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Rarity
import net.minecraft.world.gen.GenerationStep

object RagiumCommon : ModInitializer, HTContentRegister {
    override fun onInitialize() {
        RagiumAPI.log { info("Registering game objects...") }
        RagiumConfig.init()

        HTRecipeComponentTypes
        RagiumComponentTypes

        RagiumAdvancementCriteria
        RagiumBlockEntityTypes
        RagiumBlocks
        RagiumEntityTypes.init()
        RagiumFluids.init()
        RagiumRecipeSerializers
        RagiumRecipeTypes
        registerModifications()

        InternalRagiumAPI.initMachineType()
        RagiumContents
        registerContents()

        RagiumBlockEntityTypes.init()
        RagiumCauldronBehaviors.init()
        RagiumCommands.init()
        RagiumApiLookupInit.init()
        RagiumEventHandlers.init()
        RagiumItemGroup.init()
        HTHardModeResourceCondition.init()
        RagiumNetworks.init()

        RagiumAccessoriesInit.init()

        RagiumAPI.log { info("Ragium initialized!") }
    }

    @JvmStatic
    fun registerContents() {
        HTModularToolComponent.Behavior.entries.forEach { behavior: HTModularToolComponent.Behavior ->
            registerItem("${behavior.asString()}_module", behavior.asItem())
        }

        initBlockItems()

        RagiumContents.Ores.entries.forEach { ore: RagiumContents.Ores ->
            val block = Block(blockSettings(Blocks.IRON_ORE))
            registerBlock(ore, block)
            registerBlockItem(block, itemSettings())
        }
        RagiumContents.DeepOres.entries.forEach { ore: RagiumContents.DeepOres ->
            val block = Block(blockSettings(Blocks.DEEPSLATE_IRON_ORE))
            registerBlock(ore, block)
            registerBlockItem(block, itemSettings())
        }
        RagiumContents.StorageBlocks.entries.forEach { block: RagiumContents.StorageBlocks ->
            val block1 = Block(blockSettings(Blocks.IRON_BLOCK))
            registerBlock(block, block1)
            registerBlockItem(block1, itemSettings().tier(block.material.tier))
        }

        RagiumContents.Dusts.entries.forEach { registerItem(it, Item(itemSettings())) }
        RagiumContents.Ingots.entries.forEach { registerItem(it, Item(itemSettings())) }
        RagiumContents.Plates.entries.forEach { registerItem(it, Item(itemSettings())) }
        RagiumContents.RawMaterials.entries.forEach { registerItem(it, Item(itemSettings())) }

        RagiumContents.Armors.entries.forEach { registerItem(it, it.createItem()) }
        RagiumContents.Tools.entries.forEach { registerItem(it, it.createItem()) }
        RagiumContents.Accessories.entries.forEach { registerItem(it, it.createItem()) }

        RagiumContents.Hulls.entries.forEach { hull: RagiumContents.Hulls ->
            val block = Block(blockSettings(hull.material.tier.getBaseBlock()))
            registerBlock(hull, block)
            registerBlockItem(block, itemSettings().tier(hull.material.tier))
        }
        RagiumContents.Coils.entries.forEach { coil: RagiumContents.Coils ->
            val block = PillarBlock(blockSettings(Blocks.COPPER_BLOCK))
            registerBlock(coil, block)
            registerBlockItem(block, itemSettings())
        }
        RagiumContents.Motors.entries.forEach { motor: RagiumContents.Motors ->
            val block = PillarBlock(blockSettings(Blocks.IRON_BLOCK))
            registerBlock(motor, block)
            registerBlockItem(block, itemSettings())
        }
        RagiumContents.Circuits.entries.forEach { circuit: RagiumContents.Circuits ->
            registerItem(circuit, Item(itemSettings()))
        }

        RagiumContents.Foods.entries.forEach { food: RagiumContents.Foods ->
            registerItem(food, Item(itemSettings().food(food.food())))
        }
        RagiumContents.Misc.entries.forEach { ingredient: RagiumContents.Misc ->
            registerItem(ingredient, ingredient.createItem())
        }

        RagiumContents.Element.entries.forEach { element: RagiumContents.Element ->
            // Budding Block
            registerBlock("budding_${element.asString()}", element.buddingBlock)
            registerBlockItem(element.buddingBlock)
            // Cluster Block
            registerBlock("${element.asString()}_cluster", element.clusterBlock)
            registerBlockItem(element.clusterBlock)
            // dust item
            registerItem("${element.asString()}_dust", element.dustItem)
            // pendant item
            registerItem("${element.asString()}_pendant", element.pendantItem)
            // ring item
            registerItem("${element.asString()}_ring", element.ringItem)
        }

        RagiumContents.Fluids.entries.forEach { fluid: RagiumContents.Fluids ->
            registerItem(fluid, fluid.createItem())
        }
    }

    @JvmStatic
    private fun initBlockItems() {
        registerBlockItem(RagiumBlocks.POROUS_NETHERRACK)
        registerBlockItem(RagiumBlocks.SNOW_SPONGE)
        registerBlockItem(RagiumBlocks.OBLIVION_CLUSTER, itemSettings().rarity(Rarity.EPIC))

        registerBlockItem(RagiumBlocks.SPONGE_CAKE)
        registerBlockItem(
            RagiumBlocks.SWEET_BERRIES_CAKE,
            itemSettings()
                .food(
                    FoodComponent
                        .Builder()
                        .nutrition(2)
                        .saturationModifier(0.1f)
                        .build(),
                ).maxDamage(7)
                .component(RagiumComponentTypes.DAMAGE_INSTEAD_OF_DECREASE, Unit),
        )

        registerBlockItem(RagiumBlocks.CREATIVE_SOURCE)
        registerBlockItem(RagiumBlocks.BASIC_CASING)
        registerBlockItem(RagiumBlocks.ADVANCED_CASING)
        registerBlockItem(RagiumBlocks.MANUAL_GRINDER)
        registerBlockItem(RagiumBlocks.DATA_DRIVE)
        registerBlockItem(RagiumBlocks.DRIVE_SCANNER)
        registerBlockItem(RagiumBlocks.SHAFT)
        registerBlockItem(RagiumBlocks.ITEM_DISPLAY)
        registerBlockItem(RagiumBlocks.NETWORK_INTERFACE)

        registerBlockItem(RagiumBlocks.ALCHEMICAL_INFUSER, itemSettings().rarity(Rarity.EPIC))

        registerItem("meta_machine", HTMetaMachineBlockItem)
    }

    @JvmStatic
    private fun registerModifications() {
        BiomeModifications.addFeature(
            BiomeSelectors.foundInOverworld(),
            GenerationStep.Feature.UNDERGROUND_ORES,
            RegistryKey.of(RegistryKeys.PLACED_FEATURE, RagiumAPI.id("ore_raginite")),
        )
    }
}
