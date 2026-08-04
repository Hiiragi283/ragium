package hiiragi283.lib.registry

import hiiragi283.lib.resource.HTKeyLike
import hiiragi283.lib.text.Text
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.NeoForgeRegistries

/**
 * [FluidType]向けの[HTDeferredHolder]の拡張クラスです。
 * @param TYPE [FluidType]のクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDeferredFluidType<TYPE : FluidType> :
    HTDeferredHolder<FluidType, TYPE>,
    HTKeyLike.Translatable<FluidType> {
    constructor(key: ResourceKey<FluidType>) : super(key)

    constructor(id: Identifier) : super(NeoForgeRegistries.Keys.FLUID_TYPES, id)

    override val translationKey: String get() = get().descriptionId

    override fun getText(): Text = get().description
}
