package hiiragi283.ragium.api.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntityHandlerProvider
import hiiragi283.ragium.api.block.entity.HTEnchantableBlockEntity
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.extension.asServerLevel
import hiiragi283.ragium.api.extension.getLevel
import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockController
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceKey
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.level.Level
import net.neoforged.neoforge.energy.IEnergyStorage

/**
 * 機械の参照を表すインタフェース
 */
interface HTMachineAccess :
    HTBlockEntityHandlerProvider,
    HTEnchantableBlockEntity,
    HTMultiblockController {
    /**
     * 機械の正面の向き
     */
    val front: Direction

    /**
     * 機械が稼働状態かどうか判定します。
     */
    val isActive: Boolean

    /**
     * 機械がおかれている[Level]
     */
    val levelAccess: Level?

    /**
     * 機械の種類
     */
    val machineKey: HTMachineKey

    /**
     * 機械の座標
     */
    val pos: BlockPos

    /**
     * 機械のtickを返す
     */
    val containerData: ContainerData

    /**
     * 消費/生産エネルギーにかけられる倍率
     *
     * 最小値は`1`
     */
    val costModifier: Int

    /**
     * 指定した[key]のレベルを取得します。
     * @return 指定したエンチャントが登録されていない，または紐づいていない場合は`0`
     */
    override fun getEnchantmentLevel(key: ResourceKey<Enchantment>): Int = enchantments.getLevel(levelAccess?.registryAccess(), key)

    //    HTBlockEntityHandlerProvider    //

    override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = levelAccess
        ?.asServerLevel()
        ?.let(RagiumAPI.getInstance()::getEnergyNetwork)
        ?.let(HTStorageIO.INPUT::wrapEnergyStorage)

    //    HTControllerHolder    //

    override fun getMultiblockMap(): HTMultiblockMap.Relative? = null

    override fun getController(): HTControllerDefinition? = levelAccess?.let { HTControllerDefinition(it, pos, front) }
}
