package hiiragi283.ragium.api.gui.component

import hiiragi283.ragium.api.math.HTBoundsProvider
import net.minecraft.client.gui.components.Renderable
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
interface HTWidget :
    HTBoundsProvider,
    Renderable
