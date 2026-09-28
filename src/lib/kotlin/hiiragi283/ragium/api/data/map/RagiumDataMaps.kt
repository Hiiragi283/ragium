package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.datamaps.DataMapType
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent

/**
 * Ragiumで使用される[DataMapType]をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@EventBusSubscriber
data object RagiumDataMaps {
    /**
     * モルタルによるブロックの修復
     */
    @JvmField
    val MORTAR_REPAIR: DataMapType<Block, Block> = run {
        val blockCodec: Codec<Block> = BuiltInRegistries.BLOCK.byNameCodec()
        DataMapType.builder(RagiumAPI.id("mortar_repair"), Registries.BLOCK, blockCodec)
            .synced(blockCodec, false)
            .build()
    }

    @SubscribeEvent
    fun registerDataMapType(event: RegisterDataMapTypesEvent) {
        event.register(MORTAR_REPAIR)
    }
}
