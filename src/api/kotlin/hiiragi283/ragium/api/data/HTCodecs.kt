package hiiragi283.ragium.api.data

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import net.minecraft.world.item.UseAnim

object HTCodecs {
    @JvmField
    val USE_ANIM: Codec<UseAnim> = Codec.STRING.comapFlatMap(
        { name: String ->
            for (anim: UseAnim in UseAnim.entries) {
                if (anim.name.lowercase() == name) return@comapFlatMap DataResult.success(anim)
            }
            DataResult.error { "Unknown UseAnim: $name!" }
        },
        { anim: UseAnim -> anim.name.lowercase() },
    )
}
