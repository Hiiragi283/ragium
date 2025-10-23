package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.block.entity.HTBlockInteractContext
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.storage.item.getItemStack
import hiiragi283.ragium.common.storage.item.HTMachineUpgradeItemHandler
import hiiragi283.ragium.setup.RagiumAttachmentTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import java.util.function.Consumer

/**
 * 電力を扱う設備に使用される[HTConfigurableBlockEntity]の拡張クラス
 */
abstract class HTMachineBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTConfigurableBlockEntity(type, pos, state) {
    val upgradeHandler: HTMachineUpgradeItemHandler get() = getData(RagiumAttachmentTypes.MACHINE_UPGRADE)

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        output.putBoolean("is_active", this.isActive)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        this.isActive = input.getBoolean("is_active", false)
    }

    override fun onRightClicked(context: HTBlockInteractContext): InteractionResult = openGui(context.player, name)

    protected abstract fun openGui(player: Player, title: Component): InteractionResult

    override fun dropInventory(consumer: Consumer<ItemStack>) {
        super.dropInventory(consumer)
        upgradeHandler.getItemSlots(upgradeHandler.getItemSideFor()).map(HTItemSlot::getItemStack).forEach(consumer)
    }

    //    Ticking    //

    /**
     * このブロックエンティティがtick当たりで生産する電力の値
     */
    protected abstract val energyUsage: Int

    var isActive: Boolean = false
        protected set

    final override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        val network: HTEnergyBattery = getter(level) ?: return false
        val result: Boolean = onUpdateServer(level, pos, state, network)
        // 以前の結果と異なる場合は強制的に同期させる
        if (result != this.isActive) {
            isActive = result
            return true
        }
        return result
    }

    protected abstract fun onUpdateServer(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        network: HTEnergyBattery,
    ): Boolean

    //    Energy Storage    //

    protected val getter: (Level?) -> HTEnergyBattery? = RagiumPlatform.INSTANCE::getEnergyNetwork

    protected fun getModifiedEnergy(base: Int): Int = upgradeHandler.getTier()?.modifyProcessorRate(base) ?: base
}
