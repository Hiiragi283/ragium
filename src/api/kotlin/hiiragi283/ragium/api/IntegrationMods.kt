package hiiragi283.ragium.api

import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.fml.ModList
import net.neoforged.neoforge.common.conditions.ModLoadedCondition
import net.neoforged.neoforge.common.conditions.NotCondition
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import java.util.*

enum class IntegrationMods(val modId: String) : StringRepresentable {
    AA("actuallyadditions"),
    AE2("ae2"),
    CREATE("create"),
    EIO("enderio"),
    EIO_BASE("enderio_base"),
    EVC("evilcraft"),
    FD("farmersdelight"),
    IE("immersiveengineering"),
    IF("industrialforegoing"),
    MEK("mekanism"),
    MI("modern_industrialization"),
    RC("railcraft"),
    TF("twilightforest"),
    ;

    val isLoaded: Boolean get() = ModList.get().isLoaded(modId)
    val condition = ModLoadedCondition(modId)
    val notCondition = NotCondition(condition)

    fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(modId, path)

    fun <T : Any> getHolder(registry: Registry<T>, path: String): Optional<Holder.Reference<T>> = registry.getHolder(id(path))

    fun <T : Any> getHolder(registryKey: ResourceKey<Registry<T>>, path: String): Optional<Holder.Reference<T>> {
        val access: RegistryAccess = RagiumAPI.getInstance().getRegistryAccess() ?: return Optional.empty()
        val key: ResourceKey<T> = ResourceKey.create(registryKey, id(path))
        return access.lookup(registryKey).flatMap { it.get(key) }
    }

    fun <R : Any, T : R> createHolder(registryKey: ResourceKey<Registry<R>>, path: String): DeferredHolder<R, T> =
        DeferredHolder.create(registryKey, id(path))

    fun <T : Block> createBlockHolder(path: String): DeferredBlock<T> = DeferredBlock.createBlock(id(path))

    fun <T : Fluid> createFluidHolder(path: String): DeferredHolder<Fluid, T> = createHolder(Registries.FLUID, path)

    fun <T : Item> createItemHolder(path: String): DeferredItem<T> = DeferredItem.createItem(id(path))

    inline fun <T> runIfLoaded(action: () -> T): T? = if (isLoaded) action() else null

    //    StringRepresentable    //

    override fun getSerializedName(): String = modId
}
