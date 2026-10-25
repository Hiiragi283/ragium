package hiiragi283.ragium.api.data.map

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
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
    @JvmStatic
    private fun fluidFuel(name: String): DataMapType<Fluid, HTFluidFuel> =
        DataMapType.builder(RagiumAPI.id("fuel/$name"), Registries.FLUID, HTFluidFuel.CODEC)
            .synced(HTFluidFuel.CODEC, false)
            .build()

    /**
     * @since 26.1.12
     */
    @JvmField
    val MAGMATIC_FUEL: DataMapType<Fluid, HTFluidFuel> = fluidFuel("magmatic")

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
        event.register(MAGMATIC_FUEL)
        event.register(MORTAR_REPAIR)
    }
}
