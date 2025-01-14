package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialProvider
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.neoforged.neoforge.registries.DeferredHolder

//    DataComponentPatch.Builder    //

fun <T : Any> DataComponentPatch.Builder.set(
    type: DeferredHolder<DataComponentType<*>, DataComponentType<T>>,
    value: T,
): DataComponentPatch.Builder = set(type.get(), value)

/**
 * 指定した[tier]とそのレアリティを設定します。
 */
fun DataComponentPatch.Builder.tier(tier: HTMachineTier): DataComponentPatch.Builder = this
    .set(RagiumComponentTypes.MACHINE_TIER, tier)

/**
 * 指定した[text]をアイテムの名前に設定します。
 */
fun DataComponentPatch.Builder.name(text: Component): DataComponentPatch.Builder = set(DataComponents.ITEM_NAME, text)

/**
 * 指定した[prefix]と[material]，そしてその名前を設定します。
 */
fun DataComponentPatch.Builder.material(prefix: HTTagPrefix, material: HTMaterialKey): DataComponentPatch.Builder = this
    .set(RagiumComponentTypes.TAG_PREFIX, prefix)
    .set(RagiumComponentTypes.MATERIAL, material)
    .name(prefix.createText(material))

fun DataComponentPatch.Builder.material(provider: HTMaterialProvider): DataComponentPatch.Builder =
    material(provider.tagPrefix, provider.material)

/**
 * 指定した[translationKey]と[tier]をアイテムの名前として設定します。
 */
fun DataComponentPatch.Builder.tieredText(translationKey: String, tier: HTMachineTier): DataComponentPatch.Builder =
    name(tier.createPrefixedText(translationKey)).tier(tier)
