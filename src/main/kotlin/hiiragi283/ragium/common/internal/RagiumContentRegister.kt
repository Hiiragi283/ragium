package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.accessory.HTAccessoryRegistry
import hiiragi283.ragium.api.accessory.HTAccessorySlotTypes
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.fluid.HTFluidDrinkingHandler
import hiiragi283.ragium.api.fluid.HTFluidDrinkingHandlerRegistry
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.storage.HTVoidStorage
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.storage.HTEmptyFluidCubeStorage
import hiiragi283.ragium.common.storage.HTTieredFluidItemStorage
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.fabricmc.fabric.api.transfer.v1.fluid.*
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage
import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.DispenserBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.InfiniteEnergyStorage
import kotlin.jvm.optionals.getOrNull

internal object RagiumContentRegister {
    @JvmStatic
    fun initRegistry() {
        // ApiLookup
        registerItemStorages()
        registerFluidStorages()
        registerEnergyStorages()
        registerTierProviders()
        // Accessory
        HTAccessoryRegistry.register(RagiumItemsNew.STELLA_GOGGLE) {
            equippedAction = HTAccessoryRegistry.EquippedAction {
                it.addStatusEffect(StatusEffectInstance(StatusEffects.NIGHT_VISION, -1, 0))
            }
            unequippedAction = HTAccessoryRegistry.UnequippedAction {
                it.removeStatusEffect(StatusEffects.NIGHT_VISION)
            }
            slotType = HTAccessorySlotTypes.FACE
        }
        // Dispenser
        DispenserBlock.registerProjectileBehavior(RagiumItems.BEDROCK_DYNAMITE)
        DispenserBlock.registerProjectileBehavior(RagiumItems.DYNAMITE)
        DispenserBlock.registerProjectileBehavior(RagiumItems.FLATTENING_DYNAMITE)
        // Fluid Attributes
        RagiumFluids.entries.forEach { fluid: RagiumFluids ->
            FluidVariantAttributes.register(
                fluid.get(),
                object : FluidVariantAttributeHandler {
                    override fun getName(fluidVariant: FluidVariant): Text = Text.translatable(fluid.translationKey)
                },
            )
        }

        registerDrinkHandlers(HTFluidDrinkingHandlerRegistry::register)
    }

    private fun registerItemStorages() {
        registerItemStorage({ world: World, _: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            val color: DyeColor = state.getOrNull(RagiumBlockProperties.COLOR) ?: return@registerItemStorage null
            world.backpackManager
                .map { it[color] }
                .map { InventoryStorage.of(it, direction) }
                .result()
                .getOrNull()
        }, RagiumBlocks.BACKPACK_INTERFACE.get())

        registerItemStorage({ world: World, pos: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? ->
            InsertionOnlyStorage { resource: ItemVariant, maxAmount: Long, _: TransactionContext ->
                if (dropStackAt(world, pos.down(), resource.toStack(maxAmount.toInt()))) maxAmount else 0
            }
        }, RagiumBlocks.OPEN_CRATE.get())
        // trash box
        registerItemStorage(
            { _: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? -> HTVoidStorage.ITEM },
            RagiumBlocks.TRASH_BOX.get(),
        )
        // cross pipe
        registerItemStorage({ world: World, pos: BlockPos, _: BlockState, _: BlockEntity?, direction: Direction? ->
            direction?.let { front: Direction ->
                ItemStorage.SIDED.find(world, pos.offset(front.opposite), front)
            }
        }, RagiumBlocks.CrossPipes.STEEL.get())
        // pipe station
        registerItemStorage({ world: World, pos: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            val front: Direction = state.getOrDefault(Properties.FACING, Direction.NORTH)
            val others: List<Direction> =
                Direction.entries.filterNot { directionIn: Direction -> directionIn.axis == front.axis }
            CombinedStorage(
                buildList {
                    addAll(
                        others.mapNotNull { direction: Direction ->
                            ItemStorage.SIDED.find(
                                world,
                                pos.offset(direction),
                                direction.opposite,
                            )
                        },
                    )
                    add(ItemStorage.SIDED.find(world, pos.offset(front), front.opposite))
                },
            )
        }, RagiumBlocks.PipeStations.ITEM.get())
        // filtering pipe
        registerItemStorage({ world: World, pos: BlockPos, _: BlockState, _: BlockEntity?, direction: Direction? ->
            null
        }, RagiumBlocks.FilteringPipes.ITEM.get())
    }

    private fun registerFluidStorages() {
        FluidStorage
            .combinedItemApiProvider(RagiumItems.EMPTY_FLUID_CUBE)
            .register(::HTEmptyFluidCubeStorage)
        RagiumBlocks.Drums.entries
            .map(RagiumBlocks.Drums::asItem)
            .map(FluidStorage::combinedItemApiProvider)
            .forEach { event: Event<FluidStorage.CombinedItemApiProvider> -> event.register(HTTieredFluidItemStorage::find) }
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
        } // trash box
        registerFluidStorage(
            { _: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? -> HTVoidStorage.FLUID },
            RagiumBlocks.TRASH_BOX.get(),
        )
        // cross pipe
        registerFluidStorage({ world: World, pos: BlockPos, _: BlockState, _: BlockEntity?, direction: Direction? ->
            direction?.let { front: Direction ->
                FluidStorage.SIDED.find(world, pos.offset(front.opposite), front)
            }
        }, RagiumBlocks.CrossPipes.GOLD.get())
        // pipe station
        registerFluidStorage({ world: World, pos: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            val front: Direction = state.getOrDefault(Properties.FACING, Direction.NORTH)
            val others: List<Direction> =
                Direction.entries.filterNot { directionIn: Direction -> directionIn.axis == front.axis }
            CombinedStorage(
                buildList {
                    addAll(
                        others.mapNotNull { direction: Direction ->
                            FluidStorage.SIDED.find(
                                world,
                                pos.offset(direction),
                                direction.opposite,
                            )
                        },
                    )
                    add(FluidStorage.SIDED.find(world, pos.offset(front), front.opposite))
                },
            )
        }, RagiumBlocks.PipeStations.FLUID.get())
        // filtering pipe
        registerFluidStorage({ world: World, pos: BlockPos, _: BlockState, _: BlockEntity?, direction: Direction? ->
            null
        }, RagiumBlocks.FilteringPipes.FLUID.get())
    }

    private fun registerItemStorage(provider: BlockApiLookup.BlockApiProvider<Storage<ItemVariant>, Direction?>, block: Block) {
        ItemStorage.SIDED.registerForBlocks(provider, block)
    }

    private fun registerEnergyStorages() {
        EnergyStorage.SIDED.registerForBlocks(
            { _: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? -> InfiniteEnergyStorage.INSTANCE },
            RagiumBlocks.Creatives.SOURCE.get(),
        )
        EnergyStorage.SIDED.registerForBlocks({ world: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? ->
            world.energyNetwork.result().getOrNull()
        }, RagiumBlocks.NETWORK_INTERFACE.get())
    }

    private fun registerTierProviders() {
        HTMachineTier.SIDED_LOOKUP.registerFallback { _: World, _: BlockPos, state: BlockState, blockEntity: BlockEntity?, _: Direction? ->
            (blockEntity as? HTMachineTierProvider)?.tier
                ?: (state.block as? HTMachineTierProvider)?.tier
                ?: state.getOrNull(HTMachineTier.PROPERTY)
        }
    }

    private fun registerFluidStorage(provider: BlockApiLookup.BlockApiProvider<Storage<FluidVariant>, Direction?>, block: Block) {
        FluidStorage.SIDED.registerForBlocks(provider, block)
    }

    private fun registerDrinkHandlers(consumer: (Fluid, HTFluidDrinkingHandler) -> Unit) {
        consumer(Fluids.LAVA) { _: ItemStack, world: World, user: LivingEntity ->
            user.setOnFireFromLava()
            dropStackAt(user, Items.OBSIDIAN)
        }
        consumer(RagiumFluids.MILK.get()) { _: ItemStack, world: World, user: LivingEntity ->
            user.clearStatusEffects()
        }
        consumer(RagiumFluids.HONEY.get()) { _: ItemStack, world: World, user: LivingEntity ->
            user.removeStatusEffect(StatusEffects.POISON)
        }
        consumer(RagiumFluids.CHOCOLATE.get()) { _: ItemStack, world: World, user: LivingEntity ->
            user.addStatusEffect(StatusEffectInstance(StatusEffects.STRENGTH, 20 * 5, 1))
        }
    }
}
