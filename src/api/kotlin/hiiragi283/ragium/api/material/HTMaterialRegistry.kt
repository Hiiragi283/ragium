package hiiragi283.ragium.api.material

import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Keyable
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTPropertyMap
import java.util.stream.Stream

/**
 * [HTMaterialKey]のレジストリ
 * @see RagiumAPI.getMaterialRegistry
 */
interface HTMaterialRegistry :
    Iterable<Pair<HTMaterialKey, HTPropertyMap>>,
    Keyable {
    //    Type    //

    /**
     * 登録された[HTMaterialKey]の一覧
     */
    val keys: Set<HTMaterialKey>

    /**
     * 指定した[key]に登録された[HTMaterialType]を返します。
     */
    fun getType(key: HTMaterialKey): HTMaterialType = getPropertyMap(key).getOrDefault(HTMaterialPropertyKeys.MATERIAL_TYPE)

    /**
     * 指定した[key]が登録されているか判定します。
     */
    operator fun contains(key: HTMaterialKey): Boolean = key in keys

    /**
     * 指定した[key]に紐づいた[HTPropertyMap]を返します。
     */
    fun getPropertyMap(key: HTMaterialKey): HTPropertyMap

    //    Iterable    //

    override fun iterator(): Iterator<Pair<HTMaterialKey, HTPropertyMap>> =
        keys.map { key: HTMaterialKey -> key to getPropertyMap(key) }.iterator()

    //    Keyable    //

    override fun <T : Any> keys(ops: DynamicOps<T>): Stream<T> = keys
        .stream()
        .map(HTMaterialKey::name)
        .map(ops::createString)
}
