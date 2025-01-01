package hiiragi283.ragium.common.internal

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.accessory.HTAccessoryRegistry
import hiiragi283.ragium.api.accessory.HTAccessorySlotTypes
import hiiragi283.ragium.api.block.HTBlockRotationHandler
import hiiragi283.ragium.api.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.component.HTRadioactiveComponent
import hiiragi283.ragium.api.event.HTAdvancementRewardCallback
import hiiragi283.ragium.api.event.HTInventoryTickCallback
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.fluid.HTFluidDrinkingHandler
import hiiragi283.ragium.api.fluid.HTFluidDrinkingHandlerRegistry
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.recipe.HTItemIngredient
import hiiragi283.ragium.api.screen.HTMachineScreenHandlerBase
import hiiragi283.ragium.api.screen.HTScreenFluidProvider
import hiiragi283.ragium.api.storage.HTFluidVariantStack
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.HTVoidStorage
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.network.HTFluidSyncPayload
import hiiragi283.ragium.common.storage.HTEmptyFluidCubeStorage
import hiiragi283.ragium.common.storage.HTTieredFluidItemStorage
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.fabric.api.`object`.builder.v1.trade.TradeOfferHelper
import net.fabricmc.fabric.api.registry.FuelRegistry
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
import net.minecraft.advancement.AdvancementEntry
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.DispenserBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.component.ComponentMap
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.boss.BossBar
import net.minecraft.entity.boss.ServerBossBar
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.fluid.Fluids
import net.minecraft.item.*
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.*
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.village.TradeOffers
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.InfiniteEnergyStorage

internal object RagiumContentRegister {
    @JvmField
    val ENERGY_BAR = ServerBossBar(Text.empty(), BossBar.Color.YELLOW, BossBar.Style.PROGRESS)

    @JvmStatic
    fun initEvents() {
        // send title and floating item packet when unlock advancement
        HTAdvancementRewardCallback.EVENT.register { player: ServerPlayerEntity, entry: AdvancementEntry ->
            if (entry.id == RagiumAPI.id("tier1/root")) {
                player.sendTitle(Text.literal("Welcome to Tier1!").formatted(Rarity.COMMON.formatting))
                RagiumNetworks.sendFloatingItem(player, RagiumItems.Ingots.RAGI_ALLOY)
            }
            if (entry.id == RagiumAPI.id("tier2/root")) {
                player.sendTitle(Text.literal("Welcome to Tier2!").formatted(Rarity.UNCOMMON.formatting))
                RagiumNetworks.sendFloatingItem(player, RagiumItems.Ingots.RAGI_STEEL)
            }
            if (entry.id == RagiumAPI.id("tier3/root")) {
                player.sendTitle(Text.literal("Welcome to Tier3!").formatted(Rarity.RARE.formatting))
                RagiumNetworks.sendFloatingItem(player, RagiumItems.Ingots.REFINED_RAGI_STEEL)
            }
            if (entry.id == RagiumAPI.id("tier4/root")) {
                player.sendTitle(Text.literal("Welcome to Tier4!").formatted(Rarity.EPIC.formatting))
                RagiumNetworks.sendFloatingItem(player, RagiumItems.Gems.RAGIUM)
            }
        }
        // invoke accessory action when swapped armor
        ServerEntityEvents.EQUIPMENT_CHANGE.register { entity: LivingEntity, slot: EquipmentSlot, old: ItemStack, new: ItemStack ->
            if (slot.type == EquipmentSlot.Type.HUMANOID_ARMOR) {
                if (old.isEmpty && !new.isEmpty) {
                    HTAccessoryRegistry.onEquipped(entity, new)
                    return@register
                }
                if (!old.isEmpty && new.isEmpty) {
                    HTAccessoryRegistry.onUnequipped(entity, old)
                    return@register
                }
            }
        }

        ServerTickEvents.END_SERVER_TICK.register { server: MinecraftServer ->
            server.playerManager.playerList.forEach { player: ServerPlayerEntity ->
                // send fluid sync packet
                (player.currentScreenHandler as? HTMachineScreenHandlerBase)?.let { screen: HTMachineScreenHandlerBase ->
                    (screen.blockEntity as? HTScreenFluidProvider)
                        ?.getFluidsToSync()
                        ?.forEach { (index: Int, stack: HTFluidVariantStack) ->
                            ServerPlayNetworking.send(player, HTFluidSyncPayload(index, stack))
                        }
                }
                // consume energy when worm stella goggles
                if (player.armorItems.any { it.isOf(RagiumItems.StellaSuits.GOGGLE) }) {
                    if (!player.world.processEnergy(HTEnergyNetwork.Flag.CONSUME, HTMachineTier.BASIC.processCost)) {
                        player.removeStatusEffect(StatusEffects.NIGHT_VISION)
                    }
                }
                // show energy bar (boss bar) when holding energy item
                val itemContext: ContainerItemContext =
                    ContainerItemContext.forPlayerInteraction(player, Hand.MAIN_HAND)
                itemContext
                    .find(EnergyStorage.ITEM)
                    ?.let { storage: EnergyStorage ->
                        ENERGY_BAR.apply {
                            name = itemContext.itemVariant.toStack().name
                            percent = storage.energyPercent
                            addPlayer(player)
                        }
                    } ?: run { ENERGY_BAR.removePlayer(player) }
            }
        }
        // rotate block by ragi-wrench
        UseBlockCallback.EVENT.register { player: PlayerEntity, world: World, hand: Hand, result: BlockHitResult ->
            val stack: ItemStack = player.getStackInHand(hand)
            if (stack.isOf(RagiumItems.RAGI_WRENCH)) {
                val pos: BlockPos = result.blockPos
                val state: BlockState = world.getBlockState(pos)
                val handler: HTBlockRotationHandler =
                    HTBlockRotationHandler.LOOKUP.find(world, pos, null) ?: return@register ActionResult.PASS
                val rotated: BlockState = when (player.isSneaking) {
                    true -> handler.rotate(state, result.side)
                    false -> state.rotate(BlockRotation.COUNTERCLOCKWISE_90)
                }
                if (rotated != state) {
                    if (!world.isClient) {
                        world.setBlockState(pos, rotated)
                    }
                    return@register ActionResult.success(world.isClient)
                }
            }
            ActionResult.PASS
        }
        // hard mode repair
        val hardMode: Boolean = RagiumAPI.getInstance().isHardMode
        DefaultItemComponentEvents.MODIFY.register { context: DefaultItemComponentEvents.ModifyContext ->
            context.modify({
                (it as? ToolItem)?.material == ToolMaterials.IRON || (it as? ArmorItem)?.material == ArmorMaterials.IRON
            }) { builder: ComponentMap.Builder, item: Item ->
                builder.add(
                    RagiumComponentTypes.REPAIRMENT,
                    HTItemIngredient.of(RagiumHardModeContents.IRON.getPrefixedTag(hardMode)),
                )
            }
            context.modify({
                (it as? ToolItem)?.material == ToolMaterials.GOLD || (it as? ArmorItem)?.material == ArmorMaterials.GOLD
            }) { builder: ComponentMap.Builder, item: Item ->
                builder.add(
                    RagiumComponentTypes.REPAIRMENT,
                    HTItemIngredient.of(RagiumHardModeContents.GOLD.getPrefixedTag(hardMode)),
                )
            }
            context.modify({
                (it as? ToolItem)?.material == ToolMaterials.NETHERITE || (it as? ArmorItem)?.material == ArmorMaterials.NETHERITE
            }) { builder: ComponentMap.Builder, item: Item ->
                builder.add(
                    RagiumComponentTypes.REPAIRMENT,
                    HTItemIngredient.of(RagiumHardModeContents.NETHERITE.getPrefixedTag(hardMode)),
                )
            }
            context.modify(RagiumItems.AMBROSIA.get()) { builder: ComponentMap.Builder ->
                builder.add(DataComponentTypes.FOOD, RagiumFoodComponents.AMBROSIA)
            }
        }
        // radioactive effects
        HTInventoryTickCallback.EVENT.register { stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean ->
            if (RagiumAPI
                    .getInstance()
                    .config.common.enableRadioactiveEffect
            ) {
                stack.get(HTRadioactiveComponent.COMPONENT_TYPE)?.applyEffect(entity)
            }
        }
    }

    @JvmStatic
    fun initRegistry() {
        // ApiLookup
        registerItemStorages()
        registerFluidStorages()
        registerEnergyStorages()
        registerTierProviders()
        // Accessory
        HTAccessoryRegistry.register(RagiumItems.StellaSuits.GOGGLE) {
            equippedAction = HTAccessoryRegistry.EquippedAction {
                it.addInfinityStatusEffect(StatusEffects.NIGHT_VISION)
            }
            unequippedAction = HTAccessoryRegistry.UnequippedAction {
                it.removeStatusEffect(StatusEffects.NIGHT_VISION)
            }
            slotType = HTAccessorySlotTypes.FACE
        }
        // Dispenser
        RagiumItems.Dynamites.entries.forEach(DispenserBlock::registerProjectileBehavior)
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

        FuelRegistry.INSTANCE.add(RagiumItems.COAL_CHIP, 200)

        TradeOfferHelper.registerWanderingTraderOffers(1) { factories: MutableList<TradeOffers.Factory> ->
            factories.add(TradeOffers.SellItemFactory(RagiumItems.CINNAMON_STICK.get(), 5, 5, 1, 1))
        }
    }

    private fun registerItemStorages() {
        registerItemStorage({ world: World, _: BlockPos, state: BlockState, _: BlockEntity?, direction: Direction? ->
            val color: DyeColor = state.getOrNull(RagiumBlockProperties.COLOR) ?: return@registerItemStorage null
            world
                .getBackpackManager()
                .map { it[color] }
                .map { InventoryStorage.of(it, direction) }
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
            .combinedItemApiProvider(RagiumItems.EMPTY_FLUID_CUBE.get())
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
                    .ifPresent(RagiumComponentTypes.FLUID) {
                        FullItemFluidStorage(
                            context,
                            RagiumItems.EMPTY_FLUID_CUBE.get(),
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
        EnergyStorage.SIDED.registerFallback { world: World, _: BlockPos, _: BlockState, blockEntity: BlockEntity?, direction: Direction? ->
            if (blockEntity is HTMachineBlockEntityBase) {
                world.getEnergyNetwork().getOrNull()?.let(HTStorageIO.INPUT::wrapEnergyStorage)
            } else {
                null
            }
        }
        EnergyStorage.SIDED.registerForBlocks(
            { _: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? -> InfiniteEnergyStorage.INSTANCE },
            RagiumBlocks.Creatives.SOURCE.get(),
        )
        EnergyStorage.SIDED.registerForBlocks({ world: World, _: BlockPos, _: BlockState, _: BlockEntity?, _: Direction? ->
            world.getEnergyNetwork().getOrNull()
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
            user.addStatusEffect(StatusEffects.STRENGTH, 20 * 5, 1)
        }
    }
}
