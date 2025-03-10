package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTEnchantableBlockEntity
import hiiragi283.ragium.api.block.entity.HTErrorHoldingBlockEntity
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.block.entity.HTPlayerOwningBlockEntity
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidSlotHandler
import hiiragi283.ragium.api.storage.item.HTItemSlotHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.level.Level
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler

/**
 * 機械の参照を表すインタフェース
 */
interface HTMachineAccess :
    HTEnchantableBlockEntity,
    HTErrorHoldingBlockEntity,
    HTHandlerBlockEntity,
    HTItemSlotHandler,
    HTFluidSlotHandler,
    HTPlayerOwningBlockEntity {
    /**
     * 機械がおかれている[Level]
     */
    val levelAccess: Level?

    /**
     * 機械の座標
     */
    val pos: BlockPos

    /**
     * 機械の種類
     */
    val machineType: HTMachineType

    val isActive: Boolean

    /**
     * 機械のtickを返す
     */
    val containerData: ContainerData

    fun getProgress(): Float = containerData.get(0).toFloat() / containerData.get(1).toFloat()

    /**
     * 消費/生産エネルギーにかけられる倍率
     *
     * 最小値は`1`
     */
    val costModifier: Int

    //    HTBlockEntityHandlerProvider    //

    override fun getItemHandler(direction: Direction?): IItemHandler? = if (this is HTItemSlotHandler.Empty) null else this

    override fun getFluidHandler(direction: Direction?): IFluidHandler? = if (this is HTFluidSlotHandler.Empty) null else this

    override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = RagiumAPI
        .getInstance()
        .getEnergyNetwork(levelAccess)
        ?.let(HTStorageIO.INPUT::wrapEnergyStorage)
}
