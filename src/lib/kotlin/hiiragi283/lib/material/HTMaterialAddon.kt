package hiiragi283.lib.material

import hiiragi283.lib.collection.mapOptional
import hiiragi283.lib.property.HTPropertyMap
import hiiragi283.lib.registry.createKey
import hiiragi283.lib.resource.HTKeyLike
import hiiragi283.ragium.api.tag.HTPart
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

    //    Material    //

    /**
     * 既存の[ブロック][Block]を登録します。
     */
    fun registerExistingBlock(consumer: BlockConsumer) {}

    fun interface BlockConsumer {
        fun accept(part: HTPart, material: HTMaterialKey, key: ResourceKey<Block>)

        fun accept(part: HTPart, material: HTMaterialKey, id: Identifier) {
            this.accept(part, material, Registries.BLOCK.createKey(id))
        }

        fun accept(part: HTPart, material: HTMaterialKey, like: HTKeyLike<Block>) {
            this.accept(part, material, like.getKey())
        }
    }

    /**
     * 既存の[アイテム][Item]を登録します。
     */
    fun registerExistingItem(consumer: ItemConsumer) {}

    fun interface ItemConsumer {
        fun accept(part: HTPart, material: HTMaterialKey, key: ResourceKey<Item>)

        fun accept(part: HTPart, material: HTMaterialKey, id: Identifier) {
            this.accept(part, material, Registries.ITEM.createKey(id))
        }

        fun accept(part: HTPart, material: HTMaterialKey, like: HTKeyLike<Item>) {
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
        fun register(material: HTMaterialKey, part: HTPart)

        fun registerAll(material: HTMaterialKey, parts: Iterable<HTPart>) {
            parts.forEach { register(material, it) }
        }

        fun registerAll(material: HTMaterialKey, vararg parts: HTPart) {
            parts.forEach { register(material, it) }
        }
    }
}
