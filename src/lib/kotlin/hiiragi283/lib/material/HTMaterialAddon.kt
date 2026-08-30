package hiiragi283.lib.material

import hiiragi283.lib.collection.mapOptional
import hiiragi283.lib.material.part.HTPartKey
import hiiragi283.lib.property.HTPropertyGetter
import hiiragi283.lib.property.HTPropertyMap
import hiiragi283.lib.property.buildPropertyMap
import hiiragi283.lib.registry.createKey
import hiiragi283.lib.resource.HTKeyLike
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.fml.IExtensionPoint
import net.neoforged.fml.ModList

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.2
 */
interface HTMaterialAddon : IExtensionPoint {
    companion object {
        @JvmStatic
        fun getAllAddons(): List<HTMaterialAddon> = ModList.get()
            .sortedMods
            .mapOptional { it.getCustomExtension(HTMaterialAddon::class.java) }
            .sortedBy(HTMaterialAddon::priority)
    }

    val priority: Int

    //    Part    //

    /**
     * 新規で部品を登録します。
     */
    fun registerPart(register: PartRegister) {}

    fun interface PartRegister {
        fun register(key: HTPartKey, idPattern: String, properties: HTPropertyGetter)

        fun register(key: HTPartKey, idPattern: String, builderAction: HTPropertyMap.Mutable.() -> Unit) {
            register(key, idPattern, buildPropertyMap(builderAction))
        }
    }

    //    Material    //

    /**
     * 既存の[ブロック][Block]を登録します。
     */
    fun registerExistingBlock(consumer: BlockConsumer) {}

    fun interface BlockConsumer {
        fun accept(part: HTPartKey, material: HTMaterialKey, key: ResourceKey<Block>)

        fun accept(part: HTPartKey, material: HTMaterialKey, id: Identifier) {
            this.accept(part, material, Registries.BLOCK.createKey(id))
        }

        fun accept(part: HTPartKey, material: HTMaterialKey, like: HTKeyLike<Block>) {
            this.accept(part, material, like.getKey())
        }
    }

    /**
     * 既存の[アイテム][Item]を登録します。
     */
    fun registerExistingItem(consumer: ItemConsumer) {}

    fun interface ItemConsumer {
        fun accept(part: HTPartKey, material: HTMaterialKey, key: ResourceKey<Item>)

        fun accept(part: HTPartKey, material: HTMaterialKey, id: Identifier) {
            this.accept(part, material, Registries.ITEM.createKey(id))
        }

        fun accept(part: HTPartKey, material: HTMaterialKey, like: HTKeyLike<Item>) {
            this.accept(part, material, like.getKey())
        }
    }

    /**
     * 素材のプロパティを編集します。
     */
    fun modifyMaterial(provider: MaterialProvider) {}

    fun interface MaterialProvider {
        fun builder(key: HTMaterialKey): HTPropertyMap.Mutable
    }

    fun registerMaterialBlock(register: MaterialEntryRegister) {}

    fun registerMaterialItem(register: MaterialEntryRegister) {}

    fun interface MaterialEntryRegister {
        fun register(material: HTMaterialKey, part: HTPartKey)

        fun registerAll(material: HTMaterialKey, parts: Iterable<HTPartKey>) {
            parts.forEach { register(material, it) }
        }

        fun registerAll(material: HTMaterialKey, vararg parts: HTPartKey) {
            parts.forEach { register(material, it) }
        }
    }
}
