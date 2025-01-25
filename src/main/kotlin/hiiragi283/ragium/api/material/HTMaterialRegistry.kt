package hiiragi283.ragium.api.material

import com.mojang.serialization.DynamicOps
import com.mojang.serialization.Keyable
import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import java.util.stream.Stream

/**
 * [HTMaterialKey]のレジストリ
 * @see hiiragi283.ragium.api.RagiumAPI.materialRegistry
 */
interface HTMaterialRegistry : Keyable {
    //    Type    //

    val typeMap: Map<HTMaterialKey, HTMaterialType>

    /**
     * 登録された[HTMaterialKey]の一覧
     */
    val keys: Set<HTMaterialKey>
        get() = typeMap.keys

    fun getType(key: HTMaterialKey): HTMaterialType = typeMap[key] ?: error("Unknown material key: $key")

    /**
     * 指定された[key]が登録されているか判定します。
     */
    operator fun contains(key: HTMaterialKey): Boolean = key in keys

    //    Item    //

    /**
     * 指定された[prefix]と[key]に紐づいたアイテムの一覧を返します。
     * @return 値がない場合は[emptyList]
     */
    fun getItems(prefix: HTTagPrefix, key: HTMaterialKey): List<Holder<Item>>

    /**
     * 指定された[item]に紐づいた素材のデータを返します。
     * @return 値がない場合は[emptyList]
     */
    fun getDefinitions(item: ItemLike): List<HTMaterialDefinition>

    //    Keyable    //

    override fun <T : Any> keys(ops: DynamicOps<T>): Stream<T> = keys
        .stream()
        .map(HTMaterialKey::name)
        .map(ops::createString)
}
