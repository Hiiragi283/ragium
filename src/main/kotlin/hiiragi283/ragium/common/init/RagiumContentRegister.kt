package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.accessory.HTAccessoryRegistry
import hiiragi283.ragium.api.accessory.HTAccessorySlotTypes
import hiiragi283.ragium.api.content.HTContentRegister
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.block.entity.HTMetaMachineBlockEntity
import hiiragi283.ragium.common.item.HTCrafterHammerItem
import hiiragi283.ragium.common.item.HTMetaMachineBlockItem
import net.fabricmc.fabric.api.registry.FuelRegistry
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.minecraft.block.*
import net.minecraft.block.cauldron.CauldronBehavior
import net.minecraft.block.entity.BlockEntity
import net.minecraft.component.type.FoodComponent
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.util.DyeColor
import net.minecraft.util.Hand
import net.minecraft.util.ItemActionResult
import net.minecraft.util.Rarity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.InfiniteEnergyStorage

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
        RagiumContents.Gems.entries.forEach { registerItem(it, Item(itemSettings())) }
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
        
        registerItem("meta_machine", HTMetaMachineBlockItem)
    }

    @JvmStatic
    fun initRegistry() {
        // ApiLookup
        ItemStorage.SIDED.registerForBlockEntity({ blockEntity: HTMetaMachineBlockEntity, direction: Direction? ->
            blockEntity.machineEntity?.let { InventoryStorage.of(it, direction) }
        }, RagiumBlockEntityTypes.META_MACHINE)

        ItemStorage.SIDED.registerForBlocks({ world: World, _: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            val color: DyeColor = state.getOrNull(RagiumBlockProperties.COLOR) ?: return@registerForBlocks null
            world.backpackManager
                ?.get(color)
                ?.let { InventoryStorage.of(it, direction) }
        }, RagiumBlocks.BACKPACK_INTERFACE)

        EnergyStorage.SIDED.registerForBlocks(
            { _: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? -> InfiniteEnergyStorage.INSTANCE },
            RagiumBlocks.CREATIVE_SOURCE,
        )

        EnergyStorage.SIDED.registerForBlocks({ world: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? ->
            world.energyNetwork
        }, RagiumBlocks.NETWORK_INTERFACE)
        // Accessory
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
        // Cauldron
        registerCauldron(
            CauldronBehavior.WATER_CAULDRON_BEHAVIOR,
            RagiumContents.Dusts.CRUDE_RAGINITE,
        ) { state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, _: Hand, stack: ItemStack ->
            if (stack.isOf(RagiumContents.Dusts.CRUDE_RAGINITE)) {
                if (!world.isClient) {
                    val count: Int = stack.count
                    stack.count = -1
                    dropStackAt(player, ItemStack(RagiumContents.Dusts.RAGINITE, count))
                    LeveledCauldronBlock.decrementFluidLevel(state, world, pos)
                }
                ItemActionResult.success(world.isClient)
            } else {
                ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
            }
        }
        // Dispenser
        DispenserBlock.registerProjectileBehavior(RagiumContents.Misc.DYNAMITE)
        DispenserBlock.registerProjectileBehavior(RagiumContents.Misc.REMOVER_DYNAMITE)
        // Fuel Time
        FuelRegistry.INSTANCE.add(RagiumItemTags.FUEL_CUBES, 200 * 8)
        FuelRegistry.INSTANCE.add(RagiumContents.Fluids.NITRO_FUEL, 200 * 16)
    }

    @JvmStatic
    private fun registerCauldron(map: CauldronBehavior.CauldronBehaviorMap, item: ItemConvertible, behavior: CauldronBehavior) {
        map.map[item.asItem()] = behavior
    }
}
