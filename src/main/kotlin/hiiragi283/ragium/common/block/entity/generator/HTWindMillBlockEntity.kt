package hiiragi283.ragium.common.block.entity.generator

import hiiragi283.lib.transfer.useTransaction
import hiiragi283.ragium.api.data.map.RagiumDataMaps
import hiiragi283.ragium.common.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.config.HTEnergyConfig
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.transaction.Transaction

/**
 * 参考 : [TechReborn - WindMillBlockEntity](https://github.com/TechReborn/TechReborn/blob/26.3/src/main/java/techreborn/blockentity/generator/basic/WindMillBlockEntity.java)
 */
class HTWindMillBlockEntity(pos: BlockPos, state: BlockState) : HTGeneratorBlockEntity(RagiumBlockEntityTypes.WIND_MILL.get(), pos, state) {
    override fun onUpdateMachine(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = useTransaction { transaction: Transaction ->
        if (pos.y < 64) return false
        val multiplier: Int = level.getBiome(pos).getData(RagiumDataMaps.WIND_MILL_BONUS)?.multiplier ?: 1
        val inserted: Int = generateEnergy(transaction, multiplier)
        if (inserted > 0) {
            transaction.commit()
            true
        } else {
            false
        }
    }

    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.generator.windMill
}
