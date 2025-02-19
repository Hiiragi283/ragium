package hiiragi283.ragium.api.material

import com.mojang.serialization.Codec
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Keyable
import java.util.stream.Stream

/**
 * [HTMaterialKey]のレジストリ
 * @see hiiragi283.ragium.api.RagiumAPI.getMaterialRegistry
 */
interface HTMaterialRegistry : Keyable {
    fun createTypedCodec(): Codec<HTTypedMaterial> = HTMaterialKey.CODEC.xmap(::getTypedMaterial, HTTypedMaterial::material)

    //    Type    //

    /**
     * 登録された[HTMaterialKey]の一覧
     */
    val keys: Set<HTMaterialKey>

    val typedMaterials: List<HTTypedMaterial>
        get() = keys.map(::getTypedMaterial)

    /**
     * 指定した[key]に登録された[HTMaterialType]を返します。
     * @throws IllegalStateException 指定した[key]が登録されていない場合
     */
    fun getType(key: HTMaterialKey): HTMaterialType

    /**
     * 指定した[key]に登録された[HTMaterialType]から[HTTypedMaterial]返します。
     * @throws IllegalStateException 指定した[key]が登録されていない場合
     */
    fun getTypedMaterial(key: HTMaterialKey): HTTypedMaterial = TypeImpl(getType(key), key)

    /**
     * 指定した[key]が登録されているか判定します。
     */
    operator fun contains(key: HTMaterialKey): Boolean = key in keys

    //    Keyable    //

    override fun <T : Any> keys(ops: DynamicOps<T>): Stream<T> = keys
        .stream()
        .map(HTMaterialKey::name)
        .map(ops::createString)

    private class TypeImpl(override val type: HTMaterialType, override val material: HTMaterialKey) : HTTypedMaterial
}
