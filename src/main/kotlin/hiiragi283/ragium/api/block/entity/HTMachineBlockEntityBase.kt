package hiiragi283.ragium.api.block.entity

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.multiblock.HTControllerHolder
import hiiragi283.ragium.api.util.HTMachineException
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentMap
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.TriState
import net.minecraft.world.InteractionHand
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import org.slf4j.Logger

abstract class HTMachineBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntity(type, pos, state),
    MenuProvider,
    HTControllerHolder,
    HTMachineProvider,
    HTMachineTierProvider {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    val definition: HTMachineDefinition
        get() = HTMachineDefinition(machineKey, tier)

    val front: Direction
        get() = blockState.getOrDefault(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
    val isActive: Boolean
        get() = blockState.getOrDefault(RagiumBlockProperties.ACTIVE, false)
    override val tier: HTMachineTier
        get() = blockState.machineTier

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        components.set(RagiumComponentTypes.MACHINE_TIER, tier)
    }

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    override fun setBlockState(blockState: BlockState) {
        val oldTier: HTMachineTier = tier
        super.setBlockState(blockState)
        ifPresentWorld { level: Level ->
            if (!level.isClientSide && oldTier != tier) {
                onTierUpdated(oldTier, tier)
                // RagiumAPI.LOGGER.info("Machine tier updated: $oldTier -> $tier")
            }
        }
    }

    /**
     * [HTMachineTier.PROPERTY]が更新されたときに呼び出されます。
     */
    open fun onTierUpdated(oldTier: HTMachineTier, newTier: HTMachineTier) {
    }

    private fun upgrade(level: Level, player: Player, newTier: HTMachineTier): Boolean {
        val stack: ItemStack = player.getItemInHand(InteractionHand.MAIN_HAND)
        return if (stack.isOf(newTier.getHull()) && tier < newTier) {
            level.replaceBlockState(blockPos) { stateIn: BlockState ->
                stateIn.setValue(
                    HTMachineTier.PROPERTY,
                    newTier,
                )
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

    final override val tickRate: Int = tier.tickRate

    final override fun tickSecond(level: Level, pos: BlockPos, state: BlockState) {
        if (level.isClientSide) return
        val network: HTEnergyNetwork = level.getEnergyNetwork().getOrNull() ?: return
        if (energyFlag == HTEnergyNetwork.Flag.CONSUME && !network.canConsume(tier.processCost)) {
            LOGGER.error("Failed to extract required energy from network!")
            return
        }
        runCatching { process(level, pos) }
            .onSuccess {
                if (!energyFlag.processAmount(network, tier.processCost, true)) {
                    LOGGER.error("Failed to interact energy network")
                    return
                }
            }.onSuccess {
                machineKey.getEntryOrNull()?.ifPresent(HTMachinePropertyKeys.SOUND) {
                    level.playSound(null, pos, it, SoundSource.BLOCKS, 0.2f, 1.0f)
                }
                activateState(level, pos, true)
                onSucceeded(level, pos)
            }.onFailure { throwable: Throwable ->
                activateState(level, pos, false)
                onFailed(level, pos)
                val throwable1: Throwable =
                    when (throwable) {
                        is HTMachineException -> throwable.takeIf(HTMachineException::showInLog)
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
    abstract fun process(level: Level, pos: BlockPos)

    open fun onSucceeded(level: Level, pos: BlockPos) {
    }

    open fun onFailed(level: Level, pos: BlockPos) {
    }

    //    MenuProvider    //

    final override fun getDisplayName(): Component = tier.createPrefixedText(machineKey)
}
