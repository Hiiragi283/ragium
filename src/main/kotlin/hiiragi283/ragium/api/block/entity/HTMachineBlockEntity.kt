package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.capability.HTHandlerSerializer
import hiiragi283.ragium.api.energy.HTMachineEnergyData
import hiiragi283.ragium.api.event.HTMachineProcessEvent
import hiiragi283.ragium.api.extension.getOrDefault
import hiiragi283.ragium.api.fluid.HTFluidInteractable
import hiiragi283.ragium.api.machine.HTMachineAccess
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.multiblock.HTMultiblockData
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerLevel
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
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage
import java.util.function.Supplier
import kotlin.math.max

/**
 * 機械のベースとなる[HTBlockEntity]
 */
abstract class HTMachineBlockEntity(
    type: Supplier<out BlockEntityType<*>>,
    pos: BlockPos,
    state: BlockState,
    override val machineType: HTMachineType,
) : HTBlockEntity(type, pos, state),
    MenuProvider,
    HTFluidInteractable,
    HTMachineAccess {
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

    protected abstract val handlerSerializer: HTHandlerSerializer

    override fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        ItemEnchantments.CODEC
            .encodeStart(dynamicOps, enchantments)
            .ifSuccess { nbt.put(ENCH_KEY, it) }
        nbt.putBoolean(ACTIVE_KEY, isActive)
        handlerSerializer.writeNbt(nbt, dynamicOps)
    }

    override fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        ItemEnchantments.CODEC
            .parse(dynamicOps, nbt.get(ENCH_KEY))
            .ifSuccess(::updateEnchantments)
        isActive = nbt.getBoolean(ACTIVE_KEY)
        handlerSerializer.readNbt(nbt, dynamicOps)
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

    final override var enchantments: ItemEnchantments = ItemEnchantments.EMPTY

    final override var costModifier: Int = 1

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
        this.enchantments = newEnchantments
        // Efficiency -> Increase process speed
        this.tickRate = max(20, 200 - (getEnchantmentLevel(Enchantments.EFFICIENCY) * 30))
        // Unbreaking -> Decrease energy cost
        this.costModifier =
            max(1, getEnchantmentLevel(Enchantments.EFFICIENCY) - getEnchantmentLevel(Enchantments.UNBREAKING))
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    final override fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult {
        if (!level.isClientSide) {
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
            when (data.result) {
                TriState.FALSE -> return InteractionResult.PASS
                else -> {
                    // init multiblock data
                    processData(data)
                    // open machine screen
                    if (hasMenu) {
                        player.openMenu(this, pos)
                    } else {
                        return InteractionResult.PASS
                    }
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    protected open val hasMenu: Boolean = true

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
            machineType.particleHandler?.addParticle(level, pos, level.random, front)
        }
    }

    final override fun tickSecond(level: Level, pos: BlockPos, state: BlockState) {
        if (!level.isClientSide) {
            tickOnServer(level as ServerLevel, pos)
        }
        setChanged()
    }

    private fun tickOnServer(level: ServerLevel, pos: BlockPos) {
        val network: IEnergyStorage = RagiumAPI.getInstance().getEnergyNetwork(level)
        val energyData: HTMachineEnergyData = getRequiredEnergy(level, pos)
        // 取得したエネルギー量を処理できるか判定
        if (!energyData.handleEnergy(network, costModifier, true)) {
            failed(HTMachineException.HandleEnergy(false))
            return
        }
        runCatching { process(level, pos) }.fold(
            {
                energyData.handleEnergy(network, costModifier, false)
                isActive = true
                errorCache = null
                machineType.soundEvent?.let { level.playSound(null, pos, it, SoundSource.BLOCKS, 0.5f, 1.0f) }
                NeoForge.EVENT_BUS.post(HTMachineProcessEvent.Success(this))
            },
            ::failed,
        )
    }

    /**
     * 機械が要求するエネルギー量を返します。
     */
    protected abstract fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData

    /**
     * 機械の処理を行います。
     *
     * 投げられた例外は安全に処理されます。
     * @throws HTMachineException 機械がエネルギーを消費しない場合
     */
    protected abstract fun process(level: ServerLevel, pos: BlockPos)

    private fun failed(throwable: Throwable) {
        isActive = false
        errorCache = throwable.message
        NeoForge.EVENT_BUS.post(HTMachineProcessEvent.Failed(this, throwable))
    }

    final override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        handlerSerializer.dropItems(level, pos)
    }

    final override val containerData: ContainerData = object : ContainerData {
        override fun get(index: Int): Int = when (index) {
            0 -> ticks
            1 -> tickRate
            else -> -1
        }

        override fun set(index: Int, value: Int) {}

        override fun getCount(): Int = 2
    }

    //    MenuProvider    //

    override fun getDisplayName(): Component = machineType.text

    //    HTControllerHolder    //

    final override var showPreview: Boolean = false

    //    HTErrorHoldingBlockEntity    //

    private var errorCache: String? = null

    final override fun getErrorMessage(): String? = errorCache
}
