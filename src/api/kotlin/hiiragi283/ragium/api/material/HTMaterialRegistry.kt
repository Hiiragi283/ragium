package hiiragi283.ragium.api.material

import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Keyable
import java.util.stream.Stream

/**
 * [HTMaterialKey]のレジストリ
 * @see hiiragi283.ragium.api.RagiumAPI.getMaterialRegistry
 */
interface HTMaterialRegistry : Keyable {
    //    Type    //

    /**
     * 登録された[HTMaterialKey]の一覧
     */
    val keys: Set<HTMaterialKey>

    /**
     * 指定した[key]に登録された[HTMaterialType]を返します。
     * @throws IllegalStateException 指定した[key]が登録されていない場合
     */
    fun getType(key: HTMaterialKey): HTMaterialType

    /**
     * 指定した[key]が登録されているか判定します。
     */
    operator fun contains(key: HTMaterialKey): Boolean = key in keys

    //    Keyable    //

    override fun <T : Any> keys(ops: DynamicOps<T>): Stream<T> = keys
        .stream()
        .map(HTMaterialKey::name)
        .map(ops::createString)
}
