package hiiragi283.ragium.api.integration.jade

import snownee.jade.api.Accessor
import snownee.jade.api.IComponentProvider
import snownee.jade.api.StreamServerDataProvider

interface HTJadeDataProvider<ACCESSOR : Accessor<*>, DATA : Any> :
    StreamServerDataProvider<ACCESSOR, DATA>,
    IComponentProvider<ACCESSOR>,
    HTJadeTooltipProvider<ACCESSOR, DATA>
