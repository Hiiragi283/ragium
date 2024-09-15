package hiiragi283.ragium.common.registry

import com.google.common.collect.HashBasedTable
import com.google.common.collect.Table
import hiiragi283.ragium.common.data.HTLangType
import hiiragi283.ragium.common.util.forEach
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.server.loottable.BlockLootTableGenerator
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier

class HTBlockRegister(val modId: String) : Iterable<Block> {
    private val blockCache: MutableMap<Identifier, Block> = mutableMapOf()
    private val langCache: Table<Block, HTLangType, String> = HashBasedTable.create()
    private val lootCache: MutableMap<Block, (BlockLootTableGenerator) -> Unit> = mutableMapOf()
    private val stateCache: MutableMap<Block, (BlockStateModelGenerator) -> Unit> = mutableMapOf()
    private val tagCache: MutableMap<Block, List<TagKey<Block>>> = mutableMapOf()

    fun registerCopy(name: String, parent: Block, action: Builder<Block>.() -> Unit): Block =
        register(name, Block(AbstractBlock.Settings.copy(parent)), action)

    fun registerSimple(
        name: String,
        settings: AbstractBlock.Settings = AbstractBlock.Settings.create(),
        action: Builder<Block>.() -> Unit,
    ): Block = register(name, Block(settings), action)

    fun <T : Block> register(name: String, block: T, action: Builder<T>.() -> Unit): T {
        val id: Identifier = Identifier.of(modId, name)
        check(id !in blockCache)
        Registry.register(Registries.BLOCK, id, block)
        Builder(name, block, this)
            .generateSimpleState()
            .generateSimpleLoot()
            .apply(action)
        blockCache[id] = block
        return block
    }

    //    Data Gen    //

    fun generateLang(type: HTLangType, builder: TranslationBuilder) {
        langCache.forEach { block: Block, type1: HTLangType, value: String ->
            if (type == type1) builder.add(block, value)
        }
    }

    fun generateLootTable(generator: BlockLootTableGenerator) {
        lootCache.values.forEach { it(generator) }
    }

    fun generateState(generator: BlockStateModelGenerator) {
        stateCache.values.forEach { it(generator) }
    }

    fun generateTag(builderMapper: (TagKey<Block>) -> FabricTagProvider<Block>.FabricTagBuilder) {
        tagCache.forEach { (block: Block, tagKeys: List<TagKey<Block>>) ->
            tagKeys.map(builderMapper).forEach { it.add(block) }
        }
    }

    //    Iterable    //

    override fun iterator(): Iterator<Block> = blockCache.values.iterator()

    //    Builder    //

    class Builder<T : Block> internal constructor(val name: String, val block: T, val register: HTBlockRegister) {
        //    Lang    //

        fun putLang(type: HTLangType, value: String): Builder<T> = apply {
            register.langCache.put(block, type, value)
        }

        fun putEnglishLang(value: String): Builder<T> = putLang(HTLangType.EN_US, value)

        fun putJapaneseLang(value: String): Builder<T> = putLang(HTLangType.JA_JP, value)

        //    LootTable    //

        fun generateLootTable(action: (BlockLootTableGenerator) -> Unit): Builder<T> = apply {
            register.lootCache[block] = action
        }

        fun generateSimpleLoot(): Builder<T> = generateLootTable { it.addDrop(block) }

        fun setCustomLootTable(): Builder<T> = apply {
            register.lootCache.remove(block)
        }

        //    Model    //

        fun generateState(action: (BlockStateModelGenerator) -> Unit): Builder<T> = apply {
            register.stateCache[block] = action
        }

        fun generateSimpleState(): Builder<T> = generateState { it.registerSimpleCubeAll(block) }

        fun setCustomBlockState(): Builder<T> = apply {
            register.stateCache.remove(block)
        }

        //    Tag    //

        fun registerTags(tagKeys: List<TagKey<Block>>): Builder<T> = apply {
            register.tagCache[block] = tagKeys
        }

        fun registerTags(vararg tagKeys: TagKey<Block>): Builder<T> = registerTags(tagKeys.asList())
    }
}
