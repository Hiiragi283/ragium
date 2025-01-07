package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.*
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockProvider
import hiiragi283.ragium.api.storage.HTFluidInteractable
import hiiragi283.ragium.api.util.DelegatedLogger
import hiiragi283.ragium.api.util.HTMachineException
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.advancement.HTInteractMachineCriterion
import hiiragi283.ragium.common.init.RagiumBlockProperties
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.component.ComponentMap
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.PropertyDelegate
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import org.slf4j.Logger

/**
 * Ragiumで使用する機械の基礎クラス
 */
abstract class HTMachineBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(type, pos, state),
    NamedScreenHandlerFactory,
    SidedStorageBlockEntity,
    HTFluidInteractable,
    HTMachineProvider,
    HTMachineTierProvider {
    companion object {
        @JvmStatic
        private val logger: Logger by DelegatedLogger()
    }

    val definition: HTMachineDefinition
        get() = HTMachineDefinition(machineKey, tier)

    val facing: Direction
        get() = cachedState.getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
    val isActive: Boolean
        get() = cachedState.getOrDefault(RagiumBlockProperties.ACTIVE, false)
    override val tier: HTMachineTier
        get() = cachedState.tier

    //    HTBlockEntityBase    //
    final override fun addComponents(builder: ComponentMap.Builder) {
        builder.add(HTMachineTier.COMPONENT_TYPE, tier)
    }

    @Suppress("OVERRIDE_DEPRECATION", "DEPRECATION")
    final override fun setCachedState(state: BlockState) {
        val oldTier: HTMachineTier = tier
        super.setCachedState(state)
        ifPresentWorld { world: World ->
            if (!world.isClient && oldTier != tier) {
                onTierUpdated(oldTier, tier)
                // RagiumAPI.LOGGER.info("Machine tier updated: $oldTier -> $tier")
            }
        }
    }

    /**
     * [HTMachineTier.PROPERTY]が更新されたときに呼び出されます。
     */
    open fun onTierUpdated(oldTier: HTMachineTier, newTier: HTMachineTier) {}

    final override fun onRightClicked(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        // Upgrade machine when clicked with machine hull
        if (upgrade(world, player, HTMachineTier.BASIC)) {
            return ActionResult.success(world.isClient)
        }
        if (upgrade(world, player, HTMachineTier.ADVANCED)) {
            return ActionResult.success(world.isClient)
        }
        // Insert fluid from holding stack
        if (interactWithFluidStorage(player)) {
            return ActionResult.success(world.isClient)
        }
        // Validate multiblock
        val result: Boolean = (this as? HTMultiblockProvider)?.multiblockManager?.onUse(state, player) != false
        // open machine screen
        if (result) {
            if (!world.isClient) {
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos))
                HTInteractMachineCriterion.trigger(player, machineKey, tier)
            }
            return ActionResult.success(world.isClient)
        }
        return ActionResult.PASS
    }

    private fun upgrade(world: World, player: PlayerEntity, newTier: HTMachineTier): Boolean {
        val stack: ItemStack = player.getStackInMainHand()
        return if (stack.isOf(newTier.getHull()) && tier < newTier) {
            world.replaceBlockState(pos) { stateIn: BlockState -> stateIn.with(HTMachineTier.PROPERTY, newTier) }
            world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, player.soundCategory, 1f, 0.5f)
            stack.decrement(1)
            true
        } else {
            false
        }
    }

    protected fun activateState(world: World, pos: BlockPos, newState: Boolean) {
        if (!world.isClient) {
            world.replaceBlockState(pos) { stateIn: BlockState ->
                if (stateIn.contains(RagiumBlockProperties.ACTIVE)) {
                    if (stateIn.get(RagiumBlockProperties.ACTIVE) == newState) {
                        null
                    } else {
                        stateIn.with(RagiumBlockProperties.ACTIVE, newState)
                    }
                } else {
                    null
                }
            }
        }
    }

    final override val tickRate: Int = tier.tickRate

    final override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        if (world.isClient) return
        val network: HTEnergyNetwork = world.getEnergyNetwork().ifError(logger::error).getOrNull() ?: return
        if (energyFlag == HTEnergyNetwork.Flag.CONSUME && !network.canConsume(tier.processCost)) {
            logger.error("Failed to extract required energy from network!")
            return
        }
        runCatching { process(world, pos) }
            .onSuccess {
                if (!energyFlag.processAmount(network, tier.processCost)) {
                    logger.error("Failed to interact energy network")
                    return
                }
            }.onSuccess {
                machineKey.getEntryOrNull()?.ifPresent(HTMachinePropertyKeys.SOUND) {
                    world.playSound(null, pos, it, SoundCategory.BLOCKS, 0.2f, 1.0f)
                }
                activateState(world, pos, true)
                onSucceeded(world, pos)
            }.onFailure { throwable: Throwable ->
                activateState(world, pos, false)
                onFailed(world, pos)
                val throwable1: Throwable = when (throwable) {
                    is HTMachineException -> throwable.takeIf(HTMachineException::showInLog)
                    else -> throwable
                } ?: return@onFailure
                logger.error("Error on {} at {}: {}", machineKey, blockPosText(pos).string, throwable1.message)
            }
        markDirty()
    }

    open val energyFlag: HTEnergyNetwork.Flag = HTEnergyNetwork.Flag.CONSUME

    /**
     * 機械の処理を行います。
     *
     * 投げられた例外は安全に処理されます。
     * @throws HTMachineException 機械がエネルギーを消費しない場合
     */
    abstract fun process(world: World, pos: BlockPos)

    open fun onSucceeded(world: World, pos: BlockPos) {}

    open fun onFailed(world: World, pos: BlockPos) {}

    val property: PropertyDelegate = object : PropertyDelegate {
        override fun get(index: Int): Int = when (index) {
            0 -> ticks
            1 -> tickRate
            else -> -1
        }

        override fun set(index: Int, value: Int) {}

        override fun size(): Int = 3
    }

    //    NamedScreenHandlerFactory    //

    final override fun getDisplayName(): Text = tier.createPrefixedText(machineKey)
}
