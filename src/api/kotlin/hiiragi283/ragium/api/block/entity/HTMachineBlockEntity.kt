package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.event.HTMachineProcessEvent
import hiiragi283.ragium.api.extension.dropStacks
import hiiragi283.ragium.api.extension.getOrDefault
import hiiragi283.ragium.api.machine.HTMachineAccess
import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineException
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.multiblock.HTMultiblockController
import hiiragi283.ragium.api.multiblock.HTMultiblockData
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.UUIDUtil
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import java.util.*
import java.util.function.Supplier
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * 機械のベースとなる[HTBlockEntity]
 */
abstract class HTMachineBlockEntity(
    type: Supplier<out BlockEntityType<*>>,
    pos: BlockPos,
    state: BlockState,
    override val machineType: HTMachineType,
    private val baseTickRate: Int = 200,
) : HTBlockEntity(type, pos, state),
    MenuProvider,
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

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        ItemEnchantments.CODEC
            .encodeStart(registryOps, enchantments)
            .ifSuccess { nbt.put(ENCH_KEY, it) }
        nbt.putBoolean(ACTIVE_KEY, isActive)
        ownerUUID?.let { uuid: UUID ->
            UUIDUtil.STRING_CODEC
                .encodeStart(registryOps, uuid)
                .ifSuccess { nbt.put(OWNER_KEY, it) }
        }
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        ItemEnchantments.CODEC
            .parse(registryOps, nbt.get(ENCH_KEY))
            .ifSuccess(::onUpdateEnchantment)
        isActive = nbt.getBoolean(ACTIVE_KEY)
        UUIDUtil.STRING_CODEC
            .parse(registryOps, nbt.get(OWNER_KEY))
            .ifSuccess { uuid: UUID ->
                this.ownerUUID = uuid
            }
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        val enchantments: ItemEnchantments = componentInput.get(DataComponents.ENCHANTMENTS) ?: return
        onUpdateEnchantment(enchantments)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        if (!enchantments.isEmpty) {
            components.set(DataComponents.ENCHANTMENTS, enchantments)
        }
    }

    //    Enchantment    //

    final override var enchantments: ItemEnchantments = ItemEnchantments.EMPTY

    final override var costModifier: Int = 1

    override fun onUpdateEnchantment(newEnchantments: ItemEnchantments) {
        this.enchantments = newEnchantments
        // Efficiency -> Increase process speed
        val effLevel: Int = getEnchantmentLevel(Enchantments.EFFICIENCY)
        if (effLevel in (1..9)) {
            this.tickRate = (baseTickRate * ((10 - effLevel) / 10f)).roundToInt()
        }
        // Unbreaking -> Decrease energy cost
        this.costModifier =
            max(1, effLevel - getEnchantmentLevel(Enchantments.UNBREAKING))
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
            // Insert fluid from holding stack
            getFluidHandler(null)?.let { handler: IFluidHandler ->
                if (FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, handler)) {
                    return InteractionResult.SUCCESS
                }
            }
        }
        return super.onRightClicked(state, level, pos, player, hitResult)
    }

    override fun openMenu(player: Player, provider: MenuProvider) {
        player.openMenu(provider, pos)
    }

    protected fun validateMultiblock(controller: HTMultiblockController, player: Player?): Result<HTMultiblockData> = runCatching {
        val data: HTMultiblockData =
            controller.collectData { text: Component -> player?.displayClientMessage(text, true) }
        if (data.result == TriState.FALSE) {
            throw HTMachineException.InvalidMultiblock()
        }
        data
    }

    //    Ticking    //

    final override var tickRate: Int = baseTickRate
        protected set

    override fun tickClient(level: Level, pos: BlockPos, state: BlockState) {
        if (isActive) {
            // spawn particles
            machineType.particleHandler?.addParticle(level, pos, level.random, front)
        }
    }

    final override fun tickSecond(level: Level, pos: BlockPos, state: BlockState) {
        if (!level.isClientSide) {
            tickOnServer(level as ServerLevel, pos)
            setChanged()
        }
    }

    private fun tickOnServer(level: ServerLevel, pos: BlockPos) {
        val network: IEnergyStorage = RagiumAPI.getInstance().getEnergyNetwork(level)
        val energyData: HTMachineEnergyData = getRequiredEnergy(level, pos)
        // 取得したエネルギー量を処理できるか判定
        val energyResult: Result<Unit> = energyData.handleEnergy(network, costModifier, true).onFailure(::failed)
        if (energyResult.isFailure) return
        runCatching { process(level, pos) }.fold(
            {
                energyData.handleEnergy(network, costModifier, false)
                isActive = true
                errorCache = null
                machineType.soundEvent?.let {
                    level.playSound(
                        null,
                        pos,
                        it,
                        SoundSource.BLOCKS,
                        RagiumAPI.getInstance().getMachineSoundVolume(),
                        1.0f,
                    )
                }
                onSucceeded()
                FORGE_BUS.post(HTMachineProcessEvent.Success(this))
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
        onFailed(throwable)
        FORGE_BUS.post(HTMachineProcessEvent.Failed(this, throwable))
    }

    protected open fun onSucceeded() {}

    protected open fun onFailed(throwable: Throwable) {}

    override fun setPlacedBy(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack,
    ) {
        if (placer == null) return
        this.ownerUUID = placer.uuid
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean,
    ) {
        getItemHandler(null)?.dropStacks(level, pos)
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

    override fun getDisplayName(): Component = machineType.text.withStyle(ChatFormatting.WHITE)

    //    HTErrorHoldingBlockEntity    //

    private var errorCache: String? = null

    final override fun getErrorMessage(): String? = errorCache

    //    HTPlayerOwningBlockEntity    //

    override var ownerUUID: UUID? = null
}
