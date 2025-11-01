package hiiragi283.ragium.api.registry.impl

import hiiragi283.ragium.api.function.BlockFactory
import hiiragi283.ragium.api.function.IdToFunction
import hiiragi283.ragium.api.registry.HTDeferredRegister
import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.registry.createKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import java.util.function.Supplier

class HTDeferredOnlyBlockRegister(namespace: String) : HTDeferredRegister<Block>(Registries.BLOCK, namespace) {
    fun <BLOCK : Block> registerBlock(
        name: String,
        blockProp: BlockBehaviour.Properties,
        factory: BlockFactory<BLOCK>,
    ): HTDeferredOnlyBlock<BLOCK> = register(name) { _: ResourceLocation -> factory(blockProp) }

    fun registerSimpleBlock(name: String, blockProp: BlockBehaviour.Properties): HTDeferredOnlyBlock<Block> =
        registerBlock(name, blockProp, ::Block)

    //    HTDeferredRegister    //

    override fun asSequence(): Sequence<HTDeferredOnlyBlock<*>> = super.asSequence().filterIsInstance<HTDeferredOnlyBlock<*>>()

    override fun getEntries(): Collection<HTDeferredOnlyBlock<*>> = super.getEntries().filterIsInstance<HTDeferredOnlyBlock<*>>()

    override fun <I : Block> register(name: String, func: IdToFunction<out I>): HTDeferredOnlyBlock<I> =
        super.register(name, func) as HTDeferredOnlyBlock<I>

    override fun <I : Block> register(name: String, sup: Supplier<out I>): HTDeferredOnlyBlock<I> =
        super.register(name, sup) as HTDeferredOnlyBlock<I>

    override fun <I : Block> createHolder(registryKey: RegistryKey<Block>, key: ResourceLocation): HTDeferredOnlyBlock<I> =
        HTDeferredOnlyBlock(registryKey.createKey(key))
}
