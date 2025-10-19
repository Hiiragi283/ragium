package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.holder.HTEnergyStorageHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.getItemStack
import hiiragi283.ragium.common.block.entity.HTConfigurableBlockEntity
import hiiragi283.ragium.common.storage.energy.HTEnergyBatteryWrapper
import hiiragi283.ragium.common.storage.holder.HTSimpleEnergyStorageHolder
import hiiragi283.ragium.common.storage.item.HTMachineUpgradeItemHandler
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.setup.RagiumAttachmentTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import java.util.function.Consumer

/**
 * 電力を消費する設備に使用される[HTConfigurableBlockEntity]の拡張クラス
 */
abstract class HTMachineBlockEntity(protected val variant: HTMachineVariant, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(variant.blockEntityHolder, pos, state) {
    val upgradeHandler: HTMachineUpgradeItemHandler get() = getData(RagiumAttachmentTypes.MACHINE_UPGRADE)

    override fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult = openGui(player, name)

    protected abstract fun openGui(player: Player, title: Component): InteractionResult

    override fun dropInventory(consumer: Consumer<ItemStack>) {
        super.dropInventory(consumer)
        upgradeHandler.getItemSlots(upgradeHandler.getItemSideFor()).map(HTItemSlot::getItemStack).forEach(consumer)
    }

    //    Ticking    //

    /**
     * このブロックエンティティがtick当たりで消費する電力の値
     */
    protected val energyUsage: Int = variant.energyUsage

    protected var isActive: Boolean = false
    protected var requiredEnergy: Int = 0
    protected var usedEnergy: Int = 0

    protected fun getModifiedEnergy(base: Int): Int = upgradeHandler.getTier()?.modifyProcessorRate(base) ?: base

    final override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        val network: HTEnergyBattery = getter(level) ?: return false
        val result: Boolean = onUpdateServer(level, pos, state, network)
        isActive = result
        return result
    }

    protected abstract fun onUpdateServer(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: HTEnergyBattery,
    ): Boolean

    val progress: Float
        get() {
            val totalTick: Int = usedEnergy
            val maxTicks: Int = requiredEnergy
            if (maxTicks <= 0) return 0f
            val fixedTotalTicks: Int = totalTick % maxTicks
            return Mth.clamp(fixedTotalTicks / maxTicks.toFloat(), 0f, 1f)
        }

    //    Energy Storage    //

    private val getter: (Level?) -> HTEnergyBattery? = RagiumPlatform.INSTANCE::getEnergyNetwork

    final override fun initializeEnergyStorage(listener: HTContentListener): HTEnergyStorageHolder =
        HTSimpleEnergyStorageHolder.input(this, HTEnergyBatteryWrapper { getter(level) })

    //    Slot    //

    val containerData: ContainerData = object : ContainerData {
        override fun get(index: Int): Int = when (index) {
            0 -> usedEnergy
            1 -> requiredEnergy
            else -> -1
        }

        override fun set(index: Int, value: Int) {
            when (index) {
                0 -> usedEnergy = value
                1 -> requiredEnergy = value
            }
        }

        override fun getCount(): Int = 2
    }
}
