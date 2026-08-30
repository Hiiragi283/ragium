package hiiragi283.ragium.internal.material

import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.buildSetMultiMap
import hiiragi283.lib.collection.buildTable
import hiiragi283.lib.collection.forEach
import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.HTMaterialAddon
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.HTMaterialManager
import hiiragi283.lib.material.part.HTPart
import hiiragi283.lib.material.part.HTPartKey
import hiiragi283.lib.material.part.HTPartManager
import hiiragi283.lib.material.part.property.HTPartPropertyKeys
import hiiragi283.lib.property.HTPropertyGetter
import hiiragi283.lib.property.HTPropertyManager
import hiiragi283.lib.property.HTPropertyMap
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.internal.item.HTMaterialBlockItem
import hiiragi283.ragium.internal.item.HTMaterialItem
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.RegisterEvent

@EventBusSubscriber(modid = RagiumAPI.MOD_ID)
data object HTMaterialContentsRegister {
    @JvmStatic
    private var hasInit: Boolean = false

    @JvmStatic
    internal lateinit var partManager: HTPartManager
        private set

    @JvmStatic
    internal lateinit var existingContents: HTMaterialContents.Provider
        private set

    @JvmStatic
    internal lateinit var materialManager: HTMaterialManager
        private set

    @JvmStatic
    private lateinit var materialBlocks: Table<HTPartKey, HTMaterialKey, HTMaterialContents.BlockEntry>

    @JvmStatic
    private lateinit var materialItems: Table<HTPartKey, HTMaterialKey, HTMaterialContents.ItemEntry>

    @JvmStatic
    internal val registeredContents: HTMaterialContents.Provider by lazy {
        HTMaterialContents.Provider(
            HTMaterialContentsImpl(materialBlocks) { part: HTPartKey, key: HTMaterialKey -> "Unregistered $part block for $key" },
            HTMaterialContentsImpl(materialItems) { part: HTPartKey, key: HTMaterialKey -> "Unregistered $part item for $key" },
        )
    }

    @SubscribeEvent
    fun register(event: RegisterEvent) {
        initMaterials()

        event.register(Registries.BLOCK) { registerMaterialBlocks(materialManager, it) }
        event.register(Registries.ITEM) { registerMaterialItems(materialManager, it) }
    }

    //    Existing    //

    @JvmStatic
    private fun initMaterials() {
        if (!hasInit) {
            // 部品のプロパティを定義する
            gatherPartProperties()
            // 既存の素材コンテンツを登録する
            registerExistingContents()
            // 素材のプロパティを定義する
            gatherMaterialProperties()
            hasInit = true
        }
    }

    @JvmStatic
    private fun gatherPartProperties() {
        val partMap: Map<HTPartKey, HTPart> = buildMap {
            for (addon: HTMaterialAddon in HTMaterialAddon.getAllAddons()) {
                addon.registerPart { key: HTPartKey, idPattern: String, getter: HTPropertyGetter ->
                    val entry = HTPart(key, idPattern, getter)
                    check(this.put(key, entry) == null) { "Duplicated part registration: $key" }
                }
            }
        }
        partManager = HTPropertyManager(partMap)
    }

    @JvmStatic
    private fun registerExistingContents() {
        val existingBlocks: Table<HTPartKey, HTMaterialKey, HTMaterialContents.BlockEntry> = buildTable {
            for (addon: HTMaterialAddon in HTMaterialAddon.getAllAddons()) {
                addon.registerExistingBlock { part: HTPartKey, material: HTMaterialKey, key: ResourceKey<Block> ->
                    put(part, material, HTMaterialContents.BlockEntry(HTSimpleDeferredBlockAndItem(key), true))
                }
            }
        }
        val existingItems: Table<HTPartKey, HTMaterialKey, HTMaterialContents.ItemEntry> = buildTable {
            for (addon: HTMaterialAddon in HTMaterialAddon.getAllAddons()) {
                addon.registerExistingItem { part: HTPartKey, material: HTMaterialKey, key: ResourceKey<Item> ->
                    put(part, material, HTMaterialContents.ItemEntry(HTSimpleDeferredItem(key), true))
                }
            }
        }
        existingContents = HTMaterialContents.Provider(
            HTMaterialContentsImpl(existingBlocks) { part: HTPartKey, key: HTMaterialKey -> "Unknown $part block for $key" },
            HTMaterialContentsImpl(existingItems) { part: HTPartKey, key: HTMaterialKey -> "Unknown $part item for $key" },
        )
    }

    @JvmStatic
    private fun gatherMaterialProperties() {
        val builderMap: MutableMap<HTMaterialKey, HTPropertyMap.Mutable> = mutableMapOf()
        for (addon: HTMaterialAddon in HTMaterialAddon.getAllAddons()) {
            addon.modifyMaterial { key: HTMaterialKey -> builderMap.getOrPut(key, HTPropertyMap::Mutable) }
        }
        val materialMap: MutableMap<HTMaterialKey, HTMaterial> = mutableMapOf()
        for ((key: HTMaterialKey, builder: HTPropertyMap.Mutable) in builderMap) {
            materialMap[key] = HTMaterial(key, builder.toImmutable())
        }
        materialManager = HTPropertyManager(materialMap)
    }

    //    Register    //

    @JvmStatic
    private fun registerMaterialBlocks(manager: HTMaterialManager, helper: RegisterEvent.RegisterHelper<Block>) {
        // 素材ブロックを生成する
        materialBlocks = buildTable {
            for (addon: HTMaterialAddon in HTMaterialAddon.getAllAddons()) {
                for ((key: HTMaterialKey, partKeys: Collection<HTPartKey>) in buildSetMultiMap(sortedMapOf()) { addon.registerMaterialBlock(this::put) }.entries) {
                    for (partKey: HTPartKey in partKeys.toSortedSet()) {
                        val part: HTPart = partManager[partKey] ?: continue
                        val properties: BlockBehaviour.Properties = part[HTPartPropertyKeys.BLOCK_PROP] ?: continue
                        val id: Identifier = part.createId(key)
                        helper.register(id, Block(properties))
                        put(partKey, key, HTMaterialContents.BlockEntry(HTSimpleDeferredBlockAndItem(id), false))
                    }
                }
            }
        }
    }

    @JvmStatic
    private fun registerMaterialItems(manager: HTMaterialManager, helper: RegisterEvent.RegisterHelper<Item>) {
        // 素材ブロックのアイテムを生成する
        materialBlocks.forEach { (_, key: HTMaterialKey, block: HTMaterialContents.BlockEntry) ->
            helper.register(block.block.getId(), HTMaterialBlockItem(manager.getOrThrow(key), block.block.get(), Item.Properties()))
        }
        // 素材アイテムを生成する
        materialItems = buildTable {
            for (addon: HTMaterialAddon in HTMaterialAddon.getAllAddons()) {
                for ((key: HTMaterialKey, partKeys: Collection<HTPartKey>) in buildSetMultiMap(sortedMapOf()) { addon.registerMaterialItem(this::put) }.entries) {
                    val material: HTMaterial = manager[key] ?: continue
                    for (partKey: HTPartKey in partKeys.toSortedSet()) {
                        val part: HTPart = partManager[partKey] ?: continue
                        val id: Identifier = part.createId(key)
                        helper.register(id, HTMaterialItem(material, Item.Properties()))
                        put(partKey, key, HTMaterialContents.ItemEntry(HTSimpleDeferredItem(id), false))
                    }
                }
            }
        }
    }
}
