package hiiragi283.ragium.api.material.property

class HTFormingRecipeFlag(val mechanical: Boolean, val melting: Boolean) {
    companion object {
        @JvmStatic
        fun disableAll(): HTFormingRecipeFlag = HTFormingRecipeFlag(mechanical = false, melting = false)

        @JvmStatic
        fun pressOnly(): HTFormingRecipeFlag = HTFormingRecipeFlag(mechanical = true, melting = false)

        @JvmStatic
        fun solidifyOnly(): HTFormingRecipeFlag = HTFormingRecipeFlag(mechanical = false, melting = true)

        @JvmStatic
        fun enableAll(): HTFormingRecipeFlag = HTFormingRecipeFlag(mechanical = true, melting = true)
    }
}
