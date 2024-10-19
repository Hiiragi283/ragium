package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.accessory.HTAccessoryRegistry
import hiiragi283.ragium.api.accessory.HTAccessorySlotTypes
import hiiragi283.ragium.api.content.HTContentRegister
import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.api.extension.tier
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.item.HTCrafterHammerItem
import hiiragi283.ragium.common.item.HTMetaMachineBlockItem
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.PillarBlock
import net.minecraft.component.type.FoodComponent
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Item
import net.minecraft.util.Rarity

object RagiumContentRegister : HTContentRegister {
    @JvmStatic
    fun registerContents() {
        initBlockItems()

        RagiumContents.Ores.entries.forEach { ore: RagiumContents.Ores ->
            val block = Block(blockSettings(ore.baseStone))
            registerBlock(ore, block)
            registerBlockItem(block, itemSettings())
        }
        RagiumContents.StorageBlocks.entries.forEach { storage: RagiumContents.StorageBlocks ->
            val block = Block(blockSettings(Blocks.IRON_BLOCK))
            registerBlock(storage, block)
            registerBlockItem(block, itemSettings().tier(storage.material.tier))
        }

        RagiumContents.Dusts.entries.forEach { registerItem(it, Item(itemSettings())) }
        RagiumContents.Ingots.entries.forEach { registerItem(it, Item(itemSettings())) }
        RagiumContents.Plates.entries.forEach { registerItem(it, Item(itemSettings())) }
        RagiumContents.RawMaterials.entries.forEach { registerItem(it, Item(itemSettings())) }

        RagiumContents.Armors.entries.forEach { registerItem(it, it.createItem()) }
        RagiumContents.Tools.entries.forEach { registerItem(it, it.createItem()) }
        HTCrafterHammerItem.Behavior.entries.forEach { registerItem(it, Item(itemSettings())) }

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

        RagiumContents.Crops.entries.forEach { crop: RagiumContents.Crops ->
            registerBlock(crop.cropName, crop.cropBlock)
            registerItem(crop.seedName, crop.seedItem)
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

        initAccessories()
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
        registerBlockItem(RagiumBlocks.BACKPACK_INTERFACE)
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
    private fun initAccessories() {
        HTAccessoryRegistry.register(RagiumContents.Armors.STELLA_GOGGLE) {
            equippedAction = HTAccessoryRegistry.EquippedAction {
                it.addStatusEffect(StatusEffectInstance(StatusEffects.NIGHT_VISION, -1, 0))
            }
            unequippedAction = HTAccessoryRegistry.UnequippedAction {
                it.removeStatusEffect(StatusEffects.NIGHT_VISION)
            }
            slotType = HTAccessorySlotTypes.FACE
        }
        HTAccessoryRegistry.register(RagiumContents.Armors.STELLA_JACKET) {
            equippedAction = HTAccessoryRegistry.EquippedAction {
                it.addStatusEffect(StatusEffectInstance(StatusEffects.RESISTANCE, -1, 1))
            }
            unequippedAction = HTAccessoryRegistry.UnequippedAction {
                it.removeStatusEffect(StatusEffects.RESISTANCE)
            }
            slotType = HTAccessorySlotTypes.FACE
        }
        HTAccessoryRegistry.register(RagiumContents.Armors.STELLA_LEGGINGS) {
            equippedAction = HTAccessoryRegistry.EquippedAction {
                it.addStatusEffect(StatusEffectInstance(StatusEffects.SPEED, -1, 1))
                it.addStatusEffect(StatusEffectInstance(StatusEffects.JUMP_BOOST, -1, 1))
            }
            unequippedAction = HTAccessoryRegistry.UnequippedAction {
                it.removeStatusEffect(StatusEffects.SPEED)
                it.removeStatusEffect(StatusEffects.JUMP_BOOST)
            }
            slotType = HTAccessorySlotTypes.FACE
        }
        HTAccessoryRegistry.register(RagiumContents.Armors.STELLA_BOOTS) {
            equippedAction = HTAccessoryRegistry.EquippedAction {
                it.addStatusEffect(StatusEffectInstance(StatusEffects.SLOW_FALLING, -1, 0))
            }
            unequippedAction = HTAccessoryRegistry.UnequippedAction {
                it.removeStatusEffect(StatusEffects.SLOW_FALLING)
            }
            slotType = HTAccessorySlotTypes.FACE
        }
    }
}
