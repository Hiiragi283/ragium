package hiiragi283.ragium.api.extension

import com.mojang.serialization.Codec
import net.minecraft.util.StringRepresentable

//    Codec    //

/**
 * 指定した[entries]から[Codec]を返します。
 * @param T [StringRepresentable]を継承したクラス
 * @return [Codec.STRING]をベースに変換された[Codec]
 */
fun <T : StringRepresentable> stringCodec(entries: Iterable<T>): Codec<T> = Codec.STRING.comapFlatMap(
    { name: String ->
        entries.firstOrNull { it.serializedName == name }.toDataResult { "Unknown entry: $name!" }
    },
    StringRepresentable::getSerializedName,
)
