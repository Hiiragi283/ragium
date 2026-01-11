package hiiragi283.ragium.common

import com.lowdragmc.lowdraglib2.plugin.ILDLibPlugin
import com.lowdragmc.lowdraglib2.plugin.LDLibPlugin
import com.lowdragmc.lowdraglib2.syncdata.AccessorRegistries
import com.lowdragmc.lowdraglib2.syncdata.accessor.direct.CustomDirectAccessor
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.item.component.HTSpawnerMob
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.DyeColor

@LDLibPlugin
class RagiumLDLibPlugin : ILDLibPlugin {
    override fun onLoad() {
        // Dye Color
        AccessorRegistries.registerAccessor(
            CustomDirectAccessor
                .builder(DyeColor::class.java)
                .codec(VanillaBiCodecs.COLOR)
                .build(),
        )

        // Mob Spawner
        AccessorRegistries.registerAccessor(
            CustomDirectAccessor
                .builder(HTSpawnerMob::class.java)
                .codec(HTSpawnerMob.CODEC)
                .build(),
        )
    }

    private fun <T : Any> CustomDirectAccessor.Builder<T>.codec(
        codec: BiCodec<in RegistryFriendlyByteBuf, T>,
    ): CustomDirectAccessor.Builder<T> = this.codec(codec.codec).streamCodec(codec.streamCodec)
}
