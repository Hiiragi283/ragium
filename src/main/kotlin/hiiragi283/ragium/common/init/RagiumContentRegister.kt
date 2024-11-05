package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.accessory.HTAccessoryRegistry
import hiiragi283.ragium.api.accessory.HTAccessorySlotTypes
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.content.HTContentRegister
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.fluid.HTFluidDrinkingHandlerRegistry
import hiiragi283.ragium.api.fluid.HTVirtualFluid
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import hiiragi283.ragium.api.property.HTPropertyKey
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.block.HTExporterBlock
import hiiragi283.ragium.common.block.HTPipeBlock
import hiiragi283.ragium.common.fluid.HTEmptyFluidCubeStorage
import hiiragi283.ragium.common.item.HTCrafterHammerItem
import hiiragi283.ragium.common.item.HTMetaMachineBlockItem
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.*
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleItemStorage
import net.minecraft.block.*
import net.minecraft.block.cauldron.CauldronBehavior
import net.minecraft.block.entity.BlockEntity
import net.minecraft.component.type.FoodComponent
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.fluid.Fluids
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.InfiniteEnergyStorage

object RagiumContentRegister : HTContentRegister {
    private val itemBuilders: MutableMap<HTContent<Item>, HTPropertyHolderBuilder> = mutableMapOf()

    private val settingsKey: HTPropertyKey.Defaulted<(Item.Settings) -> Item.Settings> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("settings"), value = { it })

    private val itemKey: HTPropertyKey.Defaulted<(Item.Settings) -> Item> =
        HTPropertyKey.ofDefaulted(RagiumAPI.id("item"), value = ::Item)

    private fun getProperties(content: HTContent<Item>): HTPropertyHolderBuilder =
        itemBuilders.computeIfAbsent(content) { HTPropertyHolderBuilder() }

    private fun createAndRegisterItem(content: HTContent<Item>) {
        val properties: HTPropertyHolderBuilder = getProperties(content)
        val settings: Item.Settings = properties.getOrDefault(settingsKey)(itemSettings())
        val item: Item = properties.getOrDefault(itemKey)(settings)
        registerItem(content, item)
    }

    @JvmStatic
    fun registerContents() {
        // DynamicRegistries.registerSynced(HTMultiblockPattern.REGISTRY_KEY, HTMultiblockPattern.CODEC)

        initProperties()

        initBlockItems()

        RagiumContents.Ores.entries.forEach { ore: RagiumContents.Ores ->
            val block = Block(blockSettings(ore.baseStone))
            registerBlock(ore, block)
            registerBlockItem(block, itemSettings())
        }
        RagiumContents.StorageBlocks.entries.forEach { storage: RagiumContents.StorageBlocks ->
            val block = Block(blockSettings(Blocks.IRON_BLOCK))
            registerBlock(storage, block)
            registerBlockItem(block, itemSettings())
        }

        buildList {
            addAll(RagiumContents.Dusts.entries)
            addAll(RagiumContents.Gems.entries)
            addAll(RagiumContents.Ingots.entries)
            addAll(RagiumContents.Plates.entries)
            addAll(RagiumContents.RawMaterials.entries)
            addAll(RagiumContents.Armors.entries)
            addAll(RagiumContents.Tools.entries)
            addAll(HTCrafterHammerItem.Behavior.entries)
            addAll(RagiumContents.CircuitBoards.entries)
            addAll(RagiumContents.Circuits.entries)
        }.forEach(::createAndRegisterItem)

        RagiumContents.Hulls.entries.forEach { hull: RagiumContents.Hulls ->
            val tier: HTMachineTier = HTMachineTier.entries[hull.ordinal]
            val block = Block(blockSettings(tier.getBaseBlock()))
            registerBlock(hull, block)
            registerBlockItem(block, itemSettings())
        }
        RagiumContents.Coils.entries.forEach { coil: RagiumContents.Coils ->
            val block = PillarBlock(blockSettings(Blocks.COPPER_BLOCK))
            registerBlock(coil, block)
            registerBlockItem(block, itemSettings())
        }
        RagiumContents.Exporters.entries.forEach { exporter: RagiumContents.Exporters ->
            val block = HTExporterBlock(exporter.tier)
            registerBlock(exporter, block)
            registerBlockItem(block, itemSettings())
        }
        RagiumContents.Pipes.entries.forEach { pipe: RagiumContents.Pipes ->
            val block = HTPipeBlock(pipe.tier, pipe.pipeType)
            registerBlock(pipe, block)
            registerBlockItem(block, itemSettings())
        }

        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            Registry.register(Registries.FLUID, fluid.id, HTVirtualFluid())
        }
    }

    @JvmStatic
    private fun initProperties() {
        // armors
        RagiumContents.Armors.entries.forEach { armor: RagiumContents.Armors ->
            getProperties(armor)[itemKey] = { armor.armorType.createItem(armor.material, armor.multiplier) }
        }
        // tools
        RagiumContents.Tools.entries.forEach { tool: RagiumContents.Tools ->
            getProperties(tool)[itemKey] = { tool.toolType.createToolItem(tool.material, it) }
        }
    }

    @JvmStatic
    private fun initBlockItems() {
        registerBlockItem(RagiumBlocks.POROUS_NETHERRACK)

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

        registerBlockItem(RagiumBlocks.ADVANCED_CASING)
        registerBlockItem(RagiumBlocks.BACKPACK_INTERFACE)
        registerBlockItem(RagiumBlocks.BASIC_CASING)
        registerBlockItem(RagiumBlocks.CREATIVE_SOURCE)
        registerBlockItem(RagiumBlocks.FIREBOX)
        registerBlockItem(RagiumBlocks.ITEM_DISPLAY)
        registerBlockItem(RagiumBlocks.MANUAL_FORGE)
        registerBlockItem(RagiumBlocks.MANUAL_GRINDER)
        registerBlockItem(RagiumBlocks.MANUAL_MIXER)
        registerBlockItem(RagiumBlocks.NETWORK_INTERFACE)
        registerBlockItem(RagiumBlocks.SHAFT)
        registerBlockItem(RagiumBlocks.TRADER_STATION)
        registerBlockItem(RagiumBlocks.TRASH_BOX)

        registerBlockItem(RagiumBlocks.META_CONSUMER, itemSettings(), ::HTMetaMachineBlockItem)
        registerBlockItem(RagiumBlocks.META_GENERATOR, itemSettings(), ::HTMetaMachineBlockItem)
        registerBlockItem(RagiumBlocks.META_PROCESSOR, itemSettings(), ::HTMetaMachineBlockItem)
    }

    @JvmStatic
    fun initRegistry() {
        // ApiLookup
        ItemStorage.SIDED.registerForBlocks({ world: World, _: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            val color: DyeColor = state.getOrNull(RagiumBlockProperties.COLOR) ?: return@registerForBlocks null
            world.backpackManager
                ?.get(color)
                ?.let { InventoryStorage.of(it, direction) }
        }, RagiumBlocks.BACKPACK_INTERFACE)
        ItemStorage.SIDED.registerForBlocks({ _: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? ->
            object : SingleItemStorage() {
                override fun getCapacity(variant: ItemVariant): Long = Long.MAX_VALUE
            }
        }, RagiumBlocks.TRASH_BOX)

        FluidStorage
            .combinedItemApiProvider(RagiumItems.EMPTY_FLUID_CUBE)
            .register(::HTEmptyFluidCubeStorage)
        FluidStorage.GENERAL_COMBINED_PROVIDER.register { context: ContainerItemContext ->
            if (context.itemVariant.isOf(RagiumItems.FILLED_FLUID_CUBE)) {
                context
                    .itemVariant
                    .componentMap
                    .get(RagiumComponentTypes.FLUID)
                    ?.let {
                        FullItemFluidStorage(
                            context,
                            RagiumItems.EMPTY_FLUID_CUBE,
                            FluidVariant.of(it),
                            FluidConstants.BUCKET,
                        )
                    }
                    ?: return@register null
            } else {
                null
            }
        }
        FluidStorage.SIDED.registerForBlocks({ _: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? ->
            fluidStorageOf(Long.MAX_VALUE)
        }, RagiumBlocks.TRASH_BOX)

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
                it.addStatusEffect(StatusEffectInstance(StatusEffects.HASTE, -1, 1))
            }
            unequippedAction = HTAccessoryRegistry.UnequippedAction {
                it.removeStatusEffect(StatusEffects.HASTE)
            }
        }
        HTAccessoryRegistry.register(RagiumContents.Armors.STELLA_LEGGINGS) {
            equippedAction = HTAccessoryRegistry.EquippedAction {
                it.addStatusEffect(StatusEffectInstance(StatusEffects.RESISTANCE, -1, 1))
                it.addStatusEffect(StatusEffectInstance(StatusEffects.SPEED, -1, 1))
            }
            unequippedAction = HTAccessoryRegistry.UnequippedAction {
                it.removeStatusEffect(StatusEffects.RESISTANCE)
                it.removeStatusEffect(StatusEffects.SPEED)
            }
        }
        HTAccessoryRegistry.register(RagiumContents.Armors.STELLA_BOOTS) {
            equippedAction = HTAccessoryRegistry.EquippedAction {
                it.addStatusEffect(StatusEffectInstance(StatusEffects.SLOW_FALLING, -1, 0))
                it.addStatusEffect(StatusEffectInstance(StatusEffects.JUMP_BOOST, -1, 1))
            }
            unequippedAction = HTAccessoryRegistry.UnequippedAction {
                it.removeStatusEffect(StatusEffects.SLOW_FALLING)
                it.removeStatusEffect(StatusEffects.JUMP_BOOST)
            }
        }
        // Cauldron
        /*registerCauldron(
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
        }*/
        // Dispenser
        DispenserBlock.registerProjectileBehavior(RagiumItems.DYNAMITE)
        DispenserBlock.registerProjectileBehavior(RagiumItems.REMOVER_DYNAMITE)
        // Fluid Attributes
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            FluidVariantAttributes.register(
                fluid.value,
                object : FluidVariantAttributeHandler {
                    override fun getName(fluidVariant: FluidVariant): Text = Text.translatable(fluid.translationKey)
                },
            )
        }

        // HTFluidDrinkingHandlerRegistry
        HTFluidDrinkingHandlerRegistry.register(Fluids.LAVA) { _: ItemStack, world: World, user: LivingEntity ->
            if (!world.isClient) {
                user.setOnFireFromLava()
                dropStackAt(user, Items.OBSIDIAN.defaultStack)
            }
        }
        HTFluidDrinkingHandlerRegistry.register(RagiumFluids.MILK) { _: ItemStack, world: World, user: LivingEntity ->
            if (!world.isClient) {
                user.clearStatusEffects()
            }
        }
        HTFluidDrinkingHandlerRegistry.register(RagiumFluids.HONEY) { _: ItemStack, world: World, user: LivingEntity ->
            if (!world.isClient) {
                user.removeStatusEffect(StatusEffects.POISON)
            }
        }
        HTFluidDrinkingHandlerRegistry.register(RagiumFluids.CHOCOLATE) { _: ItemStack, world: World, user: LivingEntity ->
            if (!world.isClient) {
                user.addStatusEffect(
                    StatusEffectInstance(StatusEffects.STRENGTH, 20 * 5, 1),
                )
            }
        }
    }

    @JvmStatic
    private fun registerCauldron(map: CauldronBehavior.CauldronBehaviorMap, item: ItemConvertible, behavior: CauldronBehavior) {
        map.map[item.asItem()] = behavior
    }
}
