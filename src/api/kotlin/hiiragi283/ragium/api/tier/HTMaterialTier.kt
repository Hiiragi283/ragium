package hiiragi283.ragium.api.tier

import hiiragi283.ragium.api.data.lang.HTTranslatedNameProvider
import hiiragi283.ragium.api.material.HTMaterialLike

interface HTMaterialTier :
    HTMaterialLike,
    HTTierProvider,
    HTTranslatedNameProvider
