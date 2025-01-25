package hiiragi283.ragium.api.block.entity

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.fluid.HTFluidInteractable
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTControllerHolder
import hiiragi283.ragium.api.multiblock.HTMultiblockData
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.api.property.get
import hiiragi283.ragium.api.property.ifPresent
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.RagiumBlockProperties
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.fml.loading.FMLLoader
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage
import org.slf4j.Logger
import java.util.function.Supplier

abstract class HTMachineBlockEntity(type: Supplier<out BlockEntityType<*>>, pos: BlockPos, state: BlockState) :
    HTBlockEntity(type, pos, state),
    MenuProvider,
    HTBlockEntityHandlerProvider,
    HTControllerHolder,
    HTFluidInteractable,
    HTMachineTierUpgradable {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    abstract val machineKey: HTMachineKey

    val definition: HTMachineDefinition
        get() = HTMachineDefinition(machineKey, machineTier)

    val front: Direction
        get() = blockState.getOrDefault(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
    val isActive: Boolean
        get() = blockState.getOrDefault(RagiumBlockProperties.ACTIVE, false)
    override val machineTier: HTMachineTier
        get() {
            return HTMachineTier.BASIC
        }

    /*override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        components.set(RagiumComponentTypes.MACHINE_TIER, machineTier)
    }*/

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun setBlockState(blockState: BlockState) {
        val oldTier: HTMachineTier = machineTier
        super.setBlockState(blockState)
        ifPresentWorld { level: Level ->
            if (!level.isClientSide && oldTier != machineTier) {
                onUpdateTier(oldTier, machineTier)
                // RagiumAPI.LOGGER.info("Machine tier updated: $oldTier -> $tier")
            }
        }
    }

    final override fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult {
        // Toggle multiblock preview
        if (player.isShiftKeyDown) {
            showPreview = !showPreview
            return InteractionResult.SUCCESS
        }
        // Upgrade machine when clicked with machine hull
        if (upgrade(level, player, HTMachineTier.ADVANCED)) {
            return InteractionResult.SUCCESS
        }
        if (upgrade(level, player, HTMachineTier.ELITE)) {
            return InteractionResult.SUCCESS
        }
        if (upgrade(level, player, HTMachineTier.ULTIMATE)) {
            return InteractionResult.SUCCESS
        }
        // Insert fluid from holding stack
        if (interactWithFluidStorage(player)) {
            return InteractionResult.SUCCESS
        }
        // Validate multiblock
        val data: HTMultiblockData = collectData { player.displayClientMessage(it, true) }
        return when (data.result) {
            TriState.FALSE -> InteractionResult.PASS
            else -> {
                if (!level.isClientSide) {
                    // init multiblock data
                    processData(data)
                    // open machine screen
                    player.openMenu(state.getMenuProvider(level, pos))
                    // HTInteractMachineCriterion.trigger(player, machineKey, tier)
                }
                InteractionResult.SUCCESS
            }
        }
    }

    private fun upgrade(level: Level, player: Player, newTier: HTMachineTier): Boolean {
        val stack: ItemStack = player.getItemInHand(InteractionHand.MAIN_HAND)
        return if (stack.isOf(newTier.getHull()) && machineTier < newTier) {
            level.replaceBlockState(blockPos) { stateIn: BlockState ->
                // stateIn.setValue(HTMachineTier.PROPERTY, newTier)
                null
            }
            level.playSound(null, blockPos, SoundEvents.PLAYER_LEVELUP, player.soundSource, 1f, 0.5f)
            stack.shrink(1)
            true
        } else {
            false
        }
    }

    protected fun checkMultiblockOrThrow() {
        if (collectData().result == TriState.FALSE) {
            throw HTMachineException.Custom(true, "Multiblock validation failed!")
        }
    }

    protected fun activateState(level: Level, pos: BlockPos, newState: Boolean) {
        if (!level.isClientSide) {
            level.replaceBlockState(pos) { stateIn: BlockState ->
                if (stateIn.hasProperty(RagiumBlockProperties.ACTIVE)) {
                    if (stateIn.getValue(RagiumBlockProperties.ACTIVE) == newState) {
                        null
                    } else {
                        stateIn.setValue(RagiumBlockProperties.ACTIVE, newState)
                    }
                } else {
                    null
                }
            }
        }
    }

    //    Ticking    //

    final override val tickRate: Int = machineTier.tickRate

    final override fun tickSecond(level: Level, pos: BlockPos, state: BlockState) {
        val serverLevel: ServerLevel = level.asServerLevel() ?: return
        val network: HTEnergyNetwork = serverLevel.getEnergyNetwork().getOrNull() ?: return
        if (energyFlag == HTEnergyNetwork.Flag.CONSUME && !network.canConsume(machineTier.processCost)) {
            LOGGER.error("Failed to extract required energy from network!")
            return
        }
        runCatching { process(serverLevel, pos) }
            .onSuccess {
                if (!energyFlag.processAmount(network, machineTier.processCost, true)) {
                    LOGGER.error("Failed to interact energy network")
                    return
                }
            }.onSuccess {
                machineKey.getProperty().ifPresent(HTMachinePropertyKeys.SOUND) {
                    serverLevel.playSound(null, pos, it, SoundSource.BLOCKS, 0.2f, 1.0f)
                }
                activateState(serverLevel, pos, true)
                onSucceeded(serverLevel, pos)
            }.onFailure { throwable: Throwable ->
                activateState(serverLevel, pos, false)
                onFailed(serverLevel, pos)
                val throwable1: Throwable =
                    when (throwable) {
                        is HTMachineException -> throwable.takeIf { it.showInLog || !FMLLoader.isProduction() }
                        else -> throwable
                    } ?: return@onFailure
                LOGGER.error("Error on {} at {}: {}", machineKey, blockPosText(pos).string, throwable1.message)
            }
        setChanged()
    }

    open val energyFlag: HTEnergyNetwork.Flag = HTEnergyNetwork.Flag.CONSUME

    /**
     * 機械の処理を行います。
     *
     * 投げられた例外は安全に処理されます。
     * @throws HTMachineException 機械がエネルギーを消費しない場合
     */
    abstract fun process(level: ServerLevel, pos: BlockPos)

    open fun onSucceeded(level: ServerLevel, pos: BlockPos) {
    }

    open fun onFailed(level: ServerLevel, pos: BlockPos) {
    }

    val containerData: ContainerData = object : ContainerData {
        override fun get(index: Int): Int = when (index) {
            0 -> ticks
            1 -> tickRate
            else -> -1
        }

        override fun set(index: Int, value: Int) {}

        override fun getCount(): Int = 2
    }

    //    HTControllerHolder    //

    final override var showPreview: Boolean = false

    override fun getMultiblockMap(): HTMultiblockMap.Relative? = machineKey.getProperty()[HTMachinePropertyKeys.MULTIBLOCK_MAP]

    final override fun getController(): HTControllerDefinition? = ifPresentWorld { HTControllerDefinition(it, blockPos, front) }

    //    MenuProvider    //

    final override fun getDisplayName(): Component = machineTier.createPrefixedText(machineKey)

    //    HTBlockEntityHandlerProvider    //

    final override fun getEnergyStorage(direction: Direction?): IEnergyStorage? =
        level?.getEnergyNetwork()?.getOrNull()?.let(HTStorageIO.INPUT::wrapEnergyStorage)
}
