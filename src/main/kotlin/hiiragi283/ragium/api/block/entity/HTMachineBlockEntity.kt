package hiiragi283.ragium.api.block.entity

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.event.HTMachineProcessEvent
import hiiragi283.ragium.api.extension.blockPosText
import hiiragi283.ragium.api.extension.getOrDefault
import hiiragi283.ragium.api.extension.openMenu
import hiiragi283.ragium.api.fluid.HTFluidInteractable
import hiiragi283.ragium.api.machine.HTMachineAccess
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.multiblock.HTMultiblockData
import hiiragi283.ragium.api.property.get
import hiiragi283.ragium.api.property.ifPresent
import hiiragi283.ragium.api.world.HTEnergyNetwork
import hiiragi283.ragium.api.world.energyNetwork
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.fml.loading.FMLLoader
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.common.util.TriState
import org.slf4j.Logger
import java.util.function.Supplier
import kotlin.math.max

/**
 * 機械のベースとなる[HTBlockEntity]
 */
abstract class HTMachineBlockEntity(
    type: Supplier<out BlockEntityType<*>>,
    pos: BlockPos,
    state: BlockState,
    override val machineKey: HTMachineKey,
) : HTBlockEntity(type, pos, state),
    MenuProvider,
    HTFluidInteractable,
    HTMachineAccess {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    override val front: Direction
        get() = blockState.getOrDefault(
            BlockStateProperties.HORIZONTAL_FACING,
            Direction.NORTH,
        )
    override var isActive: Boolean = false
        protected set
    override val levelAccess: Level?
        get() = level
    override val pos: BlockPos
        get() = blockPos

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        ItemEnchantments.CODEC
            .encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), enchantments)
            .ifSuccess { tag.put(ENCH_KEY, it) }
        tag.putBoolean(ACTIVE_KEY, isActive)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        ItemEnchantments.CODEC
            .parse(NbtOps.INSTANCE, tag.get(ENCH_KEY))
            .ifSuccess(::updateEnchantments)
        isActive = tag.getBoolean(ACTIVE_KEY)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        val enchantments: ItemEnchantments = componentInput.get(DataComponents.ENCHANTMENTS) ?: return
        updateEnchantments(enchantments)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        if (!enchantments.isEmpty) {
            components.set(DataComponents.ENCHANTMENTS, enchantments)
        }
    }

    //    Enchantment    //

    override var enchantments: ItemEnchantments = ItemEnchantments.EMPTY
        protected set
    override val processCost: Int = 1280

    protected open fun updateEnchantments(newEnchantments: ItemEnchantments) {
        this.enchantments = newEnchantments
        this.tickRate = max(20, 200 - (getEnchantmentLevel(Enchantments.EFFICIENCY) * 30))
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
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
                    player.openMenu(::createMenu, displayName, ::writeExtraData)
                    // HTInteractMachineCriterion.trigger(player, machineKey, tier)
                }
                InteractionResult.SUCCESS
            }
        }
    }

    protected open fun writeExtraData(registryBuf: RegistryFriendlyByteBuf) {
        BlockPos.STREAM_CODEC.encode(registryBuf, blockPos)
    }

    protected fun checkMultiblockOrThrow() {
        if (collectData().result == TriState.FALSE) {
            throw HTMachineException.Custom(true, "Multiblock validation failed!")
        }
    }

    //    Ticking    //

    final override var tickRate: Int = 200
        private set

    override fun tickEach(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        ticks: Int,
    ) {
        if (isActive) {
            // spawn particles
            machineKey.getProperty()[HTMachinePropertyKeys.PARTICLE]?.addParticle(level, pos, level.random, front)
        }
    }

    final override fun tickSecond(level: Level, pos: BlockPos, state: BlockState) {
        if (!level.isClientSide) {
            tickOnServer(level as ServerLevel, pos)
        }
        setChanged()
    }

    private fun tickOnServer(level: ServerLevel, pos: BlockPos) {
        val network: HTEnergyNetwork = level.energyNetwork
        if (energyFlag == HTEnergyNetwork.Flag.CONSUME && !network.canConsume(processCost)) {
            LOGGER.error("Failed to extract required energy from network!")
            return
        }
        runCatching { process(level, pos) }
            .onSuccess {
                if (!energyFlag.processAmount(network, processCost, false)) {
                    LOGGER.error("Failed to interact energy network")
                    return
                }
            }.onSuccess {
                isActive = true
                machineKey.getProperty().ifPresent(HTMachinePropertyKeys.SOUND) { soundEvent: SoundEvent ->
                    level.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 0.5f, 1.0f)
                }
                NeoForge.EVENT_BUS.post(HTMachineProcessEvent.Success(this))
            }.onFailure { throwable: Throwable ->
                isActive = false
                NeoForge.EVENT_BUS.post(HTMachineProcessEvent.Failed(this))
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

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        super.onRemove(state, level, pos, newState, movedByPiston)
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

    //    MenuProvider    //

    override fun getDisplayName(): Component = machineKey.text

    //    HTControllerHolder    //

    final override var showPreview: Boolean = false
}
