package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.registry.HTDeferredHolder
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.text.HTHasText
import hiiragi283.ragium.api.text.HTHasTranslationKey
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class HTDeferredOnlyBlock<BLOCK : Block> :
    HTDeferredHolder<Block, BLOCK>,
    HTHasTranslationKey,
    HTHasText,
    HTItemHolderLike {
    constructor(key: ResourceKey<Block>) : super(key)

    constructor(id: ResourceLocation) : super(Registries.BLOCK, id)

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Component = get().name

    override fun asItem(): Item = get().asItem()

    fun isOf(state: BlockState): Boolean = state.`is`(this)
}
