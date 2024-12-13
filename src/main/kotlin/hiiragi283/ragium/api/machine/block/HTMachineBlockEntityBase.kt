package hiiragi283.ragium.api.machine.block

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTBlockEntityBase
import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.multiblock.HTMultiblockPatternProvider
import hiiragi283.ragium.api.storage.HTFluidInteractable
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.api.util.HTDynamicPropertyDelegate
import hiiragi283.ragium.api.util.HTUnitResult
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.common.advancement.HTInteractMachineCriterion
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.network.HTMachineKeySyncPayload
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.component.ComponentMap
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.TagKey
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

/**
 * A base class for machine block entity
 *
 * All [net.minecraft.block.entity.BlockEntity] using [HTMachineKey] and [HTMachineTier] should extend this class
 */
abstract class HTMachineBlockEntityBase(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(type, pos, state),
    NamedScreenHandlerFactory,
    SidedStorageBlockEntity,
    HTFluidInteractable {
    abstract var key: HTMachineKey
        protected set
    val definition: HTMachineDefinition
        get() = HTMachineDefinition(key, tier)
    val payload: HTMachineKeySyncPayload
        get() = HTMachineKeySyncPayload(pos, key)

    val facing: Direction
        get() = cachedState.getOrDefault(Properties.HORIZONTAL_FACING, Direction.NORTH)
    val isActive: Boolean
        get() = cachedState.getOrDefault(RagiumBlockProperties.ACTIVE, false)
    val tier: HTMachineTier
        get() = cachedState.getOrDefault(HTMachineTier.PROPERTY, HTMachineTier.PRIMITIVE)

    private var errorMessage: String? = null

    @Environment(EnvType.CLIENT)
    fun onPacketReceived(packet: HTMachineKeySyncPayload) {
        key = packet.key
    }

    //    HTBlockEntityBase    //

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        nbt.putMachineKey(MACHINE_KEY, key)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        key = nbt.getMachineKey(MACHINE_KEY)
    }

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
                RagiumAPI.LOGGER.info("Machine tier updated: $oldTier -> $tier")
            }
        }
    }

    /**
     * Called when [HTMachineTier.PROPERTY] was updated
     */
    open fun onTierUpdated(oldTier: HTMachineTier, newTier: HTMachineTier) {}

    final override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        // Open slot config screen when holding xxx
        /*if (player.getStackInActiveHand().isOf(RagiumContents.Circuits.PRIMITIVE)) {
            if (!world.isClient) {
                player.openHandledScreen(configScreenFactory)
            }
            return ActionResult.success(world.isClient)
        }*/
        // Upgrade machine when clicked with machine hull
        if (upgrade(world, player, RagiumItemTags.BASIC_UPGRADES, HTMachineTier.BASIC)) {
            return ActionResult.success(world.isClient)
        }
        if (upgrade(world, player, RagiumItemTags.ADVANCED_UPGRADES, HTMachineTier.ADVANCED)) {
            return ActionResult.success(world.isClient)
        }
        // Insert fluid from holding stack
        if (interactWithFluidStorage(player)) {
            return ActionResult.success(world.isClient)
        }
        // Validate multiblock
        val result: Boolean = (this as? HTMultiblockPatternProvider)?.multiblockManager?.onUse(state, player) != false
        // open machine screen
        if (result) {
            if (!world.isClient) {
                player.openHandledScreen(state.createScreenHandlerFactory(world, pos))
                HTInteractMachineCriterion.trigger(player, key, tier)
            }
            return ActionResult.success(world.isClient)
        }
        return ActionResult.PASS
    }

    private fun upgrade(
        world: World,
        player: PlayerEntity,
        tagKey: TagKey<Item>,
        newTier: HTMachineTier,
    ): Boolean {
        val stack: ItemStack = player.getStackInActiveHand()
        return if (stack.isIn(tagKey) && tier < newTier) {
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
        world.energyNetwork
            .validate(
                { energyFlag == HTEnergyNetwork.Flag.GENERATE || it.canConsume(tier.recipeCost) },
                { "Failed to extract required energy from network!" },
            ).flatMap { network: HTEnergyNetwork -> process(world, pos).map { network } }
            .validate(
                { network: HTEnergyNetwork -> energyFlag.processAmount(network, tier.recipeCost) },
                { "Failed to interact energy network" },
            ).ifSuccess { _: HTEnergyNetwork ->
                key.entry.ifPresent(HTMachinePropertyKeys.SOUND) {
                    world.playSound(null, pos, it, SoundCategory.BLOCKS, 0.2f, 1.0f)
                }
                errorMessage = null
                activateState(world, pos, true)
                onSucceeded(world, pos)
            }.ifError { error: DataResult.Error<HTEnergyNetwork> ->
                errorMessage = error.message()
                activateState(world, pos, false)
                onFailed(world, pos)
            }
        markDirty()
    }

    /**
     * Get [HTEnergyNetwork.Flag] for [tickSecond]
     * @see [HTEnergyNetwork.Flag.processAmount]
     */
    open val energyFlag: HTEnergyNetwork.Flag = HTEnergyNetwork.Flag.CONSUME

    /**
     * Called when the machine can consume/generate energy
     */
    abstract fun process(world: World, pos: BlockPos): HTUnitResult

    open fun onSucceeded(world: World, pos: BlockPos) {}

    open fun onFailed(world: World, pos: BlockPos) {}

    val property: PropertyDelegate = HTDynamicPropertyDelegate(3, ::getProperty, ::setProperty)

    protected open fun getProperty(index: Int): Int = when (index) {
        0 -> ticks
        1 -> tickRate
        else -> -1
    }

    protected open fun setProperty(index: Int, value: Int) {}

    //    NamedScreenHandlerFactory    //

    final override fun getDisplayName(): Text = tier.createPrefixedText(key)

    private val configScreenFactory = SimpleNamedScreenHandlerFactory(
        null,
        Text.literal("Slot Configuration"),
    )
}
