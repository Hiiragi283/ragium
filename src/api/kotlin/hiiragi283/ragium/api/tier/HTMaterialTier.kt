package hiiragi283.ragium.api.tier

import hiiragi283.ragium.api.material.HTMaterialType

/**
 * [HTMaterialType]と[HTTierProvider]を継承した拡張インターフェース
 */
interface HTMaterialTier :
    HTMaterialType.Translatable,
    HTTierProvider
