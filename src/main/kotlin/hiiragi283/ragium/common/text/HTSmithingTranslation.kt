package hiiragi283.ragium.common.text

import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.text.HTSimpleTranslation
import hiiragi283.ragium.api.text.HTTranslation

@ConsistentCopyVisibility
@JvmRecord
data class HTSmithingTranslation private constructor(
    val appliesTo: HTTranslation,
    val ingredients: HTTranslation,
    val upgradeDescription: HTTranslation,
    val baseSlotDescription: HTTranslation,
    val additionsSlotDescription: HTTranslation,
) {
    constructor(modId: String, material: HTMaterialLike) : this(modId, material.asMaterialName())

    constructor(modId: String, name: String) : this(
        HTSimpleTranslation("upgrade", modId, "${name}_upgrade", "applies_to"),
        HTSimpleTranslation("upgrade", modId, "${name}_upgrade", "ingredients"),
        HTSimpleTranslation("upgrade", modId, "${name}_upgrade"),
        HTSimpleTranslation("upgrade", modId, "${name}_upgrade", "base_slot_description"),
        HTSimpleTranslation("upgrade", modId, "${name}_upgrade", "additions_slot_description"),
    )
}
