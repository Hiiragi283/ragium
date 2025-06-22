package hiiragi283.ragium.setup

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.data.interaction.HTBlockAction
import hiiragi283.ragium.api.data.interaction.HTBreakBlockAction
import hiiragi283.ragium.api.data.interaction.HTDropItemBlockAction
import hiiragi283.ragium.api.data.interaction.HTPlaySoundBlockAction
import hiiragi283.ragium.api.data.interaction.HTReplaceBlockAction
import hiiragi283.ragium.api.data.interaction.HTSpawnEntityBlockAction
import hiiragi283.ragium.api.util.RagiumConstantValues
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object RagiumBlockActionSerializers {
    @JvmField
    val REGISTER: DeferredRegister<MapCodec<out HTBlockAction>> =
        DeferredRegister.create(RagiumRegistries.Keys.BLOCK_ACTION_SERIALIZERS, RagiumConstantValues.COMMON)

    @JvmStatic
    private fun <T : HTBlockAction> register(name: String, codec: MapCodec<T>): Supplier<MapCodec<T>> =
        REGISTER.register(name) { _: ResourceLocation -> codec }

    @JvmField
    val BREAK: Supplier<MapCodec<HTBreakBlockAction>> = register("break", HTBreakBlockAction.CODEC)

    @JvmField
    val DROP_ITEM: Supplier<MapCodec<HTDropItemBlockAction>> = register("drop_item", HTDropItemBlockAction.CODEC)

    @JvmField
    val PLAY_SOUND: Supplier<MapCodec<HTPlaySoundBlockAction>> = register("play_sound", HTPlaySoundBlockAction.CODEC)

    @JvmField
    val REPLACE: Supplier<MapCodec<HTReplaceBlockAction>> = register("replace", HTReplaceBlockAction.CODEC)

    @JvmField
    val SPAWN: Supplier<MapCodec<HTSpawnEntityBlockAction>> = register("spawn", HTSpawnEntityBlockAction.CODEC)
}
